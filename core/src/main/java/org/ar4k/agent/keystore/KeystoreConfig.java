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
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.core.Ar4kComponent;
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

  private static final long serialVersionUID = 6291742061764165257L;

  public Instant creationDate = new Instant();
  public Instant lastUpdate = new Instant();
  public UUID uniqueId = UUID.randomUUID();

  @Parameter(names = "--filePath", description = "file path for the keystore")
  public String filePathPre = "./default.keystore";

  @Parameter(names = "--keystorePassword", description = "keystore password")
  public String keystorePassword = "secA4.rk!8";

  @Parameter(names = "--label", description = "keystore label")
  public String label = "master-keytool";

  @Parameter(names = "--caAlias", description = "main cert for the keystore")
  public String caAlias = "ca";

  public String filePath() {
    return filePathPre.replaceFirst("^~", System.getProperty("user.home"));
  }

  public boolean check() {
    boolean verifica = false;
    try {
      if (KeystoreLoader.getClientKeyPair(filePath(), caAlias, keystorePassword) != null) {
        verifica = true;
      } else {
        System.out.println("Check for keystore " + filePath() + " failed");
      }
    } catch (UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | KeyStoreException
        | IOException e) {
      e.printStackTrace();
    }
    return verifica;
  }

  public boolean create(String commonName, String organization, String unit, String locality, String state,
      String country, String uri, String dns, String ip) {
    boolean verifica = false;
    try {
      verifica = KeystoreLoader.create(commonName, organization, unit, locality, state, country, uri, dns, ip,
          filePath(), caAlias, keystorePassword);
    } catch (Exception e) {
      // TODO: gestire eccezioni nella gestione del keystore
      e.printStackTrace();
    }
    return verifica;
  }

  public PrivateKey getPrivateKey(String alias) {
    PrivateKey ritorno = null;
    try {
      ritorno = KeystoreLoader.getPrivateKey(filePath(), alias, keystorePassword);
    } catch (UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | KeyStoreException
        | IOException e) {
      // TODO: gestire eccezioni nella gestione del keystore
      e.printStackTrace();
    }
    return ritorno;
  }

  public String getPrivateKeyBase64(String alias) {
    String ritorno = null;
    try {
      ritorno = KeystoreLoader.getPrivateKeyBase64(filePath(), alias, keystorePassword);
    } catch (UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | KeyStoreException
        | IOException e) {
      // TODO: gestire eccezioni nella gestione del keystore
      e.printStackTrace();
    }
    return ritorno;
  }

  public X509Certificate getClientCertificate(String alias) {
    X509Certificate ritorno = null;
    try {
      ritorno = KeystoreLoader.getClientCertificate(filePath(), alias, keystorePassword);
    } catch (UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | KeyStoreException
        | IOException e) {
      // TODO: gestire eccezioni nella gestione del keystore
      e.printStackTrace();
    }
    return ritorno;
  }

  public String getClientCertificateBase64(String alias) {
    String ritorno = null;
    try {
      ritorno = KeystoreLoader.getClientCertificateBase64(filePath(), alias, keystorePassword);
    } catch (UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | KeyStoreException
        | IOException e) {
      // TODO: gestire eccezioni nella gestione del keystore
      e.printStackTrace();
    }
    return ritorno;
  }

  public PKCS10CertificationRequest getPKCS10CertificationRequest(String alias) {
    PKCS10CertificationRequest ritorno = null;
    try {
      ritorno = KeystoreLoader.getPKCS10CertificationRequest(filePath(), alias, keystorePassword);
    } catch (UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | KeyStoreException
        | IOException | OperatorCreationException e) {
      // TODO: gestire eccezioni nella gestione del keystore
      e.printStackTrace();
    }
    return ritorno;
  }

  public String getPKCS10CertificationRequestBase64(String alias) {
    String ritorno = null;
    try {
      ritorno = KeystoreLoader.getPKCS10CertificationRequestBase64(filePath(), alias, keystorePassword);
    } catch (UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException | KeyStoreException
        | IOException | OperatorCreationException e) {
      // TODO: gestire eccezioni nella gestione del keystore
      e.printStackTrace();
    }
    return ritorno;
  }

  public boolean setClientKeyPair(String key, String crt, String alias) throws NoSuchAlgorithmException {
    boolean ritorno = false;
    ritorno = KeystoreLoader.setClientKeyPair(key, crt, filePath(), alias, keystorePassword);
    return ritorno;
  }

  public boolean createSelfSignedCert(String commonName, String organization, String unit, String locality,
      String state, String country, String uri, String dns, String ip, String alias) {
    boolean ritorno = false;
    try {
      ritorno = KeystoreLoader.createSelfSignedCert(commonName, organization, unit, locality, state, country, uri, dns,
          ip, filePath(), alias, keystorePassword);
    } catch (Exception e) {
      // TODO: gestire eccezioni nella gestione del keystore
      e.printStackTrace();
    }
    return ritorno;
  }

  public X509Certificate signCertificate(PKCS10CertificationRequest csr, String targetAlias, int validity,
      String alias) {
    X509Certificate ritorno = null;
    try {
      ritorno = KeystoreLoader.signCertificate(csr, targetAlias, validity, filePath(), targetAlias, keystorePassword);
    } catch (IOException | UnrecoverableKeyException | OperatorCreationException | KeyStoreException
        | NoSuchAlgorithmException | CertificateException | NoSuchProviderException | CMSException e) {
      // TODO: gestire eccezioni nella gestione del keystore
      e.printStackTrace();
    }
    return ritorno;
  }

  public String signCertificateBase64(PKCS10CertificationRequest csr, String targetAlias, int validity, String alias) {
    String ritorno = null;
    try {
      ritorno = KeystoreLoader.signCertificateBase64(csr, targetAlias, validity, filePath(), targetAlias,
          keystorePassword);
    } catch (IOException | UnrecoverableKeyException | OperatorCreationException | KeyStoreException
        | NoSuchAlgorithmException | CertificateException | NoSuchProviderException | CMSException e) {
      // TODO: gestire eccezioni nella gestione del keystore
      e.printStackTrace();
    }
    return ritorno;
  }

  public X509Certificate signCertificate(String csr, String targetAlias, int validity, String alias) {
    X509Certificate ritorno = null;
    try {
      ritorno = KeystoreLoader.signCertificate(csr, targetAlias, validity, filePath(), targetAlias, keystorePassword);
    } catch (IOException | UnrecoverableKeyException | OperatorCreationException | KeyStoreException
        | NoSuchAlgorithmException | CertificateException | NoSuchProviderException | ClassNotFoundException
        | CMSException e) {
      // TODO: gestire eccezioni nella gestione del keystore
      e.printStackTrace();
    }
    return ritorno;
  }

  public String signCertificateBase64(String csr, String targetAlias, int validity, String alias) {
    String ritorno = null;
    try {
      ritorno = KeystoreLoader.signCertificateBase64(csr, targetAlias, validity, filePath(), targetAlias,
          keystorePassword);
    } catch (IOException | UnrecoverableKeyException | OperatorCreationException | KeyStoreException
        | NoSuchAlgorithmException | CertificateException | NoSuchProviderException | ClassNotFoundException
        | CMSException e) {
      // TODO: gestire eccezioni nella gestione del keystore
      e.printStackTrace();
    }
    return ritorno;
  }

  public List<String> listCertificate() {
    List<String> ritorno = null;
    try {
      ritorno = KeystoreLoader.listCertificate(filePath(), keystorePassword);
    } catch (NoSuchAlgorithmException | CertificateException | KeyStoreException | IOException e) {
      // TODO: gestire eccezioni nella gestione del keystore
      e.printStackTrace();
    }
    return ritorno;
  }

  public String toBase64() {
    String risposta = null;
    try {
      risposta = Base64.getEncoder().encodeToString(Files.readAllBytes(Paths.get(filePath())));
    } catch (IOException e) {
      // TODO: gestire eccezioni nella gestione del keystore
      e.printStackTrace();
    }
    return risposta;
  }

  public void FromBase64(String FileContentBite) {
    byte[] data = Base64.getDecoder().decode(FileContentBite);
    try {
      FileUtils.writeByteArrayToFile(new File(filePath()), data);
    } catch (IOException e) {
      // TODO: gestire eccezioni nella gestione del keystore
      e.printStackTrace();
    }
  }

  @Override
  public String getName() {
    return label;
  }

  @Override
  public Ar4kComponent instanziate() {
    return null;
  }

  @Override
  public Instant getCreationDate() {
    return creationDate;
  }

  @Override
  public Instant getLastUpdateDate() {
    return lastUpdate;
  }

  @Override
  public UUID getUniqueId() {
    return uniqueId;
  }

}
