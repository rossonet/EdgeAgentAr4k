/**
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    */
package org.ar4k.agent.stunnel;

import java.util.UUID;

import org.ar4k.agent.config.tunnel.TunnelConfig;
import org.ar4k.agent.config.validator.TunnelValidator;
import org.joda.time.Instant;
import org.ar4k.agent.config.validator.KeystoreValidator;

import com.beust.jcommander.Parameter;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 * 
 * Configurzione tunnel SSL
 *
 */
public class StunnelConfig extends TunnelConfig {

  private static final long serialVersionUID = 707686741192996924L;

  @Parameter(names = "--connectionSock", description = "the target sock to connect to", validateWith = TunnelValidator.class)
  public String connectionSock = null;

  // TODO: aggiungere quelle esistenti come possibili TAB
  @Parameter(names = "--keystoreAuth", description = "keystore to authenticate to the server", validateWith = KeystoreValidator.class)
  public String keystoreAuth = null;

  // TODO: aggiungere quelle esistenti come possibili TAB
  @Parameter(names = "--keystoreTrust", description = "keystore to checking the server cert", validateWith = KeystoreValidator.class)
  public String keystoreTrust = null;

  @Parameter(names = "--redirectServer", description = "server to forward the connection")
  public String redirectServer = null;

  @Parameter(names = "--redirectPort", description = "port to forward the connection")
  public int redirectPort = 2200;

  // TODO: Aggiungere i possibili algoritmi di crittografia con validator e
  // provider
  @Parameter(names = "--algorithms", description = "port to connect")
  public String algorithms = "TLS";

  public StunnelTunnel instantiate() {
    StunnelTunnel ss = new StunnelTunnel();
    ss.setConfiguration(this);
    return ss;
  }

  @Override
  public Instant getCreationDate() {
    return creationDate;
  }

  @Override
  public Instant getLastUpdateDate() {
    return lastUpdate;
  }

  @Override
  public UUID getUniqueId() {
    return uniqueId;
  }
}
