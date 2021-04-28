// Generated by delombok at Sun Apr 18 22:20:18 CEST 2021
/*
 * Copyright (c) 2018 Takayuki Maruyama
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

import java.util.List;

/**
 * Display settings.
 * 
 * @author Takayuki Maruyama
 * @since Mattermost Server 4.9
 */
public class DisplaySettings {
  private boolean experimentalTimezone;
  /* @since Mattermost Server 5.0.0 */
  private List<String> customUrlSchemes;

  @java.lang.SuppressWarnings("all")
  public DisplaySettings() {
  }

  @java.lang.SuppressWarnings("all")
  public boolean isExperimentalTimezone() {
    return this.experimentalTimezone;
  }

  @java.lang.SuppressWarnings("all")
  public List<String> getCustomUrlSchemes() {
    return this.customUrlSchemes;
  }

  @java.lang.SuppressWarnings("all")
  public void setExperimentalTimezone(final boolean experimentalTimezone) {
    this.experimentalTimezone = experimentalTimezone;
  }

  @java.lang.SuppressWarnings("all")
  public void setCustomUrlSchemes(final List<String> customUrlSchemes) {
    this.customUrlSchemes = customUrlSchemes;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public boolean equals(final java.lang.Object o) {
    if (o == this) return true;
    if (!(o instanceof DisplaySettings)) return false;
    final DisplaySettings other = (DisplaySettings) o;
    if (!other.canEqual((java.lang.Object) this)) return false;
    if (this.isExperimentalTimezone() != other.isExperimentalTimezone()) return false;
    final java.lang.Object this$customUrlSchemes = this.getCustomUrlSchemes();
    final java.lang.Object other$customUrlSchemes = other.getCustomUrlSchemes();
    if (this$customUrlSchemes == null ? other$customUrlSchemes != null : !this$customUrlSchemes.equals(other$customUrlSchemes)) return false;
    return true;
  }

  @java.lang.SuppressWarnings("all")
  protected boolean canEqual(final java.lang.Object other) {
    return other instanceof DisplaySettings;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + (this.isExperimentalTimezone() ? 79 : 97);
    final java.lang.Object $customUrlSchemes = this.getCustomUrlSchemes();
    result = result * PRIME + ($customUrlSchemes == null ? 43 : $customUrlSchemes.hashCode());
    return result;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public java.lang.String toString() {
    return "DisplaySettings(experimentalTimezone=" + this.isExperimentalTimezone() + ", customUrlSchemes=" + this.getCustomUrlSchemes() + ")";
  }
}
