package org.ar4k.agent.control.remote;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.console.Ar4kAgent;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.helper.ContextCreationHelper;
import org.ar4k.agent.keystore.KeystoreLoader;
import org.ar4k.agent.network.NetworkTunnel;
import org.ar4k.agent.tunnels.ssh.client.SshLocalConfig;
import org.ar4k.agent.tunnels.sshd.SshdSystemConfig;
import org.ar4k.agent.tunnels.sshd.SshdSystemService;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class RemoteControlOverSsh {

  private static final String CLIENT1_LABEL = "client1";
  private static final String CLIENT2_LABEL = "client2";
  private static final String SERVER_LABEL = "server";
  private final ExecutorService executor = Executors.newCachedThreadPool();
  private final Map<String, Anima> testAnimas = new HashMap<>();
  private final File keyStoreMaster = new File("/tmp/master.ks");
  private final File keyStoreServer = new File("/tmp/server.ks");
  private final File keyStoreClient1 = new File("/tmp/client1.ks");
  private final File keyStoreClient2 = new File("/tmp/client2.ks");
  private final String masterAliasInKeystore = "master";
  private final String serverAliasInKeystore = "server";
  private final String client2AliasInKeystore = "client2";
  private final String client1AliasInKeystore = "client1";
  private final String signServerAliasInKeystore = "server-sign";
  private final String signClient2AliasInKeystore = "client2-sign";
  private final String signClient1AliasInKeystore = "client1-sign";
  private final String passwordKs = "password";

  private NetworkTunnel networkTunnel = null;
  private Future<Boolean> serverTCP = null;
  private Future<Boolean> clientTCP = null;
  private boolean completed = false;

  private void deleteDir(File dir) {
    File[] files = dir.listFiles();
    if (files != null) {
      for (final File file : files) {
        deleteDir(file);
      }
    }
    dir.delete();
  }

  @Before
  public void before() throws Exception {
    deleteDir(new File("./tmp"));
    deleteDir(new File("./tmp1"));
    deleteDir(new File("./tmp2"));
    deleteDir(new File("./tmp3"));
    Files.createDirectories(Paths.get("./tmp"));
    KeystoreLoader.createSelfSignedCert("ca", "Rossonet", "TEST UNIT", "IMOLA", "BOLOGNA", "IT",
        "urn:org.ar4k.agent:ca", "*.ar4k.net", "127.0.0.1", masterAliasInKeystore, keyStoreMaster.getAbsolutePath(),
        passwordKs, true);
    KeystoreLoader.create("server", "Rossonet", "TEST UNIT S", "IMOLA", "BOLOGNA", "IT",
        "urn:org.ar4k.agent:server-test-agent", "*.ar4k.net", "127.0.0.1", serverAliasInKeystore,
        keyStoreServer.getAbsolutePath(), passwordKs, false);
    KeystoreLoader.create("client2", "Rossonet", "TEST UNIT C2", "IMOLA", "BOLOGNA", "IT",
        "urn:org.ar4k.agent:client2-test-agent", "*.ar4k.net", "127.0.0.1", client2AliasInKeystore,
        keyStoreClient2.getAbsolutePath(), passwordKs, false);
    KeystoreLoader.create("client1", "Rossonet", "TEST UNIT C1", "IMOLA", "BOLOGNA", "IT",
        "urn:org.ar4k.agent:client1-test-agent", "*.ar4k.net", "127.0.0.1", client1AliasInKeystore,
        keyStoreClient1.getAbsolutePath(), passwordKs, false);
    PKCS10CertificationRequest csrServer = KeystoreLoader.getPKCS10CertificationRequest(serverAliasInKeystore,
        keyStoreServer.getAbsolutePath(), passwordKs);
    PKCS10CertificationRequest csrClient2 = KeystoreLoader.getPKCS10CertificationRequest(client2AliasInKeystore,
        keyStoreClient2.getAbsolutePath(), passwordKs);
    PKCS10CertificationRequest csrClient1 = KeystoreLoader.getPKCS10CertificationRequest(client1AliasInKeystore,
        keyStoreClient1.getAbsolutePath(), passwordKs);
    JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
    PublicKey pubKeyServer = converter.getPublicKey(csrServer.getSubjectPublicKeyInfo());
    writeCSr("./tmp/csr-server.pem", Base64.getEncoder().encodeToString(csrServer.getEncoded()));
    System.out.println("\nCSR SERVER\n" + pubKeyServer);
    PublicKey pubKeyClient1 = converter.getPublicKey(csrClient1.getSubjectPublicKeyInfo());
    writeCSr("./tmp/csr-client1.pem", Base64.getEncoder().encodeToString(csrClient1.getEncoded()));
    System.out.println("\nCSR CLIENT1\n" + pubKeyClient1);
    PublicKey pubKeyClient2 = converter.getPublicKey(csrClient2.getSubjectPublicKeyInfo());
    writeCSr("./tmp/csr-client2.pem", Base64.getEncoder().encodeToString(csrClient2.getEncoded()));
    System.out.println("\nCSR CLIENT2\n" + pubKeyClient2);
    KeystoreLoader.signCertificate(csrServer, signServerAliasInKeystore, 100, masterAliasInKeystore,
        keyStoreMaster.getAbsolutePath(), passwordKs);
    KeystoreLoader.signCertificate(csrClient2, signClient2AliasInKeystore, 100, masterAliasInKeystore,
        keyStoreMaster.getAbsolutePath(), passwordKs);
    KeystoreLoader.signCertificate(csrClient1, signClient1AliasInKeystore, 100, masterAliasInKeystore,
        keyStoreMaster.getAbsolutePath(), passwordKs);

    String crtServer = KeystoreLoader.getCertCaAsPem(signServerAliasInKeystore, keyStoreMaster.getAbsolutePath(),
        passwordKs);
    String keyServer = KeystoreLoader.getPrivateKeyBase64(serverAliasInKeystore, keyStoreServer.getAbsolutePath(),
        passwordKs);
    KeystoreLoader.setClientKeyPair(keyServer, crtServer, signServerAliasInKeystore, keyStoreServer.getAbsolutePath(),
        passwordKs);
    String crtClient1 = KeystoreLoader.getCertCaAsPem(signClient1AliasInKeystore, keyStoreMaster.getAbsolutePath(),
        passwordKs);
    String keyClient1 = KeystoreLoader.getPrivateKeyBase64(client1AliasInKeystore, keyStoreClient1.getAbsolutePath(),
        passwordKs);
    KeystoreLoader.setClientKeyPair(keyClient1, crtClient1, signClient1AliasInKeystore,
        keyStoreClient1.getAbsolutePath(), passwordKs);

    String crtClient2 = KeystoreLoader.getCertCaAsPem(signClient2AliasInKeystore, keyStoreMaster.getAbsolutePath(),
        passwordKs);
    String keyClient2 = KeystoreLoader.getPrivateKeyBase64(client2AliasInKeystore, keyStoreClient2.getAbsolutePath(),
        passwordKs);
    KeystoreLoader.setClientKeyPair(keyClient2, crtClient2, signClient2AliasInKeystore,
        keyStoreClient2.getAbsolutePath(), passwordKs);
    System.out
        .println("\n\nLIST MASTER " + KeystoreLoader.listCertificate(keyStoreMaster.getAbsolutePath(), passwordKs));
    System.out
        .println("\n\nLIST SERVER " + KeystoreLoader.listCertificate(keyStoreServer.getAbsolutePath(), passwordKs));
    System.out
        .println("\n\nLIST CLIENT 1 " + KeystoreLoader.listCertificate(keyStoreClient1.getAbsolutePath(), passwordKs));
  }

  @After
  public void tearDown() throws Exception {
    System.err.println("\n\nEND TESTS\n\n");
    for (Anima a : testAnimas.values()) {
      a.close();
    }
    Files.deleteIfExists(Paths.get("./tmp/test-server.config.base64.ar4k"));
    Files.deleteIfExists(Paths.get("./tmp/test-client1.config.base64.ar4k"));
    Files.deleteIfExists(Paths.get("./tmp/test-client2.config.base64.ar4k"));
    Files.deleteIfExists(keyStoreMaster.toPath());
    Files.deleteIfExists(keyStoreServer.toPath());
    Files.deleteIfExists(keyStoreClient1.toPath());
    Files.deleteIfExists(keyStoreClient2.toPath());
    if (executor != null) {
      executor.shutdownNow();
      executor.awaitTermination(1, TimeUnit.MINUTES);
    }
  }

  @Test
  public void simpleSShServerWithSystemShell() throws InterruptedException {
    SshdSystemConfig testServerConfig = new SshdSystemConfig();
    SshdSystemService server = (SshdSystemService) testServerConfig.instantiate();
    server.init();
    for (int i = 0; i < 20; i++) {
      System.out.println(server.getDescriptionJson());
      Thread.sleep(5000);
    }
    server.kill();
  }

  private void writeCSr(String path, String cert)
      throws UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException {
    FileWriter writer = new FileWriter(new File(path));
    writer.write("-----BEGIN CERTIFICATE REQUEST-----\n");
    writer.write(cert);
    writer.write("\n-----END CERTIFICATE REQUEST-----\n");
    writer.close();
  }

  @Test
  public void allNodeSimulatedSshTunnel() throws Exception {
    allNodeSimulatedWithTunnel(false);
  }

  private void allNodeSimulatedWithTunnel(boolean ssl) throws Exception {
    List<String> baseArgs = new ArrayList<>();
    List<String> baseArgsClientOne = new ArrayList<>();
    List<String> baseArgsClientTwo = new ArrayList<>();
    String certCaAsPem = "";
    if (ssl) {
      baseArgs.add("--ar4k.beaconClearText=false");
      baseArgsClientOne.add("--ar4k.beaconClearText=false");
      baseArgsClientTwo.add("--ar4k.beaconClearText=false");
      certCaAsPem = KeystoreLoader.getCertCaAsPem(masterAliasInKeystore, keyStoreMaster.getAbsolutePath(), passwordKs);
      byte[] decodedCrt = Base64.getDecoder().decode(certCaAsPem);
      X509Certificate clientCertificate = (X509Certificate) CertificateFactory.getInstance("X.509")
          .generateCertificate(new ByteArrayInputStream(decodedCrt));
      System.out.println("\n\nCA Master\n" + certCaAsPem);
      System.out.println(clientCertificate);
    }

    baseArgs.add("--ar4k.consoleOnly=false");
    baseArgs.add("--spring.shell.command.quit.enabled=false");
    baseArgs.add("--logging.level.root=INFO");
    baseArgs.add("--ar4k.confPath=./tmp1");
    baseArgs.add("--ar4k.fileConfig=./tmp1/test-server.config.base64.ar4k");
    baseArgs.add("--ar4k.webConfig=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
    baseArgs.add("--ar4k.dnsConfig=demo1.rossonet.name");

    baseArgsClientOne.add("--ar4k.consoleOnly=false");
    baseArgsClientOne.add("--spring.shell.command.quit.enabled=false");
    baseArgsClientOne.add("--logging.level.root=INFO");
    baseArgsClientOne.add("--ar4k.confPath=./tmp2");
    baseArgsClientOne.add("--ar4k.fileConfig=./tmp2/test-client1.config.base64.ar4k");
    baseArgsClientOne.add("--ar4k.webConfig=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
    baseArgsClientOne.add("--ar4k.dnsConfig=demo1.rossonet.name");

    baseArgsClientTwo.add("--ar4k.consoleOnly=false");
    baseArgsClientTwo.add("--spring.shell.command.quit.enabled=false");
    baseArgsClientTwo.add("--logging.level.root=INFO");
    baseArgsClientTwo.add("--ar4k.confPath=./tmp3");
    baseArgsClientTwo.add("--ar4k.fileConfig=./tmp3/test-client2.config.base64.ar4k");
    baseArgsClientTwo.add("--ar4k.webConfig=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
    baseArgsClientTwo.add("--ar4k.dnsConfig=demo1.rossonet.name");
//    addArgs.add("--ar4k.baseConfig=");
    baseArgs.add("--ar4k.webKeystore=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
    baseArgs.add("--ar4k.dnsKeystore=ks1.rossonet.name");

    baseArgsClientOne.add("--ar4k.webKeystore=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
    baseArgsClientOne.add("--ar4k.dnsKeystore=ks1.rossonet.name");

    baseArgsClientTwo.add("--ar4k.webKeystore=https://www.rossonet.name/dati/ar4kAgent/defaultBoot.config.base64.ar4k");
    baseArgsClientTwo.add("--ar4k.dnsKeystore=ks1.rossonet.name");

    // baseArgs.add("--ar4k.keystoreMainAlias=");
    baseArgs.add("--ar4k.keystorePassword=" + passwordKs);
    if (certCaAsPem != null && !certCaAsPem.isEmpty())
      baseArgs.add("--ar4k.beaconCaChainPem=" + certCaAsPem);
    baseArgs.add("--ar4k.adminPassword=password");

    baseArgsClientOne.add("--ar4k.keystorePassword=" + passwordKs);
    if (certCaAsPem != null && !certCaAsPem.isEmpty())
      baseArgsClientOne.add("--ar4k.beaconCaChainPem=" + certCaAsPem);
    baseArgsClientOne.add("--ar4k.adminPassword=password");

    baseArgsClientTwo.add("--ar4k.keystorePassword=" + passwordKs);
    if (certCaAsPem != null && !certCaAsPem.isEmpty())
      baseArgsClientTwo.add("--ar4k.beaconCaChainPem=" + certCaAsPem);
    baseArgsClientTwo.add("--ar4k.adminPassword=password");
//    addArgs.add("--ar4k.webRegistrationEndpoint=");
//    addArgs.add("--ar4k.dnsRegistrationEndpoint=");
    baseArgs.add("--ar4k.beaconDiscoveryFilterString=TEST-REGISTER");
    baseArgs.add("--ar4k.beaconDiscoveryPort=33667");
    baseArgs.add("--ar4k.fileConfigOrder=1");
    baseArgs.add("--ar4k.webConfigOrder=2");
    baseArgs.add("--ar4k.dnsConfigOrder=3");
    baseArgs.add("--ar4k.baseConfigOrder=0");
    baseArgs.add("--ar4k.threadSleep=1000");
    baseArgs.add("--ar4k.logoUrl=/static/img/ar4k.png");

    baseArgsClientOne.add("--ar4k.beaconDiscoveryFilterString=TEST-REGISTER");
    baseArgsClientOne.add("--ar4k.beaconDiscoveryPort=33667");
    baseArgsClientOne.add("--ar4k.fileConfigOrder=1");
    baseArgsClientOne.add("--ar4k.webConfigOrder=2");
    baseArgsClientOne.add("--ar4k.dnsConfigOrder=3");
    baseArgsClientOne.add("--ar4k.baseConfigOrder=0");
    baseArgsClientOne.add("--ar4k.threadSleep=1000");
    baseArgsClientOne.add("--ar4k.logoUrl=/static/img/ar4k.png");

    baseArgsClientTwo.add("--ar4k.beaconDiscoveryFilterString=TEST-REGISTER");
    baseArgsClientTwo.add("--ar4k.beaconDiscoveryPort=33667");
    baseArgsClientTwo.add("--ar4k.fileConfigOrder=1");
    baseArgsClientTwo.add("--ar4k.webConfigOrder=2");
    baseArgsClientTwo.add("--ar4k.dnsConfigOrder=3");
    baseArgsClientTwo.add("--ar4k.baseConfigOrder=0");
    baseArgsClientTwo.add("--ar4k.threadSleep=1000");
    baseArgsClientTwo.add("--ar4k.logoUrl=/static/img/ar4k.png");

    Ar4kConfig clientOneConfig = new Ar4kConfig();
    Ar4kConfig clientTwoConfig = new Ar4kConfig();
    Ar4kConfig serverConfig = new Ar4kConfig();
    serverConfig.name = "server-beacon";
    clientOneConfig.name = "client1-beacon";
    clientTwoConfig.name = "client2-beacon";
    // serverConfig.beaconServer = null;
    serverConfig.beaconDiscoveryPort = 0;
    serverConfig.beaconServerCertChain = certCaAsPem;
    clientOneConfig.beaconServerCertChain = certCaAsPem;
    clientTwoConfig.beaconServerCertChain = certCaAsPem;
    String destinationIp = "127.0.0.1";
    int destinationPort = 7777;
    int srcPort = 8888;
    SshdSystemConfig sshdConfig = new SshdSystemConfig();
    sshdConfig.setName("sshd mina server");
    sshdConfig.port = 10000;
    serverConfig.pots.add(sshdConfig);

    SshLocalConfig sshRight = new SshLocalConfig();
    sshRight.setName("ssh client 2");
    sshRight.redirectServer = destinationIp;
    sshRight.redirectPort = destinationPort;
    sshRight.bindPort = 10008;
    sshRight.bindHost = "0.0.0.0";
    sshRight.host = destinationIp;
    sshRight.port = 10000;
    // sshRight.authkey = "~/.ssh/id_rsa";
    sshRight.username = "admin";
    sshRight.password = "password";
    clientTwoConfig.pots.add(sshRight);

    SshLocalConfig sshLeft = new SshLocalConfig();
    sshLeft.setName("ssh client 1");
    sshLeft.redirectServer = destinationIp;
    sshLeft.redirectPort = 10008;
    sshLeft.bindPort = srcPort;
    sshLeft.bindHost = "0.0.0.0";
    sshLeft.host = destinationIp;
    sshLeft.port = 10000;
    // sshLeft.authkey = "~/.ssh/id_rsa";
    sshLeft.username = "admin";
    sshLeft.password = "password";
    clientOneConfig.pots.add(sshLeft);

    testAnimas.put(SERVER_LABEL,
        executor.submit(new ContextCreationHelper(Ar4kAgent.class, executor, "a.log", keyStoreServer.getAbsolutePath(),
            1124, baseArgs, serverConfig, serverAliasInKeystore, signServerAliasInKeystore, null)).get());
    testAnimas.put(CLIENT2_LABEL,
        executor
            .submit(new ContextCreationHelper(Ar4kAgent.class, executor, "b.log", keyStoreClient2.getAbsolutePath(),
                1125, baseArgsClientTwo, clientTwoConfig, client2AliasInKeystore, signClient2AliasInKeystore, null))
            .get());
    testAnimas.put(CLIENT1_LABEL,
        executor
            .submit(new ContextCreationHelper(Ar4kAgent.class, executor, "c.log", keyStoreClient1.getAbsolutePath(),
                1126, baseArgsClientOne, clientOneConfig, client1AliasInKeystore, signClient1AliasInKeystore, null))
            .get());
    Thread.sleep(15000);
    for (Anima a : testAnimas.values()) {
      // String animaName = a.getRuntimeConfig() != null ?
      // a.getRuntimeConfig().getName() : "no-config";
      Assert.assertEquals(a.getState(), Anima.AnimaStates.RUNNING);
    }
    Thread.sleep(25000);

    Callable<Boolean> runner = new Callable<Boolean>() {
      private int last = 0;

      @Override
      public Boolean call() throws Exception {
        @SuppressWarnings("resource")
        ServerSocket serverSocket = new ServerSocket(destinationPort);
        Socket socket = serverSocket.accept();
        PrintWriter w = new PrintWriter(socket.getOutputStream(), true);
        InputStreamReader reader = new InputStreamReader(socket.getInputStream());
        try {
          while (!completed) {
            while (reader.ready()) {
              final int valueNew = reader.read();
              System.out.println("server test received from beacon client " + valueNew);
              if (last == 0)
                last = valueNew + 1;
              else {
                if (last + 1 != valueNew) {
                  throw new Exception("error in server test last cached:" + last + ",new:" + valueNew);
                } else {
                  last = valueNew + 1;
                }
              }
              Thread.sleep(1000);
              w.write(last);
              w.flush();
              System.out.println("server test sent to beacon client " + last);
            }
          }
        } catch (InterruptedException f) {
          serverSocket.close();
          System.out.println("server closed");
        } catch (Exception a) {
          serverSocket.close();
          System.out.println("server closed");
          a.printStackTrace();
        }
        serverSocket.close();
        return true;
      }
    };

    Callable<Boolean> clientRunner = new Callable<Boolean>() {
      private int last = 0;

      @Override
      public Boolean call() throws Exception {
        SocketAddress endpoint = new InetSocketAddress(destinationIp, srcPort);
        @SuppressWarnings("resource")
        Socket socketClient = new Socket();
        socketClient.connect(endpoint, 60000);
        socketClient.setKeepAlive(true);
        InputStreamReader reader = new InputStreamReader(socketClient.getInputStream());
        PrintWriter w = new PrintWriter(socketClient.getOutputStream(), true);
        w.write(1);
        w.flush();
        last = 1;
        System.out.println("client test sent to beacon server " + last);
        try {
          while (!completed) {
            while (reader.ready()) {
              final int valueNew = reader.read();
              System.out.println("client test received from beacon server " + valueNew);
              updateClientCounter(valueNew);
              if (last == 0)
                last = valueNew + 1;
              else {
                if (last + 1 != valueNew) {
                  throw new Exception("error in client test last cached:" + last + ",new:" + valueNew);
                } else {
                  last = valueNew + 1;
                }
              }
              Thread.sleep(1000);
              w.write(last);
              w.flush();
              System.out.println("client test sent to server beacon " + last);
            }
          }
        } catch (InterruptedException f) {
          socketClient.close();
          System.out.println("client closed");
        } catch (Exception a) {
          socketClient.close();
          System.out.println("client closed");
          a.printStackTrace();
        }
        socketClient.close();
        return true;
      }
    };
    // codice
    serverTCP = executor.submit(runner);
    Thread.sleep(2000);
    System.out.println("try to send package");
    clientTCP = executor.submit(clientRunner);
    Thread.sleep(60000);
    assertTrue(completed);
  }

  protected void updateClientCounter(int valueNew) {
    // System.out.println("counter: " + valueNew);
    if (valueNew > 42) {
      completed = true;
      clientTCP.cancel(true);
      serverTCP.cancel(true);
      System.out.println("package counter [R]:" + networkTunnel.getHub().getPacketReceived() + " [S]:"
          + networkTunnel.getHub().getPacketSend());
    }
  }

}
