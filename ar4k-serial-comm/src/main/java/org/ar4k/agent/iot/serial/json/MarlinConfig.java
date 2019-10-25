/**
 *
 */
package org.ar4k.agent.iot.serial.json;

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

  // TODO: mettere a posto le descrizioni
  // input commands
  @Parameter(names = "--camelEndpointAutoHome", description = "URI for consumer AutoHome")
  public String camelEndpointAutoHome = "paho:3dprinter/control/home?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointBackward", description = "URI for consumer AutoHome")
  public String camelEndpointBackward = "paho:3dprinter/control/backward?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointForward", description = "URI for consumer AutoHome")
  public String camelEndpointForward = "paho:3dprinter/control/forward?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointLeft", description = "URI for consumer AutoHome")
  public String camelEndpointLeft = "paho:3dprinter/control/left?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointRigth", description = "URI for consumer AutoHome")
  public String camelEndpointRigth = "paho:3dprinter/control/right?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointUp", description = "URI for consumer AutoHome")
  public String camelEndpointUp = "paho:3dprinter/control/up?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointDown", description = "URI for consumer AutoHome")
  public String camelEndpointDown = "paho:3dprinter/control/down?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointSpeed", description = "URI for consumer AutoHome")
  public String camelEndpointSpeed = "paho:3dprinter/control/speed?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointFlowRate", description = "URI for consumer AutoHome")
  public String camelEndpointFlowRate = "paho:3dprinter/control/flowrate?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointFeedRate", description = "URI for consumer AutoHome")
  public String camelEndpointFeedRate = "paho:3dprinter/control/feedrate?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointMoveTo", description = "URI for consumer AutoHome")
  public String camelEndpointMoveTo = "paho:3dprinter/control/move?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointFan", description = "URI for consumer AutoHome")
  public String camelEndpointFan = "paho:3dprinter/control/fan?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointEstruderTemp", description = "URI for consumer AutoHome")
  public String camelEndpointEstruderTemp = "paho:3dprinter/control/etemp?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointBedTemp", description = "URI for consumer AutoHome")
  public String camelEndpointBedTemp = "paho:3dprinter/control/btemp?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointExtrude", description = "URI for consumer AutoHome")
  public String camelEndpointExtrude = "paho:3dprinter/control/extrude?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointDisableMotors", description = "URI for consumer AutoHome")
  public String camelEndpointDisableMotors = "paho:3dprinter/control/motoroff?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointEnableMotors", description = "URI for consumer AutoHome")
  public String camelEndpointEnableMotors = "paho:3dprinter/control/motoron?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointEmergency", description = "URI for consumer AutoHome")
  public String camelEndpointEmergency = "paho:3dprinter/control/emergency?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointReset", description = "URI for consumer AutoHome")
  public String camelEndpointReset = "paho:3dprinter/control/reset?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";

  // output trigger
  @Parameter(names = "--camelEndpointOutTemp", description = "URI for consumer AutoHome")
  public String camelEndpointOutTemp = "paho:3dprinter/status/temperature?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointOutAbsolutePosition", description = "URI for consumer AutoHome")
  public String camelEndpointOutAbsolutePosition = "paho:3dprinter/status/position/absolute?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointOutFirmware", description = "URI for consumer AutoHome")
  public String camelEndpointOutFirmware = "paho:3dprinter/status/firmware?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointOutSetting", description = "URI for consumer AutoHome")
  public String camelEndpointOutSetting = "paho:3dprinter/status/settings?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointProgress", description = "URI for consumer AutoHome")
  public String camelEndpointProgress = "paho:3dprinter/status/progress?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointListSdFiles", description = "URI for consumer AutoHome")
  public String camelEndpointListSdFiles = "paho:3dprinter/status/listfiles?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";
  @Parameter(names = "--camelEndpointPrintTime", description = "URI for consumer AutoHome")
  public String camelEndpointPrintTime = "paho:3dprinter/status/printtime?brokerUrl=tcp://192.168.1.106:18883&connectOptions=#mqttConnectionOptions&qos=0";

  public MarlinConfig() {
    // temperatura x
    TriggerCommand temperatura = new TriggerCommand();
    RouterMessagesCnc temperaturaFind = new RouterMessagesCnc();
    temperaturaFind.regExp = "T:.*T0";
    temperaturaFind.camelEndpoint = camelEndpointOutTemp;
    temperatura.timer = 31;
    temperatura.command = "M105\n";

    // posizione assoluta x
    TriggerCommand posizioneAssoluta = new TriggerCommand();
    RouterMessagesCnc posizioneAssolutaFind = new RouterMessagesCnc();
    posizioneAssolutaFind.regExp = "X:.*Y:.*Z:.*E:.*Count X:.*Y:.*Z:";
    posizioneAssolutaFind.camelEndpoint = camelEndpointOutAbsolutePosition;
    posizioneAssoluta.timer = 11;
    posizioneAssoluta.command = "M114\n";

    // firmware già richiesto da CNC
    RouterMessagesCnc firmwareFind = new RouterMessagesCnc();
    firmwareFind.regExp = "FIRMWARE_NAME:.*PROTOCOL_VERSION.*MACHINE_TYPE";
    firmwareFind.camelEndpoint = camelEndpointOutFirmware;

    // configurazione x
    TriggerCommand configurazione = new TriggerCommand();
    RouterMessagesCnc configurazioneFind = new RouterMessagesCnc();
    configurazioneFind.regExp = "echo:Advanced variables";
    configurazioneFind.camelEndpoint = camelEndpointOutSetting;
    configurazione.timer = 118;
    configurazione.command = "M503\n";

    // avanzamento lavoro x
    TriggerCommand avanzamento = new TriggerCommand();
    RouterMessagesCnc avanzamentoFind = new RouterMessagesCnc();
    avanzamentoFind.regExp = "SD printing byte";
    avanzamentoFind.camelEndpoint = camelEndpointProgress;
    avanzamento.timer = 41;
    avanzamento.command = "M27\n";

    // lista fil su SD x
    TriggerCommand listaFileSd = new TriggerCommand();
    RouterMessagesCnc listaFileSdFind = new RouterMessagesCnc();
    listaFileSdFind.regExp = "Begin file list";
    listaFileSdFind.camelEndpoint = camelEndpointListSdFiles;
    listaFileSd.timer = 210;
    listaFileSd.command = "M20\n";

    // print time x
    TriggerCommand printTime = new TriggerCommand();
    RouterMessagesCnc printTimeFind = new RouterMessagesCnc();
    printTimeFind.regExp = "echo:.*min,.*sec";
    printTimeFind.camelEndpoint = camelEndpointPrintTime;
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
