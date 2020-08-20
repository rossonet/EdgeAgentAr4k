package org.ar4k.agent.iot.serial;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.Ar4kComponent;
import org.ar4k.agent.iot.serial.SerialService.BaudRate;
import org.ar4k.agent.iot.serial.SerialService.ConventionalNotation;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione porta seriale collegata all'agente.
 */
public class SerialConfig extends AbstractServiceConfig {

  private static final long serialVersionUID = -864164274161787378L;

  @Parameter(names = "--serial", description = "serial port")
  public String serial = "/dev/ttyACM0";

  @Parameter(names = "--baudRate", description = "baudrate", validateWith = BaudRateValidator.class)
  public BaudRate baudRate = BaudRate.bs115200;

  @Parameter(names = "--convenionalNotation", description = "convenionalNotation", validateWith = ConventionalNotationValidator.class)
  public ConventionalNotation conventionalNotation = ConventionalNotation._8N1;

  @Parameter(names = "--queueSize", description = "queue size for the message received", validateWith = ConventionalNotationValidator.class)
  public int queueSize = 10000;

  @Parameter(names = "--endpointWrite", description = "internal channel to write the data to the serial port")
  private String endpointWrite = null;

  @Parameter(names = "--endpointRead", description = "internal channel to send data from serial port")
  private String endpointRead = null;

  @Parameter(names = "--endpointReadByte", description = "internal channel to send data from serial port in byte")
  private String endpointReadByte = null;

  @Parameter(names = "--endpointWriteByte", description = "internal channel to write the data to the serial port in byte")
  private String endpointWriteByte = null;

  @Override
  public Ar4kComponent instantiate() {
    SerialService ss = new SerialService();
    ss.setConfiguration(this);
    return (Ar4kComponent) ss;
  }

  @Override
  public int getPriority() {
    return 4;
  }

  @Override
  public boolean isSpringBean() {
    return false;
  }

  public String getSerial() {
    return serial;
  }

  public void setSerial(String serial) {
    this.serial = serial;
  }

  public BaudRate getBaudRate() {
    return baudRate;
  }

  public void setBaudRate(BaudRate baudRate) {
    this.baudRate = baudRate;
  }

  public ConventionalNotation getConventionalNotation() {
    return conventionalNotation;
  }

  public void setConventionalNotation(ConventionalNotation conventionalNotation) {
    this.conventionalNotation = conventionalNotation;
  }

  public int getQueueSize() {
    return queueSize;
  }

  public void setQueueSize(int queueSize) {
    this.queueSize = queueSize;
  }

  public String getEndpointWrite() {
    return endpointWrite != null ? endpointWrite : serial.replace("/", "_") + "_endpointWrite";
  }

  public void setEndpointWrite(String endpointWrite) {
    this.endpointWrite = endpointWrite;
  }

  public String getEndpointRead() {
    return endpointRead != null ? endpointRead : serial.replace("/", "_") + "_endpointRead";
  }

  public void setEndpointRead(String endpointRead) {
    this.endpointRead = endpointRead;
  }

  public String getEndpointReadByte() {
    return endpointReadByte != null ? endpointReadByte : serial.replace("/", "_") + "_endpointReadByte";
  }

  public void setEndpointReadByte(String endpointReadByte) {
    this.endpointReadByte = endpointReadByte;
  }

  public String getEndpointWriteByte() {
    return endpointWriteByte != null ? endpointWriteByte : serial.replace("/", "_") + "_endpointWriteByte";
  }

  public void setEndpointWriteByte(String endpointWriteByte) {
    this.endpointWriteByte = endpointWriteByte;
  }

}
