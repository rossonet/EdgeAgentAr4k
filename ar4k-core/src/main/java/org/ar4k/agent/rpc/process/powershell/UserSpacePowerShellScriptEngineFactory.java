package org.ar4k.agent.rpc.process.powershell;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

public class UserSpacePowerShellScriptEngineFactory implements ScriptEngineFactory {

  @Override
  public String getEngineName() {
    // TODO UserSpaceBashBindings Auto-generated method stub
    return "test";
  }

  @Override
  public String getEngineVersion() {
    // TODO UserSpaceBashBindings Auto-generated method stub
    return "test";
  }

  @Override
  public List<String> getExtensions() {
    // TODO UserSpaceBashBindings Auto-generated method stub
    return null;
  }

  @Override
  public List<String> getMimeTypes() {
    // TODO UserSpaceBashBindings Auto-generated method stub
    return null;
  }

  @Override
  public List<String> getNames() {
    // TODO UserSpaceBashBindings Auto-generated method stub
    return null;
  }

  @Override
  public String getLanguageName() {
    // TODO UserSpaceBashBindings Auto-generated method stub
    return "test";
  }

  @Override
  public String getLanguageVersion() {
    // TODO UserSpaceBashBindings Auto-generated method stub
    return "test";
  }

  @Override
  public Object getParameter(String key) {
    // TODO UserSpaceBashBindings Auto-generated method stub
    return null;
  }

  @Override
  public String getMethodCallSyntax(String obj, String m, String... args) {
    // TODO UserSpaceBashBindings Auto-generated method stub
    return "test";
  }

  @Override
  public String getOutputStatement(String toDisplay) {
    // TODO UserSpaceBashBindings Auto-generated method stub
    return "test";
  }

  @Override
  public String getProgram(String... statements) {
    // TODO UserSpaceBashBindings Auto-generated method stub
    return "test";
  }

  @Override
  public ScriptEngine getScriptEngine() {
    // TODO UserSpaceBashBindings Auto-generated method stub
    return null;
  }

}
