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
package org.ar4k.agent.mattermost.model.config;

import org.ar4k.agent.mattermost.model.config.consts.ImageProxyType;

/**
 * The image proxy settings.
 * 
 * @since Mattermost Server 5.8
 */
public class ImageProxySettings {
  private boolean enable = true;
  private ImageProxyType imageProxyType = ImageProxyType.LOCAL;
  private String remoteImageProxyUrl;
  private String remoteImageProxyOptions;

  @java.lang.SuppressWarnings("all")
  public ImageProxySettings() {
  }

  @java.lang.SuppressWarnings("all")
  public boolean isEnable() {
    return this.enable;
  }

  @java.lang.SuppressWarnings("all")
  public ImageProxyType getImageProxyType() {
    return this.imageProxyType;
  }

  @java.lang.SuppressWarnings("all")
  public String getRemoteImageProxyUrl() {
    return this.remoteImageProxyUrl;
  }

  @java.lang.SuppressWarnings("all")
  public String getRemoteImageProxyOptions() {
    return this.remoteImageProxyOptions;
  }

  @java.lang.SuppressWarnings("all")
  public void setEnable(final boolean enable) {
    this.enable = enable;
  }

  @java.lang.SuppressWarnings("all")
  public void setImageProxyType(final ImageProxyType imageProxyType) {
    this.imageProxyType = imageProxyType;
  }

  @java.lang.SuppressWarnings("all")
  public void setRemoteImageProxyUrl(final String remoteImageProxyUrl) {
    this.remoteImageProxyUrl = remoteImageProxyUrl;
  }

  @java.lang.SuppressWarnings("all")
  public void setRemoteImageProxyOptions(final String remoteImageProxyOptions) {
    this.remoteImageProxyOptions = remoteImageProxyOptions;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public boolean equals(final java.lang.Object o) {
    if (o == this) return true;
    if (!(o instanceof ImageProxySettings)) return false;
    final ImageProxySettings other = (ImageProxySettings) o;
    if (!other.canEqual((java.lang.Object) this)) return false;
    if (this.isEnable() != other.isEnable()) return false;
    final java.lang.Object this$imageProxyType = this.getImageProxyType();
    final java.lang.Object other$imageProxyType = other.getImageProxyType();
    if (this$imageProxyType == null ? other$imageProxyType != null : !this$imageProxyType.equals(other$imageProxyType)) return false;
    final java.lang.Object this$remoteImageProxyUrl = this.getRemoteImageProxyUrl();
    final java.lang.Object other$remoteImageProxyUrl = other.getRemoteImageProxyUrl();
    if (this$remoteImageProxyUrl == null ? other$remoteImageProxyUrl != null : !this$remoteImageProxyUrl.equals(other$remoteImageProxyUrl)) return false;
    final java.lang.Object this$remoteImageProxyOptions = this.getRemoteImageProxyOptions();
    final java.lang.Object other$remoteImageProxyOptions = other.getRemoteImageProxyOptions();
    if (this$remoteImageProxyOptions == null ? other$remoteImageProxyOptions != null : !this$remoteImageProxyOptions.equals(other$remoteImageProxyOptions)) return false;
    return true;
  }

  @java.lang.SuppressWarnings("all")
  protected boolean canEqual(final java.lang.Object other) {
    return other instanceof ImageProxySettings;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + (this.isEnable() ? 79 : 97);
    final java.lang.Object $imageProxyType = this.getImageProxyType();
    result = result * PRIME + ($imageProxyType == null ? 43 : $imageProxyType.hashCode());
    final java.lang.Object $remoteImageProxyUrl = this.getRemoteImageProxyUrl();
    result = result * PRIME + ($remoteImageProxyUrl == null ? 43 : $remoteImageProxyUrl.hashCode());
    final java.lang.Object $remoteImageProxyOptions = this.getRemoteImageProxyOptions();
    result = result * PRIME + ($remoteImageProxyOptions == null ? 43 : $remoteImageProxyOptions.hashCode());
    return result;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public java.lang.String toString() {
    return "ImageProxySettings(enable=" + this.isEnable() + ", imageProxyType=" + this.getImageProxyType() + ", remoteImageProxyUrl=" + this.getRemoteImageProxyUrl() + ", remoteImageProxyOptions=" + this.getRemoteImageProxyOptions() + ")";
  }
}
