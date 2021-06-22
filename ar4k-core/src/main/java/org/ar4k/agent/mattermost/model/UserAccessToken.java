// Generated by delombok at Sun Apr 18 22:20:18 CEST 2021
/*
 * Copyright (c) 2019 Takayuki Maruyama
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

public class UserAccessToken {
	private String id;
	private String token;
	private String userId;
	private String description;
	@JsonProperty("is_active")
	private boolean isActive;

	@java.lang.SuppressWarnings("all")
	public UserAccessToken() {
	}

	@java.lang.SuppressWarnings("all")
	public String getId() {
		return this.id;
	}

	@java.lang.SuppressWarnings("all")
	public String getToken() {
		return this.token;
	}

	@java.lang.SuppressWarnings("all")
	public String getUserId() {
		return this.userId;
	}

	@java.lang.SuppressWarnings("all")
	public String getDescription() {
		return this.description;
	}

	@java.lang.SuppressWarnings("all")
	public boolean isActive() {
		return this.isActive;
	}

	@java.lang.SuppressWarnings("all")
	public void setId(final String id) {
		this.id = id;
	}

	@java.lang.SuppressWarnings("all")
	public void setToken(final String token) {
		this.token = token;
	}

	@java.lang.SuppressWarnings("all")
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	@java.lang.SuppressWarnings("all")
	public void setDescription(final String description) {
		this.description = description;
	}

	@JsonProperty("is_active")
	@java.lang.SuppressWarnings("all")
	public void setActive(final boolean isActive) {
		this.isActive = isActive;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this)
			return true;
		if (!(o instanceof UserAccessToken))
			return false;
		final UserAccessToken other = (UserAccessToken) o;
		if (!other.canEqual((java.lang.Object) this))
			return false;
		if (this.isActive() != other.isActive())
			return false;
		final java.lang.Object this$id = this.getId();
		final java.lang.Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id))
			return false;
		final java.lang.Object this$token = this.getToken();
		final java.lang.Object other$token = other.getToken();
		if (this$token == null ? other$token != null : !this$token.equals(other$token))
			return false;
		final java.lang.Object this$userId = this.getUserId();
		final java.lang.Object other$userId = other.getUserId();
		if (this$userId == null ? other$userId != null : !this$userId.equals(other$userId))
			return false;
		final java.lang.Object this$description = this.getDescription();
		final java.lang.Object other$description = other.getDescription();
		if (this$description == null ? other$description != null : !this$description.equals(other$description))
			return false;
		return true;
	}

	@java.lang.SuppressWarnings("all")
	protected boolean canEqual(final java.lang.Object other) {
		return other instanceof UserAccessToken;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		result = result * PRIME + (this.isActive() ? 79 : 97);
		final java.lang.Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		final java.lang.Object $token = this.getToken();
		result = result * PRIME + ($token == null ? 43 : $token.hashCode());
		final java.lang.Object $userId = this.getUserId();
		result = result * PRIME + ($userId == null ? 43 : $userId.hashCode());
		final java.lang.Object $description = this.getDescription();
		result = result * PRIME + ($description == null ? 43 : $description.hashCode());
		return result;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "UserAccessToken(id=" + this.getId() + ", token=" + this.getToken() + ", userId=" + this.getUserId()
				+ ", description=" + this.getDescription() + ", isActive=" + this.isActive() + ")";
	}
}
