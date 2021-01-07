package org.ar4k.agent.core.archives;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.ar4k.agent.core.interfaces.ManagedArchiveAr4k;
import org.ar4k.agent.core.interfaces.ManagedArchives;

@ManagedArchiveAr4k
public class LocalFileSystemArchive implements ManagedArchives {

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
		// TODO Auto-generated method stub
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
