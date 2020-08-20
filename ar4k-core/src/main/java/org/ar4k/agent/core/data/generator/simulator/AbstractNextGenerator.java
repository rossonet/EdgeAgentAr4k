package org.ar4k.agent.core.data.generator.simulator;

import java.util.List;

import org.ar4k.agent.core.data.generator.FormatGenerator;
import org.ar4k.agent.core.data.generator.NextGenerator;
import org.ar4k.agent.core.data.generator.ReceiverData;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.google.common.collect.Lists;

public abstract class AbstractNextGenerator implements NextGenerator, ReceiverData {

	private final FormatGenerator formatSimulator;
	private final long rangeLower;
	private final long rangeHi;
	private final double delta;
	protected Object actual = null;
	private MessageHandler callBack = new MessageHandler() {

		@Override
		public void handleMessage(Message<?> message) throws MessagingException {
			actual = message.getPayload();
		}

	};

	public AbstractNextGenerator(FormatGenerator formatSimulator, long rangeLower, long rangeHi, double delta) {
		this.formatSimulator = formatSimulator;
		this.rangeLower = rangeLower;
		this.rangeHi = rangeHi;
		this.delta = delta;
	}

	@Override
	public List<Message<?>> getNextValue() {
		actual = generateNext();
		return Lists.newArrayList(formatSimulator.format(actual));
	}

	protected abstract Object generateNext();

	public long getRangeLower() {
		return rangeLower;
	}

	public long getRangeHi() {
		return rangeHi;
	}

	public double getDelta() {
		return delta;
	}

	public FormatGenerator getFormatSimulator() {
		return formatSimulator;
	}

	public boolean isNumber() {
		return formatSimulator.isNumber();
	}

	@Override
	public MessageHandler getCallBack() {
		return callBack;
	}

	@Override
	public void setValue(Object payload) {
		actual = payload;
	}

}
