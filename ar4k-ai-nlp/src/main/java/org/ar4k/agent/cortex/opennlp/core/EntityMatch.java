package org.ar4k.agent.cortex.opennlp.core;

/**
 * This class holds the details of a slot match.
 * 
 * @author rabidgremlin
 *
 */
public class EntityMatch {
	/** The slot that was matched. */
	private Entity entity;

	/** The original value that was used to matched on. */
	private String orginalValue;

	/** The value that was matched. */
	private Object value;

	/**
	 * Constructor.
	 * 
	 * @param entity       The slot that was matched.
	 * @param orginalValue The original value that was used to match on.
	 * @param value        The value that was matched.
	 */
	public EntityMatch(Entity entity, String orginalValue, Object value) {
		this.entity = entity;
		this.orginalValue = orginalValue;
		this.value = value;
	}

	/**
	 * Gets the slot that was matched on.
	 * 
	 * @return The slot that was matched on.
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * Gets the original value that was matched on.
	 * 
	 * @return The original value that was matched on.
	 */
	public String getOrginalValue() {
		return orginalValue;
	}

	/**
	 * Gets the value that was matched on.
	 * 
	 * @return the value that was matched on.
	 */
	public Object getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.String#toString()
	 * 
	 */
	@Override
	public String toString() {
		return "EntityMatch [entity=" + entity + ", orginalValue=" + orginalValue + ", value=" + value + "]";
	}

}
