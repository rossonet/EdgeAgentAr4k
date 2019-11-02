package org.ar4k.agent.iot.serial;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.iot.serial.SerialService.BaudRate;
import org.ar4k.agent.iot.serial.SerialService.ConventionalNotation;

import com.beust.jcommander.Parameter;
import com.google.gson.TypeAdapter;

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

  @Parameter(names = "--queueSize", description = "Queue size for the message received", validateWith = ConventionalNotationValidator.class)
  public int queueSize = 10000;

  @Parameter(names = "--endpointWrite", description = "internal channel to write the data to the serial port")
  public String endpointWrite = null;

  @Parameter(names = "--endpointRead", description = "internal channel to send data from serial port")
  public String endpointRead = null;

  @Parameter(names = "--endpointReadByte", description = "internal channel to send data from serial port in byte")
  public String endpointReadByte = null;

  @Parameter(names = "--endpointWriteByte", description = "internal channel to write the data to the serial port in byte")
  public String endpointWriteByte = null;

  @Parameter(names = "--fatherOfChannels", description = "directory channel for message topics")
  public String fatherOfChannels = null;

  @Parameter(names = "--scopeOfChannels", description = "scope for the parent channel. If null take the default of the address space")
  public String scopeOfChannels = null;

  @Override
  public SerialService instantiate() {
    SerialService ss = new SerialService();
    ss.setConfiguration(this);
    return ss;
  }

  @Override
  public int getPriority() {
    return 4;
  }

  @Override
  public TypeAdapter<? extends ConfigSeed> getJsonTypeAdapter() {
    return new SerialConfigJsonAdapter();
  }

  @Override
  public boolean isSpringBean() {
    return true;
  }
}
