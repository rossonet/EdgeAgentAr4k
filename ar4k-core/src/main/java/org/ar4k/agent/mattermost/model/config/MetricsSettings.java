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
package org.ar4k.agent.mattermost.model.config;

/**
 * Metrics settings.
 * 
 * @author Takayuki Maruyama
 */
public class MetricsSettings {
  private boolean enable;
  private int blockProfileRate;
  private String listenAddress;

  @java.lang.SuppressWarnings("all")
  public MetricsSettings() {
  }

  @java.lang.SuppressWarnings("all")
  public boolean isEnable() {
    return this.enable;
  }

  @java.lang.SuppressWarnings("all")
  public int getBlockProfileRate() {
    return this.blockProfileRate;
  }

  @java.lang.SuppressWarnings("all")
  public String getListenAddress() {
    return this.listenAddress;
  }

  @java.lang.SuppressWarnings("all")
  public void setEnable(final boolean enable) {
    this.enable = enable;
  }

  @java.lang.SuppressWarnings("all")
  public void setBlockProfileRate(final int blockProfileRate) {
    this.blockProfileRate = blockProfileRate;
  }

  @java.lang.SuppressWarnings("all")
  public void setListenAddress(final String listenAddress) {
    this.listenAddress = listenAddress;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public boolean equals(final java.lang.Object o) {
    if (o == this) return true;
    if (!(o instanceof MetricsSettings)) return false;
    final MetricsSettings other = (MetricsSettings) o;
    if (!other.canEqual((java.lang.Object) this)) return false;
    if (this.isEnable() != other.isEnable()) return false;
    if (this.getBlockProfileRate() != other.getBlockProfileRate()) return false;
    final java.lang.Object this$listenAddress = this.getListenAddress();
    final java.lang.Object other$listenAddress = other.getListenAddress();
    if (this$listenAddress == null ? other$listenAddress != null : !this$listenAddress.equals(other$listenAddress)) return false;
    return true;
  }

  @java.lang.SuppressWarnings("all")
  protected boolean canEqual(final java.lang.Object other) {
    return other instanceof MetricsSettings;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + (this.isEnable() ? 79 : 97);
    result = result * PRIME + this.getBlockProfileRate();
    final java.lang.Object $listenAddress = this.getListenAddress();
    result = result * PRIME + ($listenAddress == null ? 43 : $listenAddress.hashCode());
    return result;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public java.lang.String toString() {
    return "MetricsSettings(enable=" + this.isEnable() + ", blockProfileRate=" + this.getBlockProfileRate() + ", listenAddress=" + this.getListenAddress() + ")";
  }
}
