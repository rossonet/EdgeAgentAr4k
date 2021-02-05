package org.ar4k.agent.cortex.opennlp.core;

import java.util.HashMap;
import java.util.Set;

public interface IntentMatcher {

	IntentMatch match(String utterance, Set<String> expectedIntents, HashMap<String, Object> debugValues);

}
