package org.ar4k.agent.core.data;

import java.util.ArrayList;
import java.util.List;

public class DataChannelFilter {

	public enum Operator {
		OR, AND, AND_NOT, OR_NOT
	}

	public enum Label {
		TAG, DOMAIN, NAME_SPACE, STATUS
	}

	private final List<FilterLine> filters;

	public DataChannelFilter(List<FilterLine> filters) {
		this.filters = filters;
	}

	public List<FilterLine> getFilters() {
		return filters;
	}

	public boolean filtersMatch(EdgeChannel channel) {
		boolean ok = true;
		for (final FilterLine line : filters) {
			final List<String> comparedValues = new ArrayList<>();
			boolean resultLine = false;
			switch (line.getFilterLabel()) {
			case DOMAIN:
				comparedValues.add(channel.getDomainId());
				break;
			case NAME_SPACE:
				comparedValues.add(channel.getNameSpace());
				break;
			case STATUS:
				comparedValues.add(channel.getStatus().toString());
				break;
			case TAG:
				comparedValues.addAll(channel.getTags());
				break;
			default:
				resultLine = false;
				break;
			}
			switch (line.getFilterOperator()) {
			case AND:
				resultLine = true;
				for (final String check : line.getFilterValues()) {
					if (!comparedValues.contains(check)) {
						resultLine = false;
					}
				}
				break;
			case AND_NOT:
				resultLine = true;
				for (final String check : line.getFilterValues()) {
					if (comparedValues.contains(check)) {
						resultLine = false;
					}
				}
				break;
			case OR:
				resultLine = false;
				for (final String check : line.getFilterValues()) {
					if (comparedValues.contains(check)) {
						resultLine = true;
						break;
					}
				}
				break;
			case OR_NOT:
				resultLine = false;
				for (final String check : line.getFilterValues()) {
					if (!comparedValues.contains(check)) {
						resultLine = true;
						break;
					}
				}
				break;
			default:
				resultLine = false;
				break;
			}
			boolean nextStatus = false;
			switch (line.getFilterGlobalOperator()) {
			case AND:
				nextStatus = ok && resultLine;
				break;
			case AND_NOT:
				nextStatus = ok && !resultLine;
				break;
			case OR:
				nextStatus = ok || resultLine;
				break;
			case OR_NOT:
				nextStatus = ok || !resultLine;
				break;
			default:
				nextStatus = false;
				break;
			}
			ok = nextStatus;
		}
		return ok;
	}

}
