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
package org.ar4k.agent.mattermost.model.gitlab;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GitLab user.
 *
 * @author Takayuki Maruyama
 */
public class GitLabUser {
	@JsonProperty("id")
	private long id;
	@JsonProperty("username")
	private String username;
	@JsonProperty("login")
	private String login;
	@JsonProperty("email")
	private String email;
	@JsonProperty("name")
	private String name;

	@java.lang.SuppressWarnings("all")
	public GitLabUser() {
	}

	@java.lang.SuppressWarnings("all")
	public long getId() {
		return this.id;
	}

	@java.lang.SuppressWarnings("all")
	public String getUsername() {
		return this.username;
	}

	@java.lang.SuppressWarnings("all")
	public String getLogin() {
		return this.login;
	}

	@java.lang.SuppressWarnings("all")
	public String getEmail() {
		return this.email;
	}

	@java.lang.SuppressWarnings("all")
	public String getName() {
		return this.name;
	}

	@JsonProperty("id")
	@java.lang.SuppressWarnings("all")
	public void setId(final long id) {
		this.id = id;
	}

	@JsonProperty("username")
	@java.lang.SuppressWarnings("all")
	public void setUsername(final String username) {
		this.username = username;
	}

	@JsonProperty("login")
	@java.lang.SuppressWarnings("all")
	public void setLogin(final String login) {
		this.login = login;
	}

	@JsonProperty("email")
	@java.lang.SuppressWarnings("all")
	public void setEmail(final String email) {
		this.email = email;
	}

	@JsonProperty("name")
	@java.lang.SuppressWarnings("all")
	public void setName(final String name) {
		this.name = name;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof GitLabUser)) return false;
		final GitLabUser other = (GitLabUser) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		if (this.getId() != other.getId()) return false;
		final java.lang.Object this$username = this.getUsername();
		final java.lang.Object other$username = other.getUsername();
		if (this$username == null ? other$username != null : !this$username.equals(other$username)) return false;
		final java.lang.Object this$login = this.getLogin();
		final java.lang.Object other$login = other.getLogin();
		if (this$login == null ? other$login != null : !this$login.equals(other$login)) return false;
		final java.lang.Object this$email = this.getEmail();
		final java.lang.Object other$email = other.getEmail();
		if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
		final java.lang.Object this$name = this.getName();
		final java.lang.Object other$name = other.getName();
		if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
		return true;
	}

	@java.lang.SuppressWarnings("all")
	protected boolean canEqual(final java.lang.Object other) {
		return other instanceof GitLabUser;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final long $id = this.getId();
		result = result * PRIME + (int) ($id >>> 32 ^ $id);
		final java.lang.Object $username = this.getUsername();
		result = result * PRIME + ($username == null ? 43 : $username.hashCode());
		final java.lang.Object $login = this.getLogin();
		result = result * PRIME + ($login == null ? 43 : $login.hashCode());
		final java.lang.Object $email = this.getEmail();
		result = result * PRIME + ($email == null ? 43 : $email.hashCode());
		final java.lang.Object $name = this.getName();
		result = result * PRIME + ($name == null ? 43 : $name.hashCode());
		return result;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "GitLabUser(id=" + this.getId() + ", username=" + this.getUsername() + ", login=" + this.getLogin() + ", email=" + this.getEmail() + ", name=" + this.getName() + ")";
	}
}