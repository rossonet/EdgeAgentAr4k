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
package org.ar4k.agent.farm.kubernetes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.ar4k.agent.config.EdgeConfig;
import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.RpcConversation;
import org.ar4k.agent.core.interfaces.ServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 */
@Component
public class KubernetesFarmNameValueProvider extends ValueProviderSupport {

	@Autowired
	private Homunculus homunculus;

	@Autowired
	private SessionRegistry sessionRegistry;

	@Override
	public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext,
			String[] hints) {
		final String[] values = getSelectedFarmComponent();
		return Arrays.stream(values).map(CompletionProposal::new).collect(Collectors.toList());
	}

	private String[] getSelectedFarmComponent() {
		if (homunculus != null && getSessionId() != null) {
			final List<String> result = new ArrayList<>();
			final EdgeConfig config = ((RpcConversation) homunculus.getRpc(getSessionId())).getWorkingConfig();
			for (final ServiceConfig p : config.pots) {
				if (p instanceof KubernetesFarmConfig) {
					result.add(p.getName());
				}
			}
			return result.toArray(new String[0]);
		} else {
			return new String[0];
		}
	}

	private String getSessionId() {
		if (sessionRegistry != null) {
			final List<SessionInformation> ss = sessionRegistry
					.getAllSessions(SecurityContextHolder.getContext().getAuthentication(), false);
			return ss.isEmpty() ? null : ss.get(0).getSessionId();
		} else {
			return null;
		}
	}
}
