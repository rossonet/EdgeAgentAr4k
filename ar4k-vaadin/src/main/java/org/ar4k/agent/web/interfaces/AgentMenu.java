package org.ar4k.agent.web.interfaces;

import java.util.List;

import org.ar4k.agent.web.IMainView;

import com.vaadin.flow.component.Component;

public interface AgentMenu extends Comparable<AgentMenu> {

	void setMainView(IMainView mainView);

	boolean isActive();

	void addMenuWidget(com.vaadin.flow.component.menubar.MenuBar menuBar);

	List<Component> getLayots();

	void setVisibleTrue();

	Integer getMenuOrderNumber();

	@Override
	public default int compareTo(AgentMenu arg0) {
		return getMenuOrderNumber().compareTo(arg0.getMenuOrderNumber());
	}

}
