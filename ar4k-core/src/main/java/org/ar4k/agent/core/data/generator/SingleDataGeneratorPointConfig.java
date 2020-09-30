package org.ar4k.agent.core.data.generator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;

public class SingleDataGeneratorPointConfig implements Serializable, Cloneable {

	private static final long serialVersionUID = 353393059952787542L;

	public enum DataGeneratorMode {
		INCREMENTAL, SINUSOID, RANDOM, STATIC, ECHO_INCREMENTAL, DATA_BAG
	}

	public enum DataType {
		DOUBLE, INTEGER, FLOAT, LONG, JSON_NUMERIC, JSON_STRING, LOGGER, STRING, BOOLEAN
	}

	public enum ChannelType {
		DirectChannel, ExecutorChannel, PriorityChannel, PublishSubscribe, QueueChannel, RendezvousChannel
	}

	@Parameter(names = "--typeSimulator", description = "Data generator mode")
	public DataGeneratorMode typeSimulator = DataGeneratorMode.ECHO_INCREMENTAL;

	@Parameter(names = "--typeData", description = "Data type generated")
	public DataType typeData = DataType.JSON_STRING;

	@Parameter(names = "--typeChannel", description = "channel type")
	public ChannelType typeChannel = ChannelType.PublishSubscribe;

	@Parameter(names = "--frequency", description = "Simulator frequency in ms")
	public long frequency = 1000L;

	@Parameter(names = "--delta", description = "delta for increment, should be negative. In databag case is the delay time in the loop play cycle")
	public double delta = 1;

	@Parameter(names = "--rangeLower", description = "lower value for the simulator")
	public long rangeLower = 0;

	@Parameter(names = "--rangeHi", description = "hi value for the simulator")
	public long rangeHi = 1000;

	@Parameter(names = "--description", description = "description for the data")
	public String description = null;

	@Parameter(names = "--nodeId", description = "nodeId for the data")
	public String nodeId = null;

	@Parameter(names = "--namespace", description = "scope for the data")
	public String namespace = null;

	@Parameter(names = "--domainId", description = "scope for the data")
	public String domainId = null;

	@Parameter(names = "--tags", description = "tags for the data")
	public List<String> tags = new ArrayList<>();

	@Parameter(names = "--dataBagFilePath", description = "databag file path")
	public String dataBagFilePath = null;

	@Parameter(names = "--height", description = "for the sinusoid simulator")
	public double height = 100;

	@Parameter(names = "--templateJson", description = "template for json message. You can use %nodeId%, %time% and %value% as markers ")
	public String patternJson = "{" + "\"k\":\"%nodeId%\"," + "\"ts\":%time%," + "\"v\":%value%" + "}";

}
