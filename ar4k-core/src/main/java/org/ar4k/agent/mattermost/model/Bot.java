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
package org.ar4k.agent.mattermost.model;

/**
 * The bot account.
 * 
 * @author Takayuki Maruyama
 * @since Mattermost Server 5.12
 */
public class Bot {
  private String userId;
  private long createAt;
  private long updateAt;
  private long deleteAt;
  private String username;
  private String displayName;
  private String description;
  private String ownerId;

  @java.lang.SuppressWarnings("all")
  public Bot() {
  }

  @java.lang.SuppressWarnings("all")
  public String getUserId() {
    return this.userId;
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
  public String getUsername() {
    return this.username;
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
  public String getOwnerId() {
    return this.ownerId;
  }

  @java.lang.SuppressWarnings("all")
  public void setUserId(final String userId) {
    this.userId = userId;
  }

  @java.lang.SuppressWarnings("all")
  public void setCreateAt(final long createAt) {
    this.createAt = createAt;
  }

  @java.lang.SuppressWarnings("all")
  public void setUpdateAt(final long updateAt) {
    this.updateAt = updateAt;
  }

  @java.lang.SuppressWarnings("all")
  public void setDeleteAt(final long deleteAt) {
    this.deleteAt = deleteAt;
  }

  @java.lang.SuppressWarnings("all")
  public void setUsername(final String username) {
    this.username = username;
  }

  @java.lang.SuppressWarnings("all")
  public void setDisplayName(final String displayName) {
    this.displayName = displayName;
  }

  @java.lang.SuppressWarnings("all")
  public void setDescription(final String description) {
    this.description = description;
  }

  @java.lang.SuppressWarnings("all")
  public void setOwnerId(final String ownerId) {
    this.ownerId = ownerId;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public boolean equals(final java.lang.Object o) {
    if (o == this) return true;
    if (!(o instanceof Bot)) return false;
    final Bot other = (Bot) o;
    if (!other.canEqual((java.lang.Object) this)) return false;
    if (this.getCreateAt() != other.getCreateAt()) return false;
    if (this.getUpdateAt() != other.getUpdateAt()) return false;
    if (this.getDeleteAt() != other.getDeleteAt()) return false;
    final java.lang.Object this$userId = this.getUserId();
    final java.lang.Object other$userId = other.getUserId();
    if (this$userId == null ? other$userId != null : !this$userId.equals(other$userId)) return false;
    final java.lang.Object this$username = this.getUsername();
    final java.lang.Object other$username = other.getUsername();
    if (this$username == null ? other$username != null : !this$username.equals(other$username)) return false;
    final java.lang.Object this$displayName = this.getDisplayName();
    final java.lang.Object other$displayName = other.getDisplayName();
    if (this$displayName == null ? other$displayName != null : !this$displayName.equals(other$displayName)) return false;
    final java.lang.Object this$description = this.getDescription();
    final java.lang.Object other$description = other.getDescription();
    if (this$description == null ? other$description != null : !this$description.equals(other$description)) return false;
    final java.lang.Object this$ownerId = this.getOwnerId();
    final java.lang.Object other$ownerId = other.getOwnerId();
    if (this$ownerId == null ? other$ownerId != null : !this$ownerId.equals(other$ownerId)) return false;
    return true;
  }

  @java.lang.SuppressWarnings("all")
  protected boolean canEqual(final java.lang.Object other) {
    return other instanceof Bot;
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
    final java.lang.Object $userId = this.getUserId();
    result = result * PRIME + ($userId == null ? 43 : $userId.hashCode());
    final java.lang.Object $username = this.getUsername();
    result = result * PRIME + ($username == null ? 43 : $username.hashCode());
    final java.lang.Object $displayName = this.getDisplayName();
    result = result * PRIME + ($displayName == null ? 43 : $displayName.hashCode());
    final java.lang.Object $description = this.getDescription();
    result = result * PRIME + ($description == null ? 43 : $description.hashCode());
    final java.lang.Object $ownerId = this.getOwnerId();
    result = result * PRIME + ($ownerId == null ? 43 : $ownerId.hashCode());
    return result;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public java.lang.String toString() {
    return "Bot(userId=" + this.getUserId() + ", createAt=" + this.getCreateAt() + ", updateAt=" + this.getUpdateAt() + ", deleteAt=" + this.getDeleteAt() + ", username=" + this.getUsername() + ", displayName=" + this.getDisplayName() + ", description=" + this.getDescription() + ", ownerId=" + this.getOwnerId() + ")";
  }
}
