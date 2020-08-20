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

public class BeaconTunnelException extends EdgeException {

  private static final long serialVersionUID = 570723204724941542L;

  public BeaconTunnelException() {
    super();
  }

  public BeaconTunnelException(String s) {
    super(s);
  }

  public BeaconTunnelException(String s, Throwable throwable) {
    super(s, throwable);
  }

  public BeaconTunnelException(Throwable throwable) {
    super(throwable);
  }
}
