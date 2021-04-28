/**
 *
 */
package org.ar4k.agent.iot.serial.marlin;

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

	@Parameter(names = "--endpointOutTemp", description = "internal channel for consumer temp data")
	public String endpointOutTemp = null;
	@Parameter(names = "--endpointOutAbsolutePosition", description = "internal channel for consumer absolute position data")
	public String endpointOutAbsolutePosition = null;
	@Parameter(names = "--endpointOutFirmware", description = "internal channel for consumer firmware data")
	public String endpointOutFirmware = null;
	@Parameter(names = "--endpointOutSetting", description = "internal channel for consumer setting data")
	public String endpointOutSetting = null;
	@Parameter(names = "--endpointProgress", description = "internal channel for consumer progress data")
	public String endpointProgress = null;
	@Parameter(names = "--endpointListSdFiles", description = "internal channel for consumer list sd file data")
	public String endpointListSdFiles = null;
	@Parameter(names = "--endpointPrintTime", description = "internal channel for consumer print time data")
	public String endpointPrintTime = null;

	@Parameter(names = "--internalDirectoryChannel", description = "internal directory for multi node bind with regex")
	public IDirectChannel bindDirectoryChannel = null;

	@Override
	public MarlinService instantiate() {
		MarlinService ss = new MarlinService();
		ss.setConfiguration(this);
		// temperatura x
		TriggerCommand temperatura = new TriggerCommand();
		RouterMessagesCnc temperaturaFind = new RouterMessagesCnc(ss);
		temperaturaFind.regExp = "T:.*T0";
		temperaturaFind.endpoint = endpointOutTemp;
		temperatura.timer = 31;
		temperatura.command = "M105\n";

		// posizione assoluta x
		TriggerCommand posizioneAssoluta = new TriggerCommand();
		RouterMessagesCnc posizioneAssolutaFind = new RouterMessagesCnc(ss);
		posizioneAssolutaFind.regExp = "X:.*Y:.*Z:.*E:.*Count X:.*Y:.*Z:";
		posizioneAssolutaFind.endpoint = endpointOutAbsolutePosition;
		posizioneAssoluta.timer = 11;
		posizioneAssoluta.command = "M114\n";

		// firmware già richiesto da CNC
		RouterMessagesCnc firmwareFind = new RouterMessagesCnc(ss);
		firmwareFind.regExp = "FIRMWARE_NAME:.*PROTOCOL_VERSION.*MACHINE_TYPE";
		firmwareFind.endpoint = endpointOutFirmware;

		// configurazione x
		TriggerCommand configurazione = new TriggerCommand();
		RouterMessagesCnc configurazioneFind = new RouterMessagesCnc(ss);
		configurazioneFind.regExp = "echo:Advanced variables";
		configurazioneFind.endpoint = endpointOutSetting;
		configurazione.timer = 118;
		configurazione.command = "M503\n";

		// avanzamento lavoro x
		TriggerCommand avanzamento = new TriggerCommand();
		RouterMessagesCnc avanzamentoFind = new RouterMessagesCnc(ss);
		avanzamentoFind.regExp = "SD printing byte";
		avanzamentoFind.endpoint = endpointProgress;
		avanzamento.timer = 41;
		avanzamento.command = "M27\n";

		// lista fil su SD x
		TriggerCommand listaFileSd = new TriggerCommand();
		RouterMessagesCnc listaFileSdFind = new RouterMessagesCnc(ss);
		listaFileSdFind.regExp = "Begin file list";
		listaFileSdFind.endpoint = endpointListSdFiles;
		listaFileSd.timer = 210;
		listaFileSd.command = "M20\n";

		// print time x
		TriggerCommand printTime = new TriggerCommand();
		RouterMessagesCnc printTimeFind = new RouterMessagesCnc(ss);
		printTimeFind.regExp = "echo:.*min,.*sec";
		printTimeFind.endpoint = endpointPrintTime;
		printTime.timer = 37;
		printTime.command = "M31\n";

		repliesAnalizer.add(temperaturaFind);
		repliesAnalizer.add(posizioneAssolutaFind);
		repliesAnalizer.add(configurazioneFind);
		repliesAnalizer.add(avanzamentoFind);
		repliesAnalizer.add(listaFileSdFind);
		repliesAnalizer.add(printTimeFind);
		repliesAnalizer.add(firmwareFind);

		cronCommands.add(temperatura);
		cronCommands.add(posizioneAssoluta);
		cronCommands.add(configurazione);
		cronCommands.add(avanzamento);
		cronCommands.add(listaFileSd);
		cronCommands.add(printTime);

		return ss;
	}

}
