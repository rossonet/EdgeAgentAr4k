// Generated by delombok at Sun Apr 18 22:20:18 CEST 2021
/*
 * Copyright (c) 2016-present, Takayuki Maruyama
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

public class TeamStats {
	@JsonProperty("team_id")
	private String teamId;
	@JsonProperty("total_member_count")
	private long totalMemberCount;
	@JsonProperty("active_member_count")
	private long activeMemberCount;

	@java.lang.SuppressWarnings("all")
	public TeamStats() {
	}

	@java.lang.SuppressWarnings("all")
	public String getTeamId() {
		return this.teamId;
	}

	@java.lang.SuppressWarnings("all")
	public long getTotalMemberCount() {
		return this.totalMemberCount;
	}

	@java.lang.SuppressWarnings("all")
	public long getActiveMemberCount() {
		return this.activeMemberCount;
	}

	@JsonProperty("team_id")
	@java.lang.SuppressWarnings("all")
	public void setTeamId(final String teamId) {
		this.teamId = teamId;
	}

	@JsonProperty("total_member_count")
	@java.lang.SuppressWarnings("all")
	public void setTotalMemberCount(final long totalMemberCount) {
		this.totalMemberCount = totalMemberCount;
	}

	@JsonProperty("active_member_count")
	@java.lang.SuppressWarnings("all")
	public void setActiveMemberCount(final long activeMemberCount) {
		this.activeMemberCount = activeMemberCount;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this)
			return true;
		if (!(o instanceof TeamStats))
			return false;
		final TeamStats other = (TeamStats) o;
		if (!other.canEqual((java.lang.Object) this))
			return false;
		if (this.getTotalMemberCount() != other.getTotalMemberCount())
			return false;
		if (this.getActiveMemberCount() != other.getActiveMemberCount())
			return false;
		final java.lang.Object this$teamId = this.getTeamId();
		final java.lang.Object other$teamId = other.getTeamId();
		if (this$teamId == null ? other$teamId != null : !this$teamId.equals(other$teamId))
			return false;
		return true;
	}

	@java.lang.SuppressWarnings("all")
	protected boolean canEqual(final java.lang.Object other) {
		return other instanceof TeamStats;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final long $totalMemberCount = this.getTotalMemberCount();
		result = result * PRIME + (int) ($totalMemberCount >>> 32 ^ $totalMemberCount);
		final long $activeMemberCount = this.getActiveMemberCount();
		result = result * PRIME + (int) ($activeMemberCount >>> 32 ^ $activeMemberCount);
		final java.lang.Object $teamId = this.getTeamId();
		result = result * PRIME + ($teamId == null ? 43 : $teamId.hashCode());
		return result;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "TeamStats(teamId=" + this.getTeamId() + ", totalMemberCount=" + this.getTotalMemberCount()
				+ ", activeMemberCount=" + this.getActiveMemberCount() + ")";
	}
}
