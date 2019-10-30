package org.ar4k.agent.rpc.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;

@Ar4kRpcProcess
public class ScriptEngineManagerProcess implements AgentProcess {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(ScriptEngineManagerProcess.class.toString());

  private String label = null;
  private ScriptEngine engine = null;
  private Object result = null;
  private ScriptException error = null;
  private boolean runned = false;
  private boolean completed = false;

  public void setEngine(String shortEngineName) {
    ScriptEngineManager factory = new ScriptEngineManager();
    engine = factory.getEngineByName(shortEngineName);
  }

  public void addParameterToBinding(String parameterName, Object data) {
    engine.put(parameterName, data);
  }

  public Object getParameterFromBinding(String parameterName) {
    return engine.get(parameterName);
  }

  public ScriptContext getContext() {
    return engine.getContext();
  }

  public void resetBinding(String parameterName, Object data) {
    engine.setBindings(engine.createBindings(), ScriptContext.ENGINE_SCOPE);
  }

  @Override
  public void eval(String script) {
    try {
      runned = true;
      result = engine.eval(script);
      completed = true;
    } catch (ScriptException e) {
      error = e;
      logger.logException(e);
    }
  }

  public Object getResult() {
    return result;
  }

  @Override
  public String getErrors() {
    return "in " + error.getFileName() + " line " + String.valueOf(
        error.getLineNumber() + " column " + String.valueOf(error.getColumnNumber()) + ": " + error.getMessage());
  }

  public boolean isEvaluated() {
    return runned;
  }

  public boolean isEvaluatedWithoutErrors() {
    return completed;
  }

  public static List<Map<String, Object>> listScriptEngines() {
    List<Map<String, Object>> result = new ArrayList<>();
    ScriptEngineManager manager = new ScriptEngineManager();
    List<ScriptEngineFactory> factories = manager.getEngineFactories();
    for (ScriptEngineFactory factory : factories) {
      Map<String, Object> single = new HashMap<>();
      single.put("Engine Name", factory.getEngineName());
      single.put("Engine Version", factory.getEngineVersion());
      single.put("Language Name", factory.getLanguageName());
      single.put("Language Version", factory.getLanguageVersion());
      single.put("Extensions", factory.getExtensions());
      single.put("Mime Types", factory.getMimeTypes());
      single.put("Names", factory.getNames());
      result.add(single);
    }
    return result;
  }

  @Override
  public boolean isAlive() {
    return runned && !completed && error == null;
  }

  @Override
  public String getLabel() {
    return label;
  }

  @Override
  public void setLabel(String label) {
    this.label = label;
  }

  @Override
  public String getOutput() {
    return getResult().toString();
  }

  @Override
  public String toString() {
    return isAlive() ? getOutput().substring(0, 40) : "Script is dead";
  }

  public String getEngine() {
    return engine.toString();
  }

  @Override
  public void close() throws IOException {
    if (engine != null) {
      engine = null;
    }
  }

}
