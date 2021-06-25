package org.ar4k.agent.core.data.messages;

import java.awt.image.BufferedImage;

import org.ar4k.agent.core.data.messages.InternalMessage;
import org.springframework.messaging.MessageHeaders;

public class VideoMessage extends InternalMessage<BufferedImage> {

	private static final long serialVersionUID = -5291453326352482735L;
	private BufferedImage payload = null;
	private MessageHeaders headers = null;

	@Override
	public void setPayload(BufferedImage payload) {
		this.payload = payload;
	}

	@Override
	public void setHeaders(MessageHeaders headers) {
		this.headers = headers;
	}

	@Override
	public BufferedImage getPayload() {
		return payload;
	}

	@Override
	public MessageHeaders getHeaders() {
		return headers;
	}

	@Override
	public void close() throws Exception {
		payload = null;
		headers = null;
	}

}
