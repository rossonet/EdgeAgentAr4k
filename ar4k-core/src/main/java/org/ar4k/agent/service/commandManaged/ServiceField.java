package org.ar4k.agent.service.commandManaged;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class ServiceField implements Serializable {

	private static final long serialVersionUID = 6066493669741869021L;

	@NotNull
	public String label = null;

	public String value = null;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ServiceField [");
		if (label != null) {
			builder.append("label=");
			builder.append(label);
			builder.append(", ");
		}
		if (value != null) {
			builder.append("value=");
			builder.append(value);
		}
		builder.append("]");
		return builder.toString();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
