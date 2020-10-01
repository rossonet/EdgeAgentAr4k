package org.ar4k.agent.web.widget.agent;

import java.util.ArrayList;
import java.util.List;

import org.ar4k.agent.design.AgentTab;
import org.ar4k.agent.design.AgentWebTab;
import org.ar4k.agent.web.scada.ScadaAgentWrapper;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextArea;

@AgentWebTab
public class AgentStatusPage implements AgentTab {

	private ScadaAgentWrapper beaconAgentWrapper = null;

	@Override
	public boolean isActive() {
		return true;
	}

	@Override
	public String getTabName() {
		return "STATUS";
	}

	@Override
	public String getClassName() {
		return "tab-status";
	}

	public final class StatusDataLineString {

		private final String label;
		private final String data;
		private final Icon icon;

		private final Component component;

		public StatusDataLineString(Icon icon, String label, String data) {
			this.label = label;
			this.data = data;
			this.icon = icon;
			this.component = null;
		}

		public StatusDataLineString(Icon icon, String label, Component component) {
			this.label = label;
			this.data = null;
			this.icon = icon;
			this.component = component;
		}

		public String getLabel() {
			return label;
		}

		public Component getData() {
			if (component == null) {
				return new Text(data);
			} else {
				return component;
			}
		}

		public Component getIcon() {
			return icon;
		}

	}

	@Override
	public Div getPage(ScadaAgentWrapper beaconAgentWrapper) {
		Div div = new Div();
		Grid<StatusDataLineString> grid = new Grid<>(StatusDataLineString.class);
		grid.getStyle().set("position", "absolute");
		this.beaconAgentWrapper = beaconAgentWrapper;
		List<StatusDataLineString> lines = new ArrayList<>();
		lines.add(new StatusDataLineString(new Icon(VaadinIcon.CHEVRON_RIGHT), "NAME", beaconAgentWrapper.getName()));
		lines.add(new StatusDataLineString(new Icon(VaadinIcon.PLUS), "DESCRIPTION",
				beaconAgentWrapper.getDescription()));
		lines.add(new StatusDataLineString(new Icon(VaadinIcon.FLASK), "JAVA VM", beaconAgentWrapper.getJavaVm()));
		lines.add(new StatusDataLineString(new Icon(VaadinIcon.DESKTOP), "OPERATING SYSTEM",
				beaconAgentWrapper.getOsName() + " " + beaconAgentWrapper.getOsVersion()));
		lines.add(new StatusDataLineString(new Icon(VaadinIcon.CALENDAR), "LAST CONTACT DATE",
				beaconAgentWrapper.getLastContact()));
		lines.add(new StatusDataLineString(new Icon(VaadinIcon.LIST), "TOTAL COMMANDS",
				String.valueOf(beaconAgentWrapper.getCommandsCount())));
		lines.add(new StatusDataLineString(new Icon(VaadinIcon.LIST), "COMMANDS", getCommandsArea()));
		lines.add(new StatusDataLineString(new Icon(VaadinIcon.ACADEMY_CAP), "HELP ONLINE", getHelpArea()));
		lines.add(new StatusDataLineString(new Icon(VaadinIcon.DASHBOARD), "SYSTEM CPUS",
				beaconAgentWrapper.getProcessors()));
		lines.add(new StatusDataLineString(new Icon(VaadinIcon.CLIPBOARD), "SYSTEM TOTAL RAM (MB)",
				String.valueOf(beaconAgentWrapper.getTotalMemoryMB())));
		lines.add(new StatusDataLineString(new Icon(VaadinIcon.CLIPBOARD_PULSE), "SYSTEM FREE RAM (MB)",
				String.valueOf(beaconAgentWrapper.getFreeMemoryMB())));
		lines.add(new StatusDataLineString(new Icon(VaadinIcon.CODE), "RUNTIME CONFIGURATION", getConfigurationArea()));
		lines.add(new StatusDataLineString(new Icon(VaadinIcon.HARDDRIVE), "STATUS", getJsonHardwareArea()));
		grid.removeAllColumns();
		grid.addComponentColumn(StatusDataLineString::getIcon).setHeader("*");
		grid.addColumn(StatusDataLineString::getLabel).setHeader("name");
		grid.addComponentColumn(StatusDataLineString::getData).setHeader("value");
		grid.setItems(lines);
		grid.setMaxWidth("95vw");
		grid.setMaxHeight("90vh");
		grid.setSizeFull();
		grid.getColumns().forEach(col -> col.setAutoWidth(true));
		div.add(grid);
		div.setSizeFull();
		return div;
	}

	private Component getJsonHardwareArea() {
		TextArea text = new TextArea();
		text.setValue(beaconAgentWrapper.getJsonHardwareInfo());
		text.setReadOnly(true);
		text.setSizeFull();
		return text;
	}

	private Component getConfigurationArea() {
		TextArea text = new TextArea();
		text.setValue(beaconAgentWrapper.getAgentConfigYml());
		text.setReadOnly(true);
		text.setSizeFull();
		return text;
	}

	private Component getCommandsArea() {
		StringBuilder sb = new StringBuilder();
		for (String c : beaconAgentWrapper.listCommands()) {
			sb.append(c + "\n");
		}
		TextArea text = new TextArea();
		text.setValue(sb.toString());
		text.setReadOnly(true);
		text.setSizeFull();
		return text;
	}

	private Component getHelpArea() {
		TextArea text = new TextArea();
		text.setValue(beaconAgentWrapper.getAgentHelp());
		text.setReadOnly(true);
		text.setSizeFull();
		return text;
	}

	@Override
	public int getActivePriority() {
		return 10000;
	}

	@Override
	public int compareTo(AgentTab arg0) {
		return getTabOrderNumber().compareTo(arg0.getTabOrderNumber());
	}

	@Override
	public Integer getTabOrderNumber() {
		return 10;
	}

}
