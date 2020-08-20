package org.ar4k.agent.kubernetes.helm;

@Ar4kHelmRecipe
public class JasperreportsRecipe implements HelmRecipe {

  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "stable/jasperreports" };
      return cmd;
    } else
      return null;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getDescription() {
    return "The JasperReports server can be used as a stand-alone or embedded reporting and BI server that offers web-based reporting, analytic tools and visualization, and a dashboard feature for compiling multiple custom views";
  }

  @Override
  public String linkSite() {
    return "https://hub.helm.sh/charts/stable/jasperreports";
  }

}
