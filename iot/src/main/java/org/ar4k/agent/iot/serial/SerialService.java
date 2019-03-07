/**
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    */
package org.ar4k.agent.iot.serial;

import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringEscapeUtils;
import org.ar4k.agent.camel.Ar4kCamelCallback;
import org.ar4k.agent.config.ConfigSeed;
//import org.ar4k.agent.camel.DynamicRouteBuilder;
import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Ar4kService;
import org.json.JSONObject;

import com.fazecast.jSerialComm.SerialPort;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Servizio di connessione seriale.
 */
public class SerialService extends Ar4kService implements Ar4kCamelCallback {

  // porta serial target
  private SerialPort comPort = null;

  // id camel route per write seriale
  private String rottaWrite = null;

  // iniettata vedi set/get
  private SerialConfig configuration = null;

  // iniettata vedi set/get
  private Anima anima = null;

  // baudrate seriale
  public static enum BaudRate {
    bs150, bs300, bs600, bs1200, bs1800, bs2400, bs4800, bs7200, bs9600, bs14400, bs19200, bs38400, bs56000, bs57600,
    bs76800, bs115200, bs128000, bs230400, bs250000, auto

  }

  // template configurazione bit parità
  public static enum ConventionalNotation {
    _8N1, _7E1
  }

  public Queue<String> lastMessage = null;

  public SerialService() {
  }

  @Override
  @PostConstruct
  public void postCostructor() {
    super.postCostructor();
  }

  @Override
  public synchronized void loop() {
    // crea la coda se vuota
    if (lastMessage == null) {
      lastMessage = new ArrayBlockingQueue<String>(configuration.queueSize);
    }
    // se non è stata creata la porta
    if (comPort == null) {
      try {
        comPort = SerialPort.getCommPort(configuration.serial);
        // TODO: percorso della seriale come path e numero. Per ora prima porta della
        // macchina.
        // comPort = SerialPort.getCommPorts()[0];
      } catch (Exception ee) {
        System.out.println(ee.getMessage());
      }
      // configura baudrate e bit controllo
      comPort.setBaudRate(Integer.valueOf(configuration.baudRate.name().replace("bs", "")));
      if (configuration.conventionalNotation.equals(ConventionalNotation._8N1)) {
        comPort.setNumDataBits(8);
        comPort.setNumStopBits(1);
        comPort.setParity(0);
      }
      if (configuration.conventionalNotation.equals(ConventionalNotation._7E1)) {
        comPort.setNumDataBits(7);
        comPort.setNumStopBits(1);
        comPort.setParity(1);
      }
      // apre la porta
      comPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, configuration.clockRunnableClass,
          configuration.clockRunnableClass);
      comPort.openPort();
      // System.out.println("serial port timeout: " + comPort.getReadTimeout());
    } else {
      // setta la callback
      if (rottaWrite == null) {
        /*
         * DynamicRouteBuilder costruttore = new DynamicRouteBuilder(anima.camelContext,
         * configuration.camelEndpointWriteSerial,
         * "bean:anima?method=onCamelMessageToRoute");
         */
        try {
          // TODO: sistemare con JMS
          // anima.camelContext.addRoutes(costruttore);
          // rottaWrite = costruttore.getIdRotta();
        } catch (Exception e) {
          e.printStackTrace();
        }
        // anima.registeredCallback.put(rottaWrite, this);
      }
      // leggi tutti i dati
      byte[] newData = null;
      int numRead = 0;
      if (comPort.bytesAvailable() > 0) {
        newData = new byte[comPort.bytesAvailable()];
        numRead = comPort.readBytes(newData, newData.length);
      }
      if (numRead > 0) {
        // cancella l'ultimo elemnto dalla coda se piena
        if (configuration.queueSize > 0 && (((ArrayBlockingQueue<String>) lastMessage).remainingCapacity() < 1)) {
          lastMessage.poll();
        }
        lastMessage.offer(new String(newData, StandardCharsets.UTF_8));
        /*
         * anima.camelContext.createProducerTemplate().sendBody(configuration.
         * camelEndpointReadSerial, new String(newData, StandardCharsets.UTF_8));
         * anima.camelContext.createProducerTemplate().sendBody(configuration.
         * camelEndpointReadOk, String.valueOf(numRead));
         */
      }
      // recupera le disconnessioni
      if (comPort.isOpen() != true) {
        comPort = null;
      }
    }
  }

  @Override
  public void kill() {
    if (rottaWrite != null) {
      try {
        /*
         * anima.camelContext.stopRoute(rottaWrite, 10L, TimeUnit.MILLISECONDS);
         * anima.camelContext.removeRoute(rottaWrite);
         */
        rottaWrite = null;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    comPort.closePort();
    comPort = null;
    super.kill();
  }

  @Override
  protected void finalize() {
    try {
      /*
       * anima.camelContext.stopRoute(rottaWrite, 10L, TimeUnit.MILLISECONDS);
       * anima.camelContext.removeRoute(rottaWrite);
       */
      rottaWrite = null;
      comPort.closePort();
      comPort = null;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // ricezione messaggio
  public synchronized String onCamelMessage(Object message) {
    String ret = "ko";
    if (message != null) {
      byte[] mes = ((byte[]) message);
      String messaggioStringa = "";
      messaggioStringa = StringEscapeUtils.unescapeJava(new String(mes, StandardCharsets.UTF_8));
      // System.out.println("received from Camel [" + String.valueOf((byte[]) message)
      // + "]\nin String: ["
      // + messaggioStringa + "]");
      comPort.writeBytes(messaggioStringa.getBytes(), messaggioStringa.getBytes().length);
      /*
       * anima.camelContext.createProducerTemplate().sendBody(configuration.
       * camelEndpointWriteOk, String.valueOf(messaggioStringa.getBytes().length));
       */
      ret = "ok";
    }
    return ret;
  }

  // ricezione messaggio
  public synchronized String onMessageString(String message) {
    return onCamelMessage(message.getBytes());
  }

  @Override
  public SerialConfig getConfiguration() {
    return configuration;
  }

  @Override
  public void setConfiguration(ServiceConfig configuration) {
    super.setConfiguration(configuration);
    this.configuration = ((SerialConfig) configuration);
  }

  @Override
  public Anima getAnima() {
    return anima;
  }

  @Override
  public void setAnima(Anima anima) {
    super.setAnima(anima);
    this.anima = anima;
  }

  // ritorna le seriali disponibili
  public static SerialPort[] getSerialDevice() {
    return SerialPort.getCommPorts();
  }

  @Override
  public void init() {
    // TODO Auto-generated method stub

  }

  @Override
  public void setConfiguration(ConfigSeed configuration) {
    // TODO Auto-generated method stub

  }

  @Override
  public String getStatusString() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public JSONObject getStatusJson() {
    // TODO Auto-generated method stub
    return null;
  }
}
