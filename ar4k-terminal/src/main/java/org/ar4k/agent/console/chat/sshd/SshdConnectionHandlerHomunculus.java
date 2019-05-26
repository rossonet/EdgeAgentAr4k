package org.ar4k.agent.console.chat.sshd;

import org.apache.sshd.common.Factory;
import org.apache.sshd.server.command.Command;
import org.ar4k.agent.rpc.Homunculus;

//import net.engio.mbassy.listener.Handler;

public class SshdConnectionHandlerHomunculus implements Homunculus, Factory<Command> {

  @Override
  public Command create() {
    // TODO Auto-generated method stub
    return null;
  }
  
}
