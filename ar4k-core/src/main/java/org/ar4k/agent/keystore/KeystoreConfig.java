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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.interfaces.ConfigSeed;
import org.ar4k.agent.exception.EdgeException;
import org.ar4k.agent.helper.KeystoreLoader;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.joda.time.Instant;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Classe configurazione per keystore.
 *
 */
public class KeystoreConfig implements ConfigSeed {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(KeystoreConfig.class);

	private static final long serialVersionUID = 6291742061764165257L;

	public long creationDate = new Instant().getMillis();
	public long lastUpdate = new Instant().getMillis();

	public String uniqueId = UUID.randomUUID().toString();

	@Parameter(names = "--description", description = "keystore description")
	public String description = "";

	@Parameter(names = "--tags", description = "tags", variableArity = true)
	public List<String> tags = new ArrayList<>();

	@Parameter(names = "--filePath", description = "file path for the keystore")
	public String filePathPre = Homunculus.DEFAULT_KS_PATH;

	@Parameter(names = "--keystorePassword", description = "keystore password")
	public String keystorePassword = "secA4.rk!8";

	@Parameter(names = "--label", description = "keystore label")
	public String label = "master-keytool";

	@Parameter(names = "--caAlias", description = "main cert for the keystore")
	public String keyStoreAlias = "ca";

	public String filePath() {
		return filePathPre.replace("~", System.getProperty("user.home"));
	}

	public boolean check() {
		boolean verifica = false;
		try {
			if (KeystoreLoader.getClientKeyPair(keyStoreAlias, filePath(), keystorePassword) != null) {
				verifica = true;
			} else {
				logger.warn("Check for keystore " + filePath() + " failed, not found " + keyStoreAlias);
				StringBuilder sb = new StringBuilder();
				if (listCertificate() != null) {
					for (String value : listCertificate()) {
						sb.append(value + "\n");
					}
					logger.info("certs present in keystore:\n" + sb.toString());
				} else {
					logger.info("no certs present in keystore");
				}
			}
		} catch (UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | KeyStoreException
				| IOException e) {
			throw new EdgeException("problem with keystore file or password", e);
		}
		return verifica;
	}

	public boolean create(String commonName, String organization, String unit, String locality, String state,
			String country, String uri, String dns, String ip, String alias, boolean isCa, int validityDays) {
		boolean verifica = false;
		try {
			verifica = KeystoreLoader.create(commonName, organization, unit, locality, state, country, uri, dns, ip,
					alias, filePath(), keystorePassword, isCa, validityDays);
		} catch (Exception e) {
			throw new EdgeException("problem with keystore", e);
		}
		return verifica;
	}

	public PrivateKey getPrivateKey(String alias) {
		PrivateKey ritorno = null;
		try {
			ritorno = KeystoreLoader.getPrivateKey(alias, filePath(), keystorePassword);
		} catch (UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | KeyStoreException
				| IOException e) {
			throw new EdgeException("problem with keystore", e);
		}
		return ritorno;
	}

	public KeyPair getKeyPair(String alias) {
		KeyPair ritorno = null;
		try {
			ritorno = KeystoreLoader.getClientKeyPair(alias, filePath(), keystorePassword);
		} catch (UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | KeyStoreException
				| IOException e) {
			throw new EdgeException("problem with keystore", e);
		}
		return ritorno;
	}

	public String getPrivateKeyBase64(String alias) {
		String ritorno = null;
		try {
			ritorno = KeystoreLoader.getPrivateKeyBase64(alias, filePath(), keystorePassword);
		} catch (UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | KeyStoreException
				| IOException e) {
			throw new EdgeException("problem with keystore", e);
		}
		return ritorno;
	}

	public X509Certificate getClientCertificate(String alias) {
		X509Certificate ritorno = null;
		try {
			ritorno = KeystoreLoader.getClientCertificate(alias, filePath(), keystorePassword);
		} catch (UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | KeyStoreException
				| IOException e) {
			logger.logException(e);
			throw new EdgeException("problem with keystore", e);
		}
		return ritorno;
	}

	public String getCaPem(String alias) {
		String ritorno = null;
		try {
			ritorno = KeystoreLoader.getCertCaAsPem(alias, filePath(), keystorePassword);
		} catch (UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | KeyStoreException
				| IOException e) {
			throw new EdgeException("problem with keystore", e);
		}
		return ritorno;
	}

	public String getClientCertificateBase64(String alias) {
		String ritorno = null;
		try {
			ritorno = KeystoreLoader.getClientCertificateBase64(alias, filePath(), keystorePassword);
		} catch (UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | KeyStoreException
				| IOException e) {
			throw new EdgeException("problem with keystore", e);
		}
		return ritorno;
	}

	public PKCS10CertificationRequest getPKCS10CertificationRequest(String alias) {
		PKCS10CertificationRequest ritorno = null;
		try {
			ritorno = KeystoreLoader.getPKCS10CertificationRequest(alias, filePath(), keystorePassword);
		} catch (UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | KeyStoreException
				| IOException | OperatorCreationException e) {
			throw new EdgeException("problem with keystore", e);
		}
		return ritorno;
	}

	public String getPKCS10CertificationRequestBase64(String alias) {
		String ritorno = null;
		try {
			ritorno = KeystoreLoader.getPKCS10CertificationRequestBase64(alias, filePath(), keystorePassword);
		} catch (UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | KeyStoreException
				| IOException | OperatorCreationException e) {
			throw new EdgeException("problem with keystore", e);
		}
		return ritorno;
	}

	public boolean setClientKeyPair(String key, String crt, String alias) throws NoSuchAlgorithmException {
		boolean ritorno = false;
		ritorno = KeystoreLoader.setClientKeyPair(key, crt, alias, filePath(), keystorePassword);
		return ritorno;
	}

	public boolean setCa(String crt, String alias) {
		boolean ritorno = false;
		ritorno = KeystoreLoader.setCA(crt, alias, filePath(), keystorePassword);
		return ritorno;
	}

	public boolean createSelfSignedCert(String commonName, String organization, String unit, String locality,
			String state, String country, String uri, String dns, String ip, String alias, boolean isCa,
			int validityDays) {
		boolean ritorno = false;
		try {
			ritorno = KeystoreLoader.createSelfSignedCert(commonName, organization, unit, locality, state, country, uri,
					dns, ip, alias, filePath(), keystorePassword, isCa, validityDays);
		} catch (Exception e) {
			throw new EdgeException("problem with keystore", e);
		}
		return ritorno;
	}

	public X509Certificate signCertificate(PKCS10CertificationRequest csr, String targetAlias, int validity,
			String caAlias) {
		X509Certificate ritorno = null;
		ritorno = KeystoreLoader.signCertificate(csr, targetAlias, validity, caAlias, filePath(), keystorePassword);
		return ritorno;
	}

	public X509Certificate signCertificate(PKCS10CertificationRequest csr, String targetAlias, int validity,
			String caAlias, PrivateKey privateKey) {
		X509Certificate ritorno = null;
		ritorno = KeystoreLoader.signCertificate(csr, targetAlias, validity, caAlias, filePath(), keystorePassword,
				privateKey);
		return ritorno;
	}

	public String signCertificateBase64(PKCS10CertificationRequest csr, String targetAlias, int validity,
			String caAlias) {
		String ritorno = null;
		try {
			ritorno = KeystoreLoader.signCertificateBase64(csr, targetAlias, validity, caAlias, filePath(),
					keystorePassword);
		} catch (IOException | UnrecoverableKeyException | OperatorCreationException | KeyStoreException
				| NoSuchAlgorithmException | CertificateException | NoSuchProviderException | CMSException e) {
			logger.logException(e);
			throw new EdgeException("problem with keystore", e);
		}
		return ritorno;
	}

	public String signCertificateBase64(String csr, String targetAlias, int validity, String caAlias) {
		String ritorno = null;
		try {
			ritorno = KeystoreLoader.signCertificateBase64(csr, targetAlias, validity, caAlias, filePath(),
					keystorePassword);
		} catch (IOException | UnrecoverableKeyException | OperatorCreationException | KeyStoreException
				| NoSuchAlgorithmException | CertificateException | NoSuchProviderException | ClassNotFoundException
				| CMSException e) {
			logger.debug("KeystoreLoader.signCertificateBase64(" + csr + ", " + targetAlias + ", " + validity + ", "
					+ targetAlias + ", " + filePath() + ", " + keystorePassword + ")");
			try {
				byte[] data = Base64.getDecoder().decode(csr);
				PKCS10CertificationRequest csrDecoded = new PKCS10CertificationRequest(data);
				if (csrDecoded != null)
					logger.debug("CSR " + csrDecoded.getSubject().toString());
			} catch (Exception e1) {
				logger.logException(e1);
			}
			e.printStackTrace();
			throw new EdgeException("problem with keystore", e);
		}
		return ritorno;
	}

	public List<String> listCertificate() {
		List<String> ritorno = null;
		try {
			ritorno = KeystoreLoader.listCertificate(filePath(), keystorePassword);
		} catch (NoSuchAlgorithmException | CertificateException | KeyStoreException | IOException e) {
			logger.logException(e);
			throw new EdgeException("problem with keystore", e);
		}
		return ritorno;
	}

	public String toBase64() {
		String risposta = null;
		try {
			risposta = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(filePath())));
		} catch (IOException e) {
			throw new EdgeException("problem with keystore", e);
		}
		return risposta;
	}

	public void FromBase64(String FileContentBite) {
		byte[] data = Base64.getDecoder().decode(FileContentBite);
		try {
			FileUtils.writeByteArrayToFile(new File(filePath()), data);
		} catch (IOException e) {
			throw new EdgeException("problem with keystore", e);
		}
	}

	@Override
	public String getName() {
		return label;
	}

	@Override
	public long getCreationDate() {
		return creationDate;
	}

	@Override
	public long getLastUpdate() {
		return lastUpdate;
	}

	@Override
	public String getUniqueId() {
		return uniqueId;
	}

	@Override
	public String toString() {
		return "KeystoreConfig [creationDate=" + creationDate + ", lastUpdate=" + lastUpdate + ", uniqueId=" + uniqueId
				+ ", filePathPre=" + filePathPre + ", keystorePassword=*********" + ", label=" + label + ", caAlias="
				+ keyStoreAlias + "]";
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public List<String> getTags() {
		return tags;
	}

}
