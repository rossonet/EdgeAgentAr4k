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
import java.util.Map;

/**
 * Channel member.
 * 
 * @author Takayuki Maruyama
 */
public class ChannelMember {
  @JsonProperty("channel_id")
  private String channelId;
  @JsonProperty("user_id")
  private String userId;
  @JsonProperty("roles")
  private String roles;
  @JsonProperty("last_viewed_at")
  private long lastViewedAt;
  @JsonProperty("msg_count")
  private long msgCount;
  @JsonProperty("mention_count")
  private long mentionCount;
  @JsonProperty("notify_props")
  private Map<String, String> notifyProps;
  @JsonProperty("last_update_at")
  private long lastUpdateAt;
  /* @since Mattermost Server  what ver? */
  private boolean schemeUser;
  /* @since Mattermost Server  what ver? */
  private boolean schemeAdmin;
  /* @since Mattermost Server  what ver? */
  private String explicitRoles;
  /* @since Mattermost Server 5.12 */
  private boolean schemeGuest;

  @java.lang.SuppressWarnings("all")
  public ChannelMember() {
  }

  @java.lang.SuppressWarnings("all")
  public String getChannelId() {
    return this.channelId;
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
  public long getLastViewedAt() {
    return this.lastViewedAt;
  }

  @java.lang.SuppressWarnings("all")
  public long getMsgCount() {
    return this.msgCount;
  }

  @java.lang.SuppressWarnings("all")
  public long getMentionCount() {
    return this.mentionCount;
  }

  @java.lang.SuppressWarnings("all")
  public Map<String, String> getNotifyProps() {
    return this.notifyProps;
  }

  @java.lang.SuppressWarnings("all")
  public long getLastUpdateAt() {
    return this.lastUpdateAt;
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

  @JsonProperty("channel_id")
  @java.lang.SuppressWarnings("all")
  public void setChannelId(final String channelId) {
    this.channelId = channelId;
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

  @JsonProperty("last_viewed_at")
  @java.lang.SuppressWarnings("all")
  public void setLastViewedAt(final long lastViewedAt) {
    this.lastViewedAt = lastViewedAt;
  }

  @JsonProperty("msg_count")
  @java.lang.SuppressWarnings("all")
  public void setMsgCount(final long msgCount) {
    this.msgCount = msgCount;
  }

  @JsonProperty("mention_count")
  @java.lang.SuppressWarnings("all")
  public void setMentionCount(final long mentionCount) {
    this.mentionCount = mentionCount;
  }

  @JsonProperty("notify_props")
  @java.lang.SuppressWarnings("all")
  public void setNotifyProps(final Map<String, String> notifyProps) {
    this.notifyProps = notifyProps;
  }

  @JsonProperty("last_update_at")
  @java.lang.SuppressWarnings("all")
  public void setLastUpdateAt(final long lastUpdateAt) {
    this.lastUpdateAt = lastUpdateAt;
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
    if (!(o instanceof ChannelMember)) return false;
    final ChannelMember other = (ChannelMember) o;
    if (!other.canEqual((java.lang.Object) this)) return false;
    if (this.getLastViewedAt() != other.getLastViewedAt()) return false;
    if (this.getMsgCount() != other.getMsgCount()) return false;
    if (this.getMentionCount() != other.getMentionCount()) return false;
    if (this.getLastUpdateAt() != other.getLastUpdateAt()) return false;
    if (this.isSchemeUser() != other.isSchemeUser()) return false;
    if (this.isSchemeAdmin() != other.isSchemeAdmin()) return false;
    if (this.isSchemeGuest() != other.isSchemeGuest()) return false;
    final java.lang.Object this$channelId = this.getChannelId();
    final java.lang.Object other$channelId = other.getChannelId();
    if (this$channelId == null ? other$channelId != null : !this$channelId.equals(other$channelId)) return false;
    final java.lang.Object this$userId = this.getUserId();
    final java.lang.Object other$userId = other.getUserId();
    if (this$userId == null ? other$userId != null : !this$userId.equals(other$userId)) return false;
    final java.lang.Object this$roles = this.getRoles();
    final java.lang.Object other$roles = other.getRoles();
    if (this$roles == null ? other$roles != null : !this$roles.equals(other$roles)) return false;
    final java.lang.Object this$notifyProps = this.getNotifyProps();
    final java.lang.Object other$notifyProps = other.getNotifyProps();
    if (this$notifyProps == null ? other$notifyProps != null : !this$notifyProps.equals(other$notifyProps)) return false;
    final java.lang.Object this$explicitRoles = this.getExplicitRoles();
    final java.lang.Object other$explicitRoles = other.getExplicitRoles();
    if (this$explicitRoles == null ? other$explicitRoles != null : !this$explicitRoles.equals(other$explicitRoles)) return false;
    return true;
  }

  @java.lang.SuppressWarnings("all")
  protected boolean canEqual(final java.lang.Object other) {
    return other instanceof ChannelMember;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final long $lastViewedAt = this.getLastViewedAt();
    result = result * PRIME + (int) ($lastViewedAt >>> 32 ^ $lastViewedAt);
    final long $msgCount = this.getMsgCount();
    result = result * PRIME + (int) ($msgCount >>> 32 ^ $msgCount);
    final long $mentionCount = this.getMentionCount();
    result = result * PRIME + (int) ($mentionCount >>> 32 ^ $mentionCount);
    final long $lastUpdateAt = this.getLastUpdateAt();
    result = result * PRIME + (int) ($lastUpdateAt >>> 32 ^ $lastUpdateAt);
    result = result * PRIME + (this.isSchemeUser() ? 79 : 97);
    result = result * PRIME + (this.isSchemeAdmin() ? 79 : 97);
    result = result * PRIME + (this.isSchemeGuest() ? 79 : 97);
    final java.lang.Object $channelId = this.getChannelId();
    result = result * PRIME + ($channelId == null ? 43 : $channelId.hashCode());
    final java.lang.Object $userId = this.getUserId();
    result = result * PRIME + ($userId == null ? 43 : $userId.hashCode());
    final java.lang.Object $roles = this.getRoles();
    result = result * PRIME + ($roles == null ? 43 : $roles.hashCode());
    final java.lang.Object $notifyProps = this.getNotifyProps();
    result = result * PRIME + ($notifyProps == null ? 43 : $notifyProps.hashCode());
    final java.lang.Object $explicitRoles = this.getExplicitRoles();
    result = result * PRIME + ($explicitRoles == null ? 43 : $explicitRoles.hashCode());
    return result;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public java.lang.String toString() {
    return "ChannelMember(channelId=" + this.getChannelId() + ", userId=" + this.getUserId() + ", roles=" + this.getRoles() + ", lastViewedAt=" + this.getLastViewedAt() + ", msgCount=" + this.getMsgCount() + ", mentionCount=" + this.getMentionCount() + ", notifyProps=" + this.getNotifyProps() + ", lastUpdateAt=" + this.getLastUpdateAt() + ", schemeUser=" + this.isSchemeUser() + ", schemeAdmin=" + this.isSchemeAdmin() + ", explicitRoles=" + this.getExplicitRoles() + ", schemeGuest=" + this.isSchemeGuest() + ")";
  }
}
