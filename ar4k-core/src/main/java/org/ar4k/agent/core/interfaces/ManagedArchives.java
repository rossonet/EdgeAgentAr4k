package org.ar4k.agent.core.interfaces;

import java.nio.file.Path;

public interface ManagedArchives {

	public String getUniqueName();

	public void synchronize();

	public Path getFileSystemPath();

	public void install();

	public boolean isInstalled();

	public String getLog();

}
