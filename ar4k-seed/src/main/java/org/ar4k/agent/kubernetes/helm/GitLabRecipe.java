package org.ar4k.agent.kubernetes.helm;

@EdgeHelmRecipe
public class GitLabRecipe implements HelmRecipe {

  private String externalUrl = null;
  private String name = null;

  @Override
  public String[] getCmdRecipe() {
    if (name != null) {
      String[] cmd = { "--name", name, "stable/gitlab-ce", "--set", "externalUrl=" + externalUrl };
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
    return "GitLab Community Edition is an application to code, test, and deploy code together. It provides Git repository management with fine grained access controls, code reviews, issue tracking, activity feeds, wikis, and continuous integration";
  }

  @Override
  public String linkSite() {
    return "https://github.com/helm/charts/tree/master/stable/gitlab-ce";
  }

  public String getExternalUrl() {
    return externalUrl;
  }

  public void setExternalUrl(String externalUrl) {
    this.externalUrl = externalUrl;
  }

}
