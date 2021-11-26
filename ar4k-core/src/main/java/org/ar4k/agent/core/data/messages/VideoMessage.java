package org.ar4k.agent.core.data.messages;

import java.awt.image.BufferedImage;

public class VideoMessage extends InternalMessage<BufferedImage> {

	private static final long serialVersionUID = -5291453326352482735L;
	private BufferedImage payload = null;

	@Override
	public void setPayload(BufferedImage payload) {
		this.payload = payload;
	}

	@Override
	public BufferedImage getPayload() {
		return payload;
	}

	@Override
	public void close() throws Exception {
		payload = null;
	}

}
