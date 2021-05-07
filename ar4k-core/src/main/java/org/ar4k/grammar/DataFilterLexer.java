// Generated from DataFilter.g4 by ANTLR 4.5

   package org.ar4k.grammar;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DataFilterLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		OPERATOR=1, LABEL=2, QUOTA=3, IN=4, OPEN_PAR=5, CLOSE_PAR=6, COMMA=7, 
		TEXT=8, NUMBER=9, SPACES=10;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"OPERATOR", "LABEL", "QUOTA", "IN", "OPEN_PAR", "CLOSE_PAR", "COMMA", 
		"TEXT", "NUMBER", "LOWER_LINE", "HEX_DIGIT", "DIGIT", "A", "B", "C", "D", 
		"E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", 
		"S", "T", "U", "V", "W", "X", "Y", "Z", "SPACES"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, null, "'''", null, "'('", "')'", "','"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "OPERATOR", "LABEL", "QUOTA", "IN", "OPEN_PAR", "CLOSE_PAR", "COMMA", 
		"TEXT", "NUMBER", "SPACES"
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


	public DataFilterLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "DataFilter.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\f\u010c\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5"+
		"\2h\n\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\u00ac"+
		"\n\3\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\7\t\u00bb\n\t"+
		"\f\t\16\t\u00be\13\t\3\t\3\t\3\n\6\n\u00c3\n\n\r\n\16\n\u00c4\3\n\3\n"+
		"\6\n\u00c9\n\n\r\n\16\n\u00ca\5\n\u00cd\n\n\3\13\3\13\3\f\3\f\3\r\3\r"+
		"\3\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24"+
		"\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33"+
		"\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$"+
		"\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3(\3(\3\u00bc\2)\3\3\5\4\7\5\t\6\13\7\r"+
		"\b\17\t\21\n\23\13\25\2\27\2\31\2\33\2\35\2\37\2!\2#\2%\2\'\2)\2+\2-\2"+
		"/\2\61\2\63\2\65\2\67\29\2;\2=\2?\2A\2C\2E\2G\2I\2K\2M\2O\f\3\2 \4\2."+
		".\60\60\5\2\62;CHch\3\2\62;\4\2CCcc\4\2DDdd\4\2EEee\4\2FFff\4\2GGgg\4"+
		"\2HHhh\4\2IIii\4\2JJjj\4\2KKkk\4\2LLll\4\2MMmm\4\2NNnn\4\2OOoo\4\2PPp"+
		"p\4\2QQqq\4\2RRrr\4\2SSss\4\2TTtt\4\2UUuu\4\2VVvv\4\2WWww\4\2XXxx\4\2"+
		"YYyy\4\2ZZzz\4\2[[{{\4\2\\\\||\5\2\13\r\17\17\"\"\u00fb\2\3\3\2\2\2\2"+
		"\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2"+
		"\2\2\21\3\2\2\2\2\23\3\2\2\2\2O\3\2\2\2\3g\3\2\2\2\5\u00ab\3\2\2\2\7\u00ad"+
		"\3\2\2\2\t\u00af\3\2\2\2\13\u00b2\3\2\2\2\r\u00b4\3\2\2\2\17\u00b6\3\2"+
		"\2\2\21\u00b8\3\2\2\2\23\u00c2\3\2\2\2\25\u00ce\3\2\2\2\27\u00d0\3\2\2"+
		"\2\31\u00d2\3\2\2\2\33\u00d4\3\2\2\2\35\u00d6\3\2\2\2\37\u00d8\3\2\2\2"+
		"!\u00da\3\2\2\2#\u00dc\3\2\2\2%\u00de\3\2\2\2\'\u00e0\3\2\2\2)\u00e2\3"+
		"\2\2\2+\u00e4\3\2\2\2-\u00e6\3\2\2\2/\u00e8\3\2\2\2\61\u00ea\3\2\2\2\63"+
		"\u00ec\3\2\2\2\65\u00ee\3\2\2\2\67\u00f0\3\2\2\29\u00f2\3\2\2\2;\u00f4"+
		"\3\2\2\2=\u00f6\3\2\2\2?\u00f8\3\2\2\2A\u00fa\3\2\2\2C\u00fc\3\2\2\2E"+
		"\u00fe\3\2\2\2G\u0100\3\2\2\2I\u0102\3\2\2\2K\u0104\3\2\2\2M\u0106\3\2"+
		"\2\2O\u0108\3\2\2\2QR\5\67\34\2RS\5=\37\2Sh\3\2\2\2TU\5\33\16\2UV\5\65"+
		"\33\2VW\5!\21\2Wh\3\2\2\2XY\5\33\16\2YZ\5\65\33\2Z[\5!\21\2[\\\5\25\13"+
		"\2\\]\5\65\33\2]^\5\67\34\2^_\5A!\2_h\3\2\2\2`a\5\67\34\2ab\5=\37\2bc"+
		"\5\25\13\2cd\5\65\33\2de\5\67\34\2ef\5A!\2fh\3\2\2\2gQ\3\2\2\2gT\3\2\2"+
		"\2gX\3\2\2\2g`\3\2\2\2h\4\3\2\2\2ij\5A!\2jk\5\33\16\2kl\5\'\24\2l\u00ac"+
		"\3\2\2\2mn\5!\21\2no\5\67\34\2op\5\63\32\2pq\5\33\16\2qr\5+\26\2rs\5\65"+
		"\33\2s\u00ac\3\2\2\2tu\5\65\33\2uv\5\33\16\2vw\5\63\32\2wx\5#\22\2xy\5"+
		"\25\13\2yz\5? \2z{\59\35\2{|\5\33\16\2|}\5\37\20\2}~\5#\22\2~\u00ac\3"+
		"\2\2\2\177\u0080\5? \2\u0080\u0081\5A!\2\u0081\u0082\5\33\16\2\u0082\u0083"+
		"\5A!\2\u0083\u0084\5C\"\2\u0084\u0085\5? \2\u0085\u00ac\3\2\2\2\u0086"+
		"\u0087\5? \2\u0087\u0088\5#\22\2\u0088\u0089\5=\37\2\u0089\u008a\5E#\2"+
		"\u008a\u008b\5+\26\2\u008b\u008c\5\37\20\2\u008c\u008d\5#\22\2\u008d\u008e"+
		"\5\25\13\2\u008e\u008f\5\65\33\2\u008f\u0090\5\33\16\2\u0090\u0091\5\63"+
		"\32\2\u0091\u0092\5#\22\2\u0092\u00ac\3\2\2\2\u0093\u0094\5? \2\u0094"+
		"\u0095\5#\22\2\u0095\u0096\5=\37\2\u0096\u0097\5E#\2\u0097\u0098\5+\26"+
		"\2\u0098\u0099\5\37\20\2\u0099\u009a\5#\22\2\u009a\u009b\5\25\13\2\u009b"+
		"\u009c\5\37\20\2\u009c\u009d\5\61\31\2\u009d\u009e\5\33\16\2\u009e\u009f"+
		"\5? \2\u009f\u00a0\5? \2\u00a0\u00ac\3\2\2\2\u00a1\u00a2\5\35\17\2\u00a2"+
		"\u00a3\5\33\16\2\u00a3\u00a4\5? \2\u00a4\u00a5\5#\22\2\u00a5\u00a6\5\25"+
		"\13\2\u00a6\u00a7\5\65\33\2\u00a7\u00a8\5\33\16\2\u00a8\u00a9\5\63\32"+
		"\2\u00a9\u00aa\5#\22\2\u00aa\u00ac\3\2\2\2\u00abi\3\2\2\2\u00abm\3\2\2"+
		"\2\u00abt\3\2\2\2\u00ab\177\3\2\2\2\u00ab\u0086\3\2\2\2\u00ab\u0093\3"+
		"\2\2\2\u00ab\u00a1\3\2\2\2\u00ac\6\3\2\2\2\u00ad\u00ae\7)\2\2\u00ae\b"+
		"\3\2\2\2\u00af\u00b0\5+\26\2\u00b0\u00b1\5\65\33\2\u00b1\n\3\2\2\2\u00b2"+
		"\u00b3\7*\2\2\u00b3\f\3\2\2\2\u00b4\u00b5\7+\2\2\u00b5\16\3\2\2\2\u00b6"+
		"\u00b7\7.\2\2\u00b7\20\3\2\2\2\u00b8\u00bc\5\7\4\2\u00b9\u00bb\13\2\2"+
		"\2\u00ba\u00b9\3\2\2\2\u00bb\u00be\3\2\2\2\u00bc\u00bd\3\2\2\2\u00bc\u00ba"+
		"\3\2\2\2\u00bd\u00bf\3\2\2\2\u00be\u00bc\3\2\2\2\u00bf\u00c0\5\7\4\2\u00c0"+
		"\22\3\2\2\2\u00c1\u00c3\5\31\r\2\u00c2\u00c1\3\2\2\2\u00c3\u00c4\3\2\2"+
		"\2\u00c4\u00c2\3\2\2\2\u00c4\u00c5\3\2\2\2\u00c5\u00cc\3\2\2\2\u00c6\u00c8"+
		"\t\2\2\2\u00c7\u00c9\5\31\r\2\u00c8\u00c7\3\2\2\2\u00c9\u00ca\3\2\2\2"+
		"\u00ca\u00c8\3\2\2\2\u00ca\u00cb\3\2\2\2\u00cb\u00cd\3\2\2\2\u00cc\u00c6"+
		"\3\2\2\2\u00cc\u00cd\3\2\2\2\u00cd\24\3\2\2\2\u00ce\u00cf\7a\2\2\u00cf"+
		"\26\3\2\2\2\u00d0\u00d1\t\3\2\2\u00d1\30\3\2\2\2\u00d2\u00d3\t\4\2\2\u00d3"+
		"\32\3\2\2\2\u00d4\u00d5\t\5\2\2\u00d5\34\3\2\2\2\u00d6\u00d7\t\6\2\2\u00d7"+
		"\36\3\2\2\2\u00d8\u00d9\t\7\2\2\u00d9 \3\2\2\2\u00da\u00db\t\b\2\2\u00db"+
		"\"\3\2\2\2\u00dc\u00dd\t\t\2\2\u00dd$\3\2\2\2\u00de\u00df\t\n\2\2\u00df"+
		"&\3\2\2\2\u00e0\u00e1\t\13\2\2\u00e1(\3\2\2\2\u00e2\u00e3\t\f\2\2\u00e3"+
		"*\3\2\2\2\u00e4\u00e5\t\r\2\2\u00e5,\3\2\2\2\u00e6\u00e7\t\16\2\2\u00e7"+
		".\3\2\2\2\u00e8\u00e9\t\17\2\2\u00e9\60\3\2\2\2\u00ea\u00eb\t\20\2\2\u00eb"+
		"\62\3\2\2\2\u00ec\u00ed\t\21\2\2\u00ed\64\3\2\2\2\u00ee\u00ef\t\22\2\2"+
		"\u00ef\66\3\2\2\2\u00f0\u00f1\t\23\2\2\u00f18\3\2\2\2\u00f2\u00f3\t\24"+
		"\2\2\u00f3:\3\2\2\2\u00f4\u00f5\t\25\2\2\u00f5<\3\2\2\2\u00f6\u00f7\t"+
		"\26\2\2\u00f7>\3\2\2\2\u00f8\u00f9\t\27\2\2\u00f9@\3\2\2\2\u00fa\u00fb"+
		"\t\30\2\2\u00fbB\3\2\2\2\u00fc\u00fd\t\31\2\2\u00fdD\3\2\2\2\u00fe\u00ff"+
		"\t\32\2\2\u00ffF\3\2\2\2\u0100\u0101\t\33\2\2\u0101H\3\2\2\2\u0102\u0103"+
		"\t\34\2\2\u0103J\3\2\2\2\u0104\u0105\t\35\2\2\u0105L\3\2\2\2\u0106\u0107"+
		"\t\36\2\2\u0107N\3\2\2\2\u0108\u0109\t\37\2\2\u0109\u010a\3\2\2\2\u010a"+
		"\u010b\b(\2\2\u010bP\3\2\2\2\t\2g\u00ab\u00bc\u00c4\u00ca\u00cc\3\2\3"+
		"\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}