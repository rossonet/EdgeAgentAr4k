package org.ar4k.agent.docker;

import java.util.ArrayList;
import java.util.List;

import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.farm.FarmConfig;

import com.beust.jcommander.Parameter;

public class DockerHostConfig extends FarmConfig {

	private static final long serialVersionUID = -1407745298284487434L;
	@Parameter(names = "--dockerHost", description = "the dockerd host for the docker connection")
	public String dockerHost;
	@Parameter(names = "--registryUser", description = "the username for the docker connection")
	public String registryUser;
	@Parameter(names = "--registryPass", description = "the password for the docker connection")
	public String registryPass;
	@Parameter(names = "--dockerCertPath", description = "the path of the certificate file for the docker connection")
	public String dockerCertPath;
	@Parameter(names = "--dockerTlsVerify", description = "verify the tls certificate of the docker server")
	public boolean dockerTlsVerify = false;
	@Parameter(names = "--registryMail", description = "the mail for the docker connection")
	public String registryMail;
	@Parameter(names = "--registryUrl", description = "the url for the docker connection")
	public String registryUrl;
	@Parameter(names = "--initializeSystem", description = "reset containers when start the service")
	public boolean initializeSystem = true;
	@Parameter(names = "--containers", description = "list of the containers managed")
	public List<DockerContainer> containers = new ArrayList<>();

	@Override
	public EdgeComponent instantiate() {
		return new DockerHostComponent(this);
	}

}
