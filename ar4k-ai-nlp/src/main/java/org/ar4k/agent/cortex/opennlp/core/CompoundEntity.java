package org.ar4k.agent.cortex.opennlp.core;

/**
 * A Slot that is a combination of two other slots. Useful if you need different
 * approaches for matching a slot. Class will stop on the match returned by a
 * slot.
 *
 * @author rabidgremlin
 *
 */
public class CompoundEntity extends Entity {
  /** The name of the compound slot. */
  private String name;

  /** The first slost to true match against. */
  private Entity firstEntity;

  /** The second slot to try match against. */
  private Entity secondEntity;

  /**
   * Constructor.
   *
   * @param name         The name of the slot.
   * @param firstEntity  The first slot to try match with.
   * @param secondEntity The second slot to try match against,
   */
  public CompoundEntity(String name, Entity firstEntity, Entity secondEntity) {
    this.name = name;
    this.firstEntity = firstEntity;
    this.secondEntity = secondEntity;
  }

  /*
   * (non-Javadoc)
   *
   * @see com.rabidgremlin.mutters.core#match(String token, Context context)
   *
   */
  @Override
  public EntityMatch match(String token) {
    EntityMatch match = firstEntity.match(token);
    if (match == null) {
      match = secondEntity.match(token);
    }

    return match;
  }

  /*
   * (non-Javadoc)
   *
   * @see com.rabidgremlin.mutters.core#getName()
   *
   */
  @Override
  public String getName() {
    return name;
  }

}
