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
package org.ar4k.agent.blockchain;

import java.util.ArrayList;
import java.util.List;

import org.ar4k.agent.core.Anima;
import org.ethereum.core.Block;
import org.ethereum.core.Genesis;
import org.ethereum.core.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Interfaccia da linea di comando per per la gestione dell'aggregazione in
 * comunità di microservice.
 * 
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 */

@ShellCommandGroup("Ethereum Commands")
@ShellComponent
@EnableMBeanExport
@ManagedResource(objectName = "bean:name=ethereumInterface", description = "Ar4k Agent Ethereum Interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "ethereumInterface")
@RestController
@RequestMapping("/ethereumInterface")
@ConditionalOnProperty(name = "ar4k.ethereum", havingValue = "true")
public class EthereumShellInterface {

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	Anima anima;

	List<Block> genesisBlocks = new ArrayList<Block>();
	List<Repository> repositories = new ArrayList<Repository>();

	@Override
	protected void finalize() {
	}

	@ShellMethod(value = "Create a Ethereum network", group = "Ethereum Commands")
	@ManagedOperation
	public String ethereumGenesis() {
		Genesis genesis = Genesis.getInstance(null);
		String risposta = genesis.toFlatString() + " [" + String.valueOf(genesis.getGasUsed()) + "]";
		genesisBlocks.add(genesis);
		Repository repository = null;
		Genesis.populateRepository(repository, genesis);
		return risposta;
	}

}
