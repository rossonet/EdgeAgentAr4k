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
package org.ar4k.gw.studio;

//import javax.jms.ConnectionFactory;

//import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.ar4k.gw.studio.camel.DynamicRouteBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class Camel {

	private CamelContext camelContext;

	@SuppressWarnings("unused")
	private void aggiungiCamel(String da, String a) throws Throwable {
		RouteBuilder costruttore = new DynamicRouteBuilder(camelContext, da, a);
		camelContext.addRoutes(costruttore);
	}

	private void esempioCamel() throws Exception {
		// START SNIPPET: e1
		camelContext = new DefaultCamelContext();
		// END SNIPPET: e1
		// Set up the ActiveMQ JMS Components
		// START SNIPPET: e2
		//ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
		// Note we can explicit name the component
		//camelContext.addComponent("test-jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
		// END SNIPPET: e2
		// Add some configuration by hand ...
		// START SNIPPET: e3
		camelContext.addRoutes(new RouteBuilder() {
			public void configure() {
				from("test-jms:queue:test.queue").to("file://camel-test");
			}
		});
		// END SNIPPET: e3
		// Camel template - a handy class for kicking off exchanges
		// START SNIPPET: e4
		ProducerTemplate template = camelContext.createProducerTemplate();
		// END SNIPPET: e4
		// Now everything is set up - lets start the context
		camelContext.start();
		// Now send some test text to a component - for this case a JMS Queue
		// The text get converted to JMS messages - and sent to the Queue
		// test.queue
		// The file component is listening for messages from the Queue
		// test.queue, consumes
		// them and stores them to disk. The content of each file will be the
		// test we sent here.
		// The listener on the file component gets notified when new files are
		// found ... that's it!
		// START SNIPPET: e5
		for (int i = 0; i < 10; i++) {
			template.sendBody("test-jms:queue:test.queue", "Test Message: " + i);
		}
		// END SNIPPET: e5
		// wait a bit and then stop
		Thread.sleep(5000);
		camelContext.stop();
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Rule
	public TestWatcher watcher = new TestWatcher() {
		protected void starting(Description description) {
			System.out.println("\n\n\tTEST " + description.getMethodName() + " STARTED\n\n");
		}
	};

	@Test
	@Ignore
	public void test() throws Exception {
		esempioCamel();
	}
}
