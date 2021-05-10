/**
 *
 */
package org.ar4k.agent.iot.serial.json;

import org.ar4k.agent.iot.serial.SerialConfig;

import com.beust.jcommander.Parameter;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */
public class SerialJsonConfig extends SerialConfig {

	private static final long serialVersionUID = 5360712941610245833L;

	@Parameter(names = "--defaultChannel", description = "default channel for message without tag for destination")
	private String defaultChannel = null;
	@Parameter(names = "--brokenMessage", description = "default channel for message not converted in JSON")
	private String brokenMessage = null;
	@Parameter(names = "--tagLabel", description = "name of the parameter to filter")
	private String tagLabel = "tag";
	@Parameter(names = "--dataLabel", description = "name of the data payload")
	private String dataLabel = "data";
	@Parameter(names = "--preChannelName", description = "string before channel name")
	private String preChannelName = null;

	@Override
	public SerialJsonService instantiate() {
		SerialJsonService ss = new SerialJsonService();
		ss.setConfiguration(this);
		return ss;
	}

	public String getPreChannelName() {
		return preChannelName != null ? preChannelName : serial.replace("/", "_") + "_tag_";
	}

	public void setPreChannelName(String preChannelName) {
		this.preChannelName = preChannelName;
	}

	public String getDefaultChannel() {
		return defaultChannel != null ? defaultChannel : serial.replace("/", "_") + "_not_routed";
	}

	public void setDefaultChannel(String defaultChannel) {
		this.defaultChannel = defaultChannel;
	}

	public String getBrokenMessage() {
		return brokenMessage != null ? brokenMessage : serial.replace("/", "_") + "_brokenMessage";
	}

	public void setBrokenMessage(String brokenMessage) {
		this.brokenMessage = brokenMessage;
	}

	public String getTagLabel() {
		return tagLabel;
	}

	public void setTagLabel(String tagLabel) {
		this.tagLabel = tagLabel;
	}

	public String getDataLabel() {
		return dataLabel;
	}

	public void setDataLabel(String dataLabel) {
		this.dataLabel = dataLabel;
	}

}
