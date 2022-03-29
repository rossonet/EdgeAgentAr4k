package org.ar4k.agent.core.data;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.ar4k.agent.config.validator.DataTypeValidator;
import org.ar4k.agent.core.EdgeAgentCore;
import org.ar4k.agent.core.data.channels.EdgeChannel;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.ar4k.agent.tunnels.http2.grpc.beacon.DataType;
import org.joda.time.Instant;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.messaging.MessageChannel;

import com.beust.jcommander.Parameter;

public abstract class AbstractChannel implements EdgeChannel, MessageChannel, Closeable {

	// TO______DO implementare spring integration tra i channel
	// https://docs.spring.io/spring-integration/docs/current/reference/html/message.html#message
	// https://docs.spring.io/spring-integration/docs/current/reference/html/core.html#spring-integration-core-messaging
	// https://docs.spring.io/spring-integration/docs/current/reference/html/message-routing.html#messaging-routing-chapter
	// https://docs.spring.io/spring-integration/docs/current/reference/html/message-transformation.html#messaging-transformation-chapter
	// TO______DO esempio con endpoint camel in spring integration
	// https://www.baeldung.com/spring-apache-camel-tutorial
	// https://dzone.com/articles/spring-integration-and-apache

	protected static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(AbstractChannel.class);

	@Parameter(names = "--browseName", description = "browse name for the channel", required = true)
	private String browseName = null;

	private AbstractMessageChannel channel = null;

	private Type channelTypeRequest = null;

	private Instant createData = Instant.now();

	private DataAddress dataAddressBase = null;

	@Parameter(names = "--dataType", description = "data type for the channel", validateWith = DataTypeValidator.class)
	private DataType dataType = DataType.STRING;

	@Parameter(names = "--description", description = "description for this node", required = false)
	private String description = null;

	@Parameter(names = "--dictionaryRef", description = "urn of semantic dictionary", required = false)
	private String dictionaryRef = null;

	@Parameter(names = "--documentation", description = "documentation url", required = false)
	private String documentation = null;

	@Parameter(names = "--domainId", description = "unique global domain for the data")
	private String domainId = "AR4K";

	private boolean isRemote = false;

	private Queue<String> lastLogs = new ConcurrentLinkedQueue<>();

	@Parameter(names = "--logQueueSize", description = "size of queue for the logs")
	private int logQueueSize = 50;

	@Parameter(names = "--namespace", description = "namespace for the data")
	private String nameSpace = "default";

	@Parameter(names = "--nodeId", description = "nodeId for the channel", required = true)
	private String nodeId = UUID.randomUUID().toString();

	private Map<String, List<EdgeChannel>> scopeChildren = new HashMap<>();

	private Map<String, EdgeChannel> scopeFather = new HashMap<>();

	private final String serviceName;

	private String baseName;

	private final Class<? extends DataServiceOwner> serviceOwnerClass;

	private Status status = Status.INIT;

	@Parameter(names = "--tags", description = "tags for the channel")
	private List<String> tags = new ArrayList<>();

	public AbstractChannel(DataServiceOwner serviceOwnerClass) {
		this.serviceName = serviceOwnerClass.getServiceName();
		this.serviceOwnerClass = serviceOwnerClass.getClass();
		this.dataAddressBase = serviceOwnerClass.getDataAddress();
	}

	@Override
	public int addLogLine(String text) {
		lastLogs.add(text);
		if (lastLogs.size() > logQueueSize) {
			lastLogs.remove();
		}
		return lastLogs.size();
	}

	@Override
	public void addTag(String tag) {
		tags.add(tag);
	}

	@Override
	public void clearLog() {
		lastLogs.clear();
	}

	@Override
	public void close() throws IOException {
		try {
			stopFunction();
			dataAddressBase = null;
			if (getChannel() != null) {
				try {
					getChannel().destroy();
				} catch (final NoSuchMethodError ex) {
					// può succedere
				}
				setChannel(null);
			}
			scopeChildren.clear();
			scopeFather.clear();
		} catch (final Exception e) {
			logger.logException(e);
			setChannel(null);
		}
	}

	@Override
	public String getAbsoluteNameByScope(String scope) {
		final StringBuilder reply = new StringBuilder();
		if (scopeFather.containsKey(scope) && scopeFather.get(scope) != null) {
			if (scopeFather.get(scope).getAbsoluteNameByScope(scope) != null) {
				reply.append(scopeFather.get(scope).getAbsoluteNameByScope(scope) + dataAddressBase.getLevelSeparator());
			}
		}
		reply.append(getBrowseName());
		return reply.toString();
	}

	@Override
	public String getBrowseName() {
		return browseName;
	}

	@Override
	public AbstractMessageChannel getChannel() {
		return channel;
	}

	@Override
	public Type getChannelType() {
		return channelTypeRequest;
	}

	@Override
	public int getChildrenCountOfScope(String scope) {
		return scopeChildren.get(scope) != null ? scopeChildren.get(scope).size() : 0;
	}

	@Override
	public List<EdgeChannel> getChildrenOfScope(String scope) {
		return scopeChildren.get(scope);
	}

	@Override
	public Instant getCreateData() {
		return createData;
	}

	@Override
	public DataAddress getDataAddress() {
		return dataAddressBase;
	}

	@Override
	public DataType getDataType() {
		return dataType;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getDictionaryRef() {
		return dictionaryRef;
	}

	@Override
	public String getDocumentation() {
		return documentation;
	}

	@Override
	public String getDomainId() {
		return domainId;
	}

	@Override
	public EdgeChannel getFatherOfScope(String scope) {
		return scopeFather.get(scope);
	}

	@Override
	public int getLogLineSize() {
		return lastLogs.size();
	}

	@Override
	public int getLogQueueSize() {
		return logQueueSize;
	}

	@Override
	public String getNameSpace() {
		return nameSpace;
	}

	@Override
	public String getNodeId() {
		return nodeId;
	}

	@Override
	public DataTree<EdgeChannel> getScopeTreeChildren(String scope, int maxLoop) {
		if (maxLoop > 0) {
			final DataTree<EdgeChannel> tree = new DataTree<>(this);
			if (getChildrenCountOfScope(scope) > 0) {
				for (final EdgeChannel node : getChildrenOfScope(scope)) {
					tree.addTree(node.getScopeTreeChildren(scope, maxLoop - 1));
				}
			}
			return tree;
		} else {
			return null;
		}
	}

	@Override
	public Class<? extends DataServiceOwner> getServiceClass() {
		return serviceOwnerClass;
	}

	@Override
	public String getServiceName() {
		return serviceName;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	@Override
	public List<String> getTags() {
		return tags;
	}

	@Override
	public boolean isRemote() {
		return isRemote;
	}

	@Override
	public String pollLogLine() {
		return lastLogs.poll();
	}

	@Override
	public void removeFatherOfScope(String scope) {
		if (scopeFather.containsKey(scope) && scopeFather.get(scope) != null) {
			((AbstractChannel) scopeFather.get(scope)).removeChildrenOfScope(scope);
			scopeFather.remove(scope);
			if (dataAddressBase != null) {
				dataAddressBase.callAddressSpaceRefresh(this);
			}
		}
	}

	@Override
	public void setBrowseName(String browseName) {
		this.browseName = browseName;
	}

	@Override
	public String getBaseName() {
		return baseName;
	}

	@Override
	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	@Override
	public void setChannelType(Type channelType) {
		this.channelTypeRequest = channelType;
	}

	@Override
	public void setCreateData(Instant createData) {
		this.createData = createData;
	}

	@Override
	public void setDataAddress(DataAddress dataAddressBase) {
		startFunction();
		this.dataAddressBase = dataAddressBase;
	}

	@Override
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	public void setDictionaryRef(String dictionaryRef) {
		this.dictionaryRef = dictionaryRef;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	@Override
	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	@Override
	public void setFatherOfScope(String scope, EdgeChannel father) {
		if (scope == null) {
			scope = dataAddressBase.getDefaultScope();
		}
		scopeFather.put(scope, father);
		((AbstractChannel) father).addChildOfScope(scope, this);
		if (dataAddressBase != null) {
			dataAddressBase.callAddressSpaceRefresh(this);
		}
	}

	@Override
	public void setLogQueueSize(int logQueueSize) {
		this.logQueueSize = logQueueSize;
	}

	@Override
	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	@Override
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("AbstractChannel [logQueueSize=").append(logQueueSize).append(", ");
		if (nodeId != null)
			builder.append("nodeId=").append(nodeId).append(", ");
		if (baseName != null)
			builder.append("baseName=").append(baseName).append(", ");
		if (serviceName != null)
			builder.append("serviceName=").append(serviceName).append(", ");
		if (serviceOwnerClass != null)
			builder.append("serviceClass=").append(serviceOwnerClass.getCanonicalName()).append(", ");
		if (browseName != null)
			builder.append("browseName=").append(browseName).append(", ");
		if (nameSpace != null)
			builder.append("nameSpace=").append(nameSpace).append(", ");
		if (domainId != null)
			builder.append("domainId=").append(domainId).append(", ");
		if (description != null)
			builder.append("description=").append(description).append(", ");
		if (documentation != null)
			builder.append("documentation=").append(documentation).append(", ");
		if (dictionaryRef != null)
			builder.append("dictionaryRef=").append(dictionaryRef).append(", ");
		if (createData != null)
			builder.append("createData=").append(createData).append(", ");
		if (dataType != null)
			builder.append("dataType=").append(dataType).append(", ");
		if (tags != null)
			builder.append("tags=").append(tags).append(", ");
		if (lastLogs != null)
			builder.append("lastLogs=").append(lastLogs).append(", ");
		builder.append("isRemote=").append(isRemote).append(", ");
		if (channelTypeRequest != null)
			builder.append("channelTypeRequest=").append(channelTypeRequest).append(", ");
		if (status != null)
			builder.append("status=").append(status).append(", ");
		builder.append("]");
		return builder.toString();
	}

	protected void setChannel(AbstractMessageChannel channel) {
		this.channel = channel;
	}

	protected void setStatus(Status status) {
		this.status = status;
	}

	private void addChildOfScope(String scope, EdgeChannel child) {
		logger.info("add child " + child.getBrowseName() + " to " + getBrowseName() + " for scope " + scope);
		if (!scopeChildren.containsKey(scope)) {
			scopeChildren.put(scope, new ArrayList<>());
		}
		scopeChildren.get(scope).add(child);
		if (dataAddressBase != null) {
			dataAddressBase.callAddressSpaceRefresh(this);
		}
	}

	private void removeChildrenOfScope(String scope) {
		scopeChildren.remove(scope);
	}

	private void startFunction() {
		setStatus(Status.WAITING_ENDPOINTS);
		if (getChannel() != null) {
			getChannel().setBeanName(getBrowseName());
			getChannel().setComponentName(getBrowseName());
			((ConfigurableApplicationContext) EdgeAgentCore.getApplicationContextStatic()).getBeanFactory()
					.registerSingleton(getBrowseName(), getChannel());
		}
		if (dataAddressBase != null) {
			dataAddressBase.callAddressSpaceRefresh(this);
		}
		setStatus(Status.RUNNING);
	}

	private void stopFunction() {
		if (dataAddressBase != null) {
			dataAddressBase.callAddressSpaceRefresh(this);
		}
		((DefaultListableBeanFactory) ((ConfigurableApplicationContext) EdgeAgentCore.getApplicationContextStatic())
				.getBeanFactory()).destroySingleton(getBrowseName());
		setStatus(Status.DETROY);
	}
}
