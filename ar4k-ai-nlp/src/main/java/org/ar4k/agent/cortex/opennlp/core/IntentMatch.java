package org.ar4k.agent.cortex.opennlp.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class holds the details of an intent match.
 * 
 * @author rabidgremlin
 *
 */
public class IntentMatch {
	/** The intent that was matched. */
	private Intent intent;

	/** Map of slots that were matched. */
	private HashMap<Entity, EntityMatch> entityMatches;

	/** The utterance that was matched against. */
	private String utterance;

	/**
	 * Constructor.
	 * 
	 * @param intent      The intent that was matched.
	 * @param entityMatches The slots that were matched.
	 * @param utterance   The utterance that was matched against.
	 */
	public IntentMatch(Intent intent, HashMap<Entity, EntityMatch> entityMatches, String utterance) {

		this.intent = intent;
		this.entityMatches = entityMatches;
		this.utterance = utterance;
	}

	/**
	 * Returns the Intent that was matched.
	 * 
	 * @return The intent that was matched.
	 */
	public Intent getIntent() {
		return intent;
	}

	/**
	 * Returns the slots that were matched.
	 * 
	 * @return Map of the slots that were matched.
	 */
	public Map<Entity, EntityMatch> getEntityMatches() {
		return Collections.unmodifiableMap(entityMatches);
	}

	/**
	 * Returns the specified slot match if the slot was matched.
	 * 
	 * @param entityName The name of the slot to return.
	 * @return The slot match or null if the slot was not matched.
	 */
	public EntityMatch getEntityMatch(String entityName) {
		for (EntityMatch match : entityMatches.values()) {
			if (match.getEntity().getName().equalsIgnoreCase(entityName)) {
				return match;
			}
		}
		return null;
	}

	/**
	 * Removes the specified slot match from the intent match.
	 * 
	 * @param entityName The name of the slot to remove the match for.
	 */
	public void removeEntityMatch(String entityName) {
		EntityMatch match = getEntityMatch(entityName);
		if (match != null) {
			entityMatches.remove(match.getEntity());
		}
	}

	/**
	 * Returns the utterance that was matched against.
	 * 
	 * @return The utterance that was matched against.
	 */
	public String getUtterance() {
		return utterance;
	}

}
