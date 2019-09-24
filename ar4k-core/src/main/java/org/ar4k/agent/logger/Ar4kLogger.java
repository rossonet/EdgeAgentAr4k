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
import java.util.HashMap;
import java.util.Map;

import org.ar4k.agent.core.Anima;
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

public class Ar4kLogger implements Logger {

  private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  private Logger logger;

  private transient Anima anima = null;

  public Ar4kLogger(Class<?> clazz) {
    logger = LoggerFactory.getLogger(clazz);
  }

  public Ar4kLogger(String label) {
    logger = LoggerFactory.getLogger(label);
  }

  public static enum LogLevel {
    EXCEPTION, TRACE, DEBUG, INFO, WARN, ERROR, NONE
  }

  public static LogLevel level = LogLevel.INFO;

  private static String stackTraceToString(Throwable e) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    String response = null;
    if (e.getCause() != null && e.getCause().getMessage() != null) {
      response = " [M] " + e.getCause().getMessage() + " -> " + sw.toString();
    } else {
      response = " [M] " + sw.toString();
    }
    return response;
  }

  public void logException(Exception e) {
    Map<String, Object> o = new HashMap<String, Object>();
    o.put("msg", e.getMessage());
    o.put("exception", stackTraceToString(e));
    o.put("level", LogLevel.EXCEPTION.name());
    logger.info("Exception -> " + stackTraceToString(e));
    sendEvent(LogLevel.EXCEPTION, o);
  }

  public void logException(int errore, Exception e) {
    Map<String, Object> o = new HashMap<String, Object>();
    o.put("msg", e.getMessage());
    o.put("exception", stackTraceToString(e));
    o.put("level", LogLevel.EXCEPTION.name());
    logger.info("Exception -> " + stackTraceToString(e));
    sendEvent(LogLevel.EXCEPTION, o);
  }

  private void sendEvent(LogLevel level, String logMessage) {
    Map<String, Object> o = new HashMap<String, Object>();
    o.put("msg", logMessage);
    o.put("level", level.toString());
    sendEvent(level, o);
  }

  private void sendEvent(LogLevel level, Map<String, Object> logMessage) {
    // logger.info(gson.toJson(logMessage));
    try {
      if (anima == null && Anima.getApplicationContext() != null
          && Anima.getApplicationContext().getBean(Anima.class) != null
          && ((Anima) Anima.getApplicationContext().getBean(Anima.class)).getDataAddress() != null) {
        anima = (Anima) Anima.getApplicationContext().getBean(Anima.class);
      }
      if (anima != null) {
        LoggerMessage<String> messageObject = new LoggerMessage<>();
        messageObject.setPayload(gson.toJson(logMessage));
        anima.getDataAddress().getChannel("logger").send(messageObject);
      }
    } catch (Exception aa) {
      // gestisce il bootstrap
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
    sendEvent(LogLevel.DEBUG, msg);
  }

  @Override
  public void trace(String format, Object arg) {
    logger.trace(format, arg);
  }

  @Override
  public void trace(String format, Object arg1, Object arg2) {
    logger.trace(format, arg1, arg2);
  }

  @Override
  public void trace(String format, Object... arguments) {
    logger.trace(format, arguments);
  }

  @Override
  public void trace(String msg, Throwable t) {
    logger.trace(msg, t);
  }

  @Override
  public boolean isTraceEnabled(Marker marker) {
    return logger.isTraceEnabled(marker);
  }

  @Override
  public void trace(Marker marker, String msg) {
    logger.trace(marker, msg);
  }

  @Override
  public void trace(Marker marker, String format, Object arg) {
    logger.trace(marker, format, arg);
  }

  @Override
  public void trace(Marker marker, String format, Object arg1, Object arg2) {
    logger.trace(marker, format, arg1, arg2);
  }

  @Override
  public void trace(Marker marker, String format, Object... argArray) {
    logger.trace(marker, format, argArray);
  }

  @Override
  public void trace(Marker marker, String msg, Throwable t) {
    logger.trace(marker, msg, t);
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
  }

  @Override
  public void debug(String format, Object arg1, Object arg2) {
    logger.debug(format, arg1, arg2);
  }

  @Override
  public void debug(String format, Object... arguments) {
    logger.debug(format, arguments);
  }

  @Override
  public void debug(String msg, Throwable t) {
    logger.debug(msg, t);
  }

  @Override
  public boolean isDebugEnabled(Marker marker) {
    return logger.isDebugEnabled(marker);
  }

  @Override
  public void debug(Marker marker, String msg) {
    logger.debug(marker, msg);
  }

  @Override
  public void debug(Marker marker, String format, Object arg) {
    logger.debug(marker, format, arg);
  }

  @Override
  public void debug(Marker marker, String format, Object arg1, Object arg2) {
    logger.debug(marker, format, arg1, arg2);
  }

  @Override
  public void debug(Marker marker, String format, Object... arguments) {
    logger.debug(marker, format, arguments);
  }

  @Override
  public void debug(Marker marker, String msg, Throwable t) {
    logger.debug(marker, msg, t);
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
  }

  @Override
  public void info(String format, Object arg1, Object arg2) {
    logger.info(format, arg1, arg2);
  }

  @Override
  public void info(String format, Object... arguments) {
    logger.info(format, arguments);
  }

  @Override
  public void info(String msg, Throwable t) {
    logger.info(msg, t);
  }

  @Override
  public boolean isInfoEnabled(Marker marker) {
    return logger.isInfoEnabled(marker);
  }

  @Override
  public void info(Marker marker, String msg) {
    logger.info(marker, msg);
  }

  @Override
  public void info(Marker marker, String format, Object arg) {
    logger.info(marker, format, arg);
  }

  @Override
  public void info(Marker marker, String format, Object arg1, Object arg2) {
    logger.info(marker, format, arg1, arg2);
  }

  @Override
  public void info(Marker marker, String format, Object... arguments) {
    logger.info(marker, format, arguments);
  }

  @Override
  public void info(Marker marker, String msg, Throwable t) {
    logger.info(marker, msg, t);
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
  }

  @Override
  public void warn(String format, Object... arguments) {
    logger.warn(format, arguments);
  }

  @Override
  public void warn(String format, Object arg1, Object arg2) {
    logger.warn(format, arg1, arg2);
  }

  @Override
  public void warn(String msg, Throwable t) {
    logger.warn(msg, t);
  }

  @Override
  public boolean isWarnEnabled(Marker marker) {
    return logger.isWarnEnabled(marker);
  }

  @Override
  public void warn(Marker marker, String msg) {
    logger.warn(marker, msg);
  }

  @Override
  public void warn(Marker marker, String format, Object arg) {
    logger.warn(marker, format, arg);
  }

  @Override
  public void warn(Marker marker, String format, Object arg1, Object arg2) {
    logger.warn(marker, format, arg1, arg2);
  }

  @Override
  public void warn(Marker marker, String format, Object... arguments) {
    logger.warn(marker, format, arguments);
  }

  @Override
  public void warn(Marker marker, String msg, Throwable t) {
    logger.warn(marker, msg, t);
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
  }

  @Override
  public void error(String format, Object arg1, Object arg2) {
    logger.error(format, arg1, arg2);
  }

  @Override
  public void error(String format, Object... arguments) {
    logger.error(format, arguments);
  }

  @Override
  public void error(String msg, Throwable t) {
    logger.error(msg, t);
  }

  @Override
  public boolean isErrorEnabled(Marker marker) {
    return logger.isErrorEnabled(marker);
  }

  @Override
  public void error(Marker marker, String msg) {
    logger.error(marker, msg);
  }

  @Override
  public void error(Marker marker, String format, Object arg) {
    logger.error(marker, format, arg);
  }

  @Override
  public void error(Marker marker, String format, Object arg1, Object arg2) {
    logger.error(marker, format, arg1, arg2);
  }

  @Override
  public void error(Marker marker, String format, Object... arguments) {
    logger.error(marker, format, arguments);
  }

  @Override
  public void error(Marker marker, String msg, Throwable t) {
    logger.error(marker, msg, t);
  }
}
