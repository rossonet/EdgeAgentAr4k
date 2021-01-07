package org.ar4k.agent.bootstrap;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.ar4k.agent.core.archives.GitArchive;
import org.ar4k.agent.core.interfaces.ManagedArchives;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.model.GradleProject;
import org.gradle.tooling.model.Task;

public abstract class BootstrapRecipe implements AutoCloseable {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(BootstrapRecipe.class.toString());

	private BootstrapShellInterface shellInterface = null;

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

	protected void generateSimpleConfig() {
		// TODO Auto-generated method stub

	}

	protected void generateBeaconServerConfig() {
		// TODO Auto-generated method stub

	}

	protected void generateAgentJar() {
		// TODO Auto-generated method stub

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
					sb.append("- " + task.getDisplayName() + "[" + task.getDescription() + "]\n");
			}
		} finally {
			connection.close();
		}
		return sb.toString();
	}

}
