
package org.ar4k.agent.opcua.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.util.List;
import java.util.UUID;

import org.ar4k.agent.core.data.DataAddressChange;
import org.ar4k.agent.core.data.channels.EdgeChannel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.api.DataItem;
import org.eclipse.milo.opcua.sdk.server.api.ManagedNamespace;
import org.eclipse.milo.opcua.sdk.server.api.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.model.nodes.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.nodes.objects.ServerTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaFolderNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.util.SubscriptionModel;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpcUaNamespace extends ManagedNamespace implements DataAddressChange {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private volatile Thread eventThread;
	private volatile boolean keepPostingEvents = true;

	// private final DataTypeManager dictionaryManager;

	private final SubscriptionModel subscriptionModel;

	private final OpcUaServerConfig configuration;

	OpcUaNamespace(OpcUaServer server, OpcUaServerConfig configuration) {
		super(server, configuration.namespaceUri);
		this.configuration = configuration;
		subscriptionModel = new SubscriptionModel(server, this);
		// dictionaryManager = new DataTypeDictionaryManager(getNodeContext(),
		// configuration.namespaceUri);
	}

	public void onStartup() {

		// dictionaryManager.startup();
		subscriptionModel.startup();

		// Create a "HelloWorld" folder and add it to the node manager
		final NodeId folderNodeId = newNodeId(configuration.baseFolderName);

		final UaFolderNode folderNode = new UaFolderNode(getNodeContext(), folderNodeId,
				newQualifiedName(configuration.baseFolderName), LocalizedText.english(configuration.baseFolderName));

		getNodeManager().addNode(folderNode);

		// Make sure our new folder shows up under the server's Objects folder.
		folderNode.addReference(new Reference(folderNode.getNodeId(), Identifiers.Organizes,
				Identifiers.ObjectsFolder.expanded(), false));

		// Set the EventNotifier bit on Server Node for Events.
		final UaNode serverNode = getServer().getAddressSpaceManager().getManagedNode(Identifiers.Server).orElse(null);

		if (serverNode instanceof ServerTypeNode) {
			((ServerTypeNode) serverNode).setEventNotifier(ubyte(1));

			// Post a bogus Event every couple seconds
			eventThread = new Thread(() -> {
				while (keepPostingEvents) {
					try {
						final BaseEventTypeNode eventNode = getServer().getEventFactory()
								.createEvent(newNodeId(UUID.randomUUID()), Identifiers.BaseEventType);

						eventNode.setBrowseName(new QualifiedName(1, "foo"));
						eventNode.setDisplayName(LocalizedText.english("foo"));
						eventNode.setEventId(ByteString.of(new byte[] { 0, 1, 2, 3 }));
						eventNode.setEventType(Identifiers.BaseEventType);
						eventNode.setSourceNode(serverNode.getNodeId());
						eventNode.setSourceName(serverNode.getDisplayName().getText());
						eventNode.setTime(DateTime.now());
						eventNode.setReceiveTime(DateTime.NULL_VALUE);
						eventNode.setMessage(LocalizedText.english("event message!"));
						eventNode.setSeverity(ushort(2));

						getServer().getEventBus().post(eventNode);

						eventNode.delete();
					} catch (final Throwable e) {
						logger.error("Error creating EventNode: {}", e.getMessage(), e);
					}

					try {
						Thread.sleep(2_000);
					} catch (final InterruptedException ignored) {
						// ignored
					}
				}
			}, "bogus-event-poster");

			eventThread.start();
		}
	}

	public void onShutdown() {
		// dictionaryManager.shutdown();
		subscriptionModel.shutdown();

		try {
			keepPostingEvents = false;
			eventThread.interrupt();
			eventThread.join();
		} catch (final InterruptedException ignored) {
			// ignored
		}
	}

	@Override
	public void onDataItemsCreated(List<DataItem> dataItems) {
		subscriptionModel.onDataItemsCreated(dataItems);
	}

	@Override
	public void onDataItemsModified(List<DataItem> dataItems) {
		subscriptionModel.onDataItemsModified(dataItems);
	}

	@Override
	public void onDataItemsDeleted(List<DataItem> dataItems) {
		subscriptionModel.onDataItemsDeleted(dataItems);
	}

	@Override
	public void onMonitoringModeChanged(List<MonitoredItem> monitoredItems) {
		subscriptionModel.onMonitoringModeChanged(monitoredItems);
	}

	public OpcUaServerConfig getConfiguration() {
		return configuration;
	}

	@Override
	public void onDataAddressUpdate(EdgeChannel updatedChannel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDataAddressCreate(EdgeChannel createdChannel) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDataAddressDelete(String deletedChannel) {
		// TODO Auto-generated method stub

	}

}