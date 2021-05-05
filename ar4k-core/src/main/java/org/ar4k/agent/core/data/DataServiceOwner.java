package org.ar4k.agent.core.data;

import io.micrometer.core.instrument.MeterRegistry;

public interface DataServiceOwner {
	
	  DataAddress getDataAddress();
	  
	  String getServiceName();

}
