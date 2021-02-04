package org.ar4k.agent.cortex.opennlp.core;

import java.util.HashMap;

public interface EntityMatcher {

	HashMap<Entity, EntityMatch> match(Intent intent, String utterance);
}
