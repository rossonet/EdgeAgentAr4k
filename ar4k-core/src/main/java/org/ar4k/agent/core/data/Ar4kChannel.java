package org.ar4k.agent.core.data;

import java.util.List;

import org.ar4k.agent.tunnels.http.grpc.beacon.DataType;
import org.joda.time.Instant;
import org.springframework.integration.channel.AbstractMessageChannel;

public interface Ar4kChannel extends AutoCloseable {

  public static enum Type {
    PUBLISH_SUBSCRIBE, QUEUE, PRIORITY, RENDEZVOUS, DIRECT, EXECUTOR
  }

  public static enum Status {
    INIT, WAITING_ENDPOINTS, RUNNING, FAULT, PAUSED, DETROY
  }

  String getNodeId();

  String getScopeAbsoluteNameByScope(String scope);

  Class<? extends Ar4kChannel> getChannelClass();

  void setNodeId(String nodeId);

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

  void setFatherOfScope(String scope, Ar4kChannel father);

  void removeFatherOfScope(String scope);

  List<Ar4kChannel> getChildrenOfScope(String scope);

  int getChildrenCountOfScope(String scope);

  Ar4kChannel getFatherOfScope(String scope);

  DataAddress getDataAddress();

  void setDataAddress(DataAddress dataAddress);

  void addTag(String tag);

}
