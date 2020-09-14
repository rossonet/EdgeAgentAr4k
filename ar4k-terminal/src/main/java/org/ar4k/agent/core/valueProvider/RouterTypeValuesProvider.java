package org.ar4k.agent.core.valueProvider;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ar4k.agent.core.Homunculus.HomunculusRouterType;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;

class RouterTypeValuesProvider extends ValueProviderSupport {

  private final static String[] VALUES = Stream.of(HomunculusRouterType.values()).map(HomunculusRouterType::name)
      .toArray(String[]::new);

  @Override
  public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext,
      String[] hints) {
    return Arrays.stream(VALUES).map(CompletionProposal::new).collect(Collectors.toList());
  }
}