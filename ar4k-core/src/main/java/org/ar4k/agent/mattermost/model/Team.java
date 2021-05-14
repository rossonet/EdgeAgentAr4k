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
package org.ar4k.agent.mattermost.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Team.
 * 
 * @author Takayuki Maruyama
 */
public class Team {
  @JsonProperty("id")
  private String id;
  @JsonProperty("create_at")
  private long createAt;
  @JsonProperty("update_at")
  private long updateAt;
  @JsonProperty("delete_at")
  private long deleteAt;
  @JsonProperty("display_name")
  private String displayName;
  @JsonProperty("name")
  private String name;
  @JsonProperty("description")
  private String description;
  @JsonProperty("email")
  private String email;
  @JsonProperty("type")
  private TeamType type;
  @JsonProperty("company_name")
  private String companyName;
  @JsonProperty("allowed_domains")
  private String allowedDomains;
  @JsonProperty("invite_id")
  private String inviteId;
  @JsonProperty("allow_open_invite")
  private boolean allowOpenInvite;
  /* @since Mattermost Server 4.9 */
  private long lastTeamIconUpdate;
  /* @since Mattermost Server  what ver? */
  private String schemeId;
  /* @since Mattermost Server 5.10 */
  private boolean groupConstrained;

  @java.lang.SuppressWarnings("all")
  public Team() {
  }

  @java.lang.SuppressWarnings("all")
  public String getId() {
    return this.id;
  }

  @java.lang.SuppressWarnings("all")
  public long getCreateAt() {
    return this.createAt;
  }

  @java.lang.SuppressWarnings("all")
  public long getUpdateAt() {
    return this.updateAt;
  }

  @java.lang.SuppressWarnings("all")
  public long getDeleteAt() {
    return this.deleteAt;
  }

  @java.lang.SuppressWarnings("all")
  public String getDisplayName() {
    return this.displayName;
  }

  @java.lang.SuppressWarnings("all")
  public String getName() {
    return this.name;
  }

  @java.lang.SuppressWarnings("all")
  public String getDescription() {
    return this.description;
  }

  @java.lang.SuppressWarnings("all")
  public String getEmail() {
    return this.email;
  }

  @java.lang.SuppressWarnings("all")
  public TeamType getType() {
    return this.type;
  }

  @java.lang.SuppressWarnings("all")
  public String getCompanyName() {
    return this.companyName;
  }

  @java.lang.SuppressWarnings("all")
  public String getAllowedDomains() {
    return this.allowedDomains;
  }

  @java.lang.SuppressWarnings("all")
  public String getInviteId() {
    return this.inviteId;
  }

  @java.lang.SuppressWarnings("all")
  public boolean isAllowOpenInvite() {
    return this.allowOpenInvite;
  }

  @java.lang.SuppressWarnings("all")
  public long getLastTeamIconUpdate() {
    return this.lastTeamIconUpdate;
  }

  @java.lang.SuppressWarnings("all")
  public String getSchemeId() {
    return this.schemeId;
  }

  @java.lang.SuppressWarnings("all")
  public boolean isGroupConstrained() {
    return this.groupConstrained;
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

  @JsonProperty("update_at")
  @java.lang.SuppressWarnings("all")
  public void setUpdateAt(final long updateAt) {
    this.updateAt = updateAt;
  }

  @JsonProperty("delete_at")
  @java.lang.SuppressWarnings("all")
  public void setDeleteAt(final long deleteAt) {
    this.deleteAt = deleteAt;
  }

  @JsonProperty("display_name")
  @java.lang.SuppressWarnings("all")
  public void setDisplayName(final String displayName) {
    this.displayName = displayName;
  }

  @JsonProperty("name")
  @java.lang.SuppressWarnings("all")
  public void setName(final String name) {
    this.name = name;
  }

  @JsonProperty("description")
  @java.lang.SuppressWarnings("all")
  public void setDescription(final String description) {
    this.description = description;
  }

  @JsonProperty("email")
  @java.lang.SuppressWarnings("all")
  public void setEmail(final String email) {
    this.email = email;
  }

  @JsonProperty("type")
  @java.lang.SuppressWarnings("all")
  public void setType(final TeamType type) {
    this.type = type;
  }

  @JsonProperty("company_name")
  @java.lang.SuppressWarnings("all")
  public void setCompanyName(final String companyName) {
    this.companyName = companyName;
  }

  @JsonProperty("allowed_domains")
  @java.lang.SuppressWarnings("all")
  public void setAllowedDomains(final String allowedDomains) {
    this.allowedDomains = allowedDomains;
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

  @java.lang.SuppressWarnings("all")
  public void setLastTeamIconUpdate(final long lastTeamIconUpdate) {
    this.lastTeamIconUpdate = lastTeamIconUpdate;
  }

  @java.lang.SuppressWarnings("all")
  public void setSchemeId(final String schemeId) {
    this.schemeId = schemeId;
  }

  @java.lang.SuppressWarnings("all")
  public void setGroupConstrained(final boolean groupConstrained) {
    this.groupConstrained = groupConstrained;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public boolean equals(final java.lang.Object o) {
    if (o == this) return true;
    if (!(o instanceof Team)) return false;
    final Team other = (Team) o;
    if (!other.canEqual((java.lang.Object) this)) return false;
    if (this.getCreateAt() != other.getCreateAt()) return false;
    if (this.getUpdateAt() != other.getUpdateAt()) return false;
    if (this.getDeleteAt() != other.getDeleteAt()) return false;
    if (this.isAllowOpenInvite() != other.isAllowOpenInvite()) return false;
    if (this.getLastTeamIconUpdate() != other.getLastTeamIconUpdate()) return false;
    if (this.isGroupConstrained() != other.isGroupConstrained()) return false;
    final java.lang.Object this$id = this.getId();
    final java.lang.Object other$id = other.getId();
    if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
    final java.lang.Object this$displayName = this.getDisplayName();
    final java.lang.Object other$displayName = other.getDisplayName();
    if (this$displayName == null ? other$displayName != null : !this$displayName.equals(other$displayName)) return false;
    final java.lang.Object this$name = this.getName();
    final java.lang.Object other$name = other.getName();
    if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
    final java.lang.Object this$description = this.getDescription();
    final java.lang.Object other$description = other.getDescription();
    if (this$description == null ? other$description != null : !this$description.equals(other$description)) return false;
    final java.lang.Object this$email = this.getEmail();
    final java.lang.Object other$email = other.getEmail();
    if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
    final java.lang.Object this$type = this.getType();
    final java.lang.Object other$type = other.getType();
    if (this$type == null ? other$type != null : !this$type.equals(other$type)) return false;
    final java.lang.Object this$companyName = this.getCompanyName();
    final java.lang.Object other$companyName = other.getCompanyName();
    if (this$companyName == null ? other$companyName != null : !this$companyName.equals(other$companyName)) return false;
    final java.lang.Object this$allowedDomains = this.getAllowedDomains();
    final java.lang.Object other$allowedDomains = other.getAllowedDomains();
    if (this$allowedDomains == null ? other$allowedDomains != null : !this$allowedDomains.equals(other$allowedDomains)) return false;
    final java.lang.Object this$inviteId = this.getInviteId();
    final java.lang.Object other$inviteId = other.getInviteId();
    if (this$inviteId == null ? other$inviteId != null : !this$inviteId.equals(other$inviteId)) return false;
    final java.lang.Object this$schemeId = this.getSchemeId();
    final java.lang.Object other$schemeId = other.getSchemeId();
    if (this$schemeId == null ? other$schemeId != null : !this$schemeId.equals(other$schemeId)) return false;
    return true;
  }

  @java.lang.SuppressWarnings("all")
  protected boolean canEqual(final java.lang.Object other) {
    return other instanceof Team;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final long $createAt = this.getCreateAt();
    result = result * PRIME + (int) ($createAt >>> 32 ^ $createAt);
    final long $updateAt = this.getUpdateAt();
    result = result * PRIME + (int) ($updateAt >>> 32 ^ $updateAt);
    final long $deleteAt = this.getDeleteAt();
    result = result * PRIME + (int) ($deleteAt >>> 32 ^ $deleteAt);
    result = result * PRIME + (this.isAllowOpenInvite() ? 79 : 97);
    final long $lastTeamIconUpdate = this.getLastTeamIconUpdate();
    result = result * PRIME + (int) ($lastTeamIconUpdate >>> 32 ^ $lastTeamIconUpdate);
    result = result * PRIME + (this.isGroupConstrained() ? 79 : 97);
    final java.lang.Object $id = this.getId();
    result = result * PRIME + ($id == null ? 43 : $id.hashCode());
    final java.lang.Object $displayName = this.getDisplayName();
    result = result * PRIME + ($displayName == null ? 43 : $displayName.hashCode());
    final java.lang.Object $name = this.getName();
    result = result * PRIME + ($name == null ? 43 : $name.hashCode());
    final java.lang.Object $description = this.getDescription();
    result = result * PRIME + ($description == null ? 43 : $description.hashCode());
    final java.lang.Object $email = this.getEmail();
    result = result * PRIME + ($email == null ? 43 : $email.hashCode());
    final java.lang.Object $type = this.getType();
    result = result * PRIME + ($type == null ? 43 : $type.hashCode());
    final java.lang.Object $companyName = this.getCompanyName();
    result = result * PRIME + ($companyName == null ? 43 : $companyName.hashCode());
    final java.lang.Object $allowedDomains = this.getAllowedDomains();
    result = result * PRIME + ($allowedDomains == null ? 43 : $allowedDomains.hashCode());
    final java.lang.Object $inviteId = this.getInviteId();
    result = result * PRIME + ($inviteId == null ? 43 : $inviteId.hashCode());
    final java.lang.Object $schemeId = this.getSchemeId();
    result = result * PRIME + ($schemeId == null ? 43 : $schemeId.hashCode());
    return result;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public java.lang.String toString() {
    return "Team(id=" + this.getId() + ", createAt=" + this.getCreateAt() + ", updateAt=" + this.getUpdateAt() + ", deleteAt=" + this.getDeleteAt() + ", displayName=" + this.getDisplayName() + ", name=" + this.getName() + ", description=" + this.getDescription() + ", email=" + this.getEmail() + ", type=" + this.getType() + ", companyName=" + this.getCompanyName() + ", allowedDomains=" + this.getAllowedDomains() + ", inviteId=" + this.getInviteId() + ", allowOpenInvite=" + this.isAllowOpenInvite() + ", lastTeamIconUpdate=" + this.getLastTeamIconUpdate() + ", schemeId=" + this.getSchemeId() + ", groupConstrained=" + this.isGroupConstrained() + ")";
  }
}
