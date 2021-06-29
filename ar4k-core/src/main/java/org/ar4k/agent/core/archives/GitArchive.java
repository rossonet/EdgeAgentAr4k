package org.ar4k.agent.core.archives;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;

import org.ar4k.agent.core.interfaces.ManagedArchiveAr4k;
import org.ar4k.agent.core.interfaces.ManagedArchives;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

@ManagedArchiveAr4k
public class GitArchive implements ManagedArchives {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(GitArchive.class);

	private String gitUrl = null;
	private File localDirectory = null;

	private Git gitRepo = null;

	@Override
	public String getUniqueName() {
		return gitUrl;
	}

	@Override
	public void synchronize() {
		if (gitRepo == null) {
			try {
				gitRepo = Git.cloneRepository().setURI(gitUrl).setDirectory(localDirectory).call();
			} catch (GitAPIException e) {
				logger.logException(e);
			}
		} else {
			try {
				gitRepo.pull().call();
				gitRepo.add().addFilepattern(".").call();
				gitRepo.commit().setAuthor("Ar4k Agent", "origami@rossonet.org")
						.setMessage("automatic commit " + new Date().toString()).call();
				gitRepo.push().call();
			} catch (GitAPIException e) {
				logger.logException(e);
			}

		}

	}

	@Override
	public Path getFileSystemPath() {
		return localDirectory.toPath();
	}

	@Override
	public void install() {
		synchronize();
	}

	@Override
	public boolean isInstalled() {
		return gitRepo != null;
	}

	@Override
	public String getLog() {
		// TODO completare l'implementazione archivio su git con i log
		return null;
	}

	public static GitArchive fromHttpUrl(String gitUrl) {
		GitArchive object = new GitArchive();
		object.setUrl(gitUrl);
		return object;
	}

	@Override
	public void setUrl(String gitUrl) {
		this.gitUrl = gitUrl;
		try {
			localDirectory = File.createTempFile("temp_", Long.toString(System.nanoTime()));
		} catch (IOException e) {
			logger.logException(e);
		}
		if (!(localDirectory.delete()) || !(localDirectory.mkdir())) {
			logger.error("Could not create temp directory: " + localDirectory.getAbsolutePath());
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Git archive [git URL=");
		builder.append(gitUrl);
		builder.append(", local directory=");
		builder.append(localDirectory);
		builder.append("]");
		return builder.toString();
	}

}
