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
package org.ar4k.agent.mattermost.client4.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Switch account type request payload.
 * 
 * @author Takayuki Maruyama
 */
public class SwitchAccountTypeResult {
  @JsonProperty("follow_link")
  private String followLink;

  @java.lang.SuppressWarnings("all")
  public SwitchAccountTypeResult() {
  }

  @java.lang.SuppressWarnings("all")
  public String getFollowLink() {
    return this.followLink;
  }

  @JsonProperty("follow_link")
  @java.lang.SuppressWarnings("all")
  public void setFollowLink(final String followLink) {
    this.followLink = followLink;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public boolean equals(final java.lang.Object o) {
    if (o == this) return true;
    if (!(o instanceof SwitchAccountTypeResult)) return false;
    final SwitchAccountTypeResult other = (SwitchAccountTypeResult) o;
    if (!other.canEqual((java.lang.Object) this)) return false;
    final java.lang.Object this$followLink = this.getFollowLink();
    final java.lang.Object other$followLink = other.getFollowLink();
    if (this$followLink == null ? other$followLink != null : !this$followLink.equals(other$followLink)) return false;
    return true;
  }

  @java.lang.SuppressWarnings("all")
  protected boolean canEqual(final java.lang.Object other) {
    return other instanceof SwitchAccountTypeResult;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final java.lang.Object $followLink = this.getFollowLink();
    result = result * PRIME + ($followLink == null ? 43 : $followLink.hashCode());
    return result;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public java.lang.String toString() {
    return "SwitchAccountTypeResult(followLink=" + this.getFollowLink() + ")";
  }
}
