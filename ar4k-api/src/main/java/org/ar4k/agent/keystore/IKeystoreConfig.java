package org.ar4k.agent.keystore;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;

public interface IKeystoreConfig {

	String filePath();

	boolean check();

	boolean create(String commonName, String organization, String unit, String locality, String state, String country,
			String uri, String dns, String ip, String alias, boolean isCa, int validityDays);

	PrivateKey getPrivateKey(String alias);

	KeyPair getKeyPair(String alias);

	String getPrivateKeyBase64(String alias);

	X509Certificate getClientCertificate(String alias);

	String getCaPem(String alias);

	String getClientCertificateBase64(String alias);

	PKCS10CertificationRequest getPKCS10CertificationRequest(String alias);

	String getPKCS10CertificationRequestBase64(String alias);

	boolean setClientKeyPair(String key, String crt, String alias) throws NoSuchAlgorithmException;

	boolean setCa(String crt, String alias);

	boolean createSelfSignedCert(String commonName, String organization, String unit, String locality, String state,
			String country, String uri, String dns, String ip, String alias, boolean isCa, int validityDays);

	X509Certificate signCertificate(PKCS10CertificationRequest csr, String targetAlias, int validity, String caAlias);

	X509Certificate signCertificate(PKCS10CertificationRequest csr, String targetAlias, int validity, String caAlias,
			PrivateKey privateKey);

	String signCertificateBase64(PKCS10CertificationRequest csr, String targetAlias, int validity, String caAlias);

	String signCertificateBase64(String csr, String targetAlias, int validity, String caAlias);

	List<String> listCertificate();

	String toBase64();

	void FromBase64(String FileContentBite);

	String getName();

	long getCreationDate();

	long getLastUpdate();

	String getUniqueId();

	String getDescription();

	List<String> getTags();

	String getKeystorePassword();

	String getLabel();

	String getFilePathPre();

}