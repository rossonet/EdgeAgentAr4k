/**
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    */
package org.ar4k.agent.keystore;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.ar4k.agent.exception.Ar4kException;
import org.ar4k.agent.helper.NetworkHelper;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.bc.BcRSAContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;

/*
 * 
 * @author Andrea Andrea
 * 
 *         Helper keystore.
 */

public class KeystoreLoader {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(KeystoreLoader.class.toString());

  public static final String demoKeystore = "test.keystore";
  private static final String demoPassword = "secA4.rk!8";

  private static final Pattern IP_ADDR_PATTERN = Pattern
      .compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
  // private X509Certificate clientCertificate;
  // private KeyPair clientKeyPair;
  // private PrivateKey privateKey;

  public static void create() throws Exception {
    KeystoreLoader.create("rossonet-" + UUID.randomUUID().toString(), "Rossonet s.c.a r.l.", "Ar4k", "Imola", "BO",
        "IT", "urn:org.ar4k.agent:ca_rossonet-key-agent", "ca.test.ar4k.net", "127.0.0.1", demoKeystore, "master",
        demoPassword);
  }

  public static boolean create(String commonName, String organization, String unit, String locality, String state,
      String country, String uri, String dns, String ip, String keyStorePath, String alias, String password)
      throws Exception {
    char[] passwordChar = password.toCharArray();
    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    File serverKeyStore = new File(keyStorePath);
    if (!serverKeyStore.exists()) {
      logger.debug("create keystore " + keyStorePath);
      keyStore.load(null, passwordChar);
    } else {
      logger.debug("load keystore " + keyStorePath);
      keyStore.load(new FileInputStream(serverKeyStore), passwordChar);
    }
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    SelfSignedCertificateBuilder builder = new SelfSignedCertificateBuilder(keyPair).setCommonName(commonName)
        .setOrganization(organization).setOrganizationalUnit(unit).setLocalityName(locality).setStateName(state)
        .setCountryCode(country).setApplicationUri(uri).addDnsName(dns).addIpAddress(ip);
    // Get as many hostnames and IP addresses as we can listed in the certificate.
    for (String hostname : NetworkHelper.getHostnames("0.0.0.0")) {
      if (IP_ADDR_PATTERN.matcher(hostname).matches()) {
        builder.addIpAddress(hostname);
      } else {
        builder.addDnsName(hostname);
      }
    }
    X509Certificate certificate = builder.build();
    keyStore.setKeyEntry(alias, keyPair.getPrivate(), passwordChar, new X509Certificate[] { certificate });
    keyStore.store(new FileOutputStream(serverKeyStore), passwordChar);
    serverKeyStore = null;
    return true;

  }

  public static boolean setCA(String crt, String keyStorePath, String alias, String password) {
    char[] passwordChar = password.toCharArray();
    boolean ritorno = false;
    try {
      KeyStore keyStore = KeyStore.getInstance("PKCS12");
      File serverKeyStore = new File(keyStorePath);
      serverKeyStore.deleteOnExit();
      keyStore.load(null, passwordChar);
      String crtContent = crt;
      byte[] decodedCrt = Base64.getDecoder().decode(crtContent);
      X509Certificate caCertificate = (X509Certificate) CertificateFactory.getInstance("X.509")
          .generateCertificate(new ByteArrayInputStream(decodedCrt));
      logger.info("CertCA to store: " + crt);
      logger.info("CertCA: " + caCertificate.toString());
      keyStore.setCertificateEntry(alias, caCertificate);
      keyStore.store(new FileOutputStream(serverKeyStore), passwordChar);
      ritorno = true;
      serverKeyStore = null;
    } catch (Exception df) {
      ritorno = false;
      logger.logException(df);
    }
    return ritorno;
  }

  public static boolean setClientKeyPair(String key, String crt, String alias) {
    return KeystoreLoader.setClientKeyPair(key, crt, demoKeystore, alias, demoPassword);
  }

  public static boolean setClientKeyPair(String key, String crt, String keyStorePath, String alias, String password) {
    char[] passwordChar = password.toCharArray();
    boolean ritorno = false;
    try {
      KeyStore keyStore = KeyStore.getInstance("PKCS12");
      File serverKeyStore = new File(keyStorePath);
      serverKeyStore.deleteOnExit();
      keyStore.load(null, passwordChar);
      String privateKeyContent = key;
      String crtContent = crt;
      KeyFactory kf = KeyFactory.getInstance("RSA");
      byte[] decodedCrt = Base64.getDecoder().decode(crtContent);
      X509Certificate clientCertificate = (X509Certificate) CertificateFactory.getInstance("X.509")
          .generateCertificate(new ByteArrayInputStream(decodedCrt));
      PublicKey pubKey = clientCertificate.getPublicKey();
      byte[] decodedKey = Base64.getDecoder().decode(privateKeyContent);
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
      PrivateKey privKey = kf.generatePrivate(keySpec);
      KeyPair clientKeyPair = new KeyPair(pubKey, (PrivateKey) privKey);
      // PrivateKey privateKey = privKey;
      keyStore.setKeyEntry(alias, clientKeyPair.getPrivate(), passwordChar,
          new X509Certificate[] { clientCertificate });
      keyStore.store(new FileOutputStream(serverKeyStore), passwordChar);
      ritorno = true;
      serverKeyStore = null;
    } catch (Exception df) {
      ritorno = false;
      logger.logException(df);
    }
    return ritorno;
  }

  public static boolean createSelfSignedCert(String commonName, String organization, String unit, String locality,
      String state, String country, String uri, String dns, String ip, String keyStorePath, String alias,
      String password) {
    try {
      char[] passwordChar = password.toCharArray();
      KeyStore keyStore = KeyStore.getInstance("PKCS12");
      File serverKeyStore = new File(keyStorePath);
      if (serverKeyStore.exists()) {
        keyStore.load(new FileInputStream(serverKeyStore), passwordChar);
      } else {
        keyStore.load(null, passwordChar);
      }
      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      SelfSignedCertificateBuilder builder = new SelfSignedCertificateBuilder(keyPair).setCommonName(commonName)
          .setOrganization(organization).setOrganizationalUnit(unit).setLocalityName(locality).setStateName(state)
          .setCountryCode(country).setApplicationUri(uri).addDnsName(dns).addIpAddress(ip);
      // Get as many hostnames and IP addresses as we can listed in the certificate.
      for (String hostname : NetworkHelper.getHostnames("0.0.0.0")) {
        if (IP_ADDR_PATTERN.matcher(hostname).matches()) {
          builder.addIpAddress(hostname);
        } else {
          builder.addDnsName(hostname);
        }
      }
      X509Certificate certificate = builder.build();
      keyStore.setKeyEntry(alias, keyPair.getPrivate(), passwordChar, new X509Certificate[] { certificate });
      keyStore.store(new FileOutputStream(serverKeyStore), passwordChar);
      serverKeyStore = null;
      return true;
    } catch (Exception a) {
      logger.logException(a);
      return false;
    }
  }

  public static boolean createSelfSignedCert() throws Exception {
    return createSelfSignedCert("agent-" + UUID.randomUUID().toString(), "Rossonet scarl", "Ar4kAgent", "Imola", "BO",
        "IT", "urn:org.ar4k.agent:client-test1", "agent.test.ar4k.net", "127.0.0.1", demoKeystore, "client1",
        demoPassword);
  }

  public static PrivateKey getPrivateKey(String keyStorePath, String alias, String password) throws KeyStoreException,
      UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
    char[] passwordChar = password.toCharArray();
    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    File serverKeyStore = new File(keyStorePath);
    keyStore.load(new FileInputStream(serverKeyStore), passwordChar);
    Key serverPrivateKey = keyStore.getKey(alias, passwordChar);
    PrivateKey privateKey = null;
    if (serverPrivateKey instanceof PrivateKey) {
      // X509Certificate clientCertificate = (X509Certificate)
      // keyStore.getCertificate(alias);
      // PublicKey serverPublicKey = clientCertificate.getPublicKey();
      /*
       * logger.debug("\n\n-----BEGIN CERTIFICATE-----\n" +
       * Base64.getEncoder().encodeToString(clientCertificate.getEncoded()) +
       * "\n-----END CERTIFICATE-----\n\n");
       * logger.debug("\n\n-----BEGIN RSA PRIVATE KEY-----\n" +
       * Base64.getEncoder().encodeToString(serverPrivateKey.getEncoded()) +
       * "\n-----END RSA PRIVATE KEY-----\n\n");
       */
      // KeyPair clientKeyPair = new KeyPair(serverPublicKey, (PrivateKey)
      // serverPrivateKey);
      privateKey = (PrivateKey) serverPrivateKey;
    }
    serverKeyStore = null;
    return privateKey;
  }

  public static PrivateKey getPrivateKey(String alias) throws UnrecoverableKeyException, KeyStoreException,
      NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
    return KeystoreLoader.getPrivateKey(demoKeystore, alias, demoPassword);
  }

  public static KeyStore getKeyStoreAfterLoad()
      throws NoSuchAlgorithmException, KeyStoreException, CertificateException, FileNotFoundException, IOException {
    return getKeyStoreAfterLoad(demoKeystore, demoPassword);
  }

  public static KeyStore getKeyStoreAfterLoad(KeystoreConfig keystoreConfig)
      throws NoSuchAlgorithmException, KeyStoreException, CertificateException, FileNotFoundException, IOException {
    return getKeyStoreAfterLoad(keystoreConfig.filePathPre, keystoreConfig.keystorePassword);
  }

  public static KeyStore getKeyStoreAfterLoad(String keystoreConfig, Set<KeystoreConfig> listKeyStores)
      throws NoSuchAlgorithmException, KeyStoreException, CertificateException, FileNotFoundException, IOException {
    KeystoreConfig ok = null;
    for (KeystoreConfig t : listKeyStores) {
      if (t.label.equals(keystoreConfig) && t.check()) {
        ok = t;
      }
    }
    return getKeyStoreAfterLoad(ok);
  }

  public static String getPasswordKeystore(String keystoreConfig, Set<KeystoreConfig> listKeyStores)
      throws NoSuchAlgorithmException, KeyStoreException, CertificateException, FileNotFoundException, IOException {
    String ok = null;
    for (KeystoreConfig t : listKeyStores) {
      if (t.label.equals(keystoreConfig) && t.check()) {
        ok = t.keystorePassword;
      }
    }
    return ok;
  }

  public static KeyStore getKeyStoreAfterLoad(String keyStorePath, String password)
      throws NoSuchAlgorithmException, KeyStoreException, CertificateException, FileNotFoundException, IOException {
    char[] passwordChar = password.toCharArray();
    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    File serverKeyStore = new File(keyStorePath);
    keyStore.load(new FileInputStream(serverKeyStore), passwordChar);
    return keyStore;
  }

  public static KeyPair getClientKeyPair(String keyStorePath, String alias, String password)
      throws NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, KeyStoreException,
      UnrecoverableKeyException {
    char[] passwordChar = password.toCharArray();
    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    File serverKeyStore = new File(keyStorePath);
    keyStore.load(new FileInputStream(serverKeyStore), passwordChar);
    Key serverPrivateKey = keyStore.getKey(alias, passwordChar);
    // PrivateKey privateKey = null;
    KeyPair clientKeyPair = null;
    if (serverPrivateKey instanceof PrivateKey) {
      X509Certificate clientCertificate = (X509Certificate) keyStore.getCertificate(alias);
      PublicKey serverPublicKey = clientCertificate.getPublicKey();
      /*
       * logger.debug("\n\n-----BEGIN CERTIFICATE-----\n" +
       * Base64.getEncoder().encodeToString(clientCertificate.getEncoded()) +
       * "\n-----END CERTIFICATE-----\n\n");
       * logger.debug("\n\n-----BEGIN RSA PRIVATE KEY-----\n" +
       * Base64.getEncoder().encodeToString(serverPrivateKey.getEncoded()) +
       * "\n-----END RSA PRIVATE KEY-----\n\n");
       */
      clientKeyPair = new KeyPair(serverPublicKey, (PrivateKey) serverPrivateKey);
      // privateKey = (PrivateKey) serverPrivateKey;
    }
    serverKeyStore = null;
    return clientKeyPair;
  }

  public static KeyPair getClientKeyPair(String alias) throws NoSuchAlgorithmException, CertificateException,
      FileNotFoundException, IOException, KeyStoreException, UnrecoverableKeyException {
    return KeystoreLoader.getClientKeyPair(demoKeystore, alias, demoPassword);
  }

  public static X509Certificate getClientCertificate(String keyStorePath, String alias, String password)
      throws NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, KeyStoreException,
      UnrecoverableKeyException {
    char[] passwordChar = password.toCharArray();
    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    File serverKeyStore = new File(keyStorePath);
    keyStore.load(new FileInputStream(serverKeyStore), passwordChar);
    Key serverPrivateKey = keyStore.getKey(alias, passwordChar);
    // PrivateKey privateKey = null;
    // KeyPair clientKeyPair = null;
    X509Certificate clientCertificate = null;
    if (serverPrivateKey instanceof PrivateKey) {
      clientCertificate = (X509Certificate) keyStore.getCertificate(alias);
      // PublicKey serverPublicKey = clientCertificate.getPublicKey();
      /*
       * logger.debug("\n\n-----BEGIN CERTIFICATE-----\n" +
       * Base64.getEncoder().encodeToString(clientCertificate.getEncoded()) +
       * "\n-----END CERTIFICATE-----\n\n");
       * logger.debug("\n\n-----BEGIN RSA PRIVATE KEY-----\n" +
       * Base64.getEncoder().encodeToString(serverPrivateKey.getEncoded()) +
       * "\n-----END RSA PRIVATE KEY-----\n\n");
       */
      // clientKeyPair = new KeyPair(serverPublicKey, (PrivateKey) serverPrivateKey);
      // privateKey = (PrivateKey) serverPrivateKey;
    }
    serverKeyStore = null;
    return clientCertificate;
  }

  public static String getCertCaAsPem(String keyStorePath, String alias, String password)
      throws NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, KeyStoreException,
      UnrecoverableKeyException {
    char[] passwordChar = password.toCharArray();
    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    File serverKeyStore = new File(keyStorePath);
    keyStore.load(new FileInputStream(serverKeyStore), passwordChar);
    Certificate caCert = keyStore.getCertificate(alias);
    String clientCertificate = null;
    if (caCert instanceof Certificate) {
      clientCertificate = Base64.getEncoder().encodeToString(caCert.getEncoded());
    }
    serverKeyStore = null;
    return clientCertificate;
  }

  public static X509Certificate getClientCertificate(String alias) throws NoSuchAlgorithmException,
      CertificateException, FileNotFoundException, IOException, KeyStoreException, UnrecoverableKeyException {
    return KeystoreLoader.getClientCertificate(demoKeystore, alias, demoPassword);
  }

  public static PKCS10CertificationRequest getPKCS10CertificationRequest(String keyStorePath, String alias,
      String password) throws UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException,
      FileNotFoundException, KeyStoreException, IOException, OperatorCreationException {
    KeyPair k = getClientKeyPair(keyStorePath, alias, password);
    X509Certificate c = getClientCertificate(keyStorePath, alias, password);
    char[] passwordChar = password.toCharArray();
    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    File serverKeyStore = new File(keyStorePath);
    keyStore.load(new FileInputStream(serverKeyStore), passwordChar);
    X500Name x500 = (X500Name) new JcaX509CertificateHolder(c).getSubject();
    byte[] encoded = k.getPublic().getEncoded();
    SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(ASN1Sequence.getInstance(encoded));
    PKCS10CertificationRequestBuilder p10Builder = new PKCS10CertificationRequestBuilder(x500, subjectPublicKeyInfo);
    JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withRSA");
    ContentSigner signer = csBuilder.build(k.getPrivate());
    PKCS10CertificationRequest csr = p10Builder.build(signer);
    return csr;
  }

  public static PKCS10CertificationRequest getPKCS10CertificationRequest(String alias)
      throws UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, FileNotFoundException,
      KeyStoreException, IOException, OperatorCreationException {
    return getPKCS10CertificationRequest(demoKeystore, alias, demoPassword);
  }

  public static String getPKCS10CertificationRequestBase64(String keyStorePath, String alias, String password)
      throws UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, FileNotFoundException,
      KeyStoreException, OperatorCreationException, IOException {
    PKCS10CertificationRequest csrPreEncode = getPKCS10CertificationRequest(keyStorePath, alias, password);
    return Base64.getEncoder().encodeToString(csrPreEncode.getEncoded());
  }

  public static String signCertificateBase64(PKCS10CertificationRequest csr, String targetAlias, int validity,
      String keyStorePath, String alias, String password)
      throws CertificateEncodingException, UnrecoverableKeyException, OperatorCreationException, KeyStoreException,
      NoSuchAlgorithmException, CertificateException, NoSuchProviderException, IOException, CMSException {
    return Base64.getEncoder()
        .encodeToString(signCertificate(csr, targetAlias, validity, keyStorePath, alias, password).getEncoded());
  }

  public static String signCertificateBase64(String csr, String targetAlias, int validity, String keyStorePath,
      String alias, String password) throws CertificateEncodingException, UnrecoverableKeyException,
      OperatorCreationException, KeyStoreException, NoSuchAlgorithmException, CertificateException,
      NoSuchProviderException, IOException, CMSException, ClassNotFoundException {
    return Base64.getEncoder()
        .encodeToString(signCertificate(csr, targetAlias, validity, keyStorePath, alias, password).getEncoded());
  }

  public static X509Certificate signCertificate(String csr, String targetAlias, int validity, String keyStorePath,
      String alias, String password)
      throws IOException, UnrecoverableKeyException, OperatorCreationException, KeyStoreException,
      NoSuchAlgorithmException, CertificateException, NoSuchProviderException, CMSException, ClassNotFoundException {
    byte[] data = Base64.getDecoder().decode(csr);
    PKCS10CertificationRequest csrDecoded = new PKCS10CertificationRequest(data);
    return signCertificate(csrDecoded, targetAlias, validity, keyStorePath, alias, password);
  }

  public static X509Certificate signCertificate(PKCS10CertificationRequest csr, String targetAlias, int validity,
      String keyStorePath, String alias, String password) {
    try {
      AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find("SHA256withRSA");
      AlgorithmIdentifier digAlgId = new DefaultDigestAlgorithmIdentifierFinder().find(sigAlgId);
      PrivateKey cakey = getPrivateKey(keyStorePath, alias, password);
      logger.info("-- SIGN WITH PUBLIC KEY --\n" + keyStorePath + ", " + alias + " ->");
      if (getClientKeyPair(keyStorePath, alias, password) == null
          || getClientKeyPair(keyStorePath, alias, password).getPublic() == null) {
        logger.info("NO PUBLIC KEY FOR SIGN THE CERTIFICATE");
      } else {
        logger.info(((X509Certificate) getClientCertificate(keyStorePath, alias, password)).toString());
      }
      X509v3CertificateBuilder certificateGenerator = new X509v3CertificateBuilder(
          // These are the details of the CA
          // new X500Name("C=US, L=Vienna, O=Your CA Inc")
          // new X500Name(getClientCertificate(keyStorePath, alias,
          // password).getIssuerX500Principal()
          // .getName(X500Principal.RFC2253)),
          csr.getSubject(),
          // This should be a serial number that the CA keeps track of
          new BigInteger(64, new SecureRandom()),
          // Certificate validity start
          Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)),
          // Certificate validity end
          Date.from(LocalDateTime.now().plusDays(validity).toInstant(ZoneOffset.UTC)),
          // Blanket grant the subject as requested in the CSR
          // A real CA would want to vet this.
          csr.getSubject(),
          // Public key of the certificate authority
          SubjectPublicKeyInfo.getInstance(
              ASN1Sequence.getInstance(getClientKeyPair(keyStorePath, alias, password).getPublic().getEncoded())));
      ContentSigner sigGen = new BcRSAContentSignerBuilder(sigAlgId, digAlgId)
          .build(PrivateKeyFactory.createKey(cakey.getEncoded()));
      X509CertificateHolder holder = certificateGenerator.build(sigGen);
      CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
      X509Certificate certificate = (X509Certificate) certificateFactory
          .generateCertificate(new ByteArrayInputStream(holder.toASN1Structure().getEncoded()));
      char[] passwordChar = password.toCharArray();
      KeyStore keyStore = KeyStore.getInstance("PKCS12");
      File serverKeyStore = new File(keyStorePath);
      keyStore.load(new FileInputStream(serverKeyStore), passwordChar);
      StringBuilder kstorePresents = new StringBuilder();
      Enumeration<String> listAliases = keyStore.aliases();
      boolean primo = true;
      while (listAliases.hasMoreElements()) {
        if (primo == true) {
          kstorePresents.append(listAliases.nextElement());
          primo = false;
        } else {
          kstorePresents.append(", " + listAliases.nextElement());
        }
      }
      logger
          .info("saving in keystore alias " + targetAlias + " present in keystore are : " + kstorePresents.toString());
      keyStore.setCertificateEntry(targetAlias, certificate);
      keyStore.store(new FileOutputStream(serverKeyStore), passwordChar);
      /*
       * System.out.println("-----BEGIN PKCS #7 SIGNED DATA-----\n");
       * System.out.println(Base64.getEncoder().encodeToString(signeddata.getEncoded()
       * )); System.out.println("\n-----END PKCS #7 SIGNED DATA-----\n");
       */
      X509Certificate clientCertificate = (X509Certificate) keyStore.getCertificate(targetAlias);
      return clientCertificate;
    } catch (Ar4kException | KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException
        | OperatorCreationException | UnrecoverableKeyException a) {
      logger.logException(a);
      return null;
    }
  }

  public static X509Certificate signCertificate(PKCS10CertificationRequest csr, String targetAlias, int validity,
      String alias) throws UnrecoverableKeyException, OperatorCreationException, KeyStoreException,
      NoSuchAlgorithmException, CertificateException, IOException, CMSException, NoSuchProviderException {
    return signCertificate(csr, targetAlias, validity, demoKeystore, alias, demoPassword);
  }

  public static X509Certificate signCertificate(String csr, String targetAlias, int validity, String alias)
      throws UnrecoverableKeyException, OperatorCreationException, KeyStoreException, NoSuchAlgorithmException,
      CertificateException, IOException, CMSException, NoSuchProviderException, ClassNotFoundException {
    return signCertificate(csr, targetAlias, validity, demoKeystore, alias, demoPassword);
  }

  public static String signCertificateBase64(PKCS10CertificationRequest csr, String targetAlias, int validity,
      String alias) throws UnrecoverableKeyException, OperatorCreationException, KeyStoreException,
      NoSuchAlgorithmException, CertificateException, IOException, CMSException, NoSuchProviderException {
    return signCertificateBase64(csr, targetAlias, validity, demoKeystore, alias, demoPassword);
  }

  public static String signCertificateBase64(String csr, String targetAlias, int validity, String alias)
      throws UnrecoverableKeyException, OperatorCreationException, KeyStoreException, NoSuchAlgorithmException,
      CertificateException, IOException, CMSException, NoSuchProviderException, ClassNotFoundException {
    return signCertificateBase64(csr, targetAlias, validity, demoKeystore, alias, demoPassword);
  }

  public static String getPKCS10CertificationRequestBase64(String alias)
      throws UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, FileNotFoundException,
      KeyStoreException, OperatorCreationException, IOException {
    return getPKCS10CertificationRequestBase64(demoKeystore, alias, demoPassword);
  }

  public static String getPrivateKeyBase64(String keyStorePath, String alias, String password)
      throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException,
      FileNotFoundException, IOException {
    return Base64.getEncoder().encodeToString(KeystoreLoader.getPrivateKey(keyStorePath, alias, password).getEncoded());
  }

  public static String getClientCertificateBase64(String keyStorePath, String alias, String password)
      throws CertificateEncodingException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException,
      FileNotFoundException, KeyStoreException, IOException {
    return KeystoreLoader.getClientCertificate(keyStorePath, alias, password) != null ? Base64.getEncoder()
        .encodeToString(KeystoreLoader.getClientCertificate(keyStorePath, alias, password).getEncoded()) : null;
  }

  public static String getPrivateKeyBase64(String alias) throws UnrecoverableKeyException, KeyStoreException,
      NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
    return Base64.getEncoder().encodeToString(KeystoreLoader.getPrivateKey(alias).getEncoded());
  }

  public static String getClientCertificateBase64(String alias)
      throws CertificateEncodingException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException,
      FileNotFoundException, KeyStoreException, IOException {
    return Base64.getEncoder().encodeToString(KeystoreLoader.getClientCertificate(alias).getEncoded());
  }

  public static List<String> listCertificate(String keyStorePath, String password)
      throws NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, KeyStoreException {
    char[] passwordChar = password.toCharArray();
    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    File serverKeyStore = new File(keyStorePath);
    keyStore.load(new FileInputStream(serverKeyStore), passwordChar);
    // System.out.println(keyStore.size());
    List<String> ritorno = new ArrayList<String>();
    Enumeration<String> enu = keyStore.aliases();
    while (enu.hasMoreElements()) {
      ritorno.add(enu.nextElement());
    }
    return ritorno;
  }

  public static List<String> listCertificate()
      throws NoSuchAlgorithmException, CertificateException, FileNotFoundException, KeyStoreException, IOException {
    return listCertificate(demoKeystore, demoPassword);
  }
}
