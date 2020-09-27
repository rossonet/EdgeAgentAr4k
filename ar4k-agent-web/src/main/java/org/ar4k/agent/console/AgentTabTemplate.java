package org.ar4k.agent.console;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.shared.Registration;

public class AgentTabTemplate extends Tab {

	private static final long serialVersionUID = -544452562126483940L;
	private int tabIndexSelected;

	public AgentTabTemplate(String s, int tabIndexSelected) {
		super(s);
		this.tabIndexSelected = tabIndexSelected;
	}

	@DomEvent("click")
	public static class ClickTabEvent extends ComponentEvent<Tab> {

		private static final long serialVersionUID = 766258338931793580L;

		int index = 0;
		Tab sourceTarget = null;

		public ClickTabEvent(Tab source, boolean fromClient) {
			super(source, fromClient);
			this.index = ((AgentTabTemplate) source).getIndexSelected();
			this.sourceTarget = source;
		}

		public int getIndex() {
			return index;
		}

		@Override
		public Tab getSource() {
			return sourceTarget;
		}
	}

	public Registration addClickListener(ComponentEventListener<ClickTabEvent> listener) {
		return addListener(ClickTabEvent.class, listener);
	}

	public int getIndexSelected() {
		return tabIndexSelected;
	}
}
