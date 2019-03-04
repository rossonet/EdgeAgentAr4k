package org.ar4k.agent.cortex.conversationBot.messages;

import java.io.Serializable;
import java.util.Collection;

import org.ar4k.agent.cortex.OntologyTag;
import org.joda.time.Instant;

/**
 * 
 * rappresenta un fatto
 * 
 * @author andrea
 *
 */
public interface MemoryFact extends Serializable, Cloneable {

	public Collection<OntologyTag> getTags();

	public void setTag(OntologyTag tag);

	public Instant getGenerationTime();
}
