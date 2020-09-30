
package org.ar4k.agent.opcua.server;

import static com.google.common.collect.Lists.newArrayList;
import static org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS;
import static org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig.USER_TOKEN_POLICY_USERNAME;
import static org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig.USER_TOKEN_POLICY_X509;

import java.io.File;
import java.security.KeyPair;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.ar4k.agent.core.interfaces.EdgeComponent.ServiceStatus;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.api.config.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.identity.CompositeValidator;
import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.identity.X509IdentityValidator;
import org.eclipse.milo.opcua.sdk.server.util.HostnameUtil;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.DefaultTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedHttpsCertificateBuilder;
import org.eclipse.milo.opcua.stack.server.EndpointConfiguration;
import org.eclipse.milo.opcua.stack.server.security.DefaultServerCertificateValidator;

public class Ar4kOpcUaServer {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(Ar4kOpcUaServer.class.toString());

	static {
		// Required for SecurityPolicy.Aes256_Sha256_RsaPss
		Security.addProvider(new BouncyCastleProvider());
	}

	private final OpcUaServer server;
	private final OpcUaNamespace nameSpaceOpc;
	private final org.ar4k.agent.opcua.server.OpcUaServerConfig configuration;

	public Ar4kOpcUaServer(org.ar4k.agent.opcua.server.OpcUaServerConfig configuration) throws Exception {
		this.configuration = configuration;
		final File securityTempDir = new File(System.getProperty("java.io.tmpdir"), "security");
		if (!securityTempDir.exists() && !securityTempDir.mkdirs()) {
			throw new Exception("unable to create security temp dir: " + securityTempDir);
		}
		logger.debug("security temp dir: {}", securityTempDir.getAbsolutePath());

		final KeyStoreLoader loader = new KeyStoreLoader().load(securityTempDir);

		final DefaultCertificateManager certificateManager = new DefaultCertificateManager(loader.getServerKeyPair(),
				loader.getServerCertificateChain());

		final File pkiDir = securityTempDir.toPath().resolve("pki").toFile();
		final DefaultTrustListManager trustListManager = new DefaultTrustListManager(pkiDir);
		logger.debug("pki dir: {}", pkiDir.getAbsolutePath());

		final DefaultServerCertificateValidator certificateValidator = new DefaultServerCertificateValidator(
				trustListManager);

		final KeyPair httpsKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);

		final SelfSignedHttpsCertificateBuilder httpsCertificateBuilder = new SelfSignedHttpsCertificateBuilder(
				httpsKeyPair);
		httpsCertificateBuilder.setCommonName(HostnameUtil.getHostname());
		HostnameUtil.getHostnames(configuration.bindAddress).forEach(httpsCertificateBuilder::addDnsName);
		// TODO sostituire con certificati generali
		final X509Certificate httpsCertificate = httpsCertificateBuilder.build();

		final UsernameIdentityValidator identityValidator = new UsernameIdentityValidator(true, authChallenge -> {
			final String username = authChallenge.getUsername();
			final String password = authChallenge.getPassword();
//TODO inserire wrapper autenticazione
			final boolean userOk = "user".equals(username) && "password1".equals(password);
			final boolean adminOk = "admin".equals(username) && "password2".equals(password);

			return userOk || adminOk;
		});

		final X509IdentityValidator x509IdentityValidator = new X509IdentityValidator(c -> true);

		// If you need to use multiple certificates you'll have to be smarter than this.
		final X509Certificate certificate = certificateManager.getCertificates().stream().findFirst()
				.orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_ConfigurationError, "no certificate found"));

		// The configured application URI must match the one in the certificate(s)
		final String applicationUri = CertificateUtil.getSanUri(certificate)
				.orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_ConfigurationError,
						"certificate is missing the application URI"));

		final Set<EndpointConfiguration> endpointConfigurations = createEndpointConfigurations(certificate);

		final OpcUaServerConfig serverConfig = OpcUaServerConfig.builder().setApplicationUri(applicationUri)
				.setApplicationName(LocalizedText.english(configuration.applicationName))
				.setEndpoints(endpointConfigurations)
				.setBuildInfo(new BuildInfo(configuration.productUri, configuration.manufacturerName,
						configuration.productName, OpcUaServer.SDK_VERSION, "", DateTime.now()))
				.setCertificateManager(certificateManager).setTrustListManager(trustListManager)
				.setCertificateValidator(certificateValidator).setHttpsKeyPair(httpsKeyPair)
				.setHttpsCertificate(httpsCertificate)
				.setIdentityValidator(new CompositeValidator(identityValidator, x509IdentityValidator))
				.setProductUri(configuration.productUri).build();

		server = new OpcUaServer(serverConfig);

		nameSpaceOpc = new OpcUaNamespace(server, configuration);
		nameSpaceOpc.startup();
	}

	private Set<EndpointConfiguration> createEndpointConfigurations(X509Certificate certificate) {
		final Set<EndpointConfiguration> endpointConfigurations = new LinkedHashSet<>();

		final List<String> bindAddresses = newArrayList();
		bindAddresses.add(configuration.bindAddress);

		final Set<String> hostnames = new LinkedHashSet<>();
		hostnames.add(HostnameUtil.getHostname());
		hostnames.addAll(HostnameUtil.getHostnames(configuration.bindAddress));

		for (final String bindAddress : bindAddresses) {
			for (final String hostname : hostnames) {
				final EndpointConfiguration.Builder builder = EndpointConfiguration.newBuilder()
						.setBindAddress(bindAddress).setHostname(hostname).setPath(configuration.serverPath)
						.setCertificate(certificate).addTokenPolicies(USER_TOKEN_POLICY_ANONYMOUS,
								USER_TOKEN_POLICY_USERNAME, USER_TOKEN_POLICY_X509);

				final EndpointConfiguration.Builder noSecurityBuilder = builder.copy()
						.setSecurityPolicy(SecurityPolicy.None).setSecurityMode(MessageSecurityMode.None);

				endpointConfigurations.add(buildTcpEndpoint(noSecurityBuilder));
				endpointConfigurations.add(buildHttpsEndpoint(noSecurityBuilder));

				endpointConfigurations
						.add(buildTcpEndpoint(builder.copy().setSecurityPolicy(SecurityPolicy.Basic256Sha256)
								.setSecurityMode(MessageSecurityMode.SignAndEncrypt)));

				endpointConfigurations.add(buildHttpsEndpoint(builder.copy()
						.setSecurityPolicy(SecurityPolicy.Basic256Sha256).setSecurityMode(MessageSecurityMode.Sign)));

				final EndpointConfiguration.Builder discoveryBuilder = builder.copy()
						.setPath(configuration.serverPath + "/discovery").setSecurityPolicy(SecurityPolicy.None)
						.setSecurityMode(MessageSecurityMode.None);

				endpointConfigurations.add(buildTcpEndpoint(discoveryBuilder));
				endpointConfigurations.add(buildHttpsEndpoint(discoveryBuilder));
			}
		}

		return endpointConfigurations;
	}

	private EndpointConfiguration buildTcpEndpoint(EndpointConfiguration.Builder base) {
		return base.copy().setTransportProfile(TransportProfile.TCP_UASC_UABINARY).setBindPort(configuration.serverPort)
				.build();
	}

	private EndpointConfiguration buildHttpsEndpoint(EndpointConfiguration.Builder base) {
		return base.copy().setTransportProfile(TransportProfile.HTTPS_UABINARY)
				.setBindPort(configuration.serverPortHttps).build();
	}

	public OpcUaServer getServer() {
		return server;
	}

	public CompletableFuture<OpcUaServer> startup() {
		return server.startup();
	}

	public CompletableFuture<OpcUaServer> shutdown() {
		nameSpaceOpc.shutdown();

		return server.shutdown();
	}

	public ServiceStatus updateAndGetStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	public org.ar4k.agent.opcua.server.OpcUaServerConfig getConfiguration() {
		return configuration;
	}

	public OpcUaNamespace getNamespace() {
		return nameSpaceOpc;
	}

}