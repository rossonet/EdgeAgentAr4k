// Generated by delombok at Sun Apr 18 22:20:18 CEST 2021
/*
 * @(#) org.ar4k.agent.core.mattermost.model.config.TimezoneSettings Copyright (c) 2018 Takayuki Maruyama
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
 * Timezone settings.
 * 
 * @author Takayuki Maruyama
 * @since Mattermost Server 4.9
 */
public class TimezoneSettings {
  private String supportedTimezonesPath;

  @java.lang.SuppressWarnings("all")
  public TimezoneSettings() {
  }

  @java.lang.SuppressWarnings("all")
  public String getSupportedTimezonesPath() {
    return this.supportedTimezonesPath;
  }

  @java.lang.SuppressWarnings("all")
  public void setSupportedTimezonesPath(final String supportedTimezonesPath) {
    this.supportedTimezonesPath = supportedTimezonesPath;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public boolean equals(final java.lang.Object o) {
    if (o == this) return true;
    if (!(o instanceof TimezoneSettings)) return false;
    final TimezoneSettings other = (TimezoneSettings) o;
    if (!other.canEqual((java.lang.Object) this)) return false;
    final java.lang.Object this$supportedTimezonesPath = this.getSupportedTimezonesPath();
    final java.lang.Object other$supportedTimezonesPath = other.getSupportedTimezonesPath();
    if (this$supportedTimezonesPath == null ? other$supportedTimezonesPath != null : !this$supportedTimezonesPath.equals(other$supportedTimezonesPath)) return false;
    return true;
  }

  @java.lang.SuppressWarnings("all")
  protected boolean canEqual(final java.lang.Object other) {
    return other instanceof TimezoneSettings;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final java.lang.Object $supportedTimezonesPath = this.getSupportedTimezonesPath();
    result = result * PRIME + ($supportedTimezonesPath == null ? 43 : $supportedTimezonesPath.hashCode());
    return result;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public java.lang.String toString() {
    return "TimezoneSettings(supportedTimezonesPath=" + this.getSupportedTimezonesPath() + ")";
  }
}
