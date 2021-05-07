// Generated from Arithmetic.g4 by ANTLR 4.5

   package org.ar4k.grammar;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ArithmeticParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		VARIABLE=1, SCIENTIFIC_NUMBER=2, LPAREN=3, RPAREN=4, PLUS=5, MINUS=6, 
		TIMES=7, DIV=8, GT=9, LT=10, EQ=11, POINT=12, POW=13, WS=14;
	public static final int
		RULE_file_ = 0, RULE_equation = 1, RULE_expression = 2, RULE_atom = 3, 
		RULE_scientific = 4, RULE_variable = 5, RULE_relop = 6;
	public static final String[] ruleNames = {
		"file_", "equation", "expression", "atom", "scientific", "variable", "relop"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, null, "'('", "')'", "'+'", "'-'", "'*'", "'/'", "'>'", "'<'", 
		"'='", "'.'", "'^'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "VARIABLE", "SCIENTIFIC_NUMBER", "LPAREN", "RPAREN", "PLUS", "MINUS", 
		"TIMES", "DIV", "GT", "LT", "EQ", "POINT", "POW", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Arithmetic.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ArithmeticParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class File_Context extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(ArithmeticParser.EOF, 0); }
		public List<EquationContext> equation() {
			return getRuleContexts(EquationContext.class);
		}
		public EquationContext equation(int i) {
			return getRuleContext(EquationContext.class,i);
		}
		public File_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_file_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).enterFile_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).exitFile_(this);
		}
	}

	public final File_Context file_() throws RecognitionException {
		File_Context _localctx = new File_Context(_ctx, getState());
		enterRule(_localctx, 0, RULE_file_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(17);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << VARIABLE) | (1L << SCIENTIFIC_NUMBER) | (1L << LPAREN) | (1L << PLUS) | (1L << MINUS))) != 0)) {
				{
				{
				setState(14);
				equation();
				}
				}
				setState(19);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(20);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EquationContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public RelopContext relop() {
			return getRuleContext(RelopContext.class,0);
		}
		public EquationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).enterEquation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).exitEquation(this);
		}
	}

	public final EquationContext equation() throws RecognitionException {
		EquationContext _localctx = new EquationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_equation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(22);
			expression(0);
			setState(23);
			relop();
			setState(24);
			expression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(ArithmeticParser.LPAREN, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(ArithmeticParser.RPAREN, 0); }
		public AtomContext atom() {
			return getRuleContext(AtomContext.class,0);
		}
		public List<TerminalNode> PLUS() { return getTokens(ArithmeticParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(ArithmeticParser.PLUS, i);
		}
		public List<TerminalNode> MINUS() { return getTokens(ArithmeticParser.MINUS); }
		public TerminalNode MINUS(int i) {
			return getToken(ArithmeticParser.MINUS, i);
		}
		public TerminalNode POW() { return getToken(ArithmeticParser.POW, 0); }
		public TerminalNode TIMES() { return getToken(ArithmeticParser.TIMES, 0); }
		public TerminalNode DIV() { return getToken(ArithmeticParser.DIV, 0); }
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).exitExpression(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(38);
			switch (_input.LA(1)) {
			case LPAREN:
				{
				setState(27);
				match(LPAREN);
				setState(28);
				expression(0);
				setState(29);
				match(RPAREN);
				}
				break;
			case VARIABLE:
			case SCIENTIFIC_NUMBER:
			case PLUS:
			case MINUS:
				{
				setState(34);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==PLUS || _la==MINUS) {
					{
					{
					setState(31);
					_la = _input.LA(1);
					if ( !(_la==PLUS || _la==MINUS) ) {
					_errHandler.recoverInline(this);
					} else {
						consume();
					}
					}
					}
					setState(36);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(37);
				atom();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(51);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(49);
					switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(40);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(41);
						match(POW);
						setState(42);
						expression(6);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(43);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(44);
						_la = _input.LA(1);
						if ( !(_la==TIMES || _la==DIV) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(45);
						expression(5);
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(46);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(47);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
						_errHandler.recoverInline(this);
						} else {
							consume();
						}
						setState(48);
						expression(4);
						}
						break;
					}
					} 
				}
				setState(53);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class AtomContext extends ParserRuleContext {
		public ScientificContext scientific() {
			return getRuleContext(ScientificContext.class,0);
		}
		public VariableContext variable() {
			return getRuleContext(VariableContext.class,0);
		}
		public AtomContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).enterAtom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).exitAtom(this);
		}
	}

	public final AtomContext atom() throws RecognitionException {
		AtomContext _localctx = new AtomContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_atom);
		try {
			setState(56);
			switch (_input.LA(1)) {
			case SCIENTIFIC_NUMBER:
				enterOuterAlt(_localctx, 1);
				{
				setState(54);
				scientific();
				}
				break;
			case VARIABLE:
				enterOuterAlt(_localctx, 2);
				{
				setState(55);
				variable();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ScientificContext extends ParserRuleContext {
		public TerminalNode SCIENTIFIC_NUMBER() { return getToken(ArithmeticParser.SCIENTIFIC_NUMBER, 0); }
		public ScientificContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scientific; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).enterScientific(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).exitScientific(this);
		}
	}

	public final ScientificContext scientific() throws RecognitionException {
		ScientificContext _localctx = new ScientificContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_scientific);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
			match(SCIENTIFIC_NUMBER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VariableContext extends ParserRuleContext {
		public TerminalNode VARIABLE() { return getToken(ArithmeticParser.VARIABLE, 0); }
		public VariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variable; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).enterVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).exitVariable(this);
		}
	}

	public final VariableContext variable() throws RecognitionException {
		VariableContext _localctx = new VariableContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_variable);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(60);
			match(VARIABLE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RelopContext extends ParserRuleContext {
		public TerminalNode EQ() { return getToken(ArithmeticParser.EQ, 0); }
		public TerminalNode GT() { return getToken(ArithmeticParser.GT, 0); }
		public TerminalNode LT() { return getToken(ArithmeticParser.LT, 0); }
		public RelopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relop; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).enterRelop(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ArithmeticListener ) ((ArithmeticListener)listener).exitRelop(this);
		}
	}

	public final RelopContext relop() throws RecognitionException {
		RelopContext _localctx = new RelopContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_relop);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(62);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << GT) | (1L << LT) | (1L << EQ))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 2:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 5);
		case 1:
			return precpred(_ctx, 4);
		case 2:
			return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\20C\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\3\2\7\2\22\n\2\f\2\16\2\25"+
		"\13\2\3\2\3\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\7\4#\n\4\f\4\16"+
		"\4&\13\4\3\4\5\4)\n\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\7\4\64\n\4\f"+
		"\4\16\4\67\13\4\3\5\3\5\5\5;\n\5\3\6\3\6\3\7\3\7\3\b\3\b\3\b\2\3\6\t\2"+
		"\4\6\b\n\f\16\2\5\3\2\7\b\3\2\t\n\3\2\13\rB\2\23\3\2\2\2\4\30\3\2\2\2"+
		"\6(\3\2\2\2\b:\3\2\2\2\n<\3\2\2\2\f>\3\2\2\2\16@\3\2\2\2\20\22\5\4\3\2"+
		"\21\20\3\2\2\2\22\25\3\2\2\2\23\21\3\2\2\2\23\24\3\2\2\2\24\26\3\2\2\2"+
		"\25\23\3\2\2\2\26\27\7\2\2\3\27\3\3\2\2\2\30\31\5\6\4\2\31\32\5\16\b\2"+
		"\32\33\5\6\4\2\33\5\3\2\2\2\34\35\b\4\1\2\35\36\7\5\2\2\36\37\5\6\4\2"+
		"\37 \7\6\2\2 )\3\2\2\2!#\t\2\2\2\"!\3\2\2\2#&\3\2\2\2$\"\3\2\2\2$%\3\2"+
		"\2\2%\'\3\2\2\2&$\3\2\2\2\')\5\b\5\2(\34\3\2\2\2($\3\2\2\2)\65\3\2\2\2"+
		"*+\f\7\2\2+,\7\17\2\2,\64\5\6\4\b-.\f\6\2\2./\t\3\2\2/\64\5\6\4\7\60\61"+
		"\f\5\2\2\61\62\t\2\2\2\62\64\5\6\4\6\63*\3\2\2\2\63-\3\2\2\2\63\60\3\2"+
		"\2\2\64\67\3\2\2\2\65\63\3\2\2\2\65\66\3\2\2\2\66\7\3\2\2\2\67\65\3\2"+
		"\2\28;\5\n\6\29;\5\f\7\2:8\3\2\2\2:9\3\2\2\2;\t\3\2\2\2<=\7\4\2\2=\13"+
		"\3\2\2\2>?\7\3\2\2?\r\3\2\2\2@A\t\4\2\2A\17\3\2\2\2\b\23$(\63\65:";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}