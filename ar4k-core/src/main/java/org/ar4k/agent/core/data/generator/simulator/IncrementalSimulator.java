package org.ar4k.agent.core.data.generator.simulator;

import java.util.List;

import org.ar4k.agent.core.data.generator.FormatGenerator;
import org.ar4k.agent.core.data.generator.NextGenerator;
import org.springframework.messaging.Message;

import com.google.common.collect.Lists;

public class IncrementalSimulator implements NextGenerator {
	private final FormatGenerator formatSimulator;
	private final long rangeLower;
	private final long rangeHi;
	private final double delta;
	private Object actual = null;

	public IncrementalSimulator(FormatGenerator formatSimulator, long rangeLower, long rangeHi, double delta) {
		this.formatSimulator = formatSimulator;
		this.rangeLower = rangeLower;
		this.rangeHi = rangeHi;
		this.delta = delta;
	}

	private Object generateNext() {
		if (actual == null) {
			actual = Double.valueOf(rangeLower);
		}
		actual = Double.valueOf(actual.toString()) + delta;
		if ((Double) actual > rangeHi) {
			actual = Double.valueOf(rangeLower);
		}
		return actual;
	}

	@Override
	public List<Message<?>> getNextValue() {
		return Lists.newArrayList(formatSimulator.format(generateNext()));
	}
}
