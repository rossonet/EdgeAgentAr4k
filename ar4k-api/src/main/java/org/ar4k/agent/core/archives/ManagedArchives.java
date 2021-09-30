package org.ar4k.agent.core.archives;

import java.nio.file.Path;

public interface ManagedArchives {

	public Path getFileSystemPath();

	public String getLog();

	public String getUniqueName();

	public void install();

	public boolean isInstalled();

	public void setUrl(String url);

	public void synchronize();

}
