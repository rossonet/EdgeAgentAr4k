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

/**
 * Message export settings.
 * 
 * @author Takayuki Maruyama
 * @since Mattermost Server 4.5 (Enterprise Edition)
 */
public class MessageExportSettings {
  private boolean enableExport;
  private String dailyRunTime = "01:00";
  private int exportFromTimestamp;
  private String fileLocation = "export";
  private int batchSize = 10000;
  /* @since Mattermost Server 4.9 */
  private String customerType;
  /* @since Mattermost Server 4.9 */
  private String emailAddress;
  /* @since Mattermost Server 4.9 */
  private String exportFormat;
  /* @since Mattermost Server 4.9 */
  private GlobalRelayMessageExportSettings globalRelaySettings;

  @java.lang.SuppressWarnings("all")
  public MessageExportSettings() {
  }

  @java.lang.SuppressWarnings("all")
  public boolean isEnableExport() {
    return this.enableExport;
  }

  @java.lang.SuppressWarnings("all")
  public String getDailyRunTime() {
    return this.dailyRunTime;
  }

  @java.lang.SuppressWarnings("all")
  public int getExportFromTimestamp() {
    return this.exportFromTimestamp;
  }

  @java.lang.SuppressWarnings("all")
  public String getFileLocation() {
    return this.fileLocation;
  }

  @java.lang.SuppressWarnings("all")
  public int getBatchSize() {
    return this.batchSize;
  }

  @java.lang.SuppressWarnings("all")
  public String getCustomerType() {
    return this.customerType;
  }

  @java.lang.SuppressWarnings("all")
  public String getEmailAddress() {
    return this.emailAddress;
  }

  @java.lang.SuppressWarnings("all")
  public String getExportFormat() {
    return this.exportFormat;
  }

  @java.lang.SuppressWarnings("all")
  public GlobalRelayMessageExportSettings getGlobalRelaySettings() {
    return this.globalRelaySettings;
  }

  @java.lang.SuppressWarnings("all")
  public void setEnableExport(final boolean enableExport) {
    this.enableExport = enableExport;
  }

  @java.lang.SuppressWarnings("all")
  public void setDailyRunTime(final String dailyRunTime) {
    this.dailyRunTime = dailyRunTime;
  }

  @java.lang.SuppressWarnings("all")
  public void setExportFromTimestamp(final int exportFromTimestamp) {
    this.exportFromTimestamp = exportFromTimestamp;
  }

  @java.lang.SuppressWarnings("all")
  public void setFileLocation(final String fileLocation) {
    this.fileLocation = fileLocation;
  }

  @java.lang.SuppressWarnings("all")
  public void setBatchSize(final int batchSize) {
    this.batchSize = batchSize;
  }

  @java.lang.SuppressWarnings("all")
  public void setCustomerType(final String customerType) {
    this.customerType = customerType;
  }

  @java.lang.SuppressWarnings("all")
  public void setEmailAddress(final String emailAddress) {
    this.emailAddress = emailAddress;
  }

  @java.lang.SuppressWarnings("all")
  public void setExportFormat(final String exportFormat) {
    this.exportFormat = exportFormat;
  }

  @java.lang.SuppressWarnings("all")
  public void setGlobalRelaySettings(final GlobalRelayMessageExportSettings globalRelaySettings) {
    this.globalRelaySettings = globalRelaySettings;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public boolean equals(final java.lang.Object o) {
    if (o == this) return true;
    if (!(o instanceof MessageExportSettings)) return false;
    final MessageExportSettings other = (MessageExportSettings) o;
    if (!other.canEqual((java.lang.Object) this)) return false;
    if (this.isEnableExport() != other.isEnableExport()) return false;
    if (this.getExportFromTimestamp() != other.getExportFromTimestamp()) return false;
    if (this.getBatchSize() != other.getBatchSize()) return false;
    final java.lang.Object this$dailyRunTime = this.getDailyRunTime();
    final java.lang.Object other$dailyRunTime = other.getDailyRunTime();
    if (this$dailyRunTime == null ? other$dailyRunTime != null : !this$dailyRunTime.equals(other$dailyRunTime)) return false;
    final java.lang.Object this$fileLocation = this.getFileLocation();
    final java.lang.Object other$fileLocation = other.getFileLocation();
    if (this$fileLocation == null ? other$fileLocation != null : !this$fileLocation.equals(other$fileLocation)) return false;
    final java.lang.Object this$customerType = this.getCustomerType();
    final java.lang.Object other$customerType = other.getCustomerType();
    if (this$customerType == null ? other$customerType != null : !this$customerType.equals(other$customerType)) return false;
    final java.lang.Object this$emailAddress = this.getEmailAddress();
    final java.lang.Object other$emailAddress = other.getEmailAddress();
    if (this$emailAddress == null ? other$emailAddress != null : !this$emailAddress.equals(other$emailAddress)) return false;
    final java.lang.Object this$exportFormat = this.getExportFormat();
    final java.lang.Object other$exportFormat = other.getExportFormat();
    if (this$exportFormat == null ? other$exportFormat != null : !this$exportFormat.equals(other$exportFormat)) return false;
    final java.lang.Object this$globalRelaySettings = this.getGlobalRelaySettings();
    final java.lang.Object other$globalRelaySettings = other.getGlobalRelaySettings();
    if (this$globalRelaySettings == null ? other$globalRelaySettings != null : !this$globalRelaySettings.equals(other$globalRelaySettings)) return false;
    return true;
  }

  @java.lang.SuppressWarnings("all")
  protected boolean canEqual(final java.lang.Object other) {
    return other instanceof MessageExportSettings;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    result = result * PRIME + (this.isEnableExport() ? 79 : 97);
    result = result * PRIME + this.getExportFromTimestamp();
    result = result * PRIME + this.getBatchSize();
    final java.lang.Object $dailyRunTime = this.getDailyRunTime();
    result = result * PRIME + ($dailyRunTime == null ? 43 : $dailyRunTime.hashCode());
    final java.lang.Object $fileLocation = this.getFileLocation();
    result = result * PRIME + ($fileLocation == null ? 43 : $fileLocation.hashCode());
    final java.lang.Object $customerType = this.getCustomerType();
    result = result * PRIME + ($customerType == null ? 43 : $customerType.hashCode());
    final java.lang.Object $emailAddress = this.getEmailAddress();
    result = result * PRIME + ($emailAddress == null ? 43 : $emailAddress.hashCode());
    final java.lang.Object $exportFormat = this.getExportFormat();
    result = result * PRIME + ($exportFormat == null ? 43 : $exportFormat.hashCode());
    final java.lang.Object $globalRelaySettings = this.getGlobalRelaySettings();
    result = result * PRIME + ($globalRelaySettings == null ? 43 : $globalRelaySettings.hashCode());
    return result;
  }

  @java.lang.Override
  @java.lang.SuppressWarnings("all")
  public java.lang.String toString() {
    return "MessageExportSettings(enableExport=" + this.isEnableExport() + ", dailyRunTime=" + this.getDailyRunTime() + ", exportFromTimestamp=" + this.getExportFromTimestamp() + ", fileLocation=" + this.getFileLocation() + ", batchSize=" + this.getBatchSize() + ", customerType=" + this.getCustomerType() + ", emailAddress=" + this.getEmailAddress() + ", exportFormat=" + this.getExportFormat() + ", globalRelaySettings=" + this.getGlobalRelaySettings() + ")";
  }
}
