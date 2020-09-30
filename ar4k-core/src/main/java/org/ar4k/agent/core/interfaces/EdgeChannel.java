package org.ar4k.agent.core.interfaces;

import java.util.List;

import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.DataTree;
import org.ar4k.agent.tunnels.http.grpc.beacon.DataType;
import org.joda.time.Instant;
import org.springframework.integration.channel.AbstractMessageChannel;

public interface EdgeChannel extends AutoCloseable {

	public static enum Type {
		PUBLISH_SUBSCRIBE, QUEUE, PRIORITY, RENDEZVOUS, DIRECT, EXECUTOR
	}

	public static enum Status {
		INIT, WAITING_ENDPOINTS, RUNNING, FAULT, PAUSED, DETROY
	}

	String getNodeId();

	String getBrowseName();

	String getScopeAbsoluteNameByScope(String scope);

	Class<? extends EdgeChannel> getChannelClass();

	void setBrowseName(String nodeId);

	void setDataType(DataType dataType);

	DataType getDataType();

	void setCreateData(Instant createData);

	Instant getCreateData();

	void setDescription(String description);

	String getDescription();

	Type getChannelType();

	void setChannelType(Type channelType);

	boolean isRemote();

	int addLogLine(String text);

	int getLogLineSize();

	void clearLog();

	String pollLogLine();

	int getLogQueueSize();

	void setLogQueueSize(int logQueueSize);

	Status getStatus();

	AbstractMessageChannel getChannel();

	void setTags(List<String> tags);

	List<String> getTags();

	String getDomainId();

	void setDomainId(String domainId);

	String getNameSpace();

	void setNameSpace(String nameSpace);

	void setFatherOfScope(String scope, EdgeChannel father);

	void removeFatherOfScope(String scope);

	List<EdgeChannel> getChildrenOfScope(String scope);

	int getChildrenCountOfScope(String scope);

	EdgeChannel getFatherOfScope(String scope);

	DataAddress getDataAddress();

	void setDataAddress(DataAddress dataAddress);

	void addTag(String tag);

	DataTree<EdgeChannel> getScopeTreeChildren(String scope, int maxLoop);

	String getDocumentation();

	String getDictionaryRef();

}
