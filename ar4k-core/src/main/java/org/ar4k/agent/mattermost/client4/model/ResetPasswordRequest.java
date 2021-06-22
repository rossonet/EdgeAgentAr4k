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
package org.ar4k.agent.mattermost.client4.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class ResetPasswordRequest {
	@JsonProperty("token")
	private final String token;
	@JsonProperty("new_password")
	private final String newPassword;

	@java.lang.SuppressWarnings("all")
	ResetPasswordRequest(final String token, final String newPassword) {
		this.token = token;
		this.newPassword = newPassword;
	}

	@java.lang.SuppressWarnings("all")
	public static class ResetPasswordRequestBuilder {
		@java.lang.SuppressWarnings("all")
		private String token;
		@java.lang.SuppressWarnings("all")
		private String newPassword;

		@java.lang.SuppressWarnings("all")
		ResetPasswordRequestBuilder() {
		}

		@JsonProperty("token")
		@java.lang.SuppressWarnings("all")
		public ResetPasswordRequest.ResetPasswordRequestBuilder token(final String token) {
			this.token = token;
			return this;
		}

		@JsonProperty("new_password")
		@java.lang.SuppressWarnings("all")
		public ResetPasswordRequest.ResetPasswordRequestBuilder newPassword(final String newPassword) {
			this.newPassword = newPassword;
			return this;
		}

		@java.lang.SuppressWarnings("all")
		public ResetPasswordRequest build() {
			return new ResetPasswordRequest(this.token, this.newPassword);
		}

		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "ResetPasswordRequest.ResetPasswordRequestBuilder(token=" + this.token + ", newPassword="
					+ this.newPassword + ")";
		}
	}

	@java.lang.SuppressWarnings("all")
	public static ResetPasswordRequest.ResetPasswordRequestBuilder builder() {
		return new ResetPasswordRequest.ResetPasswordRequestBuilder();
	}

	@java.lang.SuppressWarnings("all")
	public String getToken() {
		return this.token;
	}

	@java.lang.SuppressWarnings("all")
	public String getNewPassword() {
		return this.newPassword;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this)
			return true;
		if (!(o instanceof ResetPasswordRequest))
			return false;
		final ResetPasswordRequest other = (ResetPasswordRequest) o;
		final java.lang.Object this$token = this.getToken();
		final java.lang.Object other$token = other.getToken();
		if (this$token == null ? other$token != null : !this$token.equals(other$token))
			return false;
		final java.lang.Object this$newPassword = this.getNewPassword();
		final java.lang.Object other$newPassword = other.getNewPassword();
		if (this$newPassword == null ? other$newPassword != null : !this$newPassword.equals(other$newPassword))
			return false;
		return true;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final java.lang.Object $token = this.getToken();
		result = result * PRIME + ($token == null ? 43 : $token.hashCode());
		final java.lang.Object $newPassword = this.getNewPassword();
		result = result * PRIME + ($newPassword == null ? 43 : $newPassword.hashCode());
		return result;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ResetPasswordRequest(token=" + this.getToken() + ", newPassword=" + this.getNewPassword() + ")";
	}
}
