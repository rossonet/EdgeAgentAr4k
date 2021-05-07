package org.ar4k.agent.core.interfaces;

import java.util.List;

import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.core.data.DataTree;
import org.ar4k.agent.tunnels.http2.grpc.beacon.DataType;
import org.joda.time.Instant;
import org.springframework.integration.channel.AbstractMessageChannel;

public interface EdgeChannel extends AutoCloseable {

	public static enum Status {
		DETROY, FAULT, INIT, PAUSED, RUNNING, WAITING_ENDPOINTS
	}

	public static enum Type {
		DIRECT, EXECUTOR, PRIORITY, PUBLISH_SUBSCRIBE, QUEUE, RENDEZVOUS
	}

	int addLogLine(String text);

	void addTag(String tag);

	void clearLog();

	String getAbsoluteNameByScope(String scope);

	String getBrowseName();

	AbstractMessageChannel getChannel();

	Class<? extends EdgeChannel> getChannelClass();

	Type getChannelType();

	int getChildrenCountOfScope(String scope);

	List<EdgeChannel> getChildrenOfScope(String scope);

	Instant getCreateData();

	DataAddress getDataAddress();

	DataType getDataType();

	String getDescription();

	String getDictionaryRef();

	String getDocumentation();

	String getDomainId();

	EdgeChannel getFatherOfScope(String scope);

	int getLogLineSize();

	int getLogQueueSize();

	String getNameSpace();

	String getNodeId();

	DataTree<EdgeChannel> getScopeTreeChildren(String scope, int maxLoop);

	Class<? extends DataServiceOwner> getServiceClass();

	String getServiceName();

	Status getStatus();

	List<String> getTags();

	boolean isRemote();

	String pollLogLine();

	void removeFatherOfScope(String scope);

	void setBrowseName(String nodeId);

	void setChannelType(Type channelType);

	void setCreateData(Instant createData);

	void setDataAddress(DataAddress dataAddress);

	void setDataType(DataType dataType);

	void setDescription(String description);

	void setDomainId(String domainId);

	void setFatherOfScope(String scope, EdgeChannel father);

	void setLogQueueSize(int logQueueSize);

	void setNameSpace(String nameSpace);

	void setTags(List<String> tags);

}
