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

import org.ar4k.agent.config.AbstractServiceConfig;
import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;

/**
 * Classe astratta da implementare per i servizi gestiti dalla piattaforma Ar4k.
 *
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *
 */
public abstract class AbstractAr4kService implements ServiceComponent {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(AbstractAr4kService.class.toString());

  // stati servizi
  public static enum ServiceStates {
    INIT, STARTING, STAMINAL, RUNNING, STOPPED, KILLED, FAULT
  }

  // stato servizio
  private ServiceStates serviceStatus = ServiceStates.INIT;

  // vero se testato
  private boolean tested = false;

  // thread processo in run
  private Thread processo = null;

  // iniettata in costruzione (vedi get/set)
  protected Anima anima;

  // iniettata in costruzione (vedi get/set)
  private AbstractServiceConfig configuration;

  private String beanName = null;

  public AbstractAr4kService() {
    serviceStatus = ServiceStates.STARTING;
  }

  @PostConstruct
  public void postCostructor() {
    serviceStatus = ServiceStates.STAMINAL;
  }

  public abstract void loop();

  @Override
  public void run() {
    while (serviceStatus != ServiceStates.KILLED) {
      if (serviceStatus == ServiceStates.RUNNING) {
        loop();
      }
    }
    try {
      Thread.sleep(configuration.clockRunnableClass);
    } catch (InterruptedException e) {
      logger.warn(e.getMessage());
      logger.logException(e);
    }
  }

  @Override
  public synchronized void start() {
    if (processo == null && configuration != null && configuration.name != null && !configuration.name.isEmpty()) {
      processo = new Thread(this);
      processo.setName(configuration.name);
      processo.start();
      serviceStatus = configuration.targetRunLevel;
    }
  }

  @Override
  public synchronized void kill() {
    serviceStatus = ServiceStates.KILLED;
  }

  @Override
  public String getStatusString() {
    return serviceStatus.name();
  }

  @Override
  public Anima getAnima() {
    return anima;
  }

  @Override
  public void setAnima(Anima anima) {
    this.anima = anima;
  }

  @Override
  public AbstractServiceConfig getConfiguration() {
    return configuration;
  }

  public void setConfiguration(AbstractServiceConfig configuration) {
    this.configuration = configuration;
  }

  public boolean isTested() {
    return tested;
  }

  public void setTested(boolean tested) {
    this.tested = tested;
  }

  @Override
  public void setBeanName(String name) {
    beanName = name;
  }

  public String getBeanName() {
    return beanName;
  }
}
