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

/**
 * User preferences.
 * 
 * @author Takayuki Maruyama
 */
public class Preference {
  @JsonProperty("user_id")
  private String userid;
  @JsonProperty("category")
  private PreferenceCategory category;
  @JsonProperty("name")
  private String name;
  @JsonProperty("value")
  private String value;


  public enum Name {
    CollapseSetting("collapse_previews"), DisplayNameFormat("name_format"), LastChannel("channel"), LastTeam("team"), EmailInterval("email_interval"), ChannelDisplayMode("channel_display_mode");
    private final String key;

    @java.lang.SuppressWarnings("all")
    private Name(final String key) {
      this.key = key;
    }

    @java.lang.SuppressWarnings("all")
    public String getKey() {
      return this.key;
    }
  }

  @java.lang.SuppressWarnings("all")
  public Preference() {
  }

  @java.lang.SuppressWarnings("all")
  public String getUserid() {
    return this.userid;
  }

  @java.lang.SuppressWarnings("all")
  public PreferenceCategory getCategory() {
    return this.category;
  }

  @java.lang.SuppressWarnings("all")
  public String getName() {
    return this.name;
  }

  @java.lang.SuppressWarnings("all")
  public String getValue() {
    return this.value;
  }

  @JsonProperty("user_id")
  @java.lang.SuppressWarnings("all")
  public void setUserid(final String userid) {
    this.userid = userid;
  }

  @JsonProperty("category")
  @java.lang.SuppressWarnings("all")
  public void setCategory(final PreferenceCategory category) {
    this.category = category;
  }

  @JsonProperty("name")
  @java.lang.SuppressWarnings("all")
  public void setName(final String name) {
    this.name = name;
  }

  @JsonProperty("value")
  @java.lang.SuppressWarnings("all")
  public void setValue(final String value) {
    this.value = value;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public boolean equals(final java.lang.Object o) {
    if (o == this) return true;
    if (!(o instanceof Preference)) return false;
    final Preference other = (Preference) o;
    if (!other.canEqual((java.lang.Object) this)) return false;
    final java.lang.Object this$userid = this.getUserid();
    final java.lang.Object other$userid = other.getUserid();
    if (this$userid == null ? other$userid != null : !this$userid.equals(other$userid)) return false;
    final java.lang.Object this$category = this.getCategory();
    final java.lang.Object other$category = other.getCategory();
    if (this$category == null ? other$category != null : !this$category.equals(other$category)) return false;
    final java.lang.Object this$name = this.getName();
    final java.lang.Object other$name = other.getName();
    if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
    final java.lang.Object this$value = this.getValue();
    final java.lang.Object other$value = other.getValue();
    if (this$value == null ? other$value != null : !this$value.equals(other$value)) return false;
    return true;
  }

  @java.lang.SuppressWarnings("all")
  protected boolean canEqual(final java.lang.Object other) {
    return other instanceof Preference;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final java.lang.Object $userid = this.getUserid();
    result = result * PRIME + ($userid == null ? 43 : $userid.hashCode());
    final java.lang.Object $category = this.getCategory();
    result = result * PRIME + ($category == null ? 43 : $category.hashCode());
    final java.lang.Object $name = this.getName();
    result = result * PRIME + ($name == null ? 43 : $name.hashCode());
    final java.lang.Object $value = this.getValue();
    result = result * PRIME + ($value == null ? 43 : $value.hashCode());
    return result;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public java.lang.String toString() {
    return "Preference(userid=" + this.getUserid() + ", category=" + this.getCategory() + ", name=" + this.getName() + ", value=" + this.getValue() + ")";
  }
}
