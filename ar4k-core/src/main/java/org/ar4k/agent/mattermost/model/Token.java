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

public class Token {
	private String token;
	private long createAt;
	private String type;
	private String extra;

	@java.lang.SuppressWarnings("all")
	public Token() {
	}

	@java.lang.SuppressWarnings("all")
	public String getToken() {
		return this.token;
	}

	@java.lang.SuppressWarnings("all")
	public long getCreateAt() {
		return this.createAt;
	}

	@java.lang.SuppressWarnings("all")
	public String getType() {
		return this.type;
	}

	@java.lang.SuppressWarnings("all")
	public String getExtra() {
		return this.extra;
	}

	@java.lang.SuppressWarnings("all")
	public void setToken(final String token) {
		this.token = token;
	}

	@java.lang.SuppressWarnings("all")
	public void setCreateAt(final long createAt) {
		this.createAt = createAt;
	}

	@java.lang.SuppressWarnings("all")
	public void setType(final String type) {
		this.type = type;
	}

	@java.lang.SuppressWarnings("all")
	public void setExtra(final String extra) {
		this.extra = extra;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Token))
			return false;
		final Token other = (Token) o;
		if (!other.canEqual((java.lang.Object) this))
			return false;
		if (this.getCreateAt() != other.getCreateAt())
			return false;
		final java.lang.Object this$token = this.getToken();
		final java.lang.Object other$token = other.getToken();
		if (this$token == null ? other$token != null : !this$token.equals(other$token))
			return false;
		final java.lang.Object this$type = this.getType();
		final java.lang.Object other$type = other.getType();
		if (this$type == null ? other$type != null : !this$type.equals(other$type))
			return false;
		final java.lang.Object this$extra = this.getExtra();
		final java.lang.Object other$extra = other.getExtra();
		if (this$extra == null ? other$extra != null : !this$extra.equals(other$extra))
			return false;
		return true;
	}

	@java.lang.SuppressWarnings("all")
	protected boolean canEqual(final java.lang.Object other) {
		return other instanceof Token;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final long $createAt = this.getCreateAt();
		result = result * PRIME + (int) ($createAt >>> 32 ^ $createAt);
		final java.lang.Object $token = this.getToken();
		result = result * PRIME + ($token == null ? 43 : $token.hashCode());
		final java.lang.Object $type = this.getType();
		result = result * PRIME + ($type == null ? 43 : $type.hashCode());
		final java.lang.Object $extra = this.getExtra();
		result = result * PRIME + ($extra == null ? 43 : $extra.hashCode());
		return result;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "Token(token=" + this.getToken() + ", createAt=" + this.getCreateAt() + ", type=" + this.getType()
				+ ", extra=" + this.getExtra() + ")";
	}
}
