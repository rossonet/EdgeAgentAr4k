package org.ar4k.agent.blockchain;

import java.io.Serializable;

import com.beust.jcommander.Parameter;

public class EthereumConfig implements Serializable, Cloneable {

  private static final long serialVersionUID = -2316110264943782081L;

  @Parameter(names = "--name", description = "label name", required = true)
  public String name;

  @Parameter(names = "--description", description = "tribe description")
  public String description;

}
