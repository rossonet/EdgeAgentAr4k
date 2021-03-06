// Generated by delombok at Sun Apr 18 22:20:18 CEST 2021
/*
 * @(#) org.ar4k.agent.core.mattermost.model.config.ExperimentalSettings Copyright (c) 2018 Takayuki Maruyama
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
package org.ar4k.agent.mattermost.model.config;

public class ExperimentalSettings {
	private boolean clientSideCertEnable;
	private String clientSideCertCheck;
	/* @since Mattermost Server 5.6 */
	private boolean enablePostMetadata;
	/* @since Mattermost Server 5.10 */
	private boolean restrictSystemAdmin;
	/* @since Mattermost Server 5.10 */
	private boolean enableClickToReply;

	@Deprecated
	public void setEnablePostMetadata(boolean enablePostMetadata) {
		this.enablePostMetadata = enablePostMetadata;
	}

	@Deprecated
	public boolean isEnablePostMetadata() {
		return enablePostMetadata;
	}

	/* @since Mattermost Server 5.8 */
	private boolean disablePostMetadata;
	/* @since Mattermost Server 5.8 */
	private long linkMetadataTimeoutMilliseconds = 5000;
	/* @since Mattermost Server 5.20 */
	private boolean useNewSamlLibrary;

	@java.lang.SuppressWarnings("all")
	public ExperimentalSettings() {
	}

	@java.lang.SuppressWarnings("all")
	public boolean isClientSideCertEnable() {
		return this.clientSideCertEnable;
	}

	@java.lang.SuppressWarnings("all")
	public String getClientSideCertCheck() {
		return this.clientSideCertCheck;
	}

	@java.lang.SuppressWarnings("all")
	public boolean isRestrictSystemAdmin() {
		return this.restrictSystemAdmin;
	}

	@java.lang.SuppressWarnings("all")
	public boolean isEnableClickToReply() {
		return this.enableClickToReply;
	}

	@java.lang.SuppressWarnings("all")
	public boolean isDisablePostMetadata() {
		return this.disablePostMetadata;
	}

	@java.lang.SuppressWarnings("all")
	public long getLinkMetadataTimeoutMilliseconds() {
		return this.linkMetadataTimeoutMilliseconds;
	}

	@java.lang.SuppressWarnings("all")
	public boolean isUseNewSamlLibrary() {
		return this.useNewSamlLibrary;
	}

	@java.lang.SuppressWarnings("all")
	public void setClientSideCertEnable(final boolean clientSideCertEnable) {
		this.clientSideCertEnable = clientSideCertEnable;
	}

	@java.lang.SuppressWarnings("all")
	public void setClientSideCertCheck(final String clientSideCertCheck) {
		this.clientSideCertCheck = clientSideCertCheck;
	}

	@java.lang.SuppressWarnings("all")
	public void setRestrictSystemAdmin(final boolean restrictSystemAdmin) {
		this.restrictSystemAdmin = restrictSystemAdmin;
	}

	@java.lang.SuppressWarnings("all")
	public void setEnableClickToReply(final boolean enableClickToReply) {
		this.enableClickToReply = enableClickToReply;
	}

	@java.lang.SuppressWarnings("all")
	public void setDisablePostMetadata(final boolean disablePostMetadata) {
		this.disablePostMetadata = disablePostMetadata;
	}

	@java.lang.SuppressWarnings("all")
	public void setLinkMetadataTimeoutMilliseconds(final long linkMetadataTimeoutMilliseconds) {
		this.linkMetadataTimeoutMilliseconds = linkMetadataTimeoutMilliseconds;
	}

	@java.lang.SuppressWarnings("all")
	public void setUseNewSamlLibrary(final boolean useNewSamlLibrary) {
		this.useNewSamlLibrary = useNewSamlLibrary;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this)
			return true;
		if (!(o instanceof ExperimentalSettings))
			return false;
		final ExperimentalSettings other = (ExperimentalSettings) o;
		if (!other.canEqual((java.lang.Object) this))
			return false;
		if (this.isClientSideCertEnable() != other.isClientSideCertEnable())
			return false;
		if (this.isEnablePostMetadata() != other.isEnablePostMetadata())
			return false;
		if (this.isRestrictSystemAdmin() != other.isRestrictSystemAdmin())
			return false;
		if (this.isEnableClickToReply() != other.isEnableClickToReply())
			return false;
		if (this.isDisablePostMetadata() != other.isDisablePostMetadata())
			return false;
		if (this.getLinkMetadataTimeoutMilliseconds() != other.getLinkMetadataTimeoutMilliseconds())
			return false;
		if (this.isUseNewSamlLibrary() != other.isUseNewSamlLibrary())
			return false;
		final java.lang.Object this$clientSideCertCheck = this.getClientSideCertCheck();
		final java.lang.Object other$clientSideCertCheck = other.getClientSideCertCheck();
		if (this$clientSideCertCheck == null ? other$clientSideCertCheck != null
				: !this$clientSideCertCheck.equals(other$clientSideCertCheck))
			return false;
		return true;
	}

	@java.lang.SuppressWarnings("all")
	protected boolean canEqual(final java.lang.Object other) {
		return other instanceof ExperimentalSettings;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		result = result * PRIME + (this.isClientSideCertEnable() ? 79 : 97);
		result = result * PRIME + (this.isEnablePostMetadata() ? 79 : 97);
		result = result * PRIME + (this.isRestrictSystemAdmin() ? 79 : 97);
		result = result * PRIME + (this.isEnableClickToReply() ? 79 : 97);
		result = result * PRIME + (this.isDisablePostMetadata() ? 79 : 97);
		final long $linkMetadataTimeoutMilliseconds = this.getLinkMetadataTimeoutMilliseconds();
		result = result * PRIME + (int) ($linkMetadataTimeoutMilliseconds >>> 32 ^ $linkMetadataTimeoutMilliseconds);
		result = result * PRIME + (this.isUseNewSamlLibrary() ? 79 : 97);
		final java.lang.Object $clientSideCertCheck = this.getClientSideCertCheck();
		result = result * PRIME + ($clientSideCertCheck == null ? 43 : $clientSideCertCheck.hashCode());
		return result;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ExperimentalSettings(clientSideCertEnable=" + this.isClientSideCertEnable() + ", clientSideCertCheck="
				+ this.getClientSideCertCheck() + ", enablePostMetadata=" + this.isEnablePostMetadata()
				+ ", restrictSystemAdmin=" + this.isRestrictSystemAdmin() + ", enableClickToReply="
				+ this.isEnableClickToReply() + ", disablePostMetadata=" + this.isDisablePostMetadata()
				+ ", linkMetadataTimeoutMilliseconds=" + this.getLinkMetadataTimeoutMilliseconds()
				+ ", useNewSamlLibrary=" + this.isUseNewSamlLibrary() + ")";
	}
}
