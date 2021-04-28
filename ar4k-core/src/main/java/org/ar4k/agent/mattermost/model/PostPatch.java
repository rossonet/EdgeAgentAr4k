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
import java.util.List;
import java.util.Map;

/**
 * Post patch.
 * 
 * @author Takayuki Maruyama
 */
public class PostPatch {
  @JsonProperty("is_pinned")
  private boolean isPinned;
  @JsonProperty("message")
  private String message;
  @JsonProperty("proprs")
  private Map<String, String> props;
  @JsonProperty("file_ids")
  private List<String> fileIds;
  @JsonProperty("has_reactions")
  private boolean hasReactions;

  @java.lang.SuppressWarnings("all")
  public PostPatch() {
  }

  @java.lang.SuppressWarnings("all")
  public boolean isPinned() {
    return this.isPinned;
  }

  @java.lang.SuppressWarnings("all")
  public String getMessage() {
    return this.message;
  }

  @java.lang.SuppressWarnings("all")
  public Map<String, String> getProps() {
    return this.props;
  }

  @java.lang.SuppressWarnings("all")
  public List<String> getFileIds() {
    return this.fileIds;
  }

  @java.lang.SuppressWarnings("all")
  public boolean isHasReactions() {
    return this.hasReactions;
  }

  @JsonProperty("is_pinned")
  @java.lang.SuppressWarnings("all")
  public void setPinned(final boolean isPinned) {
    this.isPinned = isPinned;
  }

  @JsonProperty("message")
  @java.lang.SuppressWarnings("all")
  public void setMessage(final String message) {
    this.message = message;
  }

  @JsonProperty("proprs")
  @java.lang.SuppressWarnings("all")
  public void setProps(final Map<String, String> props) {
    this.props = props;
  }

  @JsonProperty("file_ids")
  @java.lang.SuppressWarnings("all")
  public void setFileIds(final List<String> fileIds) {
    this.fileIds = fileIds;
  }

  @JsonProperty("has_reactions")
  @java.lang.SuppressWarnings("all")
  public void setHasReactions(final boolean hasReactions) {
    this.hasReactions = hasReactions;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public boolean equals(final java.lang.Object o) {
    if (o == this) return true;
    if (!(o instanceof PostPatch)) return false;
    final PostPatch other = (PostPatch) o;
    if (!other.canEqual((java.lang.Object) this)) return false;
    if (this.isPinned() != other.isPinned()) return false;
    if (this.isHasReactions() != other.isHasReactions()) return false;
    final java.lang.Object this$message = this.getMessage();
    final java.lang.Object other$message = other.getMessage();
    if (this$message == null ? other$message != null : !this$message.equals(other$message)) return false;
    final java.lang.Object this$props = this.getProps();
    final java.lang.Object other$props = other.getProps();
    if (this$props == null ? other$props != null : !this$props.equals(other$props)) return false;
    final java.lang.Object this$fileIds = this.getFileIds();
    final java.lang.Object other$fileIds = other.getFileIds();
    if (this$fileIds == null ? other$fileIds != null : !this$fileIds.equals(other$fileIds)) return false;
    return true;
  }

  @java.lang.SuppressWarnings("all")
  protected boolean canEqual(final java.lang.Object other) {
    return other instanceof PostPatch;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + (this.isPinned() ? 79 : 97);
    result = result * PRIME + (this.isHasReactions() ? 79 : 97);
    final java.lang.Object $message = this.getMessage();
    result = result * PRIME + ($message == null ? 43 : $message.hashCode());
    final java.lang.Object $props = this.getProps();
    result = result * PRIME + ($props == null ? 43 : $props.hashCode());
    final java.lang.Object $fileIds = this.getFileIds();
    result = result * PRIME + ($fileIds == null ? 43 : $fileIds.hashCode());
    return result;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public java.lang.String toString() {
    return "PostPatch(isPinned=" + this.isPinned() + ", message=" + this.getMessage() + ", props=" + this.getProps() + ", fileIds=" + this.getFileIds() + ", hasReactions=" + this.isHasReactions() + ")";
  }
}