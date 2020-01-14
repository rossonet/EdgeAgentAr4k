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
package org.ar4k.agent.console;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map.Entry;

import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.helper.AbstractShellHelper;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.keystore.KeystoreConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Interfaccia a linea di comando per la gestione dei keystores.
 *
 */

@ShellCommandGroup("Keytools Commands")
@ShellComponent
//@EnableMBeanExport
//@ManagedResource(objectName = "bean:name=keytoolsInterface", description = "Ar4k Agent keytools interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "keytoolsInterface")
@RestController
@RequestMapping("/keytoolsInterface")
public class KeystoreShellInterface extends AbstractShellHelper {

  @Autowired
  ApplicationContext applicationContext;

  @Autowired
  Anima anima;

  @SuppressWarnings("unused")
  private Availability testOneKey() {
    boolean ok = true;
    String message = "";
    try {
      if (((RpcConversation) anima.getRpc(getSessionId())).getKeyStores().size() < 1) {
        if (ok == false) {
          message += " and ";
        }
        ok = false;
        message += "you need a keystore configured on the gateway";
      }
    } catch (Exception e) {
      ok = false;
      message += "you need a valid session id";
    }
    return ok ? Availability.available() : Availability.unavailable(message);
  }

  @ShellMethod(value = "List keystores in session", group = "Keytools Commands")
  @ManagedOperation
  @ShellMethodAvailability("testOneKey")
  public String listKeystore() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(((RpcConversation) anima.getRpc(getSessionId())).getKeyStores());
  }

  @ShellMethod(value = "Add a keystore to the session", group = "Keytools Commands")
  @ManagedOperation
  public void addKeystore(@ShellOption(optOut = true) @Valid KeystoreConfig keyStore) {
    ((RpcConversation) anima.getRpc(getSessionId())).getKeyStores().put(keyStore.getUniqueId(), keyStore);
  }

  @ShellMethod(value = "Add runtime keystore to the session", group = "Keytools Commands")
  @ManagedOperation
  public void addKeystoreRuntime() {
    ((RpcConversation) anima.getRpc(getSessionId())).getKeyStores().put(anima.getMyIdentityKeystore().getUniqueId(),
        anima.getMyIdentityKeystore());
  }

  @ShellMethod(value = "Check a keystore selected by alias", group = "Keytools Commands")
  @ManagedOperation
  @ShellMethodAvailability("testOneKey")
  public boolean checkKeystore(@ShellOption(help = "label assigned to the keystore") String keystoreLabel) {
    boolean ok = false;
    for (Entry<String, KeystoreConfig> t : ((RpcConversation) anima.getRpc(getSessionId())).getKeyStores().entrySet()) {
      if (t.getValue().label.equals(keystoreLabel) && t.getValue().check()) {
        ok = true;
        break;
      }
    }
    return ok;
  }

  @ShellMethod(value = "View version base64 text prepared for dns of a keystore selected by alias", group = "Keytools Commands")
  @ManagedOperation
  @ShellMethodAvailability("testOneKey")
  public String getKeystoreForDns(@ShellOption(help = "label assigned to the keystore") String keystoreLabel,
      @ShellOption(help = "the hostname for this configuration") String name) throws IOException {
    String returnText = null;
    for (Entry<String, KeystoreConfig> t : ((RpcConversation) anima.getRpc(getSessionId())).getKeyStores().entrySet()) {
      if (t.getValue().label.equals(keystoreLabel) && t.getValue().check()) {
        File file = new File(t.getValue().filePathPre);
        byte[] data = FileUtils.readFileToByteArray(file);
        returnText = ConfigHelper.toBase64ForDns(name, data);
        break;
      }
    }
    return returnText;
  }

  @ShellMethod(value = "Save a new keypair in the keystore", group = "Keytools Commands")
  @ManagedOperation
  @ShellMethodAvailability("testOneKey")
  public boolean setClientKeyPair(@ShellOption(help = "the private key in base64 format") String base64Key,
      @ShellOption(help = "the crt in base64 format") String base64Crt,
      @ShellOption(help = "label assigned to the keystore") String keystoreLabel,
      @ShellOption(help = "the alias for the new entry in the keystore") String entryAlias) {
    boolean ok = false;
    try {
      for (Entry<String, KeystoreConfig> t : ((RpcConversation) anima.getRpc(getSessionId())).getKeyStores()
          .entrySet()) {
        if (t.getValue().label.equals(keystoreLabel)
            && t.getValue().setClientKeyPair(base64Key, base64Crt, entryAlias)) {
          ok = true;
          break;
        }
      }
    } catch (NoSuchAlgorithmException e) {
      logger.logException(e);
    }
    return ok;
  }

  @ShellMethod(value = "Create a new self signed cert in the keystore", group = "Keytools Commands")
  @ManagedOperation
  @ShellMethodAvailability("testOneKey")
  public boolean createSelfSignedCert(@ShellOption(help = "label assigned to the keystore") String keystoreLabel,
      @ShellOption(help = "the alias for the new entry in the keystore") String entryAlias,
      @ShellOption(help = "common name for the CA certificate", defaultValue = "client.agents.ar4k.net") String commonName,
      @ShellOption(help = "company for the CA certificate", defaultValue = "Rossonet s.c.a r.l.") String organization,
      @ShellOption(help = "organization unit for the CA certificate", defaultValue = "AR4K") String unit,
      @ShellOption(help = "city for the CA certificate", defaultValue = "Imola") String locality,
      @ShellOption(help = "province for the CA certificate", defaultValue = "Bologna") String state,
      @ShellOption(help = "country for the CA certificate", defaultValue = "IT") String country,
      @ShellOption(help = "URI for the CA certificate", defaultValue = "urn:org.ar4k.agent:ca-agents") String uri,
      @ShellOption(help = "host name for the CA certificate", defaultValue = "localhost") String dns,
      @ShellOption(help = "id address for the CA certificate", defaultValue = "127.0.0.1") String ip,
      @ShellOption(help = "is the certificate a CA true/false", defaultValue = "false") boolean isCa) {
    boolean ok = false;
    for (Entry<String, KeystoreConfig> t : ((RpcConversation) anima.getRpc(getSessionId())).getKeyStores().entrySet()) {
      if (t.getValue().label.equals(keystoreLabel) && t.getValue().createSelfSignedCert(commonName, organization, unit,
          locality, state, country, uri, dns, ip, entryAlias, isCa)) {
        ok = true;
        break;
      }
    }
    return ok;
  }

  @ShellMethod(value = "Get a certificate in base64 format", group = "Keytools Commands")
  @ManagedOperation
  @ShellMethodAvailability("testOneKey")
  public String getClientCertificateBase64(@ShellOption(help = "label assigned to the keystore") String keystoreLabel,
      @ShellOption(help = "the alias for the new entry in the keystore") String entryAlias) {
    String out = null;
    for (Entry<String, KeystoreConfig> t : ((RpcConversation) anima.getRpc(getSessionId())).getKeyStores().entrySet()) {
      if (t.getValue().label.equals(keystoreLabel)) {
        out = "-----BEGIN CERTIFICATE-----\n";
        out += t.getValue().getClientCertificateBase64(entryAlias);
        out += "\n-----END CERTIFICATE-----";
      }
    }
    return out;
  }

  @ShellMethod(value = "Get a private key in base64 format", group = "Keytools Commands")
  @ManagedOperation
  @ShellMethodAvailability("testOneKey")
  public String getPrivateKeyBase64(@ShellOption(help = "label assigned to the keystore") String keystoreLabel,
      @ShellOption(help = "the alias for the new entry in the keystore") String entryAlias) {
    String out = null;
    for (Entry<String, KeystoreConfig> t : ((RpcConversation) anima.getRpc(getSessionId())).getKeyStores().entrySet()) {
      if (t.getValue().label.equals(keystoreLabel)) {
        out = "-----BEGIN PRIVATE KEY-----\n";
        out += t.getValue().getPrivateKeyBase64(entryAlias);
        out += "\n-----END PRIVATE KEY-----";
      }
    }
    return out;
  }

  @ShellMethod(value = "Get a CSR in base64 format to request a sign from authority", group = "Keytools Commands")
  @ManagedOperation
  @ShellMethodAvailability("testOneKey")
  public String getPKCS10CertificationRequestBase64(
      @ShellOption(help = "label assigned to the keystore") String keystoreLabel,
      @ShellOption(help = "the alias for the new entry in the keystore") String entryAlias) {
    String out = null;
    for (Entry<String, KeystoreConfig> t : ((RpcConversation) anima.getRpc(getSessionId())).getKeyStores().entrySet()) {
      if (t.getValue().label.equals(keystoreLabel)) {
        out = "-----BEGIN CERTIFICATE REQUEST-----\n";
        out += t.getValue().getPKCS10CertificationRequestBase64(entryAlias);
        out += "\n-----END CERTIFICATE REQUEST-----";
      }
    }
    return out;
  }

  @ShellMethod(value = "Sign a CSR in base64 format", group = "Keytools Commands")
  @ManagedOperation
  @ShellMethodAvailability("testOneKey")
  public String signCertificateBase64(@ShellOption(help = "the csr in base64 format") String base64Csr,
      @ShellOption(help = "the alias of the target key") String targetAlias,
      @ShellOption(help = "the validity of the certificate in day", defaultValue = "365") String validity,
      @ShellOption(help = "label assigned to the keystore with th CA key") String keystoreLabel,
      @ShellOption(help = "the alias of the CA key") String caAlias) {
    String out = null;
    for (Entry<String, KeystoreConfig> t : ((RpcConversation) anima.getRpc(getSessionId())).getKeyStores().entrySet()) {
      if (t.getValue().label.equals(keystoreLabel)) {
        out = t.getValue().signCertificateBase64(base64Csr, targetAlias, Integer.valueOf(validity), caAlias);
      }
    }
    return out;
  }

  @ShellMethod(value = "Create a new cert and key for the keystore selected by label", group = "Keytools Commands")
  @ManagedOperation
  @ShellMethodAvailability("testOneKey")
  public boolean initializeKeystoreCa(@ShellOption(help = "label assigned to the keystore") String keystoreLabel,
      @ShellOption(help = "If set true, the original file will be deleted if exists", defaultValue = "false") String delete,
      @ShellOption(help = "common name for the CA certificate", defaultValue = "ca.agents.ar4k.net") String commonName,
      @ShellOption(help = "company for the CA certificate", defaultValue = "Rossonet s.c.a r.l.") String organization,
      @ShellOption(help = "organization unit for the CA certificate", defaultValue = "AR4K") String unit,
      @ShellOption(help = "city for the CA certificate", defaultValue = "Imola") String locality,
      @ShellOption(help = "province for the CA certificate", defaultValue = "Bologna") String state,
      @ShellOption(help = "country for the CA certificate", defaultValue = "IT") String country,
      @ShellOption(help = "URI for the CA certificate", defaultValue = "urn:org.ar4k.agent:ca-agents") String uri,
      @ShellOption(help = "host name for the CA certificate", defaultValue = "localhost") String dns,
      @ShellOption(help = "id address for the CA certificate", defaultValue = "127.0.0.1") String ip,
      @ShellOption(help = "alias for new cert in the keystore", defaultValue = "new_cert") String alias,
      @ShellOption(help = "is the certificate a CA true/false", defaultValue = "false") boolean isCa) {
    boolean ok = false;
    for (Entry<String, KeystoreConfig> t : ((RpcConversation) anima.getRpc(getSessionId())).getKeyStores().entrySet()) {
      if (t.getValue().label.equals(keystoreLabel)) {
        ok = t.getValue().create(commonName, organization, unit, locality, state, country, uri, dns, ip, alias, isCa);
      }
    }
    return ok;
  }

}
