package org.ar4k.agent.cortex.rulesEngines;

import java.util.UUID;

public class DroolsRule {

  private String ruleName = null;
  private String when = null;
  private String then = null;

  public String getDroolsRule() {
    return droolsRule(when, then);
  }

  private String droolsRule(String condition, String rule) {
    String risposta = "\npackage org.ar4k.agent.cortex.actions";
    risposta += "\nimport org.ar4k.agent.cortex.memory.TimeContextConversation";
    risposta += "\nimport org.ar4k.agent.cortex.actions.Actions";
    risposta += "\nimport org.ar4k.agent.cortex.actions.DefaultActions\n";
    risposta += "\nimport org.ar4k.agent.cortex.actions.Actions.FatherReplyTemplate\n";
    if (ruleName != null) {
      risposta += "\nrule " + ruleName;
    } else {
      risposta += "\nrule rule_" + UUID.randomUUID().toString().replaceAll("-", "");
    }
    risposta += "\nwhen";
    risposta += "\n$actions : DefaultActions()";
    risposta += "\n" + condition;
    risposta += "\nthen";
    risposta += "\n" + rule;
    risposta += "\nend\n";
    // System.out.println(risposta);
    return risposta;
  }

  public String getRuleName() {
    return ruleName;
  }

  public void setRuleName(String ruleName) {
    this.ruleName = ruleName;
  }

  public String getWhen() {
    return when;
  }

  public void setWhen(String when) {
    this.when = when;
  }

  public String getThen() {
    return then;
  }

  public void setThen(String then) {
    this.then = then;
  }

}
