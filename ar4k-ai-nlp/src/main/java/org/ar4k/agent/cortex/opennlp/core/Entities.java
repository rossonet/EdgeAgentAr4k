package org.ar4k.agent.cortex.opennlp.core;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * Helper class for managing a map of slots.
 * 
 * @author rabidgremlin
 *
 */
public class Entities {
	/** The map of slots. */
	private HashMap<String, Entity> entities = new HashMap<String, Entity>();

	/**
	 * Adds a slot to the map.
	 * 
	 * @param entity The slot to add.
	 */
	public void add(Entity entity) {
		entities.put(entity.getName().toLowerCase(), entity);
	}

	/**
	 * Gets the specified slot from the map.
	 * 
	 * @param name The name of the slot.
	 * @return The name of the slot or null if the slot does not exist in the map.
	 */
	public Entity getEntity(String name) {
		return entities.get(name.toLowerCase());
	}

	/**
	 * Returns the slots in the map.
	 * 
	 * @return The slots in the map.
	 */
	public Collection<Entity> getEntities() {
		return Collections.unmodifiableCollection(entities.values());
	}

}
