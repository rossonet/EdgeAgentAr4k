package org.ar4k.agent.kubernetes.helm;

public interface HelmRecipe {

  public String[] getCmdRecipe();

  public String getName();

  public void setName(String name);

  public String getDescription();

  public String linkSite();

}
