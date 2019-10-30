package org.ar4k.agent.core.data;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

  protected static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(AbstractChannel.class.toString());

  @Parameter(names = "--logQueueSize", description = "size of queue for the logs")
  private int logQueueSize = 50;

  @Parameter(names = "--nodeId", description = "unique name for the agent", required = true)
  private String nodeId = null;

  @Parameter(names = "--domainId", description = "unique global domain for the data")
  private String domainId = "AR4K";

  private transient AbstractMessageChannel channel = null;

  private Type channelTypeRequest = null;

  private Status status = Status.INIT;

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

  public void startDataChannel() {
    startFunction();
    setStatus(Status.WAITING_ENDPOINTS);
  }

  public void stopDataChannel() {
    stopFunction();
    setStatus(Status.DETROY);
  }

  protected void startFunction() {
    getChannel().setBeanName(getNodeId());
    getChannel().setComponentName(getNodeId());
    ((ConfigurableApplicationContext) Anima.getApplicationContext()).getBeanFactory().registerSingleton(getNodeId(),
        getChannel());
  }

  protected void stopFunction() {
    ((DefaultListableBeanFactory) ((ConfigurableApplicationContext) Anima.getApplicationContext()).getBeanFactory())
        .destroySingleton(getNodeId());
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
      getChannel().destroy();
      setChannel(null);
    } catch (Exception e) {
      logger.logException(e);
      setChannel(null);
    }
  }

  @Override
  public void setFatherOfScope(String scope, Ar4kChannel father) {
    // TODO Auto-generated method stub
  }

  @Override
  public void addChildOfScope(String scope, Ar4kChannel child) {
    // TODO Auto-generated method stub
  }

  @Override
  public List<Ar4kChannel> getChildsOfScope(String scope) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getChildsCountOfScope(String scope) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void removeParentsOfScope(String scope) {
    // TODO Auto-generated method stub
  }

  @Override
  public Ar4kChannel getFatherOfScope(String scope) {
    // TODO Auto-generated method stub
    return null;
  }

}
