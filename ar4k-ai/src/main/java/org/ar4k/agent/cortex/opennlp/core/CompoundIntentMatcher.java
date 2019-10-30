package org.ar4k.agent.cortex.opennlp.core;

import java.util.HashMap;
import java.util.Set;

/**
 * An IntentMatcher that is a combination of two other intent matchers. Useful
 * for instance for combining a TemplatedIntentMatcher and an implementation of
 * an AbstractMachineLearningIntentMatcher. Class will stop on the first match
 * returned by a matcher.
 *
 * @author rabidgremlin
 *
 */
public class CompoundIntentMatcher implements IntentMatcher {
  /** The first matcher. */
  private IntentMatcher firstMatcher;

  /** The second matcher. */
  private IntentMatcher secondMatcher;

  /**
   * Constructor for the class.
   *
   * @param firstMatcher  The first intent matcher.
   * @param secondMatcher The second intent matcher.
   */
  public CompoundIntentMatcher(IntentMatcher firstMatcher, IntentMatcher secondMatcher) {
    this.firstMatcher = firstMatcher;
    this.secondMatcher = secondMatcher;
  }

  /*
   * (non-Javadoc)
   *
   * @see com.rabidgremlin.mutters.core.IntentMatcher#match(String utterance,
   * Context context, Set<String> expectedIntents)
   *
   */
  @Override
  public IntentMatch match(String utterance, Set<String> expectedIntents, HashMap<String, Object> debugValues) {
    // see if we can find match in first matcher
    IntentMatch match = firstMatcher.match(utterance, expectedIntents, debugValues);

    // no ? try second one
    if (match == null) {
      match = secondMatcher.match(utterance, expectedIntents, debugValues);
    }

    return match;
  }
}
