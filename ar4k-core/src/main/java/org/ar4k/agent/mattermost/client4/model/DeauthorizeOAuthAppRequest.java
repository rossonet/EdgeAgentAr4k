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

public final class DeauthorizeOAuthAppRequest {
	@JsonProperty("client_id")
	private final String clientId;

	@java.lang.SuppressWarnings("all")
	DeauthorizeOAuthAppRequest(final String clientId) {
		this.clientId = clientId;
	}

	@java.lang.SuppressWarnings("all")
	public static class DeauthorizeOAuthAppRequestBuilder {
		@java.lang.SuppressWarnings("all")
		private String clientId;

		@java.lang.SuppressWarnings("all")
		DeauthorizeOAuthAppRequestBuilder() {
		}

		@JsonProperty("client_id")
		@java.lang.SuppressWarnings("all")
		public DeauthorizeOAuthAppRequest.DeauthorizeOAuthAppRequestBuilder clientId(final String clientId) {
			this.clientId = clientId;
			return this;
		}

		@java.lang.SuppressWarnings("all")
		public DeauthorizeOAuthAppRequest build() {
			return new DeauthorizeOAuthAppRequest(this.clientId);
		}

		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "DeauthorizeOAuthAppRequest.DeauthorizeOAuthAppRequestBuilder(clientId=" + this.clientId + ")";
		}
	}

	@java.lang.SuppressWarnings("all")
	public static DeauthorizeOAuthAppRequest.DeauthorizeOAuthAppRequestBuilder builder() {
		return new DeauthorizeOAuthAppRequest.DeauthorizeOAuthAppRequestBuilder();
	}

	@java.lang.SuppressWarnings("all")
	public String getClientId() {
		return this.clientId;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this)
			return true;
		if (!(o instanceof DeauthorizeOAuthAppRequest))
			return false;
		final DeauthorizeOAuthAppRequest other = (DeauthorizeOAuthAppRequest) o;
		final java.lang.Object this$clientId = this.getClientId();
		final java.lang.Object other$clientId = other.getClientId();
		if (this$clientId == null ? other$clientId != null : !this$clientId.equals(other$clientId))
			return false;
		return true;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final java.lang.Object $clientId = this.getClientId();
		result = result * PRIME + ($clientId == null ? 43 : $clientId.hashCode());
		return result;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "DeauthorizeOAuthAppRequest(clientId=" + this.getClientId() + ")";
	}
}
