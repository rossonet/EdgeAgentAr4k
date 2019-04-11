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
package org.ar4k.agent.exception;

import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;

/**
 * 
 * @author Andrea Ambrosini Rossonet s.c.a r.l.
 *
 *         Gestore eccezioni.
 *
 */

public class Ar4kException extends java.lang.RuntimeException {

  // TODO: gestione collegamento logger eccezioni
  // TODO: gestione eccezioni e stato FAULT

  private static final long serialVersionUID = -2275905233254878407L;

  private transient final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(this.getClass().toString());

  public Ar4kException() {
    super();
    logger.error("AR4K");
  }

  public Ar4kException(String s) {
    super(s);
    logger.error("AR4K: " + s);
  }

  public Ar4kException(String s, Throwable throwable) {
    super(s, throwable);
  }

  public Ar4kException(Throwable throwable) {
    super(throwable);
  }
}
