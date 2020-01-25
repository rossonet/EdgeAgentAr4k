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
package org.ar4k.agent.openshift;

import org.ar4k.agent.helper.AbstractShellHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Shell;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Interfaccia gestione openshift.
 */

@ShellCommandGroup("OpenShift Commands")
@ShellComponent
//@EnableMBeanExport
//@ManagedResource(objectName = "bean:name=openshiftInterface", description = "Ar4k Agent OpenShift Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "openshiftInterface")
@RestController
@RequestMapping("/openshiftInterface")
//TODO implementare OpenShiftShellInterface
public class OpenShiftShellInterface extends AbstractShellHelper {

  @Autowired
  Shell shell;

}
