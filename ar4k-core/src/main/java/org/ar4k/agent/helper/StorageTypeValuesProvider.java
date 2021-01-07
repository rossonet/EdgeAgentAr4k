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
package org.ar4k.agent.helper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.ar4k.agent.core.interfaces.ManagedArchiveAr4k;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.MethodParameter;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

/**
 * @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
 *
 *         Provider valori dei driver di storage
 */
@Component
public class StorageTypeValuesProvider extends ValueProviderSupport {

	private static Set<String> cachedValues = null;

	@Override
	public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext,
			String[] hints) {
		String[] values = listStorageDrivers().stream().toArray(String[]::new);
		return Arrays.stream(values).map(CompletionProposal::new).collect(Collectors.toList());
	}

	public static Set<String> listStorageDrivers() {
		if (cachedValues == null) {
			cachedValues = new HashSet<>();
			ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
					false);
			provider.addIncludeFilter(new AnnotationTypeFilter(ManagedArchiveAr4k.class));
			Set<BeanDefinition> classes = provider.findCandidateComponents("");
			for (BeanDefinition c : classes) {
				cachedValues.add(c.getBeanClassName());
			}
		}
		return cachedValues;
	}

}
