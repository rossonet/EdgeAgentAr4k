package org.ar4k.agent.core.archives;

import java.nio.file.Path;

import org.ar4k.agent.core.interfaces.ManagedArchiveAr4k;
import org.ar4k.agent.core.interfaces.ManagedArchives;

@ManagedArchiveAr4k
public class AwsS3Archive implements ManagedArchives {

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

	@Override
	public void setUrl(String url) {
		// TODO Auto-generated method stub

	}

}
