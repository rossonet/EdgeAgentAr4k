package org.ar4k.agent.tunnels.http.beacon.socket;

class CachedChunk {

	private final String[] datas;

	public CachedChunk(int chunk, int totalChunks, String data) {
		this.datas = new String[totalChunks];
		addData(chunk, data);
	}

	public boolean isComplete() {
		for (final String data : datas) {
			if (data == null) {
				return false;
			}
		}
		return true;
	}

	public String getCompleteData() {

		final StringBuilder sb = new StringBuilder();
		for (final String data : datas) {
			sb.append(data);
		}
		return sb.toString();
	}

	public void addData(int chunk, String data) {
		datas[chunk - 1] = data;

	}

}