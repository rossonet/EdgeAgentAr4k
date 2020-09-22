package org.ar4k.agent.docker;

import java.io.Serializable;
import java.nio.file.Path;

import org.ar4k.agent.core.interfaces.ManagedArchives;

public class DockerArchive implements Serializable, ManagedArchives {

	private static final long serialVersionUID = 1888277494488825033L;

	@Override
	public String getUniqueName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void synchronize() {
		// TODO Auto-generated method stub

	}

	@Override
	public Path getFileSystemPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void install() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isInstalled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getLog() {
		// TODO Auto-generated method stub
		return null;
	}

}
