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

public class PushNotification {
	@JsonProperty("platform")
	private String platform;
	@JsonProperty("server_id")
	private String serverId;
	@JsonProperty("device_id")
	private String deviceId;
	@JsonProperty("category")
	private String category;
	@JsonProperty("sound")
	private String sound;
	@JsonProperty("message")
	private String message;
	@JsonProperty("badge")
	private int badge;
	@JsonProperty("cont_ava")
	private int contentAvailable;
	@JsonProperty("channel_id")
	private String channelId;
	@JsonProperty("channel_name")
	private String channelName;
	@JsonProperty("type")
	private PushType type;
	@JsonProperty("sender_id")
	private String senderId;
	@JsonProperty("override_username")
	private String overrideUsername;
	@JsonProperty("override_icon_url")
	private String overrideIconUrl;
	@JsonProperty("from_webhook")
	private String fromWebhook;

	@java.lang.SuppressWarnings("all")
	public PushNotification() {
	}

	@java.lang.SuppressWarnings("all")
	public String getPlatform() {
		return this.platform;
	}

	@java.lang.SuppressWarnings("all")
	public String getServerId() {
		return this.serverId;
	}

	@java.lang.SuppressWarnings("all")
	public String getDeviceId() {
		return this.deviceId;
	}

	@java.lang.SuppressWarnings("all")
	public String getCategory() {
		return this.category;
	}

	@java.lang.SuppressWarnings("all")
	public String getSound() {
		return this.sound;
	}

	@java.lang.SuppressWarnings("all")
	public String getMessage() {
		return this.message;
	}

	@java.lang.SuppressWarnings("all")
	public int getBadge() {
		return this.badge;
	}

	@java.lang.SuppressWarnings("all")
	public int getContentAvailable() {
		return this.contentAvailable;
	}

	@java.lang.SuppressWarnings("all")
	public String getChannelId() {
		return this.channelId;
	}

	@java.lang.SuppressWarnings("all")
	public String getChannelName() {
		return this.channelName;
	}

	@java.lang.SuppressWarnings("all")
	public PushType getType() {
		return this.type;
	}

	@java.lang.SuppressWarnings("all")
	public String getSenderId() {
		return this.senderId;
	}

	@java.lang.SuppressWarnings("all")
	public String getOverrideUsername() {
		return this.overrideUsername;
	}

	@java.lang.SuppressWarnings("all")
	public String getOverrideIconUrl() {
		return this.overrideIconUrl;
	}

	@java.lang.SuppressWarnings("all")
	public String getFromWebhook() {
		return this.fromWebhook;
	}

	@JsonProperty("platform")
	@java.lang.SuppressWarnings("all")
	public void setPlatform(final String platform) {
		this.platform = platform;
	}

	@JsonProperty("server_id")
	@java.lang.SuppressWarnings("all")
	public void setServerId(final String serverId) {
		this.serverId = serverId;
	}

	@JsonProperty("device_id")
	@java.lang.SuppressWarnings("all")
	public void setDeviceId(final String deviceId) {
		this.deviceId = deviceId;
	}

	@JsonProperty("category")
	@java.lang.SuppressWarnings("all")
	public void setCategory(final String category) {
		this.category = category;
	}

	@JsonProperty("sound")
	@java.lang.SuppressWarnings("all")
	public void setSound(final String sound) {
		this.sound = sound;
	}

	@JsonProperty("message")
	@java.lang.SuppressWarnings("all")
	public void setMessage(final String message) {
		this.message = message;
	}

	@JsonProperty("badge")
	@java.lang.SuppressWarnings("all")
	public void setBadge(final int badge) {
		this.badge = badge;
	}

	@JsonProperty("cont_ava")
	@java.lang.SuppressWarnings("all")
	public void setContentAvailable(final int contentAvailable) {
		this.contentAvailable = contentAvailable;
	}

	@JsonProperty("channel_id")
	@java.lang.SuppressWarnings("all")
	public void setChannelId(final String channelId) {
		this.channelId = channelId;
	}

	@JsonProperty("channel_name")
	@java.lang.SuppressWarnings("all")
	public void setChannelName(final String channelName) {
		this.channelName = channelName;
	}

	@JsonProperty("type")
	@java.lang.SuppressWarnings("all")
	public void setType(final PushType type) {
		this.type = type;
	}

	@JsonProperty("sender_id")
	@java.lang.SuppressWarnings("all")
	public void setSenderId(final String senderId) {
		this.senderId = senderId;
	}

	@JsonProperty("override_username")
	@java.lang.SuppressWarnings("all")
	public void setOverrideUsername(final String overrideUsername) {
		this.overrideUsername = overrideUsername;
	}

	@JsonProperty("override_icon_url")
	@java.lang.SuppressWarnings("all")
	public void setOverrideIconUrl(final String overrideIconUrl) {
		this.overrideIconUrl = overrideIconUrl;
	}

	@JsonProperty("from_webhook")
	@java.lang.SuppressWarnings("all")
	public void setFromWebhook(final String fromWebhook) {
		this.fromWebhook = fromWebhook;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this)
			return true;
		if (!(o instanceof PushNotification))
			return false;
		final PushNotification other = (PushNotification) o;
		if (!other.canEqual((java.lang.Object) this))
			return false;
		if (this.getBadge() != other.getBadge())
			return false;
		if (this.getContentAvailable() != other.getContentAvailable())
			return false;
		final java.lang.Object this$platform = this.getPlatform();
		final java.lang.Object other$platform = other.getPlatform();
		if (this$platform == null ? other$platform != null : !this$platform.equals(other$platform))
			return false;
		final java.lang.Object this$serverId = this.getServerId();
		final java.lang.Object other$serverId = other.getServerId();
		if (this$serverId == null ? other$serverId != null : !this$serverId.equals(other$serverId))
			return false;
		final java.lang.Object this$deviceId = this.getDeviceId();
		final java.lang.Object other$deviceId = other.getDeviceId();
		if (this$deviceId == null ? other$deviceId != null : !this$deviceId.equals(other$deviceId))
			return false;
		final java.lang.Object this$category = this.getCategory();
		final java.lang.Object other$category = other.getCategory();
		if (this$category == null ? other$category != null : !this$category.equals(other$category))
			return false;
		final java.lang.Object this$sound = this.getSound();
		final java.lang.Object other$sound = other.getSound();
		if (this$sound == null ? other$sound != null : !this$sound.equals(other$sound))
			return false;
		final java.lang.Object this$message = this.getMessage();
		final java.lang.Object other$message = other.getMessage();
		if (this$message == null ? other$message != null : !this$message.equals(other$message))
			return false;
		final java.lang.Object this$channelId = this.getChannelId();
		final java.lang.Object other$channelId = other.getChannelId();
		if (this$channelId == null ? other$channelId != null : !this$channelId.equals(other$channelId))
			return false;
		final java.lang.Object this$channelName = this.getChannelName();
		final java.lang.Object other$channelName = other.getChannelName();
		if (this$channelName == null ? other$channelName != null : !this$channelName.equals(other$channelName))
			return false;
		final java.lang.Object this$type = this.getType();
		final java.lang.Object other$type = other.getType();
		if (this$type == null ? other$type != null : !this$type.equals(other$type))
			return false;
		final java.lang.Object this$senderId = this.getSenderId();
		final java.lang.Object other$senderId = other.getSenderId();
		if (this$senderId == null ? other$senderId != null : !this$senderId.equals(other$senderId))
			return false;
		final java.lang.Object this$overrideUsername = this.getOverrideUsername();
		final java.lang.Object other$overrideUsername = other.getOverrideUsername();
		if (this$overrideUsername == null ? other$overrideUsername != null
				: !this$overrideUsername.equals(other$overrideUsername))
			return false;
		final java.lang.Object this$overrideIconUrl = this.getOverrideIconUrl();
		final java.lang.Object other$overrideIconUrl = other.getOverrideIconUrl();
		if (this$overrideIconUrl == null ? other$overrideIconUrl != null
				: !this$overrideIconUrl.equals(other$overrideIconUrl))
			return false;
		final java.lang.Object this$fromWebhook = this.getFromWebhook();
		final java.lang.Object other$fromWebhook = other.getFromWebhook();
		if (this$fromWebhook == null ? other$fromWebhook != null : !this$fromWebhook.equals(other$fromWebhook))
			return false;
		return true;
	}

	@java.lang.SuppressWarnings("all")
	protected boolean canEqual(final java.lang.Object other) {
		return other instanceof PushNotification;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		result = result * PRIME + this.getBadge();
		result = result * PRIME + this.getContentAvailable();
		final java.lang.Object $platform = this.getPlatform();
		result = result * PRIME + ($platform == null ? 43 : $platform.hashCode());
		final java.lang.Object $serverId = this.getServerId();
		result = result * PRIME + ($serverId == null ? 43 : $serverId.hashCode());
		final java.lang.Object $deviceId = this.getDeviceId();
		result = result * PRIME + ($deviceId == null ? 43 : $deviceId.hashCode());
		final java.lang.Object $category = this.getCategory();
		result = result * PRIME + ($category == null ? 43 : $category.hashCode());
		final java.lang.Object $sound = this.getSound();
		result = result * PRIME + ($sound == null ? 43 : $sound.hashCode());
		final java.lang.Object $message = this.getMessage();
		result = result * PRIME + ($message == null ? 43 : $message.hashCode());
		final java.lang.Object $channelId = this.getChannelId();
		result = result * PRIME + ($channelId == null ? 43 : $channelId.hashCode());
		final java.lang.Object $channelName = this.getChannelName();
		result = result * PRIME + ($channelName == null ? 43 : $channelName.hashCode());
		final java.lang.Object $type = this.getType();
		result = result * PRIME + ($type == null ? 43 : $type.hashCode());
		final java.lang.Object $senderId = this.getSenderId();
		result = result * PRIME + ($senderId == null ? 43 : $senderId.hashCode());
		final java.lang.Object $overrideUsername = this.getOverrideUsername();
		result = result * PRIME + ($overrideUsername == null ? 43 : $overrideUsername.hashCode());
		final java.lang.Object $overrideIconUrl = this.getOverrideIconUrl();
		result = result * PRIME + ($overrideIconUrl == null ? 43 : $overrideIconUrl.hashCode());
		final java.lang.Object $fromWebhook = this.getFromWebhook();
		result = result * PRIME + ($fromWebhook == null ? 43 : $fromWebhook.hashCode());
		return result;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "PushNotification(platform=" + this.getPlatform() + ", serverId=" + this.getServerId() + ", deviceId="
				+ this.getDeviceId() + ", category=" + this.getCategory() + ", sound=" + this.getSound() + ", message="
				+ this.getMessage() + ", badge=" + this.getBadge() + ", contentAvailable=" + this.getContentAvailable()
				+ ", channelId=" + this.getChannelId() + ", channelName=" + this.getChannelName() + ", type="
				+ this.getType() + ", senderId=" + this.getSenderId() + ", overrideUsername="
				+ this.getOverrideUsername() + ", overrideIconUrl=" + this.getOverrideIconUrl() + ", fromWebhook="
				+ this.getFromWebhook() + ")";
	}
}
