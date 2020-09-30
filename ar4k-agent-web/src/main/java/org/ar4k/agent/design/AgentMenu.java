package org.ar4k.agent.design;

import java.util.List;

import org.ar4k.agent.console.MainView;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.menubar.MenuBar;

public interface AgentMenu extends Comparable<AgentMenu> {

	void setMainView(MainView mainView);

	boolean isActive();

	void addMenuWidget(MenuBar menuBar);

	List<Component> getLayots();

	void setVisibleTrue();

	Integer getMenuOrderNumber();

	@Override
	public default int compareTo(AgentMenu arg0) {
		return getMenuOrderNumber().compareTo(arg0.getMenuOrderNumber());
	}

}
