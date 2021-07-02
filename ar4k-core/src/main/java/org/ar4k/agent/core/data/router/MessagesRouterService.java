package org.ar4k.agent.core.data.router;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.channels.IDirectChannel;
import org.ar4k.agent.core.data.channels.IExecutorChannel;
import org.ar4k.agent.core.data.channels.IPriorityChannel;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.data.channels.IQueueChannel;
import org.ar4k.agent.core.data.channels.IRendezvousChannel;
import org.ar4k.agent.core.data.generator.dataType.BooleanSimulator;
import org.ar4k.agent.core.data.generator.dataType.DoubleSimulator;
import org.ar4k.agent.core.data.generator.dataType.FloatSimulator;
import org.ar4k.agent.core.data.generator.dataType.IntegerSimulator;
import org.ar4k.agent.core.data.generator.dataType.JsonNumericSimulator;
import org.ar4k.agent.core.data.generator.dataType.JsonStringSimulator;
import org.ar4k.agent.core.data.generator.dataType.LoggerSimulator;
import org.ar4k.agent.core.data.generator.dataType.LongSimulator;
import org.ar4k.agent.core.data.generator.dataType.StringSimulator;
import org.ar4k.agent.core.data.generator.simulator.DataBagSimulator;
import org.ar4k.agent.core.data.generator.simulator.EchoIncrementalSimulator;
import org.ar4k.agent.core.data.generator.simulator.IncrementalSimulator;
import org.ar4k.agent.core.data.generator.simulator.RandomSimulator;
import org.ar4k.agent.core.data.generator.simulator.SinusoidSimulator;
import org.ar4k.agent.core.data.generator.simulator.StaticSimulator;
import org.ar4k.agent.core.interfaces.EdgeChannel;
import org.ar4k.agent.core.interfaces.EdgeComponent;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.ar4k.agent.exception.ServiceInitException;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         router messages service
 *
 */
//TODO completare router messaggi
public class MessagesRouterService implements EdgeComponent {

	@Override
	public DataAddress getDataAddress() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public String getServiceName() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws Exception {
		// Auto-generated method stub
		
	}

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void init() throws ServiceInitException {
		// Auto-generated method stub
		
	}

	@Override
	public void kill() {
		// Auto-generated method stub
		
	}

	@Override
	public Homunculus getHomunculus() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void setDataAddress(DataAddress dataAddress) {
		// Auto-generated method stub
		
	}

	@Override
	public void setHomunculus(Homunculus homunculus) {
		// Auto-generated method stub
		
	}

	@Override
	public ServiceConfig getConfiguration() {
		// Auto-generated method stub
		return null;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		// Auto-generated method stub
		
	}


}
