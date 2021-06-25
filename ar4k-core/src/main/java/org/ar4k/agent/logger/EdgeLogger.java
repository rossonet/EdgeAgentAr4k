/*
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    */
package org.ar4k.agent.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.data.messages.LoggerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Logger
 *
 * @author Andrea Ambrosini Rossonet s.c.a r.l.
 *
 */

public class EdgeLogger implements Logger {

	private static final String ERROR_LABEL = "error";

	private static final String LEVEL_LABEL = "level";

	private static final String EXCEPTION_LABEL = "exception";

	private static final String MESSAGE_LABEL = "msg";

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	private Logger logger;

	private Homunculus homunculus = null;

	public EdgeLogger(Class<?> clazz) {
		logger = LoggerFactory.getLogger(clazz);
	}

	public EdgeLogger(String label) {
		logger = LoggerFactory.getLogger(label);
	}

	public enum LogLevel {
		EXCEPTION, TRACE, DEBUG, INFO, WARN, ERROR, NONE
	}

	private static LogLevel level = LogLevel.INFO;

	public static synchronized LogLevel getLevel() {
		return level;
	}

	public static synchronized void setLevel(LogLevel level) {
		EdgeLogger.level = level;
	}

	public static String stackTraceToString(Throwable e) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String response = null;
		if (e.getCause() != null && e.getCause().getMessage() != null) {
			response = " [M] " + e.getCause().getMessage() + " -> " + sw.toString();
		} else {
			response = " [M] " + sw.toString();
		}
		return response;
	}

	public static String stackTraceToString(Throwable a, int numLines) {
		try {
			final List<String> lines = Arrays.asList(stackTraceToString(a).split("\n"));
			final ArrayList<String> al = new ArrayList<>(lines.subList(0, Math.min(lines.size(), numLines)));
			final StringBuilder returnString = new StringBuilder();
			for (final String line : al) {
				returnString.append(line + "\n");
			}
			return returnString.toString();
		} catch (final Exception n) {
			return stackTraceToString(a);
		}

	}

	public void logException(Throwable e) {
		final Map<String, Object> o = new HashMap<>();
		o.put(MESSAGE_LABEL, e.getMessage());
		o.put(EXCEPTION_LABEL, stackTraceToString(e));
		o.put(LEVEL_LABEL, LogLevel.EXCEPTION.name());
		logger.info("Exception -> {}", stackTraceToString(e));
		sendEvent(LogLevel.EXCEPTION, o);
	}

	public void logExceptionDebug(Throwable e) {
		final Map<String, Object> o = new HashMap<>();
		o.put(MESSAGE_LABEL, e.getMessage());
		o.put(EXCEPTION_LABEL, stackTraceToString(e));
		o.put(LEVEL_LABEL, LogLevel.DEBUG.name());
		logger.debug("Exception -> {}", stackTraceToString(e));
		sendEvent(LogLevel.DEBUG, o);
	}

	public void logException(String error, Throwable e) {
		final Map<String, Object> o = new HashMap<>();
		o.put(MESSAGE_LABEL, e.getMessage());
		o.put(ERROR_LABEL, error);
		o.put(EXCEPTION_LABEL, stackTraceToString(e));
		o.put(LEVEL_LABEL, LogLevel.EXCEPTION.name());
		logger.info("{} -> {}", error, stackTraceToString(e));
		sendEvent(LogLevel.EXCEPTION, o);
	}

	private void sendEvent(LogLevel level, String logMessage) {
		final Map<String, Object> o = new HashMap<>();
		o.put(MESSAGE_LABEL, logMessage);
		o.put(LEVEL_LABEL, level.toString());
		sendEvent(level, o);
	}

	private void sendEventObject(LogLevel level, String format, Object arg) {
		String message = String.format(format, arg);
		sendEvent(level, message);
	}

	private void sendEventTwoObjects(LogLevel level, String format, Object arg1, Object arg2) {
		String message = String.format(format, arg1, arg2);
		sendEvent(level, message);
	}

	private void sendEventException(LogLevel level, String msg, Throwable t) {
		final Map<String, Object> o = new HashMap<>();
		o.put(MESSAGE_LABEL, t.getMessage());
		o.put(ERROR_LABEL, msg);
		o.put(EXCEPTION_LABEL, stackTraceToString(t));
		o.put(LEVEL_LABEL, LogLevel.EXCEPTION.name());
		sendEvent(level, o);
	}

	private void sendEventObjects(LogLevel level, String format, Object... args) {
		String message = String.format(format, args);
		sendEvent(level, message);

	}

	private void sendEvent(LogLevel level, Map<String, Object> logMessage) {
		try {
			if (homunculus == null && Homunculus.getApplicationContext() != null
					&& Homunculus.getApplicationContext().getBean(Homunculus.class).getDataAddress() != null) {
				homunculus = Homunculus.getApplicationContext().getBean(Homunculus.class);
			}
			if (homunculus != null) {
				final LoggerMessage messageObject = new LoggerMessage();
				messageObject.setPayload(gson.toJson(logMessage));
				((IPublishSubscribeChannel) homunculus.getDataAddress().getChannel("logger")).send(messageObject);
			}
		} catch (final Exception aa) {
			if (!level.equals(LogLevel.DEBUG))
				logger.debug("SEND LOG MESSAGE EXCEPTION [" + level + "] -> " + logMessage.toString());
		}
	}

	@Override
	public String getName() {
		return logger.getName();
	}

	@Override
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	@Override
	public void trace(String msg) {
		logger.trace(msg);
		sendEvent(LogLevel.TRACE, msg);
	}

	@Override
	public void trace(String format, Object arg) {
		logger.trace(format, arg);
		sendEventObject(LogLevel.TRACE, format, arg);
	}

	@Override
	public void trace(String format, Object arg1, Object arg2) {
		logger.trace(format, arg1, arg2);
		sendEventTwoObjects(LogLevel.TRACE, format, arg1, arg2);
	}

	@Override
	public void trace(String format, Object... arguments) {
		logger.trace(format, arguments);
		sendEventObjects(LogLevel.TRACE, format, arguments);
	}

	@Override
	public void trace(String msg, Throwable t) {
		logger.trace(msg, t);
		sendEventException(LogLevel.TRACE, msg, t);
	}

	@Override
	public boolean isTraceEnabled(Marker marker) {
		return logger.isTraceEnabled(marker);
	}

	@Override
	public void trace(Marker marker, String msg) {
		logger.trace(marker, msg);
		sendEvent(LogLevel.TRACE, msg);
	}

	@Override
	public void trace(Marker marker, String format, Object arg) {
		logger.trace(marker, format, arg);
		sendEventObject(LogLevel.TRACE, format, arg);
	}

	@Override
	public void trace(Marker marker, String format, Object arg1, Object arg2) {
		logger.trace(marker, format, arg1, arg2);
		sendEventTwoObjects(LogLevel.TRACE, format, arg1, arg2);
	}

	@Override
	public void trace(Marker marker, String format, Object... argArray) {
		logger.trace(marker, format, argArray);
		sendEventObjects(LogLevel.TRACE, format, argArray);
	}

	@Override
	public void trace(Marker marker, String msg, Throwable t) {
		logger.trace(marker, msg, t);
		sendEventException(LogLevel.TRACE, msg, t);
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(String msg) {
		logger.debug(msg);
		sendEvent(LogLevel.DEBUG, msg);
	}

	@Override
	public void debug(String format, Object arg) {
		logger.debug(format, arg);
		sendEventObject(LogLevel.DEBUG, format, arg);
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		logger.debug(format, arg1, arg2);
		sendEventTwoObjects(LogLevel.DEBUG, format, arg1, arg2);
	}

	@Override
	public void debug(String format, Object... arguments) {
		logger.debug(format, arguments);
		sendEventObjects(LogLevel.DEBUG, format, arguments);
	}

	@Override
	public void debug(String msg, Throwable t) {
		logger.debug(msg, t);
		sendEventException(LogLevel.DEBUG, msg, t);
	}

	@Override
	public boolean isDebugEnabled(Marker marker) {
		return logger.isDebugEnabled(marker);
	}

	@Override
	public void debug(Marker marker, String msg) {
		logger.debug(marker, msg);
		sendEvent(LogLevel.DEBUG, msg);
	}

	@Override
	public void debug(Marker marker, String format, Object arg) {
		logger.debug(marker, format, arg);
		sendEventObject(LogLevel.DEBUG, format, arg);
	}

	@Override
	public void debug(Marker marker, String format, Object arg1, Object arg2) {
		logger.debug(marker, format, arg1, arg2);
		sendEventTwoObjects(LogLevel.DEBUG, format, arg1, arg2);
	}

	@Override
	public void debug(Marker marker, String format, Object... arguments) {
		logger.debug(marker, format, arguments);
		sendEventObjects(LogLevel.DEBUG, format, arguments);
	}

	@Override
	public void debug(Marker marker, String msg, Throwable t) {
		logger.debug(marker, msg, t);
		sendEventException(LogLevel.DEBUG, msg, t);
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public void info(String msg) {
		logger.info(msg);
		sendEvent(LogLevel.INFO, msg);
	}

	@Override
	public void info(String format, Object arg) {
		logger.info(format, arg);
		sendEventObject(LogLevel.INFO, format, arg);
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		logger.info(format, arg1, arg2);
		sendEventTwoObjects(LogLevel.INFO, format, arg1, arg2);
	}

	@Override
	public void info(String format, Object... arguments) {
		logger.info(format, arguments);
		sendEventObjects(LogLevel.INFO, format, arguments);
	}

	@Override
	public void info(String msg, Throwable t) {
		logger.info(msg, t);
		sendEventException(LogLevel.INFO, msg, t);
	}

	@Override
	public boolean isInfoEnabled(Marker marker) {
		return logger.isInfoEnabled(marker);
	}

	@Override
	public void info(Marker marker, String msg) {
		logger.info(marker, msg);
		sendEvent(LogLevel.INFO, msg);
	}

	@Override
	public void info(Marker marker, String format, Object arg) {
		logger.info(marker, format, arg);
		sendEventObject(LogLevel.INFO, format, arg);
	}

	@Override
	public void info(Marker marker, String format, Object arg1, Object arg2) {
		logger.info(marker, format, arg1, arg2);
		sendEventTwoObjects(LogLevel.INFO, format, arg1, arg2);
	}

	@Override
	public void info(Marker marker, String format, Object... arguments) {
		logger.info(marker, format, arguments);
		sendEventObjects(LogLevel.INFO, format, arguments);
	}

	@Override
	public void info(Marker marker, String msg, Throwable t) {
		logger.info(marker, msg, t);
		sendEventException(LogLevel.INFO, msg, t);
	}

	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public void warn(String msg) {
		logger.warn(msg);
		sendEvent(LogLevel.WARN, msg);
	}

	@Override
	public void warn(String format, Object arg) {
		logger.warn(format, arg);
		sendEventObject(LogLevel.WARN, format, arg);
	}

	@Override
	public void warn(String format, Object... arguments) {
		logger.warn(format, arguments);
		sendEventObjects(LogLevel.WARN, format, arguments);
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		logger.warn(format, arg1, arg2);
		sendEventTwoObjects(LogLevel.WARN, format, arg1, arg2);
	}

	@Override
	public void warn(String msg, Throwable t) {
		logger.warn(msg, t);
		sendEventException(LogLevel.WARN, msg, t);
	}

	@Override
	public boolean isWarnEnabled(Marker marker) {
		return logger.isWarnEnabled(marker);
	}

	@Override
	public void warn(Marker marker, String msg) {
		logger.warn(marker, msg);
		sendEvent(LogLevel.WARN, msg);
	}

	@Override
	public void warn(Marker marker, String format, Object arg) {
		logger.warn(marker, format, arg);
		sendEventObject(LogLevel.WARN, format, arg);
	}

	@Override
	public void warn(Marker marker, String format, Object arg1, Object arg2) {
		logger.warn(marker, format, arg1, arg2);
		sendEventTwoObjects(LogLevel.WARN, format, arg1, arg2);
	}

	@Override
	public void warn(Marker marker, String format, Object... arguments) {
		logger.warn(marker, format, arguments);
		sendEventObjects(LogLevel.WARN, format, arguments);
	}

	@Override
	public void warn(Marker marker, String msg, Throwable t) {
		logger.warn(marker, msg, t);
		sendEventException(LogLevel.WARN, msg, t);
	}

	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public void error(String msg) {
		logger.error(msg);
		sendEvent(LogLevel.ERROR, msg);
	}

	@Override
	public void error(String format, Object arg) {
		logger.error(format, arg);
		sendEventObject(LogLevel.ERROR, format, arg);
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		logger.error(format, arg1, arg2);
		sendEventTwoObjects(LogLevel.ERROR, format, arg1, arg2);
	}

	@Override
	public void error(String format, Object... arguments) {
		logger.error(format, arguments);
		sendEventObjects(LogLevel.ERROR, format, arguments);
	}

	@Override
	public void error(String msg, Throwable t) {
		logger.error(msg, t);
		sendEventException(LogLevel.ERROR, msg, t);
	}

	@Override
	public boolean isErrorEnabled(Marker marker) {
		return logger.isErrorEnabled(marker);
	}

	@Override
	public void error(Marker marker, String msg) {
		logger.error(marker, msg);
		sendEvent(LogLevel.ERROR, msg);
	}

	@Override
	public void error(Marker marker, String format, Object arg) {
		logger.error(marker, format, arg);
		sendEventObject(LogLevel.ERROR, format, arg);
	}

	@Override
	public void error(Marker marker, String format, Object arg1, Object arg2) {
		logger.error(marker, format, arg1, arg2);
		sendEventTwoObjects(LogLevel.ERROR, format, arg1, arg2);
	}

	@Override
	public void error(Marker marker, String format, Object... arguments) {
		logger.error(marker, format, arguments);
		sendEventObjects(LogLevel.ERROR, format, arguments);
	}

	@Override
	public void error(Marker marker, String msg, Throwable t) {
		logger.error(marker, msg, t);
		sendEventException(LogLevel.ERROR, msg, t);
	}

}
