package org.ar4k.agent.core;

import java.io.Serializable;
import java.util.List;


/**
 * interfaccia da implementare per una configurazione nel sistema
 *
 *
 * @author andrea
 *
 */
public interface ConfigSeed extends Serializable, Cloneable {

	String getName();

	String getDescription();

	List<String> getTags();

	long getCreationDate();

	long getLastUpdate();

	String getUniqueId();

}
