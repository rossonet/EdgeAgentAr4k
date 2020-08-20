package org.ar4k.agent.core.data.generator.simulator;

import java.util.Random;
import java.util.UUID;

import org.ar4k.agent.core.data.generator.FormatGenerator;

public class RandomSimulator extends AbstractNextGenerator {

	private final Random random = new Random();

	public RandomSimulator(FormatGenerator formatSimulator, long rangeLower, long rangeHi, double delta) {
		super(formatSimulator, rangeLower, rangeHi, delta);
	}

	@Override
	protected Object generateNext() {
		Object next = null;
		if (super.isNumber()) {
			next = getRangeLower() + (random.nextDouble() * (getRangeHi() - getRangeLower()));
		} else {
			next = UUID.randomUUID().toString();
		}
		return next;
	}

}
