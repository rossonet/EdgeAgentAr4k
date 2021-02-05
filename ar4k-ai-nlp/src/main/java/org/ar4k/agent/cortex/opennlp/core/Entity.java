package org.ar4k.agent.cortex.opennlp.core;

public abstract class Entity {

	public abstract EntityMatch match(String token);

	public abstract String getName();

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 *
	 */
	@Override
	public int hashCode() {
		// HACK HACK assumes name match means equals. need to do this for SlotMatches
		// list but is dog dog
		// ugly
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(Object obj)
	 *
	 */
	@Override
	public boolean equals(Object obj) {
		// HACK HACK assumes name match means equals. need to do this for SlotMatches
		// list but is dog dog
		// ugly
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Entity)) {
			return false;
		}
		Entity other = (Entity) obj;
		if (getName() == null) {
			if (other.getName() != null) {
				return false;
			}
		} else {
			if (!getName().equals(other.getName())) {
				return false;
			}
		}
		return true;
	}
}
