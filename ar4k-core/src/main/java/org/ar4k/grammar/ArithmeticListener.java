// Generated from Arithmetic.g4 by ANTLR 4.5

   package org.ar4k.grammar;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ArithmeticParser}.
 */
public interface ArithmeticListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ArithmeticParser#file_}.
	 * @param ctx the parse tree
	 */
	void enterFile_(ArithmeticParser.File_Context ctx);
	/**
	 * Exit a parse tree produced by {@link ArithmeticParser#file_}.
	 * @param ctx the parse tree
	 */
	void exitFile_(ArithmeticParser.File_Context ctx);
	/**
	 * Enter a parse tree produced by {@link ArithmeticParser#equation}.
	 * @param ctx the parse tree
	 */
	void enterEquation(ArithmeticParser.EquationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArithmeticParser#equation}.
	 * @param ctx the parse tree
	 */
	void exitEquation(ArithmeticParser.EquationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArithmeticParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(ArithmeticParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArithmeticParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(ArithmeticParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArithmeticParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(ArithmeticParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArithmeticParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(ArithmeticParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArithmeticParser#scientific}.
	 * @param ctx the parse tree
	 */
	void enterScientific(ArithmeticParser.ScientificContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArithmeticParser#scientific}.
	 * @param ctx the parse tree
	 */
	void exitScientific(ArithmeticParser.ScientificContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArithmeticParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariable(ArithmeticParser.VariableContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArithmeticParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariable(ArithmeticParser.VariableContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArithmeticParser#relop}.
	 * @param ctx the parse tree
	 */
	void enterRelop(ArithmeticParser.RelopContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArithmeticParser#relop}.
	 * @param ctx the parse tree
	 */
	void exitRelop(ArithmeticParser.RelopContext ctx);
}