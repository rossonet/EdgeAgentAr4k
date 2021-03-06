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
package org.ar4k.agent.mattermost.model.license;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Customer.
 *
 * @author Takayuki Maruyama
 */
public class Customer {
	@JsonProperty("id")
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("email")
	private String email;
	@JsonProperty("company")
	private String company;
	@JsonProperty("phone_number")
	private String phoneNumber;

	@java.lang.SuppressWarnings("all")
	public Customer() {
	}

	@java.lang.SuppressWarnings("all")
	public String getId() {
		return this.id;
	}

	@java.lang.SuppressWarnings("all")
	public String getName() {
		return this.name;
	}

	@java.lang.SuppressWarnings("all")
	public String getEmail() {
		return this.email;
	}

	@java.lang.SuppressWarnings("all")
	public String getCompany() {
		return this.company;
	}

	@java.lang.SuppressWarnings("all")
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	@JsonProperty("id")
	@java.lang.SuppressWarnings("all")
	public void setId(final String id) {
		this.id = id;
	}

	@JsonProperty("name")
	@java.lang.SuppressWarnings("all")
	public void setName(final String name) {
		this.name = name;
	}

	@JsonProperty("email")
	@java.lang.SuppressWarnings("all")
	public void setEmail(final String email) {
		this.email = email;
	}

	@JsonProperty("company")
	@java.lang.SuppressWarnings("all")
	public void setCompany(final String company) {
		this.company = company;
	}

	@JsonProperty("phone_number")
	@java.lang.SuppressWarnings("all")
	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) return true;
		if (!(o instanceof Customer)) return false;
		final Customer other = (Customer) o;
		if (!other.canEqual((java.lang.Object) this)) return false;
		final java.lang.Object this$id = this.getId();
		final java.lang.Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
		final java.lang.Object this$name = this.getName();
		final java.lang.Object other$name = other.getName();
		if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
		final java.lang.Object this$email = this.getEmail();
		final java.lang.Object other$email = other.getEmail();
		if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
		final java.lang.Object this$company = this.getCompany();
		final java.lang.Object other$company = other.getCompany();
		if (this$company == null ? other$company != null : !this$company.equals(other$company)) return false;
		final java.lang.Object this$phoneNumber = this.getPhoneNumber();
		final java.lang.Object other$phoneNumber = other.getPhoneNumber();
		if (this$phoneNumber == null ? other$phoneNumber != null : !this$phoneNumber.equals(other$phoneNumber)) return false;
		return true;
	}

	@java.lang.SuppressWarnings("all")
	protected boolean canEqual(final java.lang.Object other) {
		return other instanceof Customer;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final java.lang.Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		final java.lang.Object $name = this.getName();
		result = result * PRIME + ($name == null ? 43 : $name.hashCode());
		final java.lang.Object $email = this.getEmail();
		result = result * PRIME + ($email == null ? 43 : $email.hashCode());
		final java.lang.Object $company = this.getCompany();
		result = result * PRIME + ($company == null ? 43 : $company.hashCode());
		final java.lang.Object $phoneNumber = this.getPhoneNumber();
		result = result * PRIME + ($phoneNumber == null ? 43 : $phoneNumber.hashCode());
		return result;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "Customer(id=" + this.getId() + ", name=" + this.getName() + ", email=" + this.getEmail() + ", company=" + this.getCompany() + ", phoneNumber=" + this.getPhoneNumber() + ")";
	}
}
