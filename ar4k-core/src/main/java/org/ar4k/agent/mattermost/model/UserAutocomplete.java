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
import java.util.List;

public class UserAutocomplete {
	@JsonProperty("users")
	private List<User> users;
	@JsonProperty("out_of_channel")
	private List<User> outOfChannel;

	@java.lang.SuppressWarnings("all")
	public UserAutocomplete() {
	}

	@java.lang.SuppressWarnings("all")
	public List<User> getUsers() {
		return this.users;
	}

	@java.lang.SuppressWarnings("all")
	public List<User> getOutOfChannel() {
		return this.outOfChannel;
	}

	@JsonProperty("users")
	@java.lang.SuppressWarnings("all")
	public void setUsers(final List<User> users) {
		this.users = users;
	}

	@JsonProperty("out_of_channel")
	@java.lang.SuppressWarnings("all")
	public void setOutOfChannel(final List<User> outOfChannel) {
		this.outOfChannel = outOfChannel;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this)
			return true;
		if (!(o instanceof UserAutocomplete))
			return false;
		final UserAutocomplete other = (UserAutocomplete) o;
		if (!other.canEqual((java.lang.Object) this))
			return false;
		final java.lang.Object this$users = this.getUsers();
		final java.lang.Object other$users = other.getUsers();
		if (this$users == null ? other$users != null : !this$users.equals(other$users))
			return false;
		final java.lang.Object this$outOfChannel = this.getOutOfChannel();
		final java.lang.Object other$outOfChannel = other.getOutOfChannel();
		if (this$outOfChannel == null ? other$outOfChannel != null : !this$outOfChannel.equals(other$outOfChannel))
			return false;
		return true;
	}

	@java.lang.SuppressWarnings("all")
	protected boolean canEqual(final java.lang.Object other) {
		return other instanceof UserAutocomplete;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final java.lang.Object $users = this.getUsers();
		result = result * PRIME + ($users == null ? 43 : $users.hashCode());
		final java.lang.Object $outOfChannel = this.getOutOfChannel();
		result = result * PRIME + ($outOfChannel == null ? 43 : $outOfChannel.hashCode());
		return result;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "UserAutocomplete(users=" + this.getUsers() + ", outOfChannel=" + this.getOutOfChannel() + ")";
	}
}
