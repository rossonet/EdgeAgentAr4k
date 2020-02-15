/*
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
package org.ar4k.qa.tests;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.IBeaconClient;
import org.ar4k.agent.core.IBeaconServer;
import org.ar4k.agent.core.RpcConversation;

public class GrpcBeaconDataTests {

  IBeaconServer server = null;
  IBeaconClient client = null;
  RpcConversation rpcConversation = null;
  int port = 2569;
  Anima anima = new Anima();

}
