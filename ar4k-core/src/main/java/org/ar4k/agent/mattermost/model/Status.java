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
 * User status.
 * 
 * @author Takayuki Maruyama
 */
public class Status {
  @JsonProperty("user_id")
  private String userId;
  @JsonProperty("status")
  private String status;
  @JsonProperty("manual")
  private boolean manual;
  @JsonProperty("last_activity_at")
  private long lastActivityAt;
  @JsonProperty("active_channel")
  private String activeChannel;

  @java.lang.SuppressWarnings("all")
  public Status() {
  }

  @java.lang.SuppressWarnings("all")
  public String getUserId() {
    return this.userId;
  }

  @java.lang.SuppressWarnings("all")
  public String getStatus() {
    return this.status;
  }

  @java.lang.SuppressWarnings("all")
  public boolean isManual() {
    return this.manual;
  }

  @java.lang.SuppressWarnings("all")
  public long getLastActivityAt() {
    return this.lastActivityAt;
  }

  @java.lang.SuppressWarnings("all")
  public String getActiveChannel() {
    return this.activeChannel;
  }

  @JsonProperty("user_id")
  @java.lang.SuppressWarnings("all")
  public void setUserId(final String userId) {
    this.userId = userId;
  }

  @JsonProperty("status")
  @java.lang.SuppressWarnings("all")
  public void setStatus(final String status) {
    this.status = status;
  }

  @JsonProperty("manual")
  @java.lang.SuppressWarnings("all")
  public void setManual(final boolean manual) {
    this.manual = manual;
  }

  @JsonProperty("last_activity_at")
  @java.lang.SuppressWarnings("all")
  public void setLastActivityAt(final long lastActivityAt) {
    this.lastActivityAt = lastActivityAt;
  }

  @JsonProperty("active_channel")
  @java.lang.SuppressWarnings("all")
  public void setActiveChannel(final String activeChannel) {
    this.activeChannel = activeChannel;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public boolean equals(final java.lang.Object o) {
    if (o == this) return true;
    if (!(o instanceof Status)) return false;
    final Status other = (Status) o;
    if (!other.canEqual((java.lang.Object) this)) return false;
    if (this.isManual() != other.isManual()) return false;
    if (this.getLastActivityAt() != other.getLastActivityAt()) return false;
    final java.lang.Object this$userId = this.getUserId();
    final java.lang.Object other$userId = other.getUserId();
    if (this$userId == null ? other$userId != null : !this$userId.equals(other$userId)) return false;
    final java.lang.Object this$status = this.getStatus();
    final java.lang.Object other$status = other.getStatus();
    if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
    final java.lang.Object this$activeChannel = this.getActiveChannel();
    final java.lang.Object other$activeChannel = other.getActiveChannel();
    if (this$activeChannel == null ? other$activeChannel != null : !this$activeChannel.equals(other$activeChannel)) return false;
    return true;
  }

  @java.lang.SuppressWarnings("all")
  protected boolean canEqual(final java.lang.Object other) {
    return other instanceof Status;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + (this.isManual() ? 79 : 97);
    final long $lastActivityAt = this.getLastActivityAt();
    result = result * PRIME + (int) ($lastActivityAt >>> 32 ^ $lastActivityAt);
    final java.lang.Object $userId = this.getUserId();
    result = result * PRIME + ($userId == null ? 43 : $userId.hashCode());
    final java.lang.Object $status = this.getStatus();
    result = result * PRIME + ($status == null ? 43 : $status.hashCode());
    final java.lang.Object $activeChannel = this.getActiveChannel();
    result = result * PRIME + ($activeChannel == null ? 43 : $activeChannel.hashCode());
    return result;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public java.lang.String toString() {
    return "Status(userId=" + this.getUserId() + ", status=" + this.getStatus() + ", manual=" + this.isManual() + ", lastActivityAt=" + this.getLastActivityAt() + ", activeChannel=" + this.getActiveChannel() + ")";
  }
}
