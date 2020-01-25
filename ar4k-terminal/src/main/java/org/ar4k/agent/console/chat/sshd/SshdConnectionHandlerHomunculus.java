package org.ar4k.agent.console.chat.sshd;

import org.apache.sshd.common.Factory;
import org.apache.sshd.server.command.Command;
import org.ar4k.agent.rpc.Homunculus;

//import net.engio.mbassy.listener.Handler;

// TODO implementare SshdConnectionHandlerHomunculus
public class SshdConnectionHandlerHomunculus implements Homunculus, Factory<Command> {

  @Override
  public Command create() {
    // Auto-generated method stub
    return null;
  }

  @Override
  public void close() throws Exception {
    // Auto-generated method stub

  }

}
