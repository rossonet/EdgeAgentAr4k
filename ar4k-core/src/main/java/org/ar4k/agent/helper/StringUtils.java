package org.ar4k.agent.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.ar4k.agent.core.data.DataChannelFilter;
import org.ar4k.agent.core.data.DataChannelFilter.Label;
import org.ar4k.agent.core.data.DataChannelFilter.Operator;
import org.ar4k.agent.core.data.FilterLine;
import org.ar4k.grammar.DataFilterLexer;
import org.ar4k.grammar.DataFilterParser;

public final class StringUtils {

	private final static Long[] SUBNET_MASK = new Long[] { 4294934528L, 4294950912L, 4294959104L, 4294963200L,
			4294965248L, 4294966272L, 4294966784L, 4294967040L, 4294967168L, 4294967232L, 4294967264L, 4294967280L,
			4294967288L, 4294967292L, 4294967294L, 4294967295L };

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

	public static boolean isValidIPAddress(String ip) {
		String zeroTo255 = "(\\d{1,2}|(0|1)\\" + "d{2}|2[0-4]\\d|25[0-5])";
		String regex = zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255;
		Pattern p = Pattern.compile(regex);
		if (ip == null) {
			return false;
		}
		Matcher m = p.matcher(ip);
		return m.matches();
	}

	private static long ipAddressToLong(String ipAddress) {
		if (ipAddress != null) {
			String[] s = ipAddress.split("\\.");
			if (s != null && s.length == 4) {
				long result = 0;
				for (int i = 3; i >= 0; i--) {
					try {
						long n = Long.parseLong(s[3 - i]);
						result |= n << (i * 8);
					} catch (Exception ex) {
						return -1;
					}
				}
				return result;
			}
		}
		return -1;
	}

	public static boolean isValidSubnetMask(String subnetMask) {
		if (subnetMask != null && isValidIPAddress(subnetMask)) {
			long lSubnetMask = ipAddressToLong(subnetMask);
			if (lSubnetMask > 0) {
				return Arrays.asList(SUBNET_MASK).contains(lSubnetMask);
			}
		}
		return false;
	}

	public static boolean isValidMacAddress(String macAddress) {
		String regex = "^([0-9A-Fa-f]{2}[:-])" + "{5}([0-9A-Fa-f]{2})|" + "([0-9a-fA-F]{4}\\." + "[0-9a-fA-F]{4}\\."
				+ "[0-9a-fA-F]{4})$";
		Pattern p = Pattern.compile(regex);
		if (macAddress == null) {
			return false;
		}
		Matcher m = p.matcher(macAddress);
		return m.matches();
	}

}
