package org.ar4k.agent.opcua.client;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.services.EdgeComponent;
import org.ar4k.agent.core.services.ServiceConfig;
import org.ar4k.agent.exception.ServiceInitException;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.industrial.Enumerator.SecurityMode;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.api.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.X509IdentityProvider;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio di connessione client OPCUA with Eclipse Milo.
 */

public class OpcUaClientService implements EdgeComponent {

	private static final String CERTIFICATE_CHAIN_SEPARATOR = ",";
	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(OpcUaClientService.class);
	private OpcUaClient clientOpc = null;
	private OpcUaClientConfig configuration = null;
	private DataAddress dataAddress = null;
	private Homunculus homunculus = null;
	private ServiceStatus serviceStatus = ServiceStatus.INIT;
	private Map<String, OpcUaGroupManager> groups = new HashMap<>();

	@Override
	public void close() throws Exception {
		kill();
	}

	@Override
	public ServiceConfig getConfiguration() {
		return configuration;
	}

	@Override
	public DataAddress getDataAddress() {
		return dataAddress;
	}

	@Override
	public Homunculus getHomunculus() {
		return homunculus;
	}

	@Override
	public String getServiceName() {
		return getConfiguration().getName();
	}

	@Override
	public void init() throws ServiceInitException {
		if (clientOpc == null) {
			try {
				clientOpc = createConnection();
				connectGroups();
			} catch (Exception exception) {
				logger.error("running opcua connector", exception);
			}
		}
	}

	@Override
	public void kill() {
		if (clientOpc != null) {
			clientOpc.disconnect();
		}
		serviceStatus = ServiceStatus.KILLED;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		this.configuration = (OpcUaClientConfig) configuration;
	}

	@Override
	public void setDataAddress(DataAddress dataAddress) {
		this.dataAddress = dataAddress;
	}

	@Override
	public void setHomunculus(Homunculus homunculus) {
		this.homunculus = homunculus;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OpcUaClientService [");
		if (configuration != null) {
			builder.append("configuration=");
			builder.append(configuration);
			builder.append(", ");
		}
		if (homunculus != null) {
			builder.append("homunculus=");
			builder.append(homunculus);
			builder.append(", ");
		}
		if (dataAddress != null) {
			builder.append("dataAddress=");
			builder.append(dataAddress);
			builder.append(", ");
		}
		if (serviceStatus != null) {
			builder.append("serviceStatus=");
			builder.append(serviceStatus);
			builder.append(", ");
		}
		if (clientOpc != null) {
			builder.append("clientOpc=");
			builder.append(clientOpc);
		}
		builder.append("]");
		return builder.toString();
	}

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		return serviceStatus;
	}

	private void connectGroups() {
		for (OpcUaClientNodeConfig s : configuration.subscriptions) {
			final String groupName = s.group + "-" + s.publishInterval;
			if (!groups.containsKey(groupName)) {
				try {
					groups.put(groupName, new OpcUaGroupManager(groupName, this,
							clientOpc.getSubscriptionManager().createSubscription(1000.0).get()));
				} catch (Exception exception) {
					logger.logException("during group subscription", exception);
				}
			}
			try {
				groups.get(groupName).addSingleNode(s);
			} catch (InterruptedException | ExecutionException exception) {
				logger.logException(exception);
			}
		}
	}

	private OpcUaClient createConnection() throws UaException, InterruptedException, ExecutionException {
		final EndpointDescription endpoint = getEndpoint();
		logger.info("OPCUA connection to " + endpoint);
		final OpcUaClientConfigBuilder config = org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig.builder()
				.setEndpoint(endpoint).setApplicationUri(configuration.clientName)
				.setApplicationName(LocalizedText.english(configuration.clientName));
		if (configuration.sessionTimeout != null) {
			config.setSessionTimeout(UInteger.valueOf(configuration.sessionTimeout));
		}
		if (configuration.aliasCryptoCertificateInKeystore != null) {
			config.setKeyPair(
					homunculus.getMyIdentityKeystore().getKeyPair(configuration.aliasCryptoCertificateInKeystore));
		} else if (configuration.securityMode.equals(SecurityMode.sign)
				|| configuration.securityMode.equals(SecurityMode.signAndEncrypt)) {
			final String myAliasCertInKeystore = homunculus.getMyAliasCertInKeystore();
			logger.warn("crypto policy is active but certificate alias is null, using " + myAliasCertInKeystore);
			homunculus.getMyIdentityKeystore().getKeyPair(myAliasCertInKeystore);
		}
		if (configuration.connectTimeout != null) {
			config.setConnectTimeout(UInteger.valueOf(configuration.connectTimeout));
		}
		if (configuration.keepAliveTimeout != null) {
			config.setKeepAliveTimeout(UInteger.valueOf(configuration.keepAliveTimeout));
		}
		if (configuration.requestTimeout != null) {
			config.setRequestTimeout(UInteger.valueOf(configuration.requestTimeout));
		}
		if (configuration.channelLifetime != null) {
			config.setChannelLifetime(UInteger.valueOf(configuration.channelLifetime));
		}
		config.setIdentityProvider(getIdentityProvider());
		// config.setMessageLimits(getMessageLimits());
		if (configuration.acknowledgeTimeout != null) {
			config.setAcknowledgeTimeout(UInteger.valueOf(configuration.acknowledgeTimeout));
		}
		if (configuration.cryptoServerChain != null) {
			config.setCertificateChain(getCertificateChain());
		}
		if (configuration.cryptoServerCertificate != null) {
			final X509Certificate certificate = getCertificate();
			if (certificate != null) {
				config.setCertificate(certificate);
			}
		}
		final OpcUaClient client = OpcUaClient.create(config.build());
		client.connect().get();
		return client;
	}

	private X509Certificate getCertificate() {
		byte[] decodedCrt = Base64.getDecoder().decode(configuration.cryptoServerCertificate);
		try {
			return (X509Certificate) CertificateFactory.getInstance("X.509")
					.generateCertificate(new ByteArrayInputStream(decodedCrt));
		} catch (CertificateException exception) {
			logger.logException(exception);
			return null;
		}
	}

	private X509Certificate[] getCertificateChain() {
		List<X509Certificate> certList = new ArrayList<>();
		final String certificateChainList = configuration.cryptoServerChain;
		if (certificateChainList != null) {
			if (certificateChainList.contains(CERTIFICATE_CHAIN_SEPARATOR)) {
				for (String singleCert : certificateChainList.split(CERTIFICATE_CHAIN_SEPARATOR)) {
					try {
						certList.add((X509Certificate) CertificateFactory.getInstance("X.509")
								.generateCertificate(new ByteArrayInputStream(singleCert.getBytes())));
					} catch (CertificateException exception) {
						logger.error("encoding one of crypto chains cert", exception);
					}
				}
			} else {
				try {
					certList.add((X509Certificate) CertificateFactory.getInstance("X.509")
							.generateCertificate(new ByteArrayInputStream(certificateChainList.getBytes())));
				} catch (CertificateException exception) {
					logger.error("encoding unique crypto chain cert", exception);
				}
			}
		}
		return certList.toArray(new X509Certificate[0]);
	}

	private EndpointDescription getEndpoint() {
		EndpointDescription endpointDescriptionFound = null;
		List<EndpointDescription> endpoints = null;
		try {
			logger.error("connect to opcua endpoint " + configuration.serverUrl);
			endpoints = DiscoveryClient.getEndpoints(configuration.serverUrl).get();
		} catch (ExecutionException | InterruptedException exception) {
			logger.error("error endpoint opcua ", exception);
		}
		MessageSecurityMode searchSecurityMode = MessageSecurityMode.None;
		switch (configuration.securityMode) {
		case none:
			searchSecurityMode = MessageSecurityMode.None;
			break;
		case sign:
			searchSecurityMode = MessageSecurityMode.Sign;
			break;
		case signAndEncrypt:
			searchSecurityMode = MessageSecurityMode.SignAndEncrypt;
			break;
		default:
			searchSecurityMode = MessageSecurityMode.None;
			break;
		}
		String searchSecurityPolicyTarget = SecurityPolicy.None.getUri();
		if (configuration.cryptoMode != null) {
			switch (configuration.cryptoMode) {
			case Basic128Rsa15:
				searchSecurityPolicyTarget = SecurityPolicy.Basic128Rsa15.getUri();
				break;
			case Basic256:
				searchSecurityPolicyTarget = SecurityPolicy.Basic256.getUri();
				break;
			case Basic256Sha256:
				searchSecurityPolicyTarget = SecurityPolicy.Basic256Sha256.getUri();
				break;
			case Aes128_Sha256_RsaOaep:
				searchSecurityPolicyTarget = SecurityPolicy.Aes128_Sha256_RsaOaep.getUri();
				break;
			case Aes256_Sha256_RsaPss:
				searchSecurityPolicyTarget = SecurityPolicy.Aes256_Sha256_RsaPss.getUri();
				break;
			default:
				searchSecurityPolicyTarget = SecurityPolicy.None.getUri();
				break;
			}
		}
		if (endpoints != null) {
			for (EndpointDescription e : endpoints) {
				if (e.getSecurityPolicyUri().equals(searchSecurityPolicyTarget)
						&& e.getSecurityMode().equals(searchSecurityMode)) {
					logger.info("found OPCUA endpoint with the selected functions -> " + e);
					endpointDescriptionFound = e;
					break;
				}
			}
		}
		if (endpointDescriptionFound == null) {
			logger.error("NO ENDPOINT OPCUA FOUND IN " + endpoints);
		}
		if (configuration.forceHostName == true) {
			try {
				return updateEndpointUrl(endpointDescriptionFound, new URI(configuration.serverUrl).getHost());
			} catch (Exception exception) {
				logger.logException(exception);
				return endpointDescriptionFound;
			}
		} else {
			return endpointDescriptionFound;
		}
	}

	private IdentityProvider getIdentityProvider() {
		IdentityProvider idp = null;
		switch (configuration.authMode) {
		case password:
			idp = new UsernameProvider(configuration.username, configuration.password);
			break;
		case none:
			idp = new AnonymousProvider();
			break;
		case certificate:
			if (configuration.aliasAuthCertificateInKeystore != null) {
				idp = new X509IdentityProvider(
						homunculus.getMyIdentityKeystore()
								.getClientCertificate(configuration.aliasAuthCertificateInKeystore),
						homunculus.getMyIdentityKeystore().getPrivateKey(configuration.aliasAuthCertificateInKeystore));
			} else {
				logger.error(
						"required authetication by certificate but aliasAuthCertificateInKeystore is null, try anonymous way");
				idp = new AnonymousProvider();
			}
			break;
		default:
			idp = new AnonymousProvider();
			break;
		}
		return idp;
	}

	public static EndpointDescription updateEndpointUrl(EndpointDescription original, String hostname) {
		URI uri = null;
		try {
			uri = new URI(original.getEndpointUrl()).parseServerAuthority();
		} catch (URISyntaxException e) {
			logger.logException(e);
		}
		String endpointUrl = String.format("%s://%s:%s%s", uri != null ? uri.getScheme() : null, hostname,
				uri != null ? uri.getPort() : null, uri != null ? uri.getPath() : null);
		return new EndpointDescription(endpointUrl, original.getServer(), original.getServerCertificate(),
				original.getSecurityMode(), original.getSecurityPolicyUri(), original.getUserIdentityTokens(),
				original.getTransportProfileUri(), original.getSecurityLevel());
	}

	OpcUaClient getOpcUaClient() {
		return clientOpc;
	}

}
