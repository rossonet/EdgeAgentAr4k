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
 * Websocket request payload.
 * 
 * @author Takayuki Maruyama
 */
public class WebSocketRequest {
  @JsonProperty("seq")
  private long seq;
  @JsonProperty("action")
  private String action;
  @JsonProperty("data")
  private Map<String, String> data;

  @java.lang.SuppressWarnings("all")
  public WebSocketRequest() {
  }

  @java.lang.SuppressWarnings("all")
  public long getSeq() {
    return this.seq;
  }

  @java.lang.SuppressWarnings("all")
  public String getAction() {
    return this.action;
  }

  @java.lang.SuppressWarnings("all")
  public Map<String, String> getData() {
    return this.data;
  }

  @JsonProperty("seq")
  @java.lang.SuppressWarnings("all")
  public void setSeq(final long seq) {
    this.seq = seq;
  }

  @JsonProperty("action")
  @java.lang.SuppressWarnings("all")
  public void setAction(final String action) {
    this.action = action;
  }

  @JsonProperty("data")
  @java.lang.SuppressWarnings("all")
  public void setData(final Map<String, String> data) {
    this.data = data;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public boolean equals(final java.lang.Object o) {
    if (o == this) return true;
    if (!(o instanceof WebSocketRequest)) return false;
    final WebSocketRequest other = (WebSocketRequest) o;
    if (!other.canEqual((java.lang.Object) this)) return false;
    if (this.getSeq() != other.getSeq()) return false;
    final java.lang.Object this$action = this.getAction();
    final java.lang.Object other$action = other.getAction();
    if (this$action == null ? other$action != null : !this$action.equals(other$action)) return false;
    final java.lang.Object this$data = this.getData();
    final java.lang.Object other$data = other.getData();
    if (this$data == null ? other$data != null : !this$data.equals(other$data)) return false;
    return true;
  }

  @java.lang.SuppressWarnings("all")
  protected boolean canEqual(final java.lang.Object other) {
    return other instanceof WebSocketRequest;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final long $seq = this.getSeq();
    result = result * PRIME + (int) ($seq >>> 32 ^ $seq);
    final java.lang.Object $action = this.getAction();
    result = result * PRIME + ($action == null ? 43 : $action.hashCode());
    final java.lang.Object $data = this.getData();
    result = result * PRIME + ($data == null ? 43 : $data.hashCode());
    return result;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public java.lang.String toString() {
    return "WebSocketRequest(seq=" + this.getSeq() + ", action=" + this.getAction() + ", data=" + this.getData() + ")";
  }
}
