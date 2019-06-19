package org.ar4k.agent.kubernetes.helm;

@Ar4kHelmRecipe
public class KubeLessRecipe implements HelmRecipe {

  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "--namespace", "kubeless", "incubator/kubeless" };
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
    return "Kubeless is a Kubernetes-native serverless framework. It runs on top of your Kubernetes cluster and allows you to deploy small unit of code without having to build container images";
  }

  @Override
  public String linkSite() {
    return "https://hub.helm.sh/charts/incubator/kubeless";
  }

}
