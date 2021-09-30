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
package org.ar4k.gw.studio.drools;

import org.ar4k.agent.cortex.drools.DroolsConfig;
import org.ar4k.agent.cortex.drools.DroolsService;
import org.ar4k.agent.cortex.drools.data.DataFactWrapper;
import org.junit.After;
import org.junit.Before;
//import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

public class DroolsWhitStateSessionTest {

	DroolsConfig droolsConfig = null;
	DroolsService droolsService = null;

	@Before
	public void setUp() throws Exception {
		droolsConfig = new DroolsConfig();
		droolsConfig.getSrcPathRules().add(ResourceUtils.getFile("classpath:drools/SelfTests.drl").toString());
		droolsService = (DroolsService) droolsConfig.instantiate();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	// @Ignore
	public void testInstantiate() {
		droolsService.init();
		System.out.println(droolsService.toString());
	}

	@Test
	// @Ignore
	public void testLoggerAndFacts() {
		testInstantiate();
		droolsService.insertOrExecute(new DataFactWrapper("first log line"), true);
		droolsService.insertOrExecute(new DataFactWrapper("second log line"), true);
		droolsService.insertOrExecute(new DataFactWrapper("45"), true);
	}

}
