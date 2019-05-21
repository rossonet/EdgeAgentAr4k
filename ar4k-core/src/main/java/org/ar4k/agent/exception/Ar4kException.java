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

/**
 * 
 * @author Andrea Ambrosini Rossonet s.c.a r.l.
 *
 *         Gestore eccezioni.
 *
 */

public class Ar4kException extends java.lang.RuntimeException {

  private static final long serialVersionUID = -2275905233254878407L;

  public Ar4kException() {
    super();
  }

  public Ar4kException(String s) {
    super(s);
  }

  public Ar4kException(String s, Throwable throwable) {
    super(s, throwable);
  }

  public Ar4kException(Throwable throwable) {
    super(throwable);
  }
}
