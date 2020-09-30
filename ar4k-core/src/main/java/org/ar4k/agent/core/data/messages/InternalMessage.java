package org.ar4k.agent.core.data.messages;

public abstract class InternalMessage<T> implements EdgeMessage<T> {

	private static final long serialVersionUID = 8423795771428874000L;

	@Override
	@SuppressWarnings("unchecked")
	public void setObjectPayload(Object object) {
		setPayload((T) object);
	}

}
