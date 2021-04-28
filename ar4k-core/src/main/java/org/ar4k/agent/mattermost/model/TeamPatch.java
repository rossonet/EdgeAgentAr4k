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

/**
 * Team patch.
 * 
 * @author Takayuki Maruyama
 */
public class TeamPatch {
  @JsonProperty("display_name")
  private String displayName;
  @JsonProperty("description")
  private String description;
  @JsonProperty("company_name")
  private String companyName;
  @JsonProperty("invite_id")
  private String inviteId;
  @JsonProperty("allow_open_invite")
  private boolean allowOpenInvite;

  @java.lang.SuppressWarnings("all")
  public TeamPatch() {
  }

  @java.lang.SuppressWarnings("all")
  public String getDisplayName() {
    return this.displayName;
  }

  @java.lang.SuppressWarnings("all")
  public String getDescription() {
    return this.description;
  }

  @java.lang.SuppressWarnings("all")
  public String getCompanyName() {
    return this.companyName;
  }

  @java.lang.SuppressWarnings("all")
  public String getInviteId() {
    return this.inviteId;
  }

  @java.lang.SuppressWarnings("all")
  public boolean isAllowOpenInvite() {
    return this.allowOpenInvite;
  }

  @JsonProperty("display_name")
  @java.lang.SuppressWarnings("all")
  public void setDisplayName(final String displayName) {
    this.displayName = displayName;
  }

  @JsonProperty("description")
  @java.lang.SuppressWarnings("all")
  public void setDescription(final String description) {
    this.description = description;
  }

  @JsonProperty("company_name")
  @java.lang.SuppressWarnings("all")
  public void setCompanyName(final String companyName) {
    this.companyName = companyName;
  }

  @JsonProperty("invite_id")
  @java.lang.SuppressWarnings("all")
  public void setInviteId(final String inviteId) {
    this.inviteId = inviteId;
  }

  @JsonProperty("allow_open_invite")
  @java.lang.SuppressWarnings("all")
  public void setAllowOpenInvite(final boolean allowOpenInvite) {
    this.allowOpenInvite = allowOpenInvite;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public boolean equals(final java.lang.Object o) {
    if (o == this) return true;
    if (!(o instanceof TeamPatch)) return false;
    final TeamPatch other = (TeamPatch) o;
    if (!other.canEqual((java.lang.Object) this)) return false;
    if (this.isAllowOpenInvite() != other.isAllowOpenInvite()) return false;
    final java.lang.Object this$displayName = this.getDisplayName();
    final java.lang.Object other$displayName = other.getDisplayName();
    if (this$displayName == null ? other$displayName != null : !this$displayName.equals(other$displayName)) return false;
    final java.lang.Object this$description = this.getDescription();
    final java.lang.Object other$description = other.getDescription();
    if (this$description == null ? other$description != null : !this$description.equals(other$description)) return false;
    final java.lang.Object this$companyName = this.getCompanyName();
    final java.lang.Object other$companyName = other.getCompanyName();
    if (this$companyName == null ? other$companyName != null : !this$companyName.equals(other$companyName)) return false;
    final java.lang.Object this$inviteId = this.getInviteId();
    final java.lang.Object other$inviteId = other.getInviteId();
    if (this$inviteId == null ? other$inviteId != null : !this$inviteId.equals(other$inviteId)) return false;
    return true;
  }

  @java.lang.SuppressWarnings("all")
  protected boolean canEqual(final java.lang.Object other) {
    return other instanceof TeamPatch;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + (this.isAllowOpenInvite() ? 79 : 97);
    final java.lang.Object $displayName = this.getDisplayName();
    result = result * PRIME + ($displayName == null ? 43 : $displayName.hashCode());
    final java.lang.Object $description = this.getDescription();
    result = result * PRIME + ($description == null ? 43 : $description.hashCode());
    final java.lang.Object $companyName = this.getCompanyName();
    result = result * PRIME + ($companyName == null ? 43 : $companyName.hashCode());
    final java.lang.Object $inviteId = this.getInviteId();
    result = result * PRIME + ($inviteId == null ? 43 : $inviteId.hashCode());
    return result;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public java.lang.String toString() {
    return "TeamPatch(displayName=" + this.getDisplayName() + ", description=" + this.getDescription() + ", companyName=" + this.getCompanyName() + ", inviteId=" + this.getInviteId() + ", allowOpenInvite=" + this.isAllowOpenInvite() + ")";
  }
}
