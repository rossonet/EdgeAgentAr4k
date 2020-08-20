package org.ar4k.agent.cortex.drools.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class DataFactWrapper {

	public DataFactWrapper(String data) {
		this.data = data;
	}

	private final String data;

	private final List<String> metadatas = new ArrayList<String>();

	public String getData() {
		return data;
	}

	public JSONObject getDataAsJson() {
		try {
			return new JSONObject(data);
		} catch (final Exception a) {
			return null;
		}
	}

	public Long getDataAsLong() {
		try {
			return Long.valueOf(data);
		} catch (final Exception a) {
			return null;
		}
	}

	public Integer getDataAsInteger() {
		try {
			return Integer.valueOf(data);
		} catch (final Exception a) {
			return null;
		}
	}

	public Double getDataAsDouble() {
		try {
			return Double.valueOf(data);
		} catch (final Exception a) {
			return null;
		}
	}

	public List<String> getMetadatas() {
		return metadatas;
	}

	public int addMetaData(String value) {
		metadatas.add(value);
		return metadatas.size();
	}

}
