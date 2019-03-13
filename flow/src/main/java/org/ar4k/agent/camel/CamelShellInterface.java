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
package org.ar4k.agent.camel;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.ar4k.agent.core.Anima;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Interfaccia a riga di comando per la gestione dei servizi Apache Camel.
 * 
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 */

@ShellCommandGroup("Camel Commands")
@ShellComponent
@EnableMBeanExport
@ManagedResource(objectName = "bean:name=camelInterface", description = "Ar4k Agent camel interface", log = true, logFile = "ar4k.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200, persistLocation = "ar4k", persistName = "camelInterface")
@RestController
@RequestMapping("/camelInterface")
@ConditionalOnProperty(name = "ar4k.camel", havingValue = "true")
public class CamelShellInterface {
  /*
   * @Autowired private ApplicationContext applicationContext;
   */
  @Autowired
  private Anima anima;

  @Autowired
  public CamelContext camelContext;

  Map<String, String> camelComponents = new HashMap<String, String>();

  @SuppressWarnings("unused")
  private Availability testSelectedConfigOk() {
    return anima.getWorkingConfig() != null ? Availability.available()
        : Availability.unavailable("you have to select a config before");
  }

  @ShellMethod(value = "List camel endpoints managed by the platflorm", group = "Camel Commands")
  @ManagedOperation
  public String listCamelEndpoints() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(camelComponents);
  }

  @ShellMethod(value = "Add a endpoint to the platform", group = "Camel Commands")
  @ManagedOperation
  // @ShellMethodAvailability("testSelectedConfigOk")
  public void addCamelEndpoint(@ShellOption(help = "label assigned to the endpoint") String label,
      @ShellOption(help = "uri of the endpoint") String uri) {
    camelComponents.put(label, uri);
  }

  @ShellMethod(value = "Add a camel route to the platform", group = "Camel Commands")
  @ManagedOperation
  // @ShellMethodAvailability("testSelectedConfigOk")
  public void addCamelRoute(@ShellOption(help = "label assigned to the endpoint") String label,
      @ShellOption(help = "uri of the endpoint") String xml) {
    // TODO: da implementare addCamelRoute
  }

  @ShellMethod(value = "Send message to camel endpoint", group = "Camel Commands")
  @ManagedOperation
  // @ShellMethodAvailability("testSelectedConfigOk")
  public void sendMessageToCamelEndpoint(@ShellOption(help = "label of the endpoint") String label,
      @ShellOption(help = "message to send") String message) {
    ProducerTemplate template = camelContext.createProducerTemplate();
    template.sendBody(camelComponents.get(label), message);
  }

  @ShellMethod(value = "Delete camel endpoint", group = "Camel Commands")
  @ManagedOperation
  // @ShellMethodAvailability("testSelectedConfigOk")
  public void deleteCamelEndpoint(@ShellOption(help = "label of the endpoint") String label) {
    camelComponents.remove(label);
  }

  @SuppressWarnings("unchecked")
  @ShellMethod(value = "Add (or replace) camel endpoint to the selected config", group = "Camel Commands")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public void addCamelEndpointToConfiguration(@ShellOption(help = "label of the endpoint") String label) {
    try {
      if (((HashMap<String, Object>) anima.getWorkingConfig().data.get("camel")).get("endpoints") instanceof HashMap) {
        // ok
      }
    } catch (Exception ee) {
      if (anima.getWorkingConfig() != null && anima.getWorkingConfig().data != null
          && anima.getWorkingConfig().data.containsKey("camel")) {
        anima.getWorkingConfig().data.remove("camel");
      }
    }
    if (!anima.getWorkingConfig().data.containsKey("camel")) {
      Map<String, Object> mapCamel = new HashMap<String, Object>();
      Map<String, String> eps = new HashMap<String, String>();
      mapCamel.put("endpoints", eps);
      anima.getWorkingConfig().data.put("camel", mapCamel);
    }
    ((Map<String, String>) ((HashMap<String, Object>) anima.getWorkingConfig().data.get("camel")).get("endpoints"))
        .put(label, camelComponents.get(label));
  }

  @ShellMethod(value = "Add (or replace) camel route to the selected config", group = "Camel Commands")
  @ManagedOperation
  @ShellMethodAvailability("testSelectedConfigOk")
  public void addCamelRouteToConfiguration(@ShellOption(help = "label of the route") String label) {
    // TODO: da implementare addCamelRouteToConfiguration
  }

}
