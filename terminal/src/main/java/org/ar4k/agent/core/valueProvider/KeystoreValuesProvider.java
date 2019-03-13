/**
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
package org.ar4k.agent.core.valueProvider;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.keystore.KeystoreConfig;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

/*
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Provider per interfaccia a linea di comando dei keystore configurati
 *         su Anima.
 */
@Component
public class KeystoreValuesProvider extends ValueProviderSupport {

  @Override
  public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext,
      String[] hints) {
    Anima anima = (Anima) Anima.getApplicationContext().getBean("anima");
    String[] risposta = new String[anima.getKeyStores().size()];
    int cont = 0;
    for (KeystoreConfig b : anima.getKeyStores()) {
      risposta[cont] = b.label.toString();
      cont++;
    }
    return Arrays.stream(risposta).map(CompletionProposal::new).collect(Collectors.toList());
  }
}
