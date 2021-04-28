package org.ar4k.agent.farm.local;

import org.ar4k.agent.farm.ManagedVirtualApplication;

public interface LocalAccountVirtualApplication extends ManagedVirtualApplication {

	String getLocalInstallScript();

	String getLocalStartCommand();

	String getLocalStopCommand();

	String getLocalCheckCommand();

	String getLocalPreSaveCommand();

	String getLocalPostSaveCommand();

	String getLocalPreLoadCommand();

	String getLocalPostLoadCommand();

	String getLambdaCommand();

}
