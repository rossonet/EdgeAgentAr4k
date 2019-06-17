package org.ar4k.agent.core.data;

import java.util.List;

import org.ar4k.agent.tunnels.http.grpc.beacon.DataType;
import org.joda.time.Instant;
import org.springframework.integration.channel.AbstractMessageChannel;

public interface Ar4kChannel {

  public static enum Type {
    PublishSubscribeChannel, QueueChannel, PriorityChannel, RendezvousChannel, DirectChannel, ExecutorChannel
  }

  public static enum Status {
    INIT, WAITING_ENDPOINTS, RUNNING, FAULT, PAUSED, DETROY
  }

  public String getNodeId();

  public void setNodeId(String nodeId);

  public void setDataType(DataType dataType);

  public DataType getDataType();

  public void setCreateData(Instant createData);

  public Instant getCreateData();

  public void setDescription(String description);

  public String getDescription();

  public Type getChannelType();

  public void setChannelType(Type channelType);

  public boolean isRemote();

  public void setRemote(boolean isRemote);

  public int addLogLine(String text);

  public int getLogLineSize();

  public void clearLog();

  public String pollLogLine();

  public int getLogQueueSize();

  public void setLogQueueSize(int logQueueSize);

  public Status getStatus();

  public AbstractMessageChannel getChannel();

  void setTags(List<String> tags);

  List<String> getTags();

  String getDomainId();

  void setDomainId(String domainId);

}
