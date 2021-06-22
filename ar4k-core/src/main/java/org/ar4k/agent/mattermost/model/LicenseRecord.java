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

public class LicenseRecord {
	@JsonProperty("id")
	private String id;
	@JsonProperty("create_at")
	private long createAt;

	@java.lang.SuppressWarnings("all")
	public LicenseRecord() {
	}

	@java.lang.SuppressWarnings("all")
	public String getId() {
		return this.id;
	}

	@java.lang.SuppressWarnings("all")
	public long getCreateAt() {
		return this.createAt;
	}

	@JsonProperty("id")
	@java.lang.SuppressWarnings("all")
	public void setId(final String id) {
		this.id = id;
	}

	@JsonProperty("create_at")
	@java.lang.SuppressWarnings("all")
	public void setCreateAt(final long createAt) {
		this.createAt = createAt;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this)
			return true;
		if (!(o instanceof LicenseRecord))
			return false;
		final LicenseRecord other = (LicenseRecord) o;
		if (!other.canEqual((java.lang.Object) this))
			return false;
		if (this.getCreateAt() != other.getCreateAt())
			return false;
		final java.lang.Object this$id = this.getId();
		final java.lang.Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id))
			return false;
		return true;
	}

	@java.lang.SuppressWarnings("all")
	protected boolean canEqual(final java.lang.Object other) {
		return other instanceof LicenseRecord;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final long $createAt = this.getCreateAt();
		result = result * PRIME + (int) ($createAt >>> 32 ^ $createAt);
		final java.lang.Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "LicenseRecord(id=" + this.getId() + ", createAt=" + this.getCreateAt() + ")";
	}
}
