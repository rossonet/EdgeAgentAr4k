package org.ar4k.agent.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;

import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.yaml.snakeyaml.Yaml;

import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ConfigHelper {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(ConfigHelper.class.toString());

  public static final String NETTY_CTX_CLIENT = "net-ctx-c";
  public static final String NETTY_CTX_SERVER = "net-ctx-s";
  public static final String KOPS_BINARY_PATH = "~/bin/kops";
  public static final String BASE_BASH_CMD = "/bin/bash -l";
  public static final String LATEST_KOPS_URL = "https://api.github.com/repos/kubernetes/kops/releases/latest";
  public static final String KOPS_URL = "https://github.com/kubernetes/kops/releases/download/$version/kops-linux-amd64";
  public static final String MINIKUBE_BINARY_PATH = "~/bin/minikube";
  public static final String MINIKUBE_URL = "https://storage.googleapis.com/minikube/releases/v1.1.1/minikube-linux-amd64";
  public static final String HELM_TGZ_PATH = "~/bin/helm.tgz";
  public static final String HELM_COMPRESSED_URL = "https://get.helm.sh/helm-v2.14.1-linux-amd64.tar.gz";
  public static final String HELM_DIRECTORY_PATH = "~/bin";
  public static final String LATEST_KUBECTL_URL = "https://storage.googleapis.com/kubernetes-release/release/stable.txt";
  public static final String KUBECTL_BINARY_PATH = "~/bin/kubectl";
  public static final String KUBECTL_URL = "https://storage.googleapis.com/kubernetes-release/release/$version/bin/linux/amd64/kubectl";
  public static final String KUBEFLOW_TGZ_PATH = "~/bin/kubeflow.tgz";
  public static final String KUBEFLOW_COMPRESSED_URL = "https://github.com/kubeflow/kubeflow/archive/v0.4.1.tar.gz";
  public static final String KUBEFLOW_DIRECTORY_PATH = "~/bin";
  public static final String KSONNET_TGZ_PATH = "~/bin/ksonnet.tgz";
  public static final String KSONNET_COMPRESSED_URL = "https://github.com/ksonnet/ksonnet/releases/download/v0.13.1/ks_0.13.1_linux_amd64.tar.gz";
  public static final String KSONNET_DIRECTORY_PATH = "~/bin";
  public static final String KUBECONFIG = "~/.kube/config";
  public static final String SHELL_INTERACTIVE_START = "~/.ssty_noecho";

  // default value
  public static final String organization = "Rossonet";
  public static final String unit = "Ar4k";
  public static final String locality = "Imola";
  public static final String state = "Bologna";
  public static final String country = "IT";
  public static final String uri = "https://www.rossonet.net";
  public static final String dns = NetworkHelper.getHostname();
  public static final String ip = "127.0.0.1";

  public static final int defaulBeaconSignvalidity = 100;

  private ConfigHelper() {
    System.out.println("Just for static usage");
  }

  public static String createRandomRegistryId() {
    StringBuilder val = new StringBuilder();
    val.append("AR");
    // char (1), random A-Z
    int ranChar = 65 + (new Random()).nextInt(90 - 65);
    char ch = (char) ranChar;
    val.append(ch);
    // numbers (6), random 0-9
    Random r = new Random();
    int numbers = 100000 + (int) (r.nextFloat() * 899900);
    val.append(String.valueOf(numbers));
    val.append("-");
    // char or numbers (5), random 0-9 A-Z
    for (int i = 0; i < 6;) {
      int ranAny = 48 + (new Random()).nextInt(90 - 65);
      if (!(57 < ranAny && ranAny <= 65)) {
        char c = (char) ranAny;
        val.append(c);
        i++;
      }
    }
    return val.toString();
  }

  public static String generateNewUniqueName() {
    String result = null;
    try {
      result = InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      logger.info("no hostname found...");
      result = "";
    }
    result = result + "_" + UUID.randomUUID().toString().replaceAll("-", "");
    return result;
  }

  public static String toJson(ConfigSeed configObject) {
    GsonBuilder builder = new GsonBuilder();
    // builder.registerTypeAdapter(ConfigSeed.class, new
    // ConfigSeedJsonAdapter<ConfigSeed>());
    Gson gson = builder.setPrettyPrinting().create();
    return gson.toJson(configObject);
  }

  public static String toBase64(ConfigSeed configObject) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(configObject);
    oos.close();
    return Base64.getEncoder().encodeToString(baos.toByteArray());
  }

  public static String toBase64ForDns(String name, ConfigSeed configObject) throws IOException {
    Iterable<String> chunks = Splitter.fixedLength(254).split(toBase64(configObject));
    StringBuilder result = new StringBuilder();
    int counter = 0;
    for (String s : chunks) {
      result.append(name + "-" + String.valueOf(counter) + "\tIN\tTXT\t" + '"' + s + '"' + "\n");
      counter++;
    }
    result.append(name + "-max" + "\tIN\tTXT\t" + '"' + String.valueOf(counter) + '"' + "\n");
    return result.toString();
  }

  public static String toBase64Rsa(ConfigSeed configObject, String aliasPrivateKey)
      throws IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException,
      UnrecoverableEntryException, NoSuchPaddingException, InvalidKeyException {
    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    keyStore.load(null);
    KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(aliasPrivateKey, null);
    Cipher inputCipher = Cipher.getInstance("SHA256withRSA");
    inputCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    CipherOutputStream cipherOutputStream = new CipherOutputStream(baos, inputCipher);
    ObjectOutputStream oos = new ObjectOutputStream(cipherOutputStream);
    oos.writeObject(configObject);
    cipherOutputStream.close();
    oos.close();
    return Base64.getEncoder().encodeToString(baos.toByteArray());
  }

  public static ConfigSeed fromJson(String jsonConfig) {
    Gson gson = new Gson();
    return gson.fromJson(jsonConfig, ConfigSeed.class);
  }

  public static ConfigSeed fromBase64(String base64Config) throws IOException, ClassNotFoundException {
    byte[] data = Base64.getDecoder().decode(base64Config);
    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
    ConfigSeed rit = (ConfigSeed) ois.readObject();
    ois.close();
    return rit;
  }

  // TODO: da completare con la crittografia e provare
  public static ConfigSeed fromBase64Rsa(String base64RsaConfig) throws IOException, ClassNotFoundException {
    byte[] data = Base64.getDecoder().decode(base64RsaConfig);
    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
    ConfigSeed rit = (ConfigSeed) ois.readObject();
    ois.close();
    return rit;
  }

  public static Ar4kConfig fromYaml(String yamlConfig) {
    Yaml yaml = new Yaml();
    return yaml.load(yamlConfig);
  }

  public static String toYaml(Ar4kConfig workingConfig) {
    Yaml yaml = new Yaml();
    return yaml.dump(workingConfig);
  }

}
