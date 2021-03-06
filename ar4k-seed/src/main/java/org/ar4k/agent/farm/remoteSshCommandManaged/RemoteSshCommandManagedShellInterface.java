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
package org.ar4k.agent.farm.remoteSshCommandManaged;

import org.ar4k.agent.helper.AbstractShellHelper;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Interfaccia da linea di comando per la gestione delle installazioni remote
 *
 */

//TODO completare gestione modulo da gestione comandi remoti come servizi (esempio Staer SG e OpenVPN e cisco)

@ShellCommandGroup("Remote SSH Command Managed Executor Commands")
@ShellComponent
@RestController
@RequestMapping("/remoteSshCommandManagedExecutorInterface")
public class RemoteSshCommandManagedShellInterface extends AbstractShellHelper {

}
