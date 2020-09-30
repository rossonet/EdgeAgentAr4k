package org.ar4k.agent.core.data.generator.simulator;

import java.util.Date;
import java.util.List;

import org.ar4k.agent.core.data.DataBag;
import org.ar4k.agent.core.data.generator.NextGenerator;
import org.springframework.messaging.Message;

public class DataBagSimulator implements NextGenerator {

	private final String dataBagFilePath;
	private final long frequency;
	private final long timeBetweenLoops;
	private final DataBag databag;

	public DataBagSimulator(String dataBagFilePath, long frequency, Double delta) {
		this.dataBagFilePath = dataBagFilePath;
		this.frequency = frequency;
		timeBetweenLoops = delta.longValue();
		databag = new DataBag(dataBagFilePath);
		databag.sincronize(new Date().getTime());
	}

	@Override
	public List<Message<?>> getNextValue() {
		final long time = new Date().getTime();
		if (!databag.isComplete()) {
			return databag.getValues(time);
		} else if ((databag.getCompleteTime() + timeBetweenLoops) < time) {
			// System.out.println("*** 1. complete time calc =>\n" +
			// databag.getCompleteTime() + " + " + timeBetweenLoops
			// + " < " + time);
			databag.sincronize(time);
			return databag.getValues(time);
		} else {
			// System.out.println("*** 2. complete time calc =>\n" +
			// databag.getCompleteTime() + " + " + timeBetweenLoops
			// + " < " + time);
			return null;
		}
	}

	public String getDataBagFilePath() {
		return dataBagFilePath;
	}

	public long getFrequency() {
		return frequency;
	}

	public double getTimeBetweenLoops() {
		return timeBetweenLoops;
	}

}
