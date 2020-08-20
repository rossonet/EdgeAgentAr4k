package org.ar4k.agent.cortex.opennlp.bot.intents;

import org.ar4k.agent.cortex.opennlp.core.Tokenizer;

/**
 * This class wraps an Open NLP tokenizer as a Mutters tokenizer.
 * 
 * @author rabidgremlin
 *
 */
public class OpenNLPTokenizer implements Tokenizer {
	private opennlp.tools.tokenize.Tokenizer tokenizer;

	/**
	 * Constructor.
	 * 
	 * @param tokenizer The OpenNLP tokenizer to wrap.
	 */
	public OpenNLPTokenizer(opennlp.tools.tokenize.Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}

	@Override
	public String[] tokenize(String text) {
		return tokenizer.tokenize(text);
	}
}
