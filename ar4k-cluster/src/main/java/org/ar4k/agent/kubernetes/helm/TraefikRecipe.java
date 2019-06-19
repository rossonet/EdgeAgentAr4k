package org.ar4k.agent.kubernetes.helm;

@Ar4kHelmRecipe
public class TraefikRecipe implements HelmRecipe {

  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "stable/traefik", "--namespace", "kube-system" };
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
    return "A Traefik based Kubernetes ingress controller with Let's Encrypt support";
  }

  @Override
  public String linkSite() {
    return "https://hub.helm.sh/charts/stable/traefik";
  }

}
