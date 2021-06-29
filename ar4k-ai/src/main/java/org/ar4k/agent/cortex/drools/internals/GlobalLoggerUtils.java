package org.ar4k.agent.cortex.drools.internals;

import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;

public class GlobalLoggerUtils {

	private GlobalLoggerUtils() {
		// usare getStaticInstance()
	}

	private static final GlobalLoggerUtils staticInstance = new GlobalLoggerUtils();

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(GlobalLoggerUtils.class);

	public void writeInfoLog(String logLine) {
		logger.info(logLine);
	}

	public void writeDebugLog(String logLine) {
		logger.debug(logLine);
	}

	public void writeWarnLog(String logLine) {
		logger.warn(logLine);
	}

	public void writeErrorLog(String logLine) {
		logger.error(logLine);
	}

	public static GlobalLoggerUtils getStaticInstance() {
		return staticInstance;
	}

}
