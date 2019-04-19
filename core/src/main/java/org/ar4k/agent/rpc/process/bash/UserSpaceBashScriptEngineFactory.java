package org.ar4k.agent.rpc.process.bash;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

public class UserSpaceBashScriptEngineFactory implements ScriptEngineFactory {

  @Override
  public String getEngineName() {
    // TODO Auto-generated method stub
    return "test";
  }

  @Override
  public String getEngineVersion() {
    // TODO Auto-generated method stub
    return "test";
  }

  @Override
  public List<String> getExtensions() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<String> getMimeTypes() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<String> getNames() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getLanguageName() {
    // TODO Auto-generated method stub
    return "test";
  }

  @Override
  public String getLanguageVersion() {
    // TODO Auto-generated method stub
    return "test";
  }

  @Override
  public Object getParameter(String key) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getMethodCallSyntax(String obj, String m, String... args) {
    // TODO Auto-generated method stub
    return "test";
  }

  @Override
  public String getOutputStatement(String toDisplay) {
    // TODO Auto-generated method stub
    return "test";
  }

  @Override
  public String getProgram(String... statements) {
    // TODO Auto-generated method stub
    return "test";
  }

  @Override
  public ScriptEngine getScriptEngine() {
    // TODO Auto-generated method stub
    return null;
  }

}
