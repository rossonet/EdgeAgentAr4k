package org.ar4k.agent.farm;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ar4k.agent.core.interfaces.ManagedArchives;
import org.json.JSONObject;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public interface ManagedVirtualApplication {

	public enum SystemStatus {
		DESIDERATA, DEPLOY, RUNNING, STOP, REMOVED
	}

	public static List<String> getSupportedRecipes(String packageBaseSearch) {
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AnnotationTypeFilter(EdgeContainerizedVirtualApplication.class));
		Set<BeanDefinition> classes = provider.findCandidateComponents(packageBaseSearch);
		final List<String> rit = new ArrayList<>();
		for (BeanDefinition c : classes) {
			final String classTarget = c.getBeanClassName();
			rit.add(classTarget);
		}
		return rit;
	}

	public void setAutoStart(boolean isAutostartEnable);

	public boolean isAutostartEnabled();

	public Map<String, ManagedArchives> getArchives();

	public SystemStatus getStatus();

	public SystemStatus start();

	public SystemStatus stop();

	public SystemStatus remove();

	public JSONObject getJSONStatus();

	public String getLog();

	public Path getLogPath();

}
