package org.ar4k.agent.cortex.drools.data;

public class GlobalDataInterface {

	private GlobalDataInterface() {
		// usare getStaticInstance()
	}

	private static final GlobalDataInterface staticInstance = new GlobalDataInterface();

	public static GlobalDataInterface getStaticInstance() {
		return staticInstance;
	}

	public String getString(Object data) {
		return String.valueOf(data);
	}

	public boolean isNotNull(Object data) {
		return data != null;
	}

}
