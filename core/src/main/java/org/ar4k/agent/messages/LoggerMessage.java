package org.ar4k.agent.messages;

import java.util.HashMap;
import java.util.Map;

import org.ar4k.agent.logger.Ar4kLogger;
import org.springframework.messaging.MessageHeaders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LoggerMessage implements Ar4kMessage {

  private String message;
  private Exception exception;
  private Ar4kLogger.LogLevel logLevel;

  @Override
  public Object getPayload() {
    Object o = null;
    if (exception != null) {
      o = exception;
    } else {
      o = message;
    }
    return o;
  }

  @Override
  public MessageHeaders getHeaders() {
    Map<String, Object> arrayHeader = new HashMap<>();
    arrayHeader.put("logLevel", logLevel);
    arrayHeader.put("exception", (exception != null));
    return new MessageHeaders(arrayHeader);
  }

  @Override
  public String toString() {
    String txt = null;
    if (exception != null) {
      txt = "Exception -> " + exception.getMessage();
    } else {
      txt = logLevel + " -> " + exception.getMessage();
    }
    return txt;
  }

  public String toJson() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(this);
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Exception getException() {
    return exception;
  }

  public void setException(Exception exception) {
    this.exception = exception;
  }

  public Ar4kLogger.LogLevel getLogLevel() {
    return logLevel;
  }

  public void setLogLevel(Ar4kLogger.LogLevel logLevel) {
    this.logLevel = logLevel;
  }

}
