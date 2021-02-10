package org.ar4k.agent.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;

import javax.crypto.NoSuchPaddingException;

import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.config.json.PotInterfaceAdapter;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.interfaces.ConfigSeed;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.bouncycastle.cms.CMSAlgorithm;
import org.bouncycastle.cms.CMSEnvelopedData;
import org.bouncycastle.cms.CMSEnvelopedDataGenerator;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.KeyTransRecipientInformation;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OutputEncryptor;
import org.yaml.snakeyaml.Yaml;

import com.google.common.base.Splitter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ConfigHelper {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
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

	private static final String TILDE = "~";

	public static final String USER_HOME = System.getProperty("user.home");

	private ConfigHelper() {
		throw new UnsupportedOperationException("Just for static usage");
	}

	public static int countWorkingStringSplittedByComma(String input, boolean isFile) {
		return resolveWorkingString(input, isFile).split(",").length;
	}

	public static String createRandomRegistryId() {
		final StringBuilder val = new StringBuilder();
		val.append("AR");
		// char (1), random A-Z
		final int ranChar = 65 + (new Random()).nextInt(90 - 65);
		final char ch = (char) ranChar;
		val.append(ch);
		// numbers (6), random 0-9
		final Random r = new Random();
		final int numbers = 100000 + (int) (r.nextFloat() * 899900);
		val.append(String.valueOf(numbers));
		val.append("-");
		// char or numbers (5), random 0-9 A-Z
		for (int i = 0; i < 6;) {
			final int ranAny = 48 + (new Random()).nextInt(90 - 65);
			if (!(57 < ranAny && ranAny <= 65)) {
				final char c = (char) ranAny;
				val.append(c);
				i++;
			}
		}
		return val.toString();
	}

	public static byte[] decryptData(byte[] encryptedData, PrivateKey decryptionKey) throws CMSException {
		final byte[] decryptedData = null;
		if (null != encryptedData && null != decryptionKey) {
			final CMSEnvelopedData envelopedData = new CMSEnvelopedData(encryptedData);
			final Collection<RecipientInformation> recipients = envelopedData.getRecipientInfos().getRecipients();
			final KeyTransRecipientInformation recipientInfo = (KeyTransRecipientInformation) recipients.iterator()
					.next();
			final JceKeyTransRecipient recipient = new JceKeyTransEnvelopedRecipient(decryptionKey);
			return recipientInfo.getContent(recipient);
		}
		return decryptedData;
	}

	public static byte[] encryptData(byte[] data, X509Certificate encryptionCertificate)
			throws CertificateEncodingException, CMSException, IOException {
		byte[] encryptedData = null;
		if (null != data && null != encryptionCertificate) {
			final CMSEnvelopedDataGenerator cmsEnvelopedDataGenerator = new CMSEnvelopedDataGenerator();
			final JceKeyTransRecipientInfoGenerator jceKey = new JceKeyTransRecipientInfoGenerator(
					encryptionCertificate);
			cmsEnvelopedDataGenerator.addRecipientInfoGenerator(jceKey);
			final CMSTypedData msg = new CMSProcessableByteArray(data);
			final OutputEncryptor encryptor = new JceCMSContentEncryptorBuilder(CMSAlgorithm.AES128_CBC)
					.setProvider(new BouncyCastleProvider()).build();
			final CMSEnvelopedData cmsEnvelopedData = cmsEnvelopedDataGenerator.generate(msg, encryptor);
			encryptedData = cmsEnvelopedData.getEncoded();
		}
		return encryptedData;
	}

	public static ConfigSeed fromBase64(String base64Config) throws IOException, ClassNotFoundException {
		final byte[] data = Base64.getDecoder().decode(base64Config);
		final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		final ConfigSeed rit = (ConfigSeed) ois.readObject();
		ois.close();
		return rit;
	}

	public static ConfigSeed fromBase64Crypto(String base64RsaConfig, String aliasKey)
			throws ClassNotFoundException, IOException, CMSException, NoSuchAlgorithmException, NoSuchPaddingException {
		final PrivateKey key = Homunculus.getApplicationContext().getBean(Homunculus.class).getMyIdentityKeystore()
				.getPrivateKey(aliasKey);
		return fromBase64(new String(decryptData(Base64.getDecoder().decode(base64RsaConfig), key)));
	}

	public static ConfigSeed fromJson(String jsonConfig, Class<? extends ConfigSeed> targetClass) {
		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(ServiceConfig.class, new PotInterfaceAdapter());
		final Gson gson = builder.setPrettyPrinting().create();
		return gson.fromJson(jsonConfig, targetClass);
	}

	public static EdgeConfig fromYaml(String yamlConfig) {
		final Yaml yaml = new Yaml();
		return yaml.load(yamlConfig);
	}

	public static String generateNewUniqueName(String nameInParameters, String fileNameInParameters) {
		String result = null;
		if (fileNameInParameters != null && !fileNameInParameters.isEmpty()) {
			final File fileUniqueName = new File(ConfigHelper.resolveWorkingString(fileNameInParameters, true));
			if (fileUniqueName.exists() && fileUniqueName.canRead()) {
				try {
					result = Files.readAllLines(fileUniqueName.toPath()).get(0);
				} catch (final IOException e) {
					logger.logException("uniqueName file " + fileUniqueName + " exception", e);
				}
			} else {
				logger.info("uniqueName file " + fileUniqueName + " not exists or is not readable");
			}
		} else {
			if (nameInParameters != null && !nameInParameters.isEmpty()) {
				result = ConfigHelper.resolveWorkingString(nameInParameters, false);
			} else {
				try {
					result = InetAddress.getLocalHost().getHostName() + "_"
							+ UUID.randomUUID().toString().replaceAll("-", "");
				} catch (final UnknownHostException e) {
					logger.info("no hostname found...");
					result = "agent_" + UUID.randomUUID().toString().replaceAll("-", "");
				}
			}
		}
		return result;
	}

	public static String resolveWorkingString(String input, boolean isFile) {
		String resultString = null;
		if (isFile) {
			resultString = input.replace(TILDE, USER_HOME);
		} else {
			resultString = input;
		}
		return resultString.replace("{hostname}", dns).replace("{env-check}", "test-conf")
				.replace("{env-check}", "test-conf").replace("{mac}", NetworkHelper.getFirstMacAddressAsString());
	}

	public static String resolveWorkingStringSplittedByComma(String input, boolean isFile, int pos) {
		return resolveWorkingString(input, isFile).split(",")[pos];
	}

	public static String toBase64(Object configObject) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(configObject);
		oos.close();
		return Base64.getEncoder().encodeToString(baos.toByteArray());
	}

	public static String toBase64Crypto(Object configObject, String aliasKey)
			throws CertificateEncodingException, UnsupportedEncodingException, CMSException, IOException {
		final X509Certificate certificate = Homunculus.getApplicationContext().getBean(Homunculus.class)
				.getMyIdentityKeystore().getClientCertificate(aliasKey);
		return Base64.getEncoder().encodeToString(encryptData(toBase64(configObject).getBytes("UTF-8"), certificate));
	}

	public static String toBase64ForDns(String name, Object configObject) throws IOException {
		final Iterable<String> chunks = Splitter.fixedLength(254).split(toBase64(configObject));
		final StringBuilder result = new StringBuilder();
		int counter = 0;
		for (final String s : chunks) {
			result.append(name + "-" + String.valueOf(counter) + "\tIN\tTXT\t" + '"' + s + '"' + "\n");
			counter++;
		}
		result.append(name + "-max" + "\tIN\tTXT\t" + '"' + String.valueOf(counter) + '"' + "\n");
		return result.toString();
	}

	public static String toBase64ForDnsCrypto(String name, Object configObject, String aliasKey)
			throws IOException, CertificateEncodingException, CMSException {
		final Iterable<String> chunks = Splitter.fixedLength(254).split(toBase64Crypto(configObject, aliasKey));
		final StringBuilder result = new StringBuilder();
		int counter = 0;
		for (final String s : chunks) {
			result.append(name + "-" + String.valueOf(counter) + "\tIN\tTXT\t" + '"' + s + '"' + "\n");
			counter++;
		}
		result.append(name + "-max" + "\tIN\tTXT\t" + '"' + String.valueOf(counter) + '"' + "\n");
		return result.toString();
	}

	public static String toJson(ConfigSeed configObject) {
		final GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(ServiceConfig.class, new PotInterfaceAdapter());
		final Gson gson = builder.setPrettyPrinting().create();
		return gson.toJson(configObject);
	}

	public static String toYaml(ConfigSeed workingConfig) {
		final Yaml yaml = new Yaml();
		return yaml.dump(workingConfig);
	}

}
