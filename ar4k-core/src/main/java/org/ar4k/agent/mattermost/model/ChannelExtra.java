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

public class ChannelExtra {
	@JsonProperty("id")
	private String id;
	@JsonProperty("members")
	private ExtraMember[] members;
	@JsonProperty("member_count")
	private long memberCont;

	@java.lang.SuppressWarnings("all")
	public ChannelExtra() {
	}

	@java.lang.SuppressWarnings("all")
	public String getId() {
		return this.id;
	}

	@java.lang.SuppressWarnings("all")
	public ExtraMember[] getMembers() {
		return this.members;
	}

	@java.lang.SuppressWarnings("all")
	public long getMemberCont() {
		return this.memberCont;
	}

	@JsonProperty("id")
	@java.lang.SuppressWarnings("all")
	public void setId(final String id) {
		this.id = id;
	}

	@JsonProperty("members")
	@java.lang.SuppressWarnings("all")
	public void setMembers(final ExtraMember[] members) {
		this.members = members;
	}

	@JsonProperty("member_count")
	@java.lang.SuppressWarnings("all")
	public void setMemberCont(final long memberCont) {
		this.memberCont = memberCont;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this)
			return true;
		if (!(o instanceof ChannelExtra))
			return false;
		final ChannelExtra other = (ChannelExtra) o;
		if (!other.canEqual((java.lang.Object) this))
			return false;
		if (this.getMemberCont() != other.getMemberCont())
			return false;
		final java.lang.Object this$id = this.getId();
		final java.lang.Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id))
			return false;
		if (!java.util.Arrays.deepEquals(this.getMembers(), other.getMembers()))
			return false;
		return true;
	}

	@java.lang.SuppressWarnings("all")
	protected boolean canEqual(final java.lang.Object other) {
		return other instanceof ChannelExtra;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final long $memberCont = this.getMemberCont();
		result = result * PRIME + (int) ($memberCont >>> 32 ^ $memberCont);
		final java.lang.Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		result = result * PRIME + java.util.Arrays.deepHashCode(this.getMembers());
		return result;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ChannelExtra(id=" + this.getId() + ", members=" + java.util.Arrays.deepToString(this.getMembers())
				+ ", memberCont=" + this.getMemberCont() + ")";
	}
}
