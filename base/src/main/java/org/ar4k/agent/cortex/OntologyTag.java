package org.ar4k.agent.cortex;

/**
 * 
 * Link per tag alla mappa concettuale
 * 
 * @author andrea
 *
 */
public interface OntologyTag {
  String getLabel();

  String setLabel(String label);
  // TODO: definire il link alla mappa concettuale nel graph
}
