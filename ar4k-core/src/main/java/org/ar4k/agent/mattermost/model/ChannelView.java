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

public class ChannelView {
	public ChannelView(String channelId) {
		this.channelId = channelId;
	}

	@JsonProperty("channel_id")
	private String channelId;
	@JsonProperty("prev_channel_id")
	private String prevChannelId;

	@java.lang.SuppressWarnings("all")
	public String getChannelId() {
		return this.channelId;
	}

	@java.lang.SuppressWarnings("all")
	public String getPrevChannelId() {
		return this.prevChannelId;
	}

	@JsonProperty("channel_id")
	@java.lang.SuppressWarnings("all")
	public void setChannelId(final String channelId) {
		this.channelId = channelId;
	}

	@JsonProperty("prev_channel_id")
	@java.lang.SuppressWarnings("all")
	public void setPrevChannelId(final String prevChannelId) {
		this.prevChannelId = prevChannelId;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this)
			return true;
		if (!(o instanceof ChannelView))
			return false;
		final ChannelView other = (ChannelView) o;
		if (!other.canEqual((java.lang.Object) this))
			return false;
		final java.lang.Object this$channelId = this.getChannelId();
		final java.lang.Object other$channelId = other.getChannelId();
		if (this$channelId == null ? other$channelId != null : !this$channelId.equals(other$channelId))
			return false;
		final java.lang.Object this$prevChannelId = this.getPrevChannelId();
		final java.lang.Object other$prevChannelId = other.getPrevChannelId();
		if (this$prevChannelId == null ? other$prevChannelId != null : !this$prevChannelId.equals(other$prevChannelId))
			return false;
		return true;
	}

	@java.lang.SuppressWarnings("all")
	protected boolean canEqual(final java.lang.Object other) {
		return other instanceof ChannelView;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final java.lang.Object $channelId = this.getChannelId();
		result = result * PRIME + ($channelId == null ? 43 : $channelId.hashCode());
		final java.lang.Object $prevChannelId = this.getPrevChannelId();
		result = result * PRIME + ($prevChannelId == null ? 43 : $prevChannelId.hashCode());
		return result;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ChannelView(channelId=" + this.getChannelId() + ", prevChannelId=" + this.getPrevChannelId() + ")";
	}

	@java.lang.SuppressWarnings("all")
	public ChannelView() {
	}
}
