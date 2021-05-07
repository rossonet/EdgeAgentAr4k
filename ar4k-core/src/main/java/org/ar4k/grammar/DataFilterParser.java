// Generated from DataFilter.g4 by ANTLR 4.5

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
public class DataFilterParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		OPERATOR=1, LABEL=2, QUOTA=3, IN=4, OPEN_PAR=5, CLOSE_PAR=6, COMMA=7, 
		TEXT=8, SPACES=9;
	public static final int
		RULE_start = 0, RULE_array_comparator = 1, RULE_single_check = 2, RULE_filter_query = 3;
	public static final String[] ruleNames = {
		"start", "array_comparator", "single_check", "filter_query"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, null, "'''", null, "'('", "')'", "','"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "OPERATOR", "LABEL", "QUOTA", "IN", "OPEN_PAR", "CLOSE_PAR", "COMMA", 
		"TEXT", "SPACES"
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
	public String getGrammarFileName() { return "DataFilter.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public DataFilterParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class StartContext extends ParserRuleContext {
		public Filter_queryContext filter_query() {
			return getRuleContext(Filter_queryContext.class,0);
		}
		public StartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_start; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DataFilterListener ) ((DataFilterListener)listener).enterStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataFilterListener ) ((DataFilterListener)listener).exitStart(this);
		}
	}

	public final StartContext start() throws RecognitionException {
		StartContext _localctx = new StartContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_start);
		try {
			setState(10);
			switch (_input.LA(1)) {
			case OPERATOR:
				enterOuterAlt(_localctx, 1);
				{
				setState(8);
				filter_query();
				}
				break;
			case EOF:
				enterOuterAlt(_localctx, 2);
				{
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

	public static class Array_comparatorContext extends ParserRuleContext {
		public TerminalNode OPERATOR() { return getToken(DataFilterParser.OPERATOR, 0); }
		public TerminalNode OPEN_PAR() { return getToken(DataFilterParser.OPEN_PAR, 0); }
		public List<TerminalNode> TEXT() { return getTokens(DataFilterParser.TEXT); }
		public TerminalNode TEXT(int i) {
			return getToken(DataFilterParser.TEXT, i);
		}
		public TerminalNode CLOSE_PAR() { return getToken(DataFilterParser.CLOSE_PAR, 0); }
		public List<TerminalNode> COMMA() { return getTokens(DataFilterParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(DataFilterParser.COMMA, i);
		}
		public Array_comparatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array_comparator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DataFilterListener ) ((DataFilterListener)listener).enterArray_comparator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataFilterListener ) ((DataFilterListener)listener).exitArray_comparator(this);
		}
	}

	public final Array_comparatorContext array_comparator() throws RecognitionException {
		Array_comparatorContext _localctx = new Array_comparatorContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_array_comparator);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(12);
			match(OPERATOR);
			setState(13);
			match(OPEN_PAR);
			setState(14);
			match(TEXT);
			setState(19);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(15);
					match(COMMA);
					setState(16);
					match(TEXT);
					}
					} 
				}
				setState(21);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			setState(22);
			match(CLOSE_PAR);
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

	public static class Single_checkContext extends ParserRuleContext {
		public TerminalNode OPERATOR() { return getToken(DataFilterParser.OPERATOR, 0); }
		public TerminalNode LABEL() { return getToken(DataFilterParser.LABEL, 0); }
		public TerminalNode IN() { return getToken(DataFilterParser.IN, 0); }
		public Array_comparatorContext array_comparator() {
			return getRuleContext(Array_comparatorContext.class,0);
		}
		public Single_checkContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_single_check; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DataFilterListener ) ((DataFilterListener)listener).enterSingle_check(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataFilterListener ) ((DataFilterListener)listener).exitSingle_check(this);
		}
	}

	public final Single_checkContext single_check() throws RecognitionException {
		Single_checkContext _localctx = new Single_checkContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_single_check);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(24);
			match(OPERATOR);
			setState(25);
			match(LABEL);
			setState(26);
			match(IN);
			setState(27);
			array_comparator();
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

	public static class Filter_queryContext extends ParserRuleContext {
		public List<Single_checkContext> single_check() {
			return getRuleContexts(Single_checkContext.class);
		}
		public Single_checkContext single_check(int i) {
			return getRuleContext(Single_checkContext.class,i);
		}
		public Filter_queryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_filter_query; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DataFilterListener ) ((DataFilterListener)listener).enterFilter_query(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DataFilterListener ) ((DataFilterListener)listener).exitFilter_query(this);
		}
	}

	public final Filter_queryContext filter_query() throws RecognitionException {
		Filter_queryContext _localctx = new Filter_queryContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_filter_query);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(29);
			single_check();
			setState(33);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=1 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1+1 ) {
					{
					{
					setState(30);
					single_check();
					}
					} 
				}
				setState(35);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
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

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\13\'\4\2\t\2\4\3"+
		"\t\3\4\4\t\4\4\5\t\5\3\2\3\2\5\2\r\n\2\3\3\3\3\3\3\3\3\3\3\7\3\24\n\3"+
		"\f\3\16\3\27\13\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\7\5\"\n\5\f\5\16"+
		"\5%\13\5\3\5\4\25#\2\6\2\4\6\b\2\2%\2\f\3\2\2\2\4\16\3\2\2\2\6\32\3\2"+
		"\2\2\b\37\3\2\2\2\n\r\5\b\5\2\13\r\3\2\2\2\f\n\3\2\2\2\f\13\3\2\2\2\r"+
		"\3\3\2\2\2\16\17\7\3\2\2\17\20\7\7\2\2\20\25\7\n\2\2\21\22\7\t\2\2\22"+
		"\24\7\n\2\2\23\21\3\2\2\2\24\27\3\2\2\2\25\26\3\2\2\2\25\23\3\2\2\2\26"+
		"\30\3\2\2\2\27\25\3\2\2\2\30\31\7\b\2\2\31\5\3\2\2\2\32\33\7\3\2\2\33"+
		"\34\7\4\2\2\34\35\7\6\2\2\35\36\5\4\3\2\36\7\3\2\2\2\37#\5\6\4\2 \"\5"+
		"\6\4\2! \3\2\2\2\"%\3\2\2\2#$\3\2\2\2#!\3\2\2\2$\t\3\2\2\2%#\3\2\2\2\5"+
		"\f\25#";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}