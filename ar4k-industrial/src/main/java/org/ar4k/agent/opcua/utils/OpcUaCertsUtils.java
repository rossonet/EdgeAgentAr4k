package org.ar4k.agent.opcua.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;
import java.util.regex.Pattern;

import org.ar4k.agent.keystore.SelfSignedCertificateBuilder;
import org.ar4k.agent.keystore.SelfSignedCertificateGenerator;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.opcua.client.OpcUaClientService;
import org.eclipse.milo.opcua.sdk.server.util.HostnameUtil;

/**
 * 
 * @author andrea ambrosini
 *
 */
public class OpcUaCertsUtils {
	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(OpcUaClientService.class.toString());

	private static final Pattern IP_ADDR_PATTERN = Pattern
			.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

	private static final String CLIENT_ALIAS = "Ar4kAgent-opc";

	private static final String keyStorePassword = "password";

	private static final char[] PASSWORD = keyStorePassword.toCharArray();

	private X509Certificate clientCertificate = null;
	private KeyPair clientKeyPair = null;
	private PrivateKey privateKey = null;

	public OpcUaCertsUtils create() throws Exception {
		return create("Rossonet scarl " + UUID.randomUUID().toString(), "Rossonet", "IoT_OT", "Imola", "BO", "IT",
				"urn:rossonet:client:opc-ua:client", "localhost", "127.0.0.1");
	}

	public OpcUaCertsUtils create(String commonName, String organization, String unit, String locality, String state,
			String country, String uri, String dns, String ip) throws Exception {
		try {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			File serverKeyStore = new File("keystore.pfx");
			if (!serverKeyStore.exists()) {
				logger.info("CERTIFICATE GENERATION");
				keyStore.load(null, PASSWORD);

				KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);

				SelfSignedCertificateBuilder builder = new SelfSignedCertificateBuilder(keyPair)
						.setCommonName(commonName).setOrganization(organization).setOrganizationalUnit(unit)
						.setLocalityName(locality).setStateName(state).setCountryCode(country).setApplicationUri(uri)
						.addDnsName(dns).addIpAddress(ip);

				for (String hostname : HostnameUtil.getHostnames("0.0.0.0")) {
					if (IP_ADDR_PATTERN.matcher(hostname).matches()) {
						builder.addIpAddress(hostname);
					} else {
						builder.addDnsName(hostname);
					}
				}
				X509Certificate certificate = builder.build();
				keyStore.setKeyEntry(CLIENT_ALIAS, keyPair.getPrivate(), PASSWORD,
						new X509Certificate[] { certificate });
				keyStore.store(new FileOutputStream(serverKeyStore), PASSWORD);
			} else {
				keyStore.load(new FileInputStream(serverKeyStore), PASSWORD);
			}
			Key serverPrivateKey = keyStore.getKey(CLIENT_ALIAS, PASSWORD);
			if (serverPrivateKey instanceof PrivateKey) {
				clientCertificate = (X509Certificate) keyStore.getCertificate(CLIENT_ALIAS);
				PublicKey serverPublicKey = clientCertificate.getPublicKey();
				logger.debug("\n\n-----BEGIN CERTIFICATE-----\n"
						+ Base64.getEncoder().encodeToString(clientCertificate.getEncoded())
						+ "\n-----END CERTIFICATE-----\n\n");
				logger.debug("\n\n-----BEGIN RSA PRIVATE KEY-----\n"
						+ Base64.getEncoder().encodeToString(serverPrivateKey.getEncoded())
						+ "\n-----END RSA PRIVATE KEY-----\n\n");
				clientKeyPair = new KeyPair(serverPublicKey, (PrivateKey) serverPrivateKey);
				privateKey = (PrivateKey) serverPrivateKey;
			}
		} catch (Exception e) {
			throw new Exception(e);
		}
		return this;
	}

	public KeyPair getClientKeyPair() {
		return clientKeyPair;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public X509Certificate getClientCertificate() {
		return clientCertificate;
	}

	public String getPrivateKeyBase64() throws Exception {
		return Base64.getEncoder().encodeToString(privateKey.getEncoded());
	}

	public String getClientCertificateBase64() throws Exception, CertificateEncodingException {
		return Base64.getEncoder().encodeToString(clientCertificate.getEncoded());
	}

	public boolean setClientKeyPair(String key, String crt) throws Exception {
		logger.info("checking cert/key\nkey:" + key + "\ncrt:" + crt);
		boolean result = false;
		try {
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			File serverKeyStore = new File("keystore.pfx");
			serverKeyStore.deleteOnExit();
			keyStore.load(null, PASSWORD);
			String privateKeyContent = key;
			String crtContent = crt;
			KeyFactory kf = KeyFactory.getInstance("RSA");
			byte[] decodedCrt = Base64.getDecoder().decode(crtContent);
			clientCertificate = (X509Certificate) CertificateFactory.getInstance("X.509")
					.generateCertificate(new ByteArrayInputStream(decodedCrt));
			PublicKey pubKey = clientCertificate.getPublicKey();
			byte[] decodedKey = Base64.getDecoder().decode(privateKeyContent);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
			PrivateKey privKey = kf.generatePrivate(keySpec);
			clientKeyPair = new KeyPair(pubKey, privKey);
			privateKey = privKey;
			keyStore.setKeyEntry(CLIENT_ALIAS, clientKeyPair.getPrivate(), PASSWORD,
					new X509Certificate[] { clientCertificate });
			keyStore.store(new FileOutputStream(serverKeyStore), PASSWORD);
			result = true;
		} catch (Exception e) {
			logger.logException(e);
			throw new Exception(e);
		}
		return result;
	}

}
