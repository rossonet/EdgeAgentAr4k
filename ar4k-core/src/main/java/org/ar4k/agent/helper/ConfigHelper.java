package org.ar4k.agent.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;

import org.ar4k.agent.config.ConfigSeed;

import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ConfigHelper {

  private ConfigHelper() {
    System.out.println("Just for static usage");
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

}
