package org.ar4k.agent.core.archives;

import java.nio.file.Path;
import java.nio.file.Paths;

@ManagedArchiveAr4k
public class LocalFileSystemArchive implements ManagedArchive {

	private Path path = null;

	@Override
	public String getUniqueName() {
		return path.toString();
	}

	@Override
	public void synchronize() {
		// nothing to do
	}

	@Override
	public Path getFileSystemPath() {
		return path;
	}

	@Override
	public void install() {
		// nothing to do
	}

	@Override
	public boolean isInstalled() {
		return true;
	}

	@Override
	public String getLog() {
		// TODO completare la gestione del repository su fs locale
		return null;
	}

	@Override
	public void setUrl(String url) {
		this.path = Paths.get(url);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Local File System Archive [path=");
		builder.append(path.toFile().getAbsolutePath());
		builder.append("]");
		return builder.toString();
	}

}
