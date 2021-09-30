package org.ar4k.agent.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.ar4k.agent.core.data.DataChannelFilter;
import org.ar4k.agent.core.data.IDataChannelFilter.Label;
import org.ar4k.agent.core.data.IDataChannelFilter.Operator;
import org.ar4k.agent.core.data.FilterLine;
import org.ar4k.grammar.DataFilterLexer;
import org.ar4k.grammar.DataFilterParser;

public final class StringUtils {

	private StringUtils() {
		throw new UnsupportedOperationException("Just for static usage");
	}

	public static String toEmptyStringIfNull(Object value) {
		return value == null ? "" : value.toString();
	}

	public static DataChannelFilter dataChannelFilterFromString(String filterString) throws IOException {
		CharStream charStream = new ANTLRInputStream(filterString);
		DataFilterLexer lexer = new DataFilterLexer(charStream);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		DataFilterParser parser = new DataFilterParser(tokens);
		final int error = parser.getNumberOfSyntaxErrors();
		if (error > 0) {
			throw new IOException("the filter: " + filterString + " contains " + error + " grammar errors");
		}
		ParseTree tree = parser.start();
		List<FilterLine> lineList = new ArrayList<>();
		int linesNumber = tree.getChild(0).getChildCount();
		for (int ln = 0; ln < linesNumber; ln++) {
			lineList.add(generateLine(tree.getChild(0).getChild(ln)));
		}
		return new DataChannelFilter(lineList);
	}

	private static FilterLine generateLine(ParseTree child) {
		final String globalOperatorString = child.getChild(0).getText().toUpperCase();
		final String labelString = child.getChild(1).getText().toUpperCase();
		final ParseTree checkArray = child.getChild(3);
		final String arrayOperatorString = checkArray.getChild(0).getText().toUpperCase();
		final List<String> valuesToCheck = new ArrayList<>();
		for (int vn = 2; vn < checkArray.getChildCount(); vn++) {
			final String value = checkArray.getChild(vn).getText();
			if (!value.equals(",")) {
				valuesToCheck.add(value.replace("'", ""));
			}
		}
		return generateLine(globalOperatorString, labelString, arrayOperatorString, valuesToCheck);
	}

	private static FilterLine generateLine(String globalOperatorString, String labelString, String arrayOperatorString,
			List<String> valuesToCheck) {
		Operator filterGlobalOperator = null;
		Label filterLabel = null;
		Operator filterOperator = null;
		switch (globalOperatorString) {
		case "AND":
			filterGlobalOperator = Operator.AND;
			break;
		case "AND_NOT":
			filterGlobalOperator = Operator.AND_NOT;
			break;
		case "OR":
			filterGlobalOperator = Operator.OR;
			break;
		case "OR_NOT":
			filterGlobalOperator = Operator.OR_NOT;
			break;
		}
		switch (arrayOperatorString) {
		case "AND":
			filterOperator = Operator.AND;
			break;
		case "AND_NOT":
			filterOperator = Operator.AND_NOT;
			break;
		case "OR":
			filterOperator = Operator.OR;
			break;
		case "OR_NOT":
			filterOperator = Operator.OR_NOT;
			break;
		}
		switch (labelString) {
		case "DOMAIN":
			filterLabel = Label.DOMAIN;
			break;
		case "NAME_SPACE":
			filterLabel = Label.NAME_SPACE;
			break;
		case "STATUS":
			filterLabel = Label.STATUS;
			break;
		case "TAG":
			filterLabel = Label.TAG;
			break;
		case "SERVICE_NAME":
			filterLabel = Label.SERVICE_NAME;
			break;
		case "SERVICE_CLASS":
			filterLabel = Label.SERVICE_CLASS;
			break;
		}
		return new FilterLine(filterGlobalOperator, filterLabel, valuesToCheck, filterOperator);
	}

}
