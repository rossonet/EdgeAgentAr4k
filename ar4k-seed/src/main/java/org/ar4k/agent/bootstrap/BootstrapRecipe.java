package org.ar4k.agent.bootstrap;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.core.archives.GitArchive;
import org.ar4k.agent.core.interfaces.ManagedArchives;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http2.beacon.BeaconServiceConfig;
import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.model.GradleProject;
import org.gradle.tooling.model.Task;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

public abstract class BootstrapRecipe implements AutoCloseable {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BootstrapRecipe.class.toString());

	protected BootstrapShellInterface shellInterface = null;

	public abstract void setUp();

	public abstract void start();

	public abstract void stop();

	public abstract void destroy();

	public abstract boolean isAuthRequired();

	public abstract String descriptionAuthenticationRequired();

	public abstract boolean isEndPointRequired();

	public abstract String descriptionEndPointRequired();

	public abstract boolean isSetupRequired();

	public abstract boolean isStarted();

	public boolean isMasterKeystoreRequired() {
		return (shellInterface == null || shellInterface.getMasterKeystore() == null);
	}

	public String descriptionMasterKeystoreRequired() {
		return "Please create a master keystore";
	}

	public void setShellInterface(BootstrapShellInterface bootstrapShellInterface) {
		this.shellInterface = bootstrapShellInterface;
	}

	public boolean isRunningArchiveRequired() {
		return (shellInterface == null || shellInterface.getRunningProject() == null);
	}

	public String descriptionRunningArchiveRequired() {
		return "Please configure a running archive path";
	}

	protected void generateBeaconServerConfig() {
		String serverPort = "8442";
		String keystoreFile = "keys/master.ks";
		String keystoreCa = "master";
		String keystoreBeacon = "master";
		String keystorePassword = "secA4.rk!8";
		String adminPassword = "password1";
		String discoveryPort = "8444";
		String beaconserverPort = "8443";
		EdgeConfig ar4kConfig = new EdgeConfig();
		BeaconServiceConfig beaconServiceConfig = new BeaconServiceConfig();
		beaconServiceConfig.aliasBeaconServerInKeystore = keystoreBeacon;
		beaconServiceConfig.discoveryPort = Integer.valueOf(discoveryPort);
		beaconServiceConfig.port = Integer.valueOf(beaconserverPort);
		ar4kConfig.pots.add(beaconServiceConfig);
		try {
			String base64Config = ConfigHelper.toBase64(ar4kConfig);
			ResourceLoader resourceLoader = new DefaultResourceLoader();
			Resource resource = resourceLoader.getResource("classpath:application.properties.template");
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			String templateApplicationProperties = FileCopyUtils.copyToString(reader);
			String elaboratedConfig = templateApplicationProperties.replace("<?base64-config?>", base64Config)
					.replace("<?server-port?>", serverPort).replace("<?keystore-file?>", keystoreFile)
					.replace("<?keystore-ca?>", keystoreCa).replace("<?keystore-beacon?>", keystoreBeacon)
					.replace("<?keystore-password?>", keystorePassword).replace("<?admin-password?>", adminPassword)
					.replace("<?discovery-port?>", discoveryPort)
					.replace("<?beacon-endpoint?>", "http://localhost:" + beaconserverPort);
			FileUtils.write(new File(shellInterface.getRunningProject().getFileSystemPath().toFile().getAbsolutePath()
					+ "/application.properties"), elaboratedConfig, StandardCharsets.UTF_8);
		} catch (Exception e) {
			logger.logException(e);
		}
		generateSystemFiles();
	}

	private void generateSystemFiles() {
		try {
			ResourceLoader resourceLoader = new DefaultResourceLoader();
			Resource resource = resourceLoader.getResource("classpath:agent.service.template");
			Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
			String templateAgentService = FileCopyUtils.copyToString(reader);
			FileUtils.write(new File(shellInterface.getRunningProject().getFileSystemPath().toFile().getAbsolutePath()
					+ "/agent.service"), templateAgentService, StandardCharsets.UTF_8);
		} catch (Exception e) {
			logger.logException(e);
		}

	}

	protected void generateAgentJar() {
		if (shellInterface != null) {
			File sourceDirectory = new File(
					shellInterface.getRunningProject().getFileSystemPath().toFile().getAbsolutePath() + "/source");
			File buildJarFolder = new File(
					shellInterface.getRunningProject().getFileSystemPath().toFile().getAbsolutePath()
							+ "/source/build/libs");
			GradleConnector connector = GradleConnector.newConnector();
			connector.forProjectDirectory(sourceDirectory);
			ProjectConnection connection = connector.connect();
			try {
				BuildLauncher build = connection.newBuild();
				build.forTasks("clean", "bootJar");
				build.run();
				for (File f : buildJarFolder.listFiles()) {
					if (f.getName().endsWith(".jar"))
						try {
							FileUtils.copyFile(f, new File(
									shellInterface.getRunningProject().getFileSystemPath().toFile().getAbsolutePath()
											+ "/app.jar"));
						} catch (IOException e) {
							logger.logException(e);
						}
				}
			} finally {
				connection.close();
			}
		}
	}

	protected void copyMasterKeyToLocalStorage() {
		if (shellInterface != null) {
			ManagedArchives runningStorage = shellInterface.getRunningProject();
			runningStorage.synchronize();
			if (shellInterface.getMasterKeystore() != null && runningStorage.isInstalled()) {
				String destination = runningStorage.getFileSystemPath().toFile().getAbsolutePath() + "/keys";
				String keyStoreFile = new File(shellInterface.getMasterKeystore().filePath()).getAbsolutePath();
				new File(destination).mkdirs();
				try {
					logger.warn("try to copy " + keyStoreFile + " to " + destination);
					FileUtils.copyFile(new File(keyStoreFile), new File(destination + "/master.ks"));
				} catch (IOException e) {
					logger.logException(e);
				}
			} else {
				logger.error("master key or project storage are not ok");
			}

		}

	}

	protected void copyTemplateToLocalStorage() {
		if (shellInterface != null) {
			GitArchive template = shellInterface.getTemplateProject();
			template.synchronize();
			ManagedArchives runningStorage = shellInterface.getRunningProject();
			runningStorage.synchronize();
			if (template.isInstalled() && runningStorage.isInstalled()) {
				try {
					FileUtils.copyDirectory(template.getFileSystemPath().toFile(),
							new File(runningStorage.getFileSystemPath().toFile().getAbsolutePath() + "/source"));
				} catch (IOException e) {
					logger.logException(e);
				}
			} else {
				logger.error("template or project storage are not ok");
			}

		}

	}

	public String listGradleTasks() {
		StringBuilder sb = new StringBuilder();
		File sourceDirectory = new File(
				shellInterface.getRunningProject().getFileSystemPath().toFile().getAbsolutePath() + "/source");
		GradleConnector connector = GradleConnector.newConnector();
		connector.forProjectDirectory(sourceDirectory);
		ProjectConnection connection = connector.connect();
		try {
			GradleProject project = connection.getModel(GradleProject.class);
			sb.append("Project: " + project.getName() + "\n");
			sb.append("Tasks:\n");
			for (Task task : project.getTasks()) {
				if (task.getDisplayName() != null)
					sb.append(task.getName() + " -> " + task.getDisplayName() + "[" + task.getDescription() + "]\n");
			}
		} finally {
			connection.close();
		}
		return sb.toString();
	}

}
