package org.ar4k.agent.farm;

public interface ManagedVirtualApplication {

	int getPriority();

	boolean startOnBoot();

	boolean autoRestart();

}
