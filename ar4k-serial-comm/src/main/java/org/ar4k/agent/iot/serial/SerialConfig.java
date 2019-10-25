package org.ar4k.agent.iot.serial;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.config.ConfigSeed;
import org.ar4k.agent.core.data.Ar4kChannel;
import org.ar4k.agent.core.data.channels.IDirectChannel;
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
  public Ar4kChannel endpointWrite = null;

  @Parameter(names = "--endpointRead", description = "internal channel to send data from serial port")
  public Ar4kChannel endpointRead = null;

  @Parameter(names = "--endpointReadByte", description = "internal channel to send data from serial port in byte")
  public Ar4kChannel endpointReadByte = null;

  @Parameter(names = "--endpointWriteByte", description = "internal channel to write the data to the serial port in byte")
  public Ar4kChannel endpointWriteByte = null;

  @Parameter(names = "--endpointMetaSerial", description = "internal channel for manage the serial with AT commands")
  public Ar4kChannel endpointMetaSerial = null;

  @Parameter(names = "--internalDirectoryChannel", description = "internal directory for multi node bind")
  public IDirectChannel bindDirectoryChannel = null;

  @Override
  public SerialService instantiate() {
    SerialService ss = new SerialService();
    ss.setConfiguration(this);
    return ss;
  }

  @Override
  public int getPriority() {
    return 3;
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
