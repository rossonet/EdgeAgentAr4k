/*
 * Copyright (c) 2016 Kevin Herron
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *   http://www.eclipse.org/org/documents/edl-v10.html.
 */

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
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateValidator;
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
import org.slf4j.LoggerFactory;

public class Ar4kOpcUaServer {

  static {
    // Required for SecurityPolicy.Aes256_Sha256_RsaPss
    Security.addProvider(new BouncyCastleProvider());
  }

  private OpcUaServer server = null;

  private int tcpBindPort = 45341;
  private int httpsBindPort = 8443;
  private String bindAddress = "0.0.0.0";
  private String serverText = "Ar4k Agent OPC UA Server";
  private String productUri = "urn:ar4k:agent:opcua-server";
  private String manufacturerName = "Agent xxx";
  private String productName = "Ar4k Agent";
  private String serverPath = "/agent/discovery";

  public void prepare() throws Exception {
    File securityTempDir = new File(System.getProperty("java.io.tmpdir"), "security");
    if (!securityTempDir.exists() && !securityTempDir.mkdirs()) {
      throw new Exception("unable to create security temp dir: " + securityTempDir);
    }
    LoggerFactory.getLogger(getClass()).info("security temp dir: {}", securityTempDir.getAbsolutePath());

    KeyStoreLoader loader = new KeyStoreLoader().load(securityTempDir);

    DefaultCertificateManager certificateManager = new DefaultCertificateManager(loader.getServerKeyPair(),
        loader.getServerCertificateChain());

    File pkiDir = securityTempDir.toPath().resolve("pki").toFile();
    DefaultTrustListManager trustListManager = new DefaultTrustListManager(pkiDir);
    LoggerFactory.getLogger(getClass()).info("pki dir: {}", pkiDir.getAbsolutePath());

    DefaultCertificateValidator certificateValidator = new DefaultCertificateValidator(trustListManager);

    KeyPair httpsKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);

    SelfSignedHttpsCertificateBuilder httpsCertificateBuilder = new SelfSignedHttpsCertificateBuilder(httpsKeyPair);
    httpsCertificateBuilder.setCommonName(HostnameUtil.getHostname());
    HostnameUtil.getHostnames(bindAddress).forEach(httpsCertificateBuilder::addDnsName);
    X509Certificate httpsCertificate = httpsCertificateBuilder.build();

    UsernameIdentityValidator identityValidator = new UsernameIdentityValidator(true, authChallenge -> {
      String username = authChallenge.getUsername();
      String password = authChallenge.getPassword();
//TODO inserire usermanager Ar4k
      boolean userOk = "user".equals(username) && "password1".equals(password);
      boolean adminOk = "admin".equals(username) && "password2".equals(password);

      return userOk || adminOk;
    });

    X509IdentityValidator x509IdentityValidator = new X509IdentityValidator(c -> true);

    // If you need to use multiple certificates you'll have to be smarter than this.
    X509Certificate certificate = certificateManager.getCertificates().stream().findFirst()
        .orElseThrow(() -> new UaRuntimeException(StatusCodes.Bad_ConfigurationError, "no certificate found"));

    // The configured application URI must match the one in the certificate(s)
    String applicationUri = CertificateUtil.getSanUri(certificate).orElseThrow(
        () -> new UaRuntimeException(StatusCodes.Bad_ConfigurationError, "certificate is missing the application URI"));

    Set<EndpointConfiguration> endpointConfigurations = createEndpointConfigurations(certificate);

    OpcUaServerConfig serverConfig = OpcUaServerConfig.builder().setApplicationUri(applicationUri)
        .setApplicationName(LocalizedText.english(serverText)).setEndpoints(endpointConfigurations)
        .setBuildInfo(
            new BuildInfo(productUri, manufacturerName, productName, OpcUaServer.SDK_VERSION, "", DateTime.now()))
        .setCertificateManager(certificateManager).setTrustListManager(trustListManager)
        .setCertificateValidator(certificateValidator).setHttpsKeyPair(httpsKeyPair)
        .setHttpsCertificate(httpsCertificate)
        .setIdentityValidator(new CompositeValidator(identityValidator, x509IdentityValidator))
        .setProductUri(productUri).build();

    server = new OpcUaServer(serverConfig);

    OpcUaNamespace namespace = new OpcUaNamespace(server);
    namespace.startup();
  }

  private Set<EndpointConfiguration> createEndpointConfigurations(X509Certificate certificate) {
    Set<EndpointConfiguration> endpointConfigurations = new LinkedHashSet<>();

    List<String> bindAddresses = newArrayList();
    bindAddresses.add(bindAddress);

    Set<String> hostnames = new LinkedHashSet<>();
    hostnames.add(HostnameUtil.getHostname());
    hostnames.addAll(HostnameUtil.getHostnames(bindAddress));

    for (String bindAddress : bindAddresses) {
      for (String hostname : hostnames) {
        EndpointConfiguration.Builder builder = EndpointConfiguration.newBuilder().setBindAddress(bindAddress)
            .setHostname(hostname).setPath("/milo").setCertificate(certificate)
            .addTokenPolicies(USER_TOKEN_POLICY_ANONYMOUS, USER_TOKEN_POLICY_USERNAME, USER_TOKEN_POLICY_X509);

        EndpointConfiguration.Builder noSecurityBuilder = builder.copy().setSecurityPolicy(SecurityPolicy.None)
            .setSecurityMode(MessageSecurityMode.None);

        endpointConfigurations.add(buildTcpEndpoint(noSecurityBuilder));
        endpointConfigurations.add(buildHttpsEndpoint(noSecurityBuilder));

        // TCP Basic256Sha256 / SignAndEncrypt
        endpointConfigurations.add(buildTcpEndpoint(builder.copy().setSecurityPolicy(SecurityPolicy.Basic256Sha256)
            .setSecurityMode(MessageSecurityMode.SignAndEncrypt)));

        // HTTPS Basic256Sha256 / Sign (SignAndEncrypt not allowed for HTTPS)
        endpointConfigurations.add(buildHttpsEndpoint(
            builder.copy().setSecurityPolicy(SecurityPolicy.Basic256Sha256).setSecurityMode(MessageSecurityMode.Sign)));

        /*
         * It's good practice to provide a discovery-specific endpoint with no security.
         * It's required practice if all regular endpoints have security configured.
         *
         * Usage of the "/discovery" suffix is defined by OPC UA Part 6:
         *
         * Each OPC UA Server Application implements the Discovery Service Set. If the
         * OPC UA Server requires a different address for this Endpoint it shall create
         * the address by appending the path "/discovery" to its base address.
         */
        EndpointConfiguration.Builder discoveryBuilder = builder.copy().setPath(serverPath)
            .setSecurityPolicy(SecurityPolicy.None).setSecurityMode(MessageSecurityMode.None);

        endpointConfigurations.add(buildTcpEndpoint(discoveryBuilder));
        endpointConfigurations.add(buildHttpsEndpoint(discoveryBuilder));
      }
    }

    return endpointConfigurations;
  }

  private EndpointConfiguration buildTcpEndpoint(EndpointConfiguration.Builder base) {
    return base.copy().setTransportProfile(TransportProfile.TCP_UASC_UABINARY).setBindPort(tcpBindPort).build();
  }

  private EndpointConfiguration buildHttpsEndpoint(EndpointConfiguration.Builder base) {
    return base.copy().setTransportProfile(TransportProfile.HTTPS_UABINARY).setBindPort(httpsBindPort).build();
  }

  public OpcUaServer getServer() {
    return server;
  }

  public CompletableFuture<OpcUaServer> startup() {
    return server.startup();
  }

  public CompletableFuture<OpcUaServer> shutdown() {
    return server.shutdown();
  }

  public String getBindAddress() {
    return bindAddress;
  }

  public void setBindAddress(String bindAddress) {
    this.bindAddress = bindAddress;
  }

  public String getServerText() {
    return serverText;
  }

  public void setServerText(String serverText) {
    this.serverText = serverText;
  }

  public String getProductUri() {
    return productUri;
  }

  public void setProductUri(String productUri) {
    this.productUri = productUri;
  }

  public String getManufacturerName() {
    return manufacturerName;
  }

  public void setManufacturerName(String manufacturerName) {
    this.manufacturerName = manufacturerName;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getServerPath() {
    return serverPath;
  }

  public void setServerPath(String serverPath) {
    this.serverPath = serverPath;
  }

}
