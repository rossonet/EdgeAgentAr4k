package org.ar4k.agent.cortex.drools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.core.services.EdgeComponent;
import org.ar4k.agent.cortex.drools.DroolsService.DroolsServiceUpdateAction;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Configurazione Drools collegata all'agente.
 */
public class DroolsConfig extends AbstractServiceConfig {
	//TODO completare servizio implementazioni regole Drools sui dati e comandi
	
	//TODO permettere a Drools di gestire la classificazione e regrassione con Weka
	
	//TODO permettere a Drools di gestire deepLearning4J con modelli supervisionati da configurazione, non supervizionati e rinforzo in runtime.

	private static final long serialVersionUID = -5184060109145099045L;
	@Parameter(names = "--aiName", description = "unique name for the AI")
	public String aiName = "beelzebub";
	@Parameter(names = "--beanName", description = "the beanName for the Spring registration")
	public String beanName = "beelzebub-instance";
	@Parameter(names = "--stateless", description = "is the Drools session stateless")
	public boolean stateless = false;
	@Parameter(names = "--sessionName", description = "Drools session name")
	public String sessionName = "default-session";
	@Parameter(names = "--srcPathRules", description = "Path of rules")
	public Collection<String> srcPathRules = new ArrayList<>();
	@Parameter(names = "--rootFolder", description = "Root path for kie service")
	public String rootFolder = "./";
	@Parameter(names = "--urlModules", description = "list of url modules")
	public Collection<String> urlModules = new ArrayList<>();
	@Parameter(names = "--fileModules", description = "list of file components")
	public Collection<String> fileModules = new ArrayList<>();
	@Parameter(names = "--stringModules", description = "list of string components")
	public Collection<String> stringModules = new ArrayList<>();
	@Parameter(names = "--globalData", description = "array of global datas")
	public Map<String, String> globalData = new HashMap<>();
	@Parameter(names = "--inserthomunculus", description = "insert Homunculus istance in Drools workspace (true/false)")
	public boolean insertHomunculus = false;
	@Parameter(names = "--insertDataAddress", description = "insert DataAddress in Drools workspace (true/false)")
	public boolean insertDataAddress = false;
	@Parameter(names = "--insertDataStore", description = "insert Homunculus DataStore in Drools workspace (true/false)")
	public boolean insertDataStore = false;
	@Parameter(names = "--basePath", description = "basePath for class searching")
	public String basePath = "drools";
	@Parameter(names = "--openNlpEnable", description = "enable open nlp support")
	public boolean openNlpEnable = false;
	@Parameter(names = "--openNlpEnable", description = "action called from update", validateWith = DroolsServiceUpdateActionValidator.class)
	private final DroolsServiceUpdateAction updateAction = DroolsServiceUpdateAction.FIRE_UNTIL_HALT;

	@Override
	public EdgeComponent instantiate() {
		final DroolsService ss = new DroolsService();
		ss.setConfiguration(this);
		return ss;
	}

	@Override
	public int getPriority() {
		return 19;
	}

	@Override
	public boolean isSpringBean() {
		return true;
	}

	public boolean isStateless() {
		return stateless;
	}

	public String getSessionName() {
		return sessionName;
	}

	public Collection<String> getUrlModules() {
		return urlModules;
	}

	public Collection<String> getFileModules() {
		return fileModules;
	}

	public Collection<String> getStringModules() {
		return stringModules;
	}

	public Map<String, String> getGlobalData() {
		return globalData;
	}

	public boolean insertHomunculus() {
		return insertHomunculus;
	}

	public boolean insertDataAddress() {
		return insertDataAddress;
	}

	public boolean insertDataStore() {
		return insertDataStore;
	}

	public String getBasePath() {
		return basePath;
	}

	public boolean isOpenNlpEnable() {
		return openNlpEnable;
	}

	public DroolsServiceUpdateAction getUpdateAction() {
		return updateAction;
	}

	public Collection<String> getSrcPathRules() {
		return srcPathRules;
	}

	public void setSrcPathRules(Collection<String> srcPathRules) {
		this.srcPathRules = srcPathRules;
	}

	public String getRootFolder() {
		return rootFolder;
	}

	public void setRootFolder(String rootFolder) {
		this.rootFolder = rootFolder;
	}

	@Override
	public String toString() {
		return "DroolsConfig [aiName=" + aiName + ", beanName=" + beanName + ", stateless=" + stateless
				+ ", sessionName=" + sessionName + ", urlModules=" + urlModules + ", fileModules=" + fileModules
				+ ", stringModules=" + stringModules + ", globalData=" + globalData + ", insertHomunculus="
				+ insertHomunculus + ", insertDataAddress=" + insertDataAddress + ", insertDataStore=" + insertDataStore
				+ ", basePath=" + basePath + ", openNlpEnable=" + openNlpEnable + ", updateAction="
				+ updateAction + "]";
	}

}
