package org.ar4k.agent.helper;

import java.io.File;
import java.util.List;

public class RootFileSystem {

  private List<RootFileSystem> childs = null;
  private long totalSpace = 0;
  private long freeSpace = 0;
  private File absoluteFile = null;

  public void setAbsolutePath(File absoluteFile) {
    this.absoluteFile = absoluteFile;
  }

  public void setFreeSpace(long freeSpace) {
    this.freeSpace = freeSpace;
  }

  public void setTotalSpace(long totalSpace) {
    this.totalSpace = totalSpace;
  }

  public void setChilds(List<RootFileSystem> childs) {
    this.childs = childs;
  }

  protected File getAbsoluteFile() {
    return absoluteFile;
  }

  protected void setAbsoluteFile(File absoluteFile) {
    this.absoluteFile = absoluteFile;
  }

  protected List<RootFileSystem> getChilds() {
    return childs;
  }

  protected long getTotalSpace() {
    return totalSpace;
  }

  protected long getFreeSpace() {
    return freeSpace;
  }

}
