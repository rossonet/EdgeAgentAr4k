package org.ar4k.agent.core.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.data.channels.INoDataChannel;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.data.channels.IQueueChannel;
import org.ar4k.agent.helper.HardwareHelper;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.ar4k.agent.spring.HealthMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DataAddressAnima extends DataAddress {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(DataAddressAnima.class.toString());

  public DataAddressAnima(Anima anima, String dataNamePrefix) {
    super(anima, dataNamePrefix);
  }

  // task per health
  private HealthTimer repeatedTask = new HealthTimer();

  private long delay = 35000L;
  private long period = 15000L;

  private transient Timer timer = new Timer("TimerHealthDataAddress");

  private transient Set<DataAddress> slaves = new HashSet<>();

  @Override
  public Collection<Ar4kChannel> getDataChannels() {
    Collection<Ar4kChannel> myData = super.getDataChannels();
    for (DataAddress slave : slaves) {
      myData.addAll(slave.getDataChannels());
    }
    return myData;
  }

  @Override
  public void callAddressSpaceRefresh(Ar4kChannel nodeUpdated) {
    super.callAddressSpaceRefresh(nodeUpdated);
    for (DataAddress slave : slaves) {
      slave.callAddressSpaceRefresh(nodeUpdated);
    }
  }

  @Override
  public void clearDataChannels() {
    for (DataAddress slave : slaves) {
      slave.clearDataChannels();
    }
    super.clearDataChannels();
  }

  @Override
  public Collection<String> listChannels() {
    Collection<String> myData = super.listChannels();
    for (DataAddress slave : slaves) {
      myData.addAll(slave.listChannels());
    }
    return myData;
  }

  @Override
  public void close() throws Exception {
    for (DataAddress slave : slaves) {
      slave.close();
    }
    super.close();
  }

  public void firstStart() {
    Ar4kChannel systemChannel = createOrGetDataChannel("system", INoDataChannel.class, "local JVM system",
        (String) null, null);
    createOrGetDataChannel("logger", IPublishSubscribeChannel.class, "logger queue", systemChannel, getDefaultScope());
    createOrGetDataChannel("health", IPublishSubscribeChannel.class, "local machine hardware and software stats",
        systemChannel, getDefaultScope());
    createOrGetDataChannel("command", IQueueChannel.class, "RPC interface", systemChannel, getDefaultScope());
    // start health regular messages
    repeatedTask.setAnima(anima);
    timer.scheduleAtFixedRate(repeatedTask, delay, period);
  }

  public void registerSlave(DataAddress a) {
    slaves.add(a);
  }

  public void removeSlave(DataAddress a) {
    slaves.remove(a);
  }

  private class HealthTimer extends TimerTask {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private transient Anima anima = null;

    public void setAnima(Anima anima) {
      this.anima = anima;
    }

    @Override
    public void run() {
      try {
        sendEvent(HardwareHelper.getSystemInfo().getHealthIndicator());
      } catch (Exception e) {
        logger.logException(e);
      }
    }

    private void sendEvent(Map<String, Object> healthMessage) {
      try {
        if (anima == null && Anima.getApplicationContext() != null
            && Anima.getApplicationContext().getBean(Anima.class) != null
            && Anima.getApplicationContext().getBean(Anima.class).getDataAddress() != null) {
          anima = Anima.getApplicationContext().getBean(Anima.class);
        }
      } catch (Exception ee) {
        logger.debug(Ar4kLogger.stackTraceToString(ee));
      }
      if (anima != null && anima.getDataAddress() != null && anima.getDataAddress().getChannel("health") != null) {
        HealthMessage<String> messageObject = new HealthMessage<>();
        messageObject.setPayload(gson.toJson(healthMessage));
        ((IPublishSubscribeChannel) anima.getDataAddress().getChannel("health")).send(messageObject);
      }
    }
  };

}
