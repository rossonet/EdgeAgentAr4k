package org.ar4k.agent.rpc.process.bash.executable;

import static org.ar4k.agent.helper.IOUtils.pipe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.ar4k.agent.helper.IOUtils;
import org.ar4k.agent.helper.StringUtils;

public class UserSpaceExecutableScriptEngine extends AbstractScriptEngine {

  @Override
  public Object eval(String script, ScriptContext scriptContext) throws ScriptException {
    try {

      String commandLineWithBindings = expandAndReplaceBindings(script, scriptContext);
      ProcessBuilder processBuilder = new ProcessBuilder(CommandLine.translateCommandline(commandLineWithBindings));

      Map<String, String> environment = processBuilder.environment();
      for (Map.Entry<String, Object> binding : scriptContext.getBindings(ScriptContext.ENGINE_SCOPE).entrySet()) {
        environment.put(binding.getKey(), StringUtils.toEmptyStringIfNull(binding.getValue()));
      }

      final Process process = processBuilder.start();
      Thread input = writeProcessInput(process.getOutputStream(), scriptContext.getReader());
      Thread output = readProcessOutput(process.getInputStream(), scriptContext.getWriter());
      Thread error = readProcessOutput(process.getErrorStream(), scriptContext.getErrorWriter());

      input.start();
      output.start();
      error.start();

      process.waitFor();
      output.join();
      error.join();
      input.interrupt();

      int exitValue = process.exitValue();
      if (exitValue != 0) {
        throw new ScriptException("Command execution failed with exit code " + exitValue);
      }
      return exitValue;
    } catch (ScriptException e) {
      throw e;
    } catch (Exception e) {
      throw new ScriptException(e);
    }
  }

  private String expandAndReplaceBindings(String script, ScriptContext scriptContext) {
    Bindings collectionBindings = createBindings();

    for (Map.Entry<String, Object> binding : scriptContext.getBindings(ScriptContext.ENGINE_SCOPE).entrySet()) {
      String bindingKey = binding.getKey();
      Object bindingValue = binding.getValue();

      if (bindingValue instanceof Object[]) {
        addArrayBindingAsEnvironmentVariable(bindingKey, (Object[]) bindingValue, collectionBindings);
      } else if (bindingValue instanceof Collection) {
        addCollectionBindingAsEnvironmentVariable(bindingKey, (Collection) bindingValue, collectionBindings);
      } else if (bindingValue instanceof Map) {
        addMapBindingAsEnvironmentVariable(bindingKey, (Map<?, ?>) bindingValue, collectionBindings);
      }
    }

    scriptContext.getBindings(ScriptContext.ENGINE_SCOPE).putAll(collectionBindings);

    Set<Map.Entry<String, Object>> bindings = scriptContext.getBindings(ScriptContext.ENGINE_SCOPE).entrySet();

    ArrayList<Map.Entry<String, Object>> sortedBindings = new ArrayList<>(bindings);
    Collections.sort(sortedBindings, LONGER_KEY_FIRST);

    for (Map.Entry<String, Object> binding : sortedBindings) {
      String bindingKey = binding.getKey();
      Object bindingValue = binding.getValue();

      if (script.contains("$" + bindingKey)) {
        script = script.replaceAll("\\$" + bindingKey, StringUtils.toEmptyStringIfNull(bindingValue));
      }
      if (script.contains("${" + bindingKey + "}")) {
        script = script.replaceAll("\\$\\{" + bindingKey + "\\}", StringUtils.toEmptyStringIfNull(bindingValue));
      }
    }
    return script;
  }

  private void addMapBindingAsEnvironmentVariable(String bindingKey, Map<?, ?> bindingValue, Bindings bindings) {
    for (Map.Entry<?, ?> entry : ((Map<?, ?>) bindingValue).entrySet()) {
      bindings.put(bindingKey + "_" + entry.getKey(),
          (entry.getValue() == null ? "" : StringUtils.toEmptyStringIfNull(entry.getValue())));
    }
  }

  private void addCollectionBindingAsEnvironmentVariable(String bindingKey, Collection bindingValue,
      Bindings bindings) {
    Object[] bindingValueAsArray = bindingValue.toArray();
    addArrayBindingAsEnvironmentVariable(bindingKey, bindingValueAsArray, bindings);
  }

  private void addArrayBindingAsEnvironmentVariable(String bindingKey, Object[] bindingValue, Bindings bindings) {
    for (int i = 0; i < bindingValue.length; i++) {
      bindings.put(bindingKey + "_" + i,
          (bindingValue[i] == null ? "" : StringUtils.toEmptyStringIfNull(bindingValue[i])));
    }
  }

  private static Thread readProcessOutput(final InputStream processOutput, final Writer contextWriter) {
    return new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          pipe(new BufferedReader(new InputStreamReader(processOutput)), new BufferedWriter(contextWriter));
        } catch (IOException ignored) {
        }
      }
    });
  }

  private static Thread writeProcessInput(final OutputStream processOutput, final Reader contextWriter) {
    return new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          pipe(new BufferedReader(contextWriter), new OutputStreamWriter(processOutput));
        } catch (IOException closed) {
          try {
            processOutput.close();
          } catch (IOException ignored) {
          }
        }
      }
    });
  }

  @Override
  public Object eval(Reader reader, ScriptContext context) throws ScriptException {
    return eval(IOUtils.toString(reader), context);
  }

  @Override
  public Bindings createBindings() {
    return new SimpleBindings();
  }

  @Override
  public ScriptEngineFactory getFactory() {
    return new UserSpaceExecutableScriptEngineFactory();
  }

  public static final Comparator<Map.Entry<String, Object>> LONGER_KEY_FIRST = new Comparator<Map.Entry<String, Object>>() {
    @Override
    public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
      return o2.getKey().length() - o1.getKey().length();
    }
  };

}
