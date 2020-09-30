package org.ar4k.agent.core.data.generator.simulator;

import org.ar4k.agent.core.data.generator.FormatGenerator;

public class StaticSimulator extends AbstractNextGenerator {

	public StaticSimulator(FormatGenerator formatSimulator, long rangeLower, long rangeHi, double delta) {
		super(formatSimulator, rangeLower, rangeHi, delta);
	}

	@Override
	protected Object generateNext() {
		return actual;
	}

}
