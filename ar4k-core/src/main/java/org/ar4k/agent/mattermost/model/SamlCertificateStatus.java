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

public class SamlCertificateStatus {
	@JsonProperty("idp_certificate_file")
	private boolean idpCertificateFile;
	@JsonProperty("private_key_file")
	private boolean privateKeyFile;
	@JsonProperty("public_certificate_file")
	private boolean publicCertificateFile;

	@java.lang.SuppressWarnings("all")
	public SamlCertificateStatus() {
	}

	@java.lang.SuppressWarnings("all")
	public boolean isIdpCertificateFile() {
		return this.idpCertificateFile;
	}

	@java.lang.SuppressWarnings("all")
	public boolean isPrivateKeyFile() {
		return this.privateKeyFile;
	}

	@java.lang.SuppressWarnings("all")
	public boolean isPublicCertificateFile() {
		return this.publicCertificateFile;
	}

	@JsonProperty("idp_certificate_file")
	@java.lang.SuppressWarnings("all")
	public void setIdpCertificateFile(final boolean idpCertificateFile) {
		this.idpCertificateFile = idpCertificateFile;
	}

	@JsonProperty("private_key_file")
	@java.lang.SuppressWarnings("all")
	public void setPrivateKeyFile(final boolean privateKeyFile) {
		this.privateKeyFile = privateKeyFile;
	}

	@JsonProperty("public_certificate_file")
	@java.lang.SuppressWarnings("all")
	public void setPublicCertificateFile(final boolean publicCertificateFile) {
		this.publicCertificateFile = publicCertificateFile;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this)
			return true;
		if (!(o instanceof SamlCertificateStatus))
			return false;
		final SamlCertificateStatus other = (SamlCertificateStatus) o;
		if (!other.canEqual((java.lang.Object) this))
			return false;
		if (this.isIdpCertificateFile() != other.isIdpCertificateFile())
			return false;
		if (this.isPrivateKeyFile() != other.isPrivateKeyFile())
			return false;
		if (this.isPublicCertificateFile() != other.isPublicCertificateFile())
			return false;
		return true;
	}

	@java.lang.SuppressWarnings("all")
	protected boolean canEqual(final java.lang.Object other) {
		return other instanceof SamlCertificateStatus;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		result = result * PRIME + (this.isIdpCertificateFile() ? 79 : 97);
		result = result * PRIME + (this.isPrivateKeyFile() ? 79 : 97);
		result = result * PRIME + (this.isPublicCertificateFile() ? 79 : 97);
		return result;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "SamlCertificateStatus(idpCertificateFile=" + this.isIdpCertificateFile() + ", privateKeyFile="
				+ this.isPrivateKeyFile() + ", publicCertificateFile=" + this.isPublicCertificateFile() + ")";
	}
}
