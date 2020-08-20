package org.ar4k.agent.cortex.drools;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.Ar4kComponent;
import org.ar4k.agent.core.data.DataAddress;
import org.ar4k.agent.cortex.annotation.Ar4kDroolsContext;
import org.ar4k.agent.cortex.annotation.DroolsGlobalClass;
import org.ar4k.agent.cortex.drools.data.GlobalDataInterface;
import org.ar4k.agent.cortex.drools.internals.GlobalLoggerUtils;
import org.ar4k.agent.exception.ServiceWatchDogException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.json.JSONObject;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         AI Drools
 */
public class DroolsService implements Ar4kComponent {

	// TODO aggiungere global data anima se flag true

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(DroolsService.class.toString());

	@Override
	public String toString() {
		return "DroolsService [configuration=" + configuration + ", kieContainer=" + kieContainer + ", kieServices="
				+ kieServices + ", kieRepository=" + kieRepository + ", allResources=" + allResources + ", kieModule="
				+ kieModule + ", kieSession=" + kieSession + ", kieStatelessSession=" + kieStatelessSession + ", anima="
				+ anima + ", dataAddress=" + dataAddress + "]";
	}

	public enum DroolsServiceUpdateAction {
		FIRE_UNTIL_HALT, FIRE_ALL_RULES, NONE
	}

	private DroolsConfig configuration = null;
	private KieContainer kieContainer = null;
	private KieServices kieServices = null;
	private KieRepository kieRepository = null;
	private final List<Resource> allResources = new ArrayList<>();
	private KieModule kieModule = null;
	private KieSession kieSession = null;
	private StatelessKieSession kieStatelessSession = null;
	private Anima anima = null;
	private DataAddress dataAddress = null;

	public boolean isStateless() {
		return configuration.isStateless();
	}

	public FactHandle insertOrExecute(Object newObject, boolean fireAllRules) {
		if (isStateless()) {
			getKieStalessSession(configuration.getSessionName()).execute(newObject);
			return null;
		} else {
			final FactHandle r = getKieSession(configuration.getSessionName()).insert(newObject);
			if (fireAllRules) {
				getKieSession(configuration.getSessionName()).fireAllRules();
			}
			return r;
		}
	}

	public void delete(FactHandle factHandle, boolean fireAllRules) {
		if (isStateless()) {
			throw new IllegalArgumentException("you can not delete fact from stateless session");
		} else {
			getKieSession(configuration.getSessionName()).delete(factHandle);
			if (fireAllRules) {
				getKieSession(configuration.getSessionName()).fireAllRules();
			}
		}
	}

	public void fireUntilHalt() {
		getKieSession(configuration.getSessionName()).fireUntilHalt();
	}

	public int fireAllRules() {
		return getKieSession(configuration.getSessionName()).fireAllRules();
	}

	public int fireAllRules(AgendaFilter max) {
		return getKieSession(configuration.getSessionName()).fireAllRules(max);
	}

	public void fireUntilHalt(AgendaFilter agendaFilter) {
		getKieSession(configuration.getSessionName()).fireUntilHalt(agendaFilter);
	}

	private KieSession getKieSession(String sessionName) {
		if (kieSession == null) {
			kieSession = getKieContainer().newKieSession();
		}
		return kieSession;
	}

	private StatelessKieSession getKieStalessSession(String sessionName) {
		if (kieStatelessSession == null) {
			kieStatelessSession = getKieContainer().newStatelessKieSession();
		}
		return kieStatelessSession;
	}

	private KieServices getKieService() {
		if (kieServices == null) {
			kieServices = KieServices.Factory.get();
			final KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
			for (final String file : configuration.srcPathRules) {
				kieFileSystem.write(ResourceFactory.newClassPathResource(file, "UTF-8"));
			}
		}
		return kieServices;
	}

	private KieContainer getKieContainer() {
		if (kieContainer == null) {
			kieContainer = getKieService().newKieContainer(getKieModule().getReleaseId());
		}
		return kieContainer;
	}

	private KieModule getKieModule() {
		if (kieModule == null) {
			final KieBuilder kieBuilder = getKieService().newKieBuilder(new File(configuration.getRootFolder()));
			if (allResources.isEmpty()) {
				kieModule = kieBuilder.getKieModule();
			} else {
				for (final Resource m : allResources) {
					kieModule = getKieRepository().addKieModule(m);
				}
			}
		}
		return kieModule;
	}

	private KieRepository getKieRepository() {
		if (kieRepository == null) {
			kieRepository = getKieService().getRepository();
		}
		return kieRepository;
	}

	@Override
	public void kill() {
		deregisterBean();
		if (kieSession != null) {
			kieSession.dispose();
		}
		if (kieContainer != null) {
			kieContainer.dispose();
		}
		kieContainer = null;
		kieServices = null;
		kieRepository = null;
		allResources.clear();
		kieModule = null;
		kieSession = null;
		kieStatelessSession = null;
	}

	@Override
	public void init() {
		for (final String urlModule : configuration.getUrlModules()) {
			addUrlModule(urlModule);
		}
		for (final String fileModule : configuration.getFileModules()) {
			addFileModule(fileModule);
		}
		for (final String stringModule : configuration.getStringModules()) {
			addStringModule(stringModule);
		}
		logger.info("starting drools");
		if (isStateless()) {
			getKieStalessSession(configuration.getSessionName());
		} else {
			getKieSession(configuration.getSessionName());
		}
		logger.info("searching for class by annotation");
		popolateClassObjects();
		popolateNlp();
		logger.info("popolate global data");
		popolateGlobalData();
		logger.info("register bean");
		registerBean();
		logger.info("Drools service " + configuration.getSessionName() + " started");
	}

	private void addUrlModule(String pathResource) {
		allResources.add(getKieService().getResources().newUrlResource(pathResource));
	}

	private void addFileModule(String fileName) {
		allResources.add(getKieService().getResources().newFileSystemResource(fileName));
	}

	private void addStringModule(String stringResource) {
		final Reader reader = new StringReader(stringResource);
		allResources.add(getKieService().getResources().newReaderResource(reader));
	}

	private void registerBean() {
		try {
			if (Anima.getApplicationContext() != null)
				((ConfigurableApplicationContext) Anima.getApplicationContext()).getBeanFactory()
						.registerSingleton(configuration.beanName, this);
			else
				logger.warn("Starting without Spring context");
		} catch (final Exception aa) {
			logger.logException(aa);
		}
	}

	private void deregisterBean() {
		try {
			if (Anima.getApplicationContext() != null)
				((ConfigurableApplicationContext) Anima.getApplicationContext()).getBeanFactory().destroyBean(this);
			else
				logger.warn("Starting without Spring context");
		} catch (final Exception aa) {
			logger.logException(aa);
		}
	}

	private void popolateNlp() {
		if (configuration.isOpenNlpEnable()) {
			logger.info("starting NLP");
			// TODO inserire ogetti per NLP
		}
	}

	private void popolateClassObjects() {
		for (final Object singleDroolsGlobalClass : listDroolsContextObjects(configuration.getBasePath())) {
			if (singleDroolsGlobalClass instanceof DroolsGlobalClass) {
				try {
					for (final Entry<String, Object> singleObject : ((DroolsGlobalClass) singleDroolsGlobalClass)
							.getAllObjects().entrySet())
						if (isStateless()) {
							getKieStalessSession(configuration.getSessionName()).setGlobal(singleObject.getKey(),
									singleObject.getValue());
						} else {
							getKieSession(configuration.getSessionName()).setGlobal(singleObject.getKey(),
									singleObject.getValue());
						}
				} catch (final Exception e) {
					logger.logException(e);
				}
			}
		}
	}

	private Set<Object> listDroolsContextObjects(String packageName) {
		final Set<Object> rit = new HashSet<>();
		final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
				false);
		provider.addIncludeFilter(new AnnotationTypeFilter(Ar4kDroolsContext.class));
		final Set<BeanDefinition> classes = provider.findCandidateComponents(packageName);
		for (final BeanDefinition c : classes) {
			try {
				final Class<?> classe = Class.forName(c.getBeanClassName());
				final Constructor<?> con = classe.getConstructor();
				rit.add(con.newInstance());
			} catch (final Exception e) {
				logger.logException(e);
			}
		}
		return rit;
	}

	private void popolateGlobalData() {
		if (isStateless()) {
			getKieStalessSession(configuration.getSessionName()).setGlobal("logger",
					GlobalLoggerUtils.getStaticInstance());
			getKieStalessSession(configuration.getSessionName()).setGlobal("utils",
					GlobalDataInterface.getStaticInstance());
		} else {
			getKieSession(configuration.getSessionName()).setGlobal("logger", GlobalLoggerUtils.getStaticInstance());
			getKieSession(configuration.getSessionName()).setGlobal("utils", GlobalDataInterface.getStaticInstance());
		}
		for (final Entry<String, String> singleData : configuration.getGlobalData().entrySet()) {
			if (isStateless()) {
				getKieStalessSession(configuration.getSessionName()).setGlobal(singleData.getKey(),
						singleData.getValue());
			} else {
				getKieSession(configuration.getSessionName()).setGlobal(singleData.getKey(), singleData.getValue());
			}
		}
	}

	@Override
	public void close() throws Exception {
		kill();
	}

	@Override
	public ServiceStatus updateAndGetStatus() throws ServiceWatchDogException {
		try {
			switch (configuration.getUpdateAction()) {
			case FIRE_ALL_RULES:
				fireAllRules();
				break;
			case FIRE_UNTIL_HALT:
				fireUntilHalt();
				break;
			case NONE:
				// nothing to do
				break;
			}
		} catch (final Exception aa) {
			logger.logException("WHEN " + configuration.getUpdateAction(), aa);
		}
		return null;
	}

	@Override
	public Anima getAnima() {
		return anima;
	}

	@Override
	public DataAddress getDataAddress() {
		return dataAddress;
	}

	@Override
	public void setDataAddress(DataAddress dataAddress) {
		this.dataAddress = dataAddress;
	}

	@Override
	public void setAnima(Anima anima) {
		this.anima = anima;
	}

	@Override
	public ServiceConfig getConfiguration() {
		return configuration;
	}

	@Override
	public void setConfiguration(ServiceConfig configuration) {
		this.configuration = (DroolsConfig) configuration;
	}

	@Override
	public JSONObject getDescriptionJson() {
		// TODO Auto-generated method getDescriptionJson
		return null;
	}

}
