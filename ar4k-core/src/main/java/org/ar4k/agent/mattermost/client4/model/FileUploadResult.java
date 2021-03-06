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
package org.ar4k.agent.mattermost.client4.model;

import org.ar4k.agent.mattermost.model.FileInfo;

public class FileUploadResult {
	private FileInfo[] fileInfos;
	private String[] clientIds;

	@java.lang.SuppressWarnings("all")
	public FileUploadResult() {
	}

	@java.lang.SuppressWarnings("all")
	public FileInfo[] getFileInfos() {
		return this.fileInfos;
	}

	@java.lang.SuppressWarnings("all")
	public String[] getClientIds() {
		return this.clientIds;
	}

	@java.lang.SuppressWarnings("all")
	public void setFileInfos(final FileInfo[] fileInfos) {
		this.fileInfos = fileInfos;
	}

	@java.lang.SuppressWarnings("all")
	public void setClientIds(final String[] clientIds) {
		this.clientIds = clientIds;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this)
			return true;
		if (!(o instanceof FileUploadResult))
			return false;
		final FileUploadResult other = (FileUploadResult) o;
		if (!other.canEqual((java.lang.Object) this))
			return false;
		if (!java.util.Arrays.deepEquals(this.getFileInfos(), other.getFileInfos()))
			return false;
		if (!java.util.Arrays.deepEquals(this.getClientIds(), other.getClientIds()))
			return false;
		return true;
	}

	@java.lang.SuppressWarnings("all")
	protected boolean canEqual(final java.lang.Object other) {
		return other instanceof FileUploadResult;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		result = result * PRIME + java.util.Arrays.deepHashCode(this.getFileInfos());
		result = result * PRIME + java.util.Arrays.deepHashCode(this.getClientIds());
		return result;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "FileUploadResult(fileInfos=" + java.util.Arrays.deepToString(this.getFileInfos()) + ", clientIds="
				+ java.util.Arrays.deepToString(this.getClientIds()) + ")";
	}
}
