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
package org.ar4k.agent.core;

import javax.annotation.PostConstruct;

import org.ar4k.agent.config.ServiceConfig;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;
import org.slf4j.Logger;

/**
 * Classe astratta da implementare per i servizi gestiti dalla piattaforma Ar4k.
 * 
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 * 
 * 
 */
public abstract class Ar4kService implements Runnable, Ar4kComponent {

  private static final Logger logger = Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(Anima.class.toString());

  // stati servizi
  public static enum ServiceStates {
    INIT, STARTING, STAMINAL, RUNNING, KILLED, FAULT
  }

  // stato servizio
  private ServiceStates serviceStatus = ServiceStates.INIT;

  // vero se testato
  private boolean tested = false;

  // thread processo in run
  private Thread processo = null;

  // iniettata in costruzione (vedi get/set)
  private Anima anima;

  // iniettata in costruzione (vedi get/set)
  private ServiceConfig configuration;

  public Ar4kService() {
    serviceStatus = ServiceStates.STARTING;
  }

  @PostConstruct
  public void postCostructor() {
    serviceStatus = ServiceStates.STAMINAL;
  }

  public synchronized void loop() {
    System.out.println("test loop service");
  }

  public void run() {
    while (serviceStatus != ServiceStates.KILLED) {
      if (serviceStatus == ServiceStates.RUNNING) {
        loop();
      }
    }
    // sleep
    try {
      Thread.sleep((long) configuration.clockRunnableClass);
    } catch (InterruptedException e) {
      logger.warn(e.getMessage());
    }
  }

  public synchronized void start() {
    if (processo == null && configuration != null && configuration.name != null && configuration.name != "") {
      processo = new Thread(this);
      processo.setName(configuration.name);
      processo.start();
    }
    serviceStatus = configuration.targetRunLevel;
  }

  public synchronized void kill() {
    serviceStatus = ServiceStates.KILLED;
  }

  public String status() {
    return serviceStatus.name();
  }

  public Anima getAnima() {
    return anima;
  }

  public void setAnima(Anima anima) {
    this.anima = anima;
  }

  public ServiceConfig getConfiguration() {
    return configuration;
  }

  public void setConfiguration(ServiceConfig configuration) {
    this.configuration = configuration;
  }

  public boolean isTested() {
    return tested;
  }

  public void setTested(boolean tested) {
    this.tested = tested;
  }
}
