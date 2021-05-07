// Generated from DataFilter.g4 by ANTLR 4.5

   package org.ar4k.grammar;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DataFilterParser}.
 */
public interface DataFilterListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link DataFilterParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(DataFilterParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link DataFilterParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(DataFilterParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by {@link DataFilterParser#array_comparator}.
	 * @param ctx the parse tree
	 */
	void enterArray_comparator(DataFilterParser.Array_comparatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link DataFilterParser#array_comparator}.
	 * @param ctx the parse tree
	 */
	void exitArray_comparator(DataFilterParser.Array_comparatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link DataFilterParser#single_check}.
	 * @param ctx the parse tree
	 */
	void enterSingle_check(DataFilterParser.Single_checkContext ctx);
	/**
	 * Exit a parse tree produced by {@link DataFilterParser#single_check}.
	 * @param ctx the parse tree
	 */
	void exitSingle_check(DataFilterParser.Single_checkContext ctx);
	/**
	 * Enter a parse tree produced by {@link DataFilterParser#filter_query}.
	 * @param ctx the parse tree
	 */
	void enterFilter_query(DataFilterParser.Filter_queryContext ctx);
	/**
	 * Exit a parse tree produced by {@link DataFilterParser#filter_query}.
	 * @param ctx the parse tree
	 */
	void exitFilter_query(DataFilterParser.Filter_queryContext ctx);
}