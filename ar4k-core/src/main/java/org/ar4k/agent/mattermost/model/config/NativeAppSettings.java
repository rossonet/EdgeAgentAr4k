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
package org.ar4k.agent.mattermost.model.config;

/**
 * Native app settings.
 * 
 * @author Takayuki Maruyama
 */
public class NativeAppSettings {
  private String appDownloadLink;
  private String androidAppDownloadLink;
  private String iosAppDownloadLink;

  @java.lang.SuppressWarnings("all")
  public NativeAppSettings() {
  }

  @java.lang.SuppressWarnings("all")
  public String getAppDownloadLink() {
    return this.appDownloadLink;
  }

  @java.lang.SuppressWarnings("all")
  public String getAndroidAppDownloadLink() {
    return this.androidAppDownloadLink;
  }

  @java.lang.SuppressWarnings("all")
  public String getIosAppDownloadLink() {
    return this.iosAppDownloadLink;
  }

  @java.lang.SuppressWarnings("all")
  public void setAppDownloadLink(final String appDownloadLink) {
    this.appDownloadLink = appDownloadLink;
  }

  @java.lang.SuppressWarnings("all")
  public void setAndroidAppDownloadLink(final String androidAppDownloadLink) {
    this.androidAppDownloadLink = androidAppDownloadLink;
  }

  @java.lang.SuppressWarnings("all")
  public void setIosAppDownloadLink(final String iosAppDownloadLink) {
    this.iosAppDownloadLink = iosAppDownloadLink;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public boolean equals(final java.lang.Object o) {
    if (o == this) return true;
    if (!(o instanceof NativeAppSettings)) return false;
    final NativeAppSettings other = (NativeAppSettings) o;
    if (!other.canEqual((java.lang.Object) this)) return false;
    final java.lang.Object this$appDownloadLink = this.getAppDownloadLink();
    final java.lang.Object other$appDownloadLink = other.getAppDownloadLink();
    if (this$appDownloadLink == null ? other$appDownloadLink != null : !this$appDownloadLink.equals(other$appDownloadLink)) return false;
    final java.lang.Object this$androidAppDownloadLink = this.getAndroidAppDownloadLink();
    final java.lang.Object other$androidAppDownloadLink = other.getAndroidAppDownloadLink();
    if (this$androidAppDownloadLink == null ? other$androidAppDownloadLink != null : !this$androidAppDownloadLink.equals(other$androidAppDownloadLink)) return false;
    final java.lang.Object this$iosAppDownloadLink = this.getIosAppDownloadLink();
    final java.lang.Object other$iosAppDownloadLink = other.getIosAppDownloadLink();
    if (this$iosAppDownloadLink == null ? other$iosAppDownloadLink != null : !this$iosAppDownloadLink.equals(other$iosAppDownloadLink)) return false;
    return true;
  }

  @java.lang.SuppressWarnings("all")
  protected boolean canEqual(final java.lang.Object other) {
    return other instanceof NativeAppSettings;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final java.lang.Object $appDownloadLink = this.getAppDownloadLink();
    result = result * PRIME + ($appDownloadLink == null ? 43 : $appDownloadLink.hashCode());
    final java.lang.Object $androidAppDownloadLink = this.getAndroidAppDownloadLink();
    result = result * PRIME + ($androidAppDownloadLink == null ? 43 : $androidAppDownloadLink.hashCode());
    final java.lang.Object $iosAppDownloadLink = this.getIosAppDownloadLink();
    result = result * PRIME + ($iosAppDownloadLink == null ? 43 : $iosAppDownloadLink.hashCode());
    return result;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public java.lang.String toString() {
    return "NativeAppSettings(appDownloadLink=" + this.getAppDownloadLink() + ", androidAppDownloadLink=" + this.getAndroidAppDownloadLink() + ", iosAppDownloadLink=" + this.getIosAppDownloadLink() + ")";
  }
}
