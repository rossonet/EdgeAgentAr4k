// Generated by delombok at Sun Apr 18 22:20:18 CEST 2021
/*
 * Copyright (c) 2017-present, Takayuki Maruyama
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.ar4k.agent.mattermost.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClusterStats {
	@JsonProperty("id")
	private String id;
	@JsonProperty("total_websocket_connections")
	private int totalWebsocketConnections;
	@JsonProperty("total_read_db_connections")
	private int totalReadDbConnections;
	@JsonProperty("total_master_db_connections")
	private int totalMasterDbConnections;

	@java.lang.SuppressWarnings("all")
	public ClusterStats() {
	}

	@java.lang.SuppressWarnings("all")
	public String getId() {
		return this.id;
	}

	@java.lang.SuppressWarnings("all")
	public int getTotalWebsocketConnections() {
		return this.totalWebsocketConnections;
	}

	@java.lang.SuppressWarnings("all")
	public int getTotalReadDbConnections() {
		return this.totalReadDbConnections;
	}

	@java.lang.SuppressWarnings("all")
	public int getTotalMasterDbConnections() {
		return this.totalMasterDbConnections;
	}

	@JsonProperty("id")
	@java.lang.SuppressWarnings("all")
	public void setId(final String id) {
		this.id = id;
	}

	@JsonProperty("total_websocket_connections")
	@java.lang.SuppressWarnings("all")
	public void setTotalWebsocketConnections(final int totalWebsocketConnections) {
		this.totalWebsocketConnections = totalWebsocketConnections;
	}

	@JsonProperty("total_read_db_connections")
	@java.lang.SuppressWarnings("all")
	public void setTotalReadDbConnections(final int totalReadDbConnections) {
		this.totalReadDbConnections = totalReadDbConnections;
	}

	@JsonProperty("total_master_db_connections")
	@java.lang.SuppressWarnings("all")
	public void setTotalMasterDbConnections(final int totalMasterDbConnections) {
		this.totalMasterDbConnections = totalMasterDbConnections;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this)
			return true;
		if (!(o instanceof ClusterStats))
			return false;
		final ClusterStats other = (ClusterStats) o;
		if (!other.canEqual((java.lang.Object) this))
			return false;
		if (this.getTotalWebsocketConnections() != other.getTotalWebsocketConnections())
			return false;
		if (this.getTotalReadDbConnections() != other.getTotalReadDbConnections())
			return false;
		if (this.getTotalMasterDbConnections() != other.getTotalMasterDbConnections())
			return false;
		final java.lang.Object this$id = this.getId();
		final java.lang.Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id))
			return false;
		return true;
	}

	@java.lang.SuppressWarnings("all")
	protected boolean canEqual(final java.lang.Object other) {
		return other instanceof ClusterStats;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		result = result * PRIME + this.getTotalWebsocketConnections();
		result = result * PRIME + this.getTotalReadDbConnections();
		result = result * PRIME + this.getTotalMasterDbConnections();
		final java.lang.Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ClusterStats(id=" + this.getId() + ", totalWebsocketConnections=" + this.getTotalWebsocketConnections()
				+ ", totalReadDbConnections=" + this.getTotalReadDbConnections() + ", totalMasterDbConnections="
				+ this.getTotalMasterDbConnections() + ")";
	}
}
