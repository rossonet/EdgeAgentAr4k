/**
 *
 */
package org.ar4k.agent.iot.serial.marlin;

import org.ar4k.agent.core.data.Ar4kChannel;
import org.ar4k.agent.core.data.channels.IDirectChannel;
import org.ar4k.agent.iot.serial.cnc.CncConfig;
import org.ar4k.agent.iot.serial.cnc.RouterMessagesCnc;
import org.ar4k.agent.iot.serial.cnc.TriggerCommand;

import com.beust.jcommander.Parameter;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */
public class MarlinConfig extends CncConfig {

  private static final long serialVersionUID = -864164279161787378L;

  // TODO: completare descrizioni
  @Parameter(names = "--endpointAutoHome", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointAutoHome = null;
  @Parameter(names = "--endpointBackward", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointBackward = null;
  @Parameter(names = "--endpointForward", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointForward = null;
  @Parameter(names = "--endpointLeft", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointLeft = null;
  @Parameter(names = "--endpointRigth", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointRigth = null;
  @Parameter(names = "--endpointUp", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointUp = null;
  @Parameter(names = "--endpointDown", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointDown = null;
  @Parameter(names = "--endpointSpeed", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointSpeed = null;
  @Parameter(names = "--endpointFlowRate", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointFlowRate = null;
  @Parameter(names = "--endpointFeedRate", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointFeedRate = null;
  @Parameter(names = "--endpointMoveTo", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointMoveTo = null;
  @Parameter(names = "--endpointFan", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointFan = null;
  @Parameter(names = "--endpointEstruderTemp", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointEstruderTemp = null;
  @Parameter(names = "--endpointBedTemp", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointBedTemp = null;
  @Parameter(names = "--endpointExtrude", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointExtrude = null;
  @Parameter(names = "--endpointDisableMotors", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointDisableMotors = null;
  @Parameter(names = "--endpointEnableMotors", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointEnableMotors = null;
  @Parameter(names = "--endpointEmergency", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointEmergency = null;
  @Parameter(names = "--endpointReset", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointReset = null;

  // output trigger
  @Parameter(names = "--endpointOutTemp", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointOutTemp = null;
  @Parameter(names = "--endpointOutAbsolutePosition", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointOutAbsolutePosition = null;
  @Parameter(names = "--endpointOutFirmware", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointOutFirmware = null;
  @Parameter(names = "--endpointOutSetting", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointOutSetting = null;
  @Parameter(names = "--endpointProgress", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointProgress = null;
  @Parameter(names = "--endpointListSdFiles", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointListSdFiles = null;
  @Parameter(names = "--endpointPrintTime", description = "internal channel for consumer AutoHome")
  public Ar4kChannel endpointPrintTime = null;

  @Parameter(names = "--internalDirectoryChannel", description = "internal directory for multi node bind with regex")
  public IDirectChannel bindDirectoryChannel = null;

  public MarlinConfig() {
    // temperatura x
    TriggerCommand temperatura = new TriggerCommand();
    RouterMessagesCnc temperaturaFind = new RouterMessagesCnc();
    temperaturaFind.regExp = "T:.*T0";
    temperaturaFind.endpoint = endpointOutTemp;
    temperatura.timer = 31;
    temperatura.command = "M105\n";

    // posizione assoluta x
    TriggerCommand posizioneAssoluta = new TriggerCommand();
    RouterMessagesCnc posizioneAssolutaFind = new RouterMessagesCnc();
    posizioneAssolutaFind.regExp = "X:.*Y:.*Z:.*E:.*Count X:.*Y:.*Z:";
    posizioneAssolutaFind.endpoint = endpointOutAbsolutePosition;
    posizioneAssoluta.timer = 11;
    posizioneAssoluta.command = "M114\n";

    // firmware già richiesto da CNC
    RouterMessagesCnc firmwareFind = new RouterMessagesCnc();
    firmwareFind.regExp = "FIRMWARE_NAME:.*PROTOCOL_VERSION.*MACHINE_TYPE";
    firmwareFind.endpoint = endpointOutFirmware;

    // configurazione x
    TriggerCommand configurazione = new TriggerCommand();
    RouterMessagesCnc configurazioneFind = new RouterMessagesCnc();
    configurazioneFind.regExp = "echo:Advanced variables";
    configurazioneFind.endpoint = endpointOutSetting;
    configurazione.timer = 118;
    configurazione.command = "M503\n";

    // avanzamento lavoro x
    TriggerCommand avanzamento = new TriggerCommand();
    RouterMessagesCnc avanzamentoFind = new RouterMessagesCnc();
    avanzamentoFind.regExp = "SD printing byte";
    avanzamentoFind.endpoint = endpointProgress;
    avanzamento.timer = 41;
    avanzamento.command = "M27\n";

    // lista fil su SD x
    TriggerCommand listaFileSd = new TriggerCommand();
    RouterMessagesCnc listaFileSdFind = new RouterMessagesCnc();
    listaFileSdFind.regExp = "Begin file list";
    listaFileSdFind.endpoint = endpointListSdFiles;
    listaFileSd.timer = 210;
    listaFileSd.command = "M20\n";

    // print time x
    TriggerCommand printTime = new TriggerCommand();
    RouterMessagesCnc printTimeFind = new RouterMessagesCnc();
    printTimeFind.regExp = "echo:.*min,.*sec";
    printTimeFind.endpoint = endpointPrintTime;
    printTime.timer = 37;
    printTime.command = "M31\n";

    replies.add(temperaturaFind);
    replies.add(posizioneAssolutaFind);
    replies.add(configurazioneFind);
    replies.add(avanzamentoFind);
    replies.add(listaFileSdFind);
    replies.add(printTimeFind);
    replies.add(firmwareFind);

    cronCommands.add(temperatura);
    cronCommands.add(posizioneAssoluta);
    cronCommands.add(configurazione);
    cronCommands.add(avanzamento);
    cronCommands.add(listaFileSd);
    cronCommands.add(printTime);
  }

  @Override
  public MarlinService instantiate() {
    // System.out.println("Serial service start");
    MarlinService ss = new MarlinService();
    ss.setConfiguration(this);
    return ss;
  }

}
