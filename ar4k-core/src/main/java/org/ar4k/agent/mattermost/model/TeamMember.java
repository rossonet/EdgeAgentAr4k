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
 * Team member.
 * 
 * @author Takayuki Maruyama
 */
public class TeamMember {
  public TeamMember() {
    // default constructor
  }

  public TeamMember(String teamId, String userId) {
    setTeamId(teamId);
    setUserId(userId);
  }

  @JsonProperty("team_id")
  private String teamId;
  @JsonProperty("user_id")
  private String userId;
  @JsonProperty("roles")
  private String roles;
  @JsonProperty("delete_at")
  private long deleteAt;
  /* @since Mattermost Server  what ver? */
  private boolean schemeUser;
  /* @since Mattermost Server  what ver? */
  private boolean schemeAdmin;
  /* @since Mattermost Server  what ver? */
  private String explicitRoles;
  /* @since Mattermost Server 5.12 */
  private boolean schemeGuest;

  @java.lang.SuppressWarnings("all")
  public String getTeamId() {
    return this.teamId;
  }

  @java.lang.SuppressWarnings("all")
  public String getUserId() {
    return this.userId;
  }

  @java.lang.SuppressWarnings("all")
  public String getRoles() {
    return this.roles;
  }

  @java.lang.SuppressWarnings("all")
  public long getDeleteAt() {
    return this.deleteAt;
  }

  @java.lang.SuppressWarnings("all")
  public boolean isSchemeUser() {
    return this.schemeUser;
  }

  @java.lang.SuppressWarnings("all")
  public boolean isSchemeAdmin() {
    return this.schemeAdmin;
  }

  @java.lang.SuppressWarnings("all")
  public String getExplicitRoles() {
    return this.explicitRoles;
  }

  @java.lang.SuppressWarnings("all")
  public boolean isSchemeGuest() {
    return this.schemeGuest;
  }

  @JsonProperty("team_id")
  @java.lang.SuppressWarnings("all")
  public void setTeamId(final String teamId) {
    this.teamId = teamId;
  }

  @JsonProperty("user_id")
  @java.lang.SuppressWarnings("all")
  public void setUserId(final String userId) {
    this.userId = userId;
  }

  @JsonProperty("roles")
  @java.lang.SuppressWarnings("all")
  public void setRoles(final String roles) {
    this.roles = roles;
  }

  @JsonProperty("delete_at")
  @java.lang.SuppressWarnings("all")
  public void setDeleteAt(final long deleteAt) {
    this.deleteAt = deleteAt;
  }

  @java.lang.SuppressWarnings("all")
  public void setSchemeUser(final boolean schemeUser) {
    this.schemeUser = schemeUser;
  }

  @java.lang.SuppressWarnings("all")
  public void setSchemeAdmin(final boolean schemeAdmin) {
    this.schemeAdmin = schemeAdmin;
  }

  @java.lang.SuppressWarnings("all")
  public void setExplicitRoles(final String explicitRoles) {
    this.explicitRoles = explicitRoles;
  }

  @java.lang.SuppressWarnings("all")
  public void setSchemeGuest(final boolean schemeGuest) {
    this.schemeGuest = schemeGuest;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public boolean equals(final java.lang.Object o) {
    if (o == this) return true;
    if (!(o instanceof TeamMember)) return false;
    final TeamMember other = (TeamMember) o;
    if (!other.canEqual((java.lang.Object) this)) return false;
    if (this.getDeleteAt() != other.getDeleteAt()) return false;
    if (this.isSchemeUser() != other.isSchemeUser()) return false;
    if (this.isSchemeAdmin() != other.isSchemeAdmin()) return false;
    if (this.isSchemeGuest() != other.isSchemeGuest()) return false;
    final java.lang.Object this$teamId = this.getTeamId();
    final java.lang.Object other$teamId = other.getTeamId();
    if (this$teamId == null ? other$teamId != null : !this$teamId.equals(other$teamId)) return false;
    final java.lang.Object this$userId = this.getUserId();
    final java.lang.Object other$userId = other.getUserId();
    if (this$userId == null ? other$userId != null : !this$userId.equals(other$userId)) return false;
    final java.lang.Object this$roles = this.getRoles();
    final java.lang.Object other$roles = other.getRoles();
    if (this$roles == null ? other$roles != null : !this$roles.equals(other$roles)) return false;
    final java.lang.Object this$explicitRoles = this.getExplicitRoles();
    final java.lang.Object other$explicitRoles = other.getExplicitRoles();
    if (this$explicitRoles == null ? other$explicitRoles != null : !this$explicitRoles.equals(other$explicitRoles)) return false;
    return true;
  }

  @java.lang.SuppressWarnings("all")
  protected boolean canEqual(final java.lang.Object other) {
    return other instanceof TeamMember;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final long $deleteAt = this.getDeleteAt();
    result = result * PRIME + (int) ($deleteAt >>> 32 ^ $deleteAt);
    result = result * PRIME + (this.isSchemeUser() ? 79 : 97);
    result = result * PRIME + (this.isSchemeAdmin() ? 79 : 97);
    result = result * PRIME + (this.isSchemeGuest() ? 79 : 97);
    final java.lang.Object $teamId = this.getTeamId();
    result = result * PRIME + ($teamId == null ? 43 : $teamId.hashCode());
    final java.lang.Object $userId = this.getUserId();
    result = result * PRIME + ($userId == null ? 43 : $userId.hashCode());
    final java.lang.Object $roles = this.getRoles();
    result = result * PRIME + ($roles == null ? 43 : $roles.hashCode());
    final java.lang.Object $explicitRoles = this.getExplicitRoles();
    result = result * PRIME + ($explicitRoles == null ? 43 : $explicitRoles.hashCode());
    return result;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public java.lang.String toString() {
    return "TeamMember(teamId=" + this.getTeamId() + ", userId=" + this.getUserId() + ", roles=" + this.getRoles() + ", deleteAt=" + this.getDeleteAt() + ", schemeUser=" + this.isSchemeUser() + ", schemeAdmin=" + this.isSchemeAdmin() + ", explicitRoles=" + this.getExplicitRoles() + ", schemeGuest=" + this.isSchemeGuest() + ")";
  }
}
