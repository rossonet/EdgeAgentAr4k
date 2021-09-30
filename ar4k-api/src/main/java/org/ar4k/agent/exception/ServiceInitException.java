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
 */

public class ServiceInitException extends EdgeException {

  private static final long serialVersionUID = 570723204724941542L;

  public ServiceInitException() {
    super();
  }

  public ServiceInitException(String s) {
    super(s);
  }

  public ServiceInitException(String s, Throwable throwable) {
    super(s, throwable);
  }

  public ServiceInitException(Throwable throwable) {
    super(throwable);
  }
}
