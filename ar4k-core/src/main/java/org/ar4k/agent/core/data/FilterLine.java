package org.ar4k.agent.core.data;

import java.util.List;

import org.ar4k.agent.core.data.DataChannelFilter.Label;
import org.ar4k.agent.core.data.DataChannelFilter.Operator;

public final class FilterLine {
	private final Operator filterGlobalOperator;
	private final Operator filterOperator;
	private final Label filterLabel;
	private final List<String> filterValues;

	public FilterLine(Operator filterGlobalOperator, Label filterLabel, List<String> filterValues,
			Operator filterOperator) {
		this.filterOperator = filterOperator;
		this.filterLabel = filterLabel;
		this.filterValues = filterValues;
		this.filterGlobalOperator = filterGlobalOperator;
	}

	public Operator getFilterOperator() {
		return filterOperator;
	}

	public Label getFilterLabel() {
		return filterLabel;
	}

	public List<String> getFilterValues() {
		return filterValues;
	}

	public Operator getFilterGlobalOperator() {
		return filterGlobalOperator;
	}

}
