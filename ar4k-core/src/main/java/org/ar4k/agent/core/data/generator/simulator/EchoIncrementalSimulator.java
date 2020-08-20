package org.ar4k.agent.core.data.generator.simulator;

import org.ar4k.agent.core.data.generator.FormatGenerator;

public class EchoIncrementalSimulator extends AbstractNextGenerator {

	public EchoIncrementalSimulator(FormatGenerator formatSimulator, long rangeLower, long rangeHi, double delta) {
		super(formatSimulator, rangeLower, rangeHi, delta);
	}

	@Override
	protected Object generateNext() {
		if (actual != null) {
			try {
				return Double.valueOf(actual.toString()) + getDelta();
			} catch (final Exception aa) {
				return String.valueOf(getDelta()) + " + " + actual.toString() + " => undef";
			}
		} else {
			return 0;
		}
	}

}
