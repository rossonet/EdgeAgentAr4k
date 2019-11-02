package org.ar4k.agent.core.data;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.ar4k.agent.config.validator.DataTypeValidator;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.ar4k.agent.tunnels.http.grpc.beacon.DataType;
import org.joda.time.Instant;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.SubscribableChannel;

import com.beust.jcommander.Parameter;

public abstract class AbstractChannel implements Ar4kChannel, Closeable, PollableChannel, SubscribableChannel {

  protected static transient final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton()
      .getLoggerFactory().getLogger(AbstractChannel.class.toString());

  @Parameter(names = "--logQueueSize", description = "size of queue for the logs")
  private int logQueueSize = 50;

  @Parameter(names = "--nodeId", description = "unique name for the agent", required = true)
  private String nodeId = null;

  @Parameter(names = "--domainId", description = "unique global domain for the data")
  private String domainId = "AR4K";

  @Parameter(names = "--description", description = "description for this node", required = false)
  private String description = null;

  private Instant createData = Instant.now();

  @Parameter(names = "--dataType", description = "data type for the channel", validateWith = DataTypeValidator.class)
  private DataType dataType = DataType.STRING;

  private List<String> tags = new ArrayList<>();

  private Queue<String> lastLogs = new ConcurrentLinkedQueue<>();

  @Parameter(names = "--namespace", description = "namespace for the data")
  private String nameSpace = "default";

  private boolean isRemote = false;

  private transient DataAddress dataAddress = null;

  private transient AbstractMessageChannel channel = null;

  private Type channelTypeRequest = null;

  private Status status = Status.INIT;

  private Map<String, List<Ar4kChannel>> scopeChildren = new HashMap<>();
  private Map<String, Ar4kChannel> scopeFather = new HashMap<>();

  @Override
  public String getNodeId() {
    return nodeId;
  }

  @Override
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
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
  public String pollLogLine() {
    return lastLogs.poll();
  }

  @Override
  public int getLogLineSize() {
    return lastLogs.size();
  }

  @Override
  public void clearLog() {
    lastLogs.clear();
  }

  @Override
  public Instant getCreateData() {
    return createData;
  }

  @Override
  public void setCreateData(Instant createData) {
    this.createData = createData;
  }

  @Override
  public DataType getDataType() {
    return dataType;
  }

  @Override
  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }

  @Override
  public Type getChannelType() {
    return channelTypeRequest;
  }

  @Override
  public void setChannelType(Type channelType) {
    this.channelTypeRequest = channelType;
  }

  @Override
  public boolean isRemote() {
    return isRemote;
  }

  @Override
  public int getLogQueueSize() {
    return logQueueSize;
  }

  @Override
  public void setLogQueueSize(int logQueueSize) {
    this.logQueueSize = logQueueSize;
  }

  @Override
  public Status getStatus() {
    return status;
  }

  @Override
  public AbstractMessageChannel getChannel() {
    return channel;
  }

  @Override
  public List<String> getTags() {
    return tags;
  }

  @Override
  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  @Override
  public void addTag(String tag) {
    tags.add(tag);
  }

  @Override
  public String getDomainId() {
    return domainId;
  }

  @Override
  public void setDomainId(String domainId) {
    this.domainId = domainId;
  }

  @Override
  public String getNameSpace() {
    return nameSpace;
  }

  @Override
  public void setNameSpace(String nameSpace) {
    this.nameSpace = nameSpace;
  }

  protected void setStatus(Status status) {
    this.status = status;
  }

  protected void setChannel(AbstractMessageChannel channel) {
    this.channel = channel;
  }

  private void startFunction() {
    setStatus(Status.WAITING_ENDPOINTS);
    getChannel().setBeanName(getNodeId());
    getChannel().setComponentName(getNodeId());
    ((ConfigurableApplicationContext) Anima.getApplicationContext()).getBeanFactory().registerSingleton(getNodeId(),
        getChannel());
  }

  private void stopFunction() {
    ((DefaultListableBeanFactory) ((ConfigurableApplicationContext) Anima.getApplicationContext()).getBeanFactory())
        .destroySingleton(getNodeId());
    setStatus(Status.DETROY);
  }

  @Override
  public String toString() {
    return "Channel [logQueueSize=" + logQueueSize + ", nodeId=" + nodeId + ", domainId=" + domainId + ", channel="
        + channel + ", channelTypeRequest=" + channelTypeRequest + ", status=" + status + ", description=" + description
        + ", createData=" + createData + ", dataType=" + dataType + ", tags=" + tags + ", lastLogs=" + lastLogs
        + ", nameSpace=" + nameSpace + "]";
  }

  @Override
  public void close() throws IOException {
    try {
      stopFunction();
      dataAddress = null;
      getChannel().destroy();
      setChannel(null);
      scopeChildren.clear();
      scopeFather.clear();
    } catch (Exception e) {
      logger.logException(e);
      setChannel(null);
    }
  }

  @Override
  public void setFatherOfScope(String scope, Ar4kChannel father) {
    scopeFather.put(scope, father);
    ((AbstractChannel) father).addChildOfScope(scope, this);
  }

  private void addChildOfScope(String scope, Ar4kChannel child) {
    scopeChildren.get(scope).add(child);
  }

  @Override
  public List<Ar4kChannel> getChildrenOfScope(String scope) {
    return scopeChildren.get(scope);
  }

  @Override
  public int getChildrenCountOfScope(String scope) {
    return scopeChildren.get(scope).size();
  }

  private void removeChildrenOfScope(String scope) {
    scopeChildren.remove(scope);
  }

  @Override
  public void removeFatherOfScope(String scope) {
    if (scopeFather.containsKey(scope) && scopeFather.get(scope) != null) {
      ((AbstractChannel) scopeFather.get(scope)).removeChildrenOfScope(scope);
      scopeFather.remove(scope);
    }
  }

  @Override
  public Ar4kChannel getFatherOfScope(String scope) {
    return scopeFather.get(scope);
  }

  @Override
  public String getScopeAbsoluteNameByScope(String scope) {
    StringBuilder reply = new StringBuilder();
    if (scopeFather.containsKey(scope) && scopeFather.get(scope) != null) {
      if (scopeFather.get(scope).getScopeAbsoluteNameByScope(scope) != null) {
        reply.append(scopeFather.get(scope).getScopeAbsoluteNameByScope(scope) + dataAddress.getLevelSeparator());
      }
    }
    reply.append(getNodeId());
    return reply.toString();
  }

  @Override
  public DataAddress getDataAddress() {
    return dataAddress;
  }

  @Override
  public void setDataAddress(DataAddress dataAddress) {
    startFunction();
    this.dataAddress = dataAddress;
  }

}
