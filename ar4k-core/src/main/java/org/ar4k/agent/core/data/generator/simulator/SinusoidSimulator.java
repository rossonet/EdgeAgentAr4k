package org.ar4k.agent.core.data.generator.simulator;

import java.util.List;

import org.ar4k.agent.core.data.generator.FormatGenerator;
import org.ar4k.agent.core.data.generator.NextGenerator;
import org.springframework.messaging.Message;

import com.google.common.collect.Lists;

public class SinusoidSimulator implements NextGenerator {

	private final FormatGenerator formatSimulator;
	private final long rangeLower;
	private final long rangeHi;
	private final double delta;
	private Double actual = null;
	private final double height;

	public SinusoidSimulator(FormatGenerator formatSimulator, long rangeLower, long rangeHi, double delta,
			double height) {
		this.formatSimulator = formatSimulator;
		this.rangeLower = rangeLower;
		this.rangeHi = rangeHi;
		this.delta = delta;
		this.height = height;
	}

	@Override
	public List<Message<?>> getNextValue() {
		return Lists.newArrayList(formatSimulator.format(generateNext()));
	}

	private Double generateNext() {
		if (actual == null) {
			actual = new Long(rangeLower).doubleValue();
		}
		actual = Double.valueOf(actual.toString()) + delta;
		if (actual > rangeHi) {
			actual = new Long(rangeLower).doubleValue();
		}
		// System.out.println("actual is " + actual);
		return Math.sin(Math.toRadians(actual)) * height;

	}

}
