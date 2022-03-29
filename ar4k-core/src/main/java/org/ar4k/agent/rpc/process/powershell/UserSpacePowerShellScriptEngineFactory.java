package org.ar4k.agent.rpc.process.powershell;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import com.google.common.collect.Lists;

//TO______DO PROPOSE Sviluppare script engine Power Shell per piattaforme Windows (JSR 223)
public class UserSpacePowerShellScriptEngineFactory implements ScriptEngineFactory {

  @Override
  public String getEngineName() {
    // UserSpaceBashBindings Auto-generated method stub
    return "Power Shell script engine";
  }

  @Override
  public String getEngineVersion() {
    // UserSpaceBashBindings Auto-generated method stub
    return "NOT WORKING JUST A PLACEHOLDER";
  }

  @Override
  public List<String> getExtensions() {
    // UserSpaceBashBindings Auto-generated method stub
    return null;
  }

  @Override
  public List<String> getMimeTypes() {
    // UserSpaceBashBindings Auto-generated method stub
    return null;
  }

  @Override
  public List<String> getNames() {
    return Lists.newArrayList("powershell", "dos");
  }

  @Override
  public String getLanguageName() {
    // UserSpaceBashBindings Auto-generated method stub
    return "powershell";
  }

  @Override
  public String getLanguageVersion() {
    // UserSpaceBashBindings Auto-generated method stub
    return "0.1";
  }

  @Override
  public Object getParameter(String key) {
    // UserSpaceBashBindings Auto-generated method stub
    return null;
  }

  @Override
  public String getMethodCallSyntax(String obj, String m, String... args) {
    // UserSpaceBashBindings Auto-generated method stub
    return "to do...";
  }

  @Override
  public String getOutputStatement(String toDisplay) {
    // UserSpaceBashBindings Auto-generated method stub
    return "wello world";
  }

  @Override
  public String getProgram(String... statements) {
    // UserSpaceBashBindings Auto-generated method stub
    return "just for placeholder";
  }

  @Override
  public ScriptEngine getScriptEngine() {
    // UserSpaceBashBindings Auto-generated method stub
    return null;
  }

}
