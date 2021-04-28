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
 * Post image metadata.
 * 
 * @since Mattermost Server 5.8
 */
public class PostImage {
  private int width;
  private int height;
  /* @since Mattermost Server 5.11 */
  private String format;
  /* @since Mattermost Server 5.11 */
  private int frameCount;

  @java.lang.SuppressWarnings("all")
  public PostImage() {
  }

  @java.lang.SuppressWarnings("all")
  public int getWidth() {
    return this.width;
  }

  @java.lang.SuppressWarnings("all")
  public int getHeight() {
    return this.height;
  }

  @java.lang.SuppressWarnings("all")
  public String getFormat() {
    return this.format;
  }

  @java.lang.SuppressWarnings("all")
  public int getFrameCount() {
    return this.frameCount;
  }

  @java.lang.SuppressWarnings("all")
  public void setWidth(final int width) {
    this.width = width;
  }

  @java.lang.SuppressWarnings("all")
  public void setHeight(final int height) {
    this.height = height;
  }

  @java.lang.SuppressWarnings("all")
  public void setFormat(final String format) {
    this.format = format;
  }

  @java.lang.SuppressWarnings("all")
  public void setFrameCount(final int frameCount) {
    this.frameCount = frameCount;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public boolean equals(final java.lang.Object o) {
    if (o == this) return true;
    if (!(o instanceof PostImage)) return false;
    final PostImage other = (PostImage) o;
    if (!other.canEqual((java.lang.Object) this)) return false;
    if (this.getWidth() != other.getWidth()) return false;
    if (this.getHeight() != other.getHeight()) return false;
    if (this.getFrameCount() != other.getFrameCount()) return false;
    final java.lang.Object this$format = this.getFormat();
    final java.lang.Object other$format = other.getFormat();
    if (this$format == null ? other$format != null : !this$format.equals(other$format)) return false;
    return true;
  }

  @java.lang.SuppressWarnings("all")
  protected boolean canEqual(final java.lang.Object other) {
    return other instanceof PostImage;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + this.getWidth();
    result = result * PRIME + this.getHeight();
    result = result * PRIME + this.getFrameCount();
    final java.lang.Object $format = this.getFormat();
    result = result * PRIME + ($format == null ? 43 : $format.hashCode());
    return result;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public java.lang.String toString() {
    return "PostImage(width=" + this.getWidth() + ", height=" + this.getHeight() + ", format=" + this.getFormat() + ", frameCount=" + this.getFrameCount() + ")";
  }
}
