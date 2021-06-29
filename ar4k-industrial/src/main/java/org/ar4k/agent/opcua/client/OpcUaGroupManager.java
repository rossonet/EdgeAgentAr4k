package org.ar4k.agent.opcua.client;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.ar4k.agent.core.data.channels.IPublishSubscribeChannel;
import org.ar4k.agent.core.data.messages.IndustrialMessage;
import org.ar4k.agent.core.data.messages.IndustrialPayload;
import org.ar4k.agent.core.data.messages.IndustrialPayload.DataType;
import org.ar4k.agent.core.data.messages.IndustrialPayload.Quality;
import org.ar4k.agent.core.interfaces.EdgeChannel;
import org.ar4k.agent.helper.ConfigHelper;
import org.ar4k.agent.industrial.Enumerator.DataChangeTrigger;
import org.ar4k.agent.industrial.Enumerator.DeadbandType;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem.ValueConsumer;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription.ItemCreationCallback;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.DataChangeFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;

public class OpcUaGroupManager implements AutoCloseable {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(OpcUaGroupManager.class);

	private final OpcUaClientService opcUaClientService;
	private final UaSubscription uaSubscription;
	private final ValueConsumer valueConsumer;
	private final Map<UInteger, OpcUaClientNodeConfig> nodeMapConfig = new HashMap<>();
	private final Map<UInteger, EdgeChannel> nodeMapCallback = new HashMap<>();

	private final String groupName;

	private final Set<OpcUaWriter> writeChannels = new HashSet<>();

	public OpcUaGroupManager(String groupName, OpcUaClientService opcUaClientService, UaSubscription uaSubscription) {
		this.groupName = groupName;
		this.opcUaClientService = opcUaClientService;
		this.uaSubscription = uaSubscription;
		this.valueConsumer = new ValueConsumer() {

			@Override
			public void onValueArrived(UaMonitoredItem item, DataValue value) {
				onNewValue(item, value);
			}

		};
	}

	public void addSingleNode(OpcUaClientNodeConfig singleNode) throws InterruptedException, ExecutionException {
		if (singleNode.readChannel != null && !singleNode.readChannel.isEmpty()) {
			addReaderChannel(singleNode);
		}
		if (singleNode.writeChannel != null && !singleNode.writeChannel.isEmpty()) {
			addWriteChannel(singleNode);
		}

	}

	private void addReaderChannel(OpcUaClientNodeConfig singleNode) throws InterruptedException, ExecutionException {
		UInteger clientHandle = uaSubscription.nextClientHandle();
		ReadValueId readValueId = new ReadValueId(NodeId.parse(singleNode.nodeId), AttributeId.Value.uid(), null,
				QualifiedName.NULL_VALUE);

		DataChangeFilter filter = null;
		if (!singleNode.dataChangeTrigger.equals(DataChangeTrigger.statusOrValueOrTimestamp)
				|| !singleNode.deadbandType.equals(DeadbandType.none)) {
			org.eclipse.milo.opcua.stack.core.types.enumerated.DataChangeTrigger dataChange = org.eclipse.milo.opcua.stack.core.types.enumerated.DataChangeTrigger.StatusValueTimestamp;
			org.eclipse.milo.opcua.stack.core.types.enumerated.DeadbandType deadBandType = org.eclipse.milo.opcua.stack.core.types.enumerated.DeadbandType.None;
			switch (singleNode.dataChangeTrigger) {
			case status:
				dataChange = org.eclipse.milo.opcua.stack.core.types.enumerated.DataChangeTrigger.Status;
				break;
			case statusOrValue:
				dataChange = org.eclipse.milo.opcua.stack.core.types.enumerated.DataChangeTrigger.StatusValue;
				break;
			case statusOrValueOrTimestamp:
				dataChange = org.eclipse.milo.opcua.stack.core.types.enumerated.DataChangeTrigger.StatusValueTimestamp;
				break;
			default:
				dataChange = org.eclipse.milo.opcua.stack.core.types.enumerated.DataChangeTrigger.StatusValueTimestamp;
				break;
			}
			switch (singleNode.deadbandType) {
			case absolute:
				deadBandType = org.eclipse.milo.opcua.stack.core.types.enumerated.DeadbandType.Absolute;
				break;
			case none:
				deadBandType = org.eclipse.milo.opcua.stack.core.types.enumerated.DeadbandType.None;
				break;
			case percent:
				deadBandType = org.eclipse.milo.opcua.stack.core.types.enumerated.DeadbandType.Percent;
				break;
			default:
				deadBandType = org.eclipse.milo.opcua.stack.core.types.enumerated.DeadbandType.None;
				break;
			}
			filter = new DataChangeFilter(dataChange, UInteger.valueOf(deadBandType.getValue()),
					singleNode.deadbandValue);
		}

		MonitoringParameters parameters = new MonitoringParameters(clientHandle, singleNode.samplingInterval,
				filter != null
						? ExtensionObject.encode(opcUaClientService.getOpcUaClient().getStaticSerializationContext(),
								filter)
						: null,
				UInteger.valueOf(singleNode.queueSize), singleNode.discardOldest);

		MonitoredItemCreateRequest request = new MonitoredItemCreateRequest(readValueId, MonitoringMode.Reporting,
				parameters);

		UaSubscription.ItemCreationCallback onItemCreated = new ItemCreationCallback() {

			@Override
			public void onItemCreated(UaMonitoredItem item, int index) {
				onSubscriptionValue(item, index);

			}

		};
		nodeMapConfig.put(clientHandle, singleNode);
		uaSubscription.createMonitoredItems(TimestampsToReturn.Both, newArrayList(request), onItemCreated).get();
	}

	private void addWriteChannel(OpcUaClientNodeConfig singleNode) {
		final EdgeChannel channelWrite = opcUaClientService.getDataAddress().createOrGetDataChannel(
				singleNode.writeChannel, IPublishSubscribeChannel.class, "status of Watson connection",
				singleNode.fatherOfChannels, singleNode.scopeOfChannels,
				ConfigHelper.mergeTags(singleNode.tags, opcUaClientService.getConfiguration().getTags()),
				opcUaClientService);
		final OpcUaWriter opcUaWriter = new OpcUaWriter(opcUaClientService, singleNode);
		((IPublishSubscribeChannel) channelWrite).subscribe(opcUaWriter);
		writeChannels.add(opcUaWriter);
	}

	private void onSubscriptionValue(UaMonitoredItem item, int index) {
		item.setValueConsumer(valueConsumer);
		logger.info("item " + item.getMonitoredItemId() + " [" + item.getReadValueId().getNodeId() + "] added to group "
				+ groupName);
	}

	private void onNewValue(UaMonitoredItem uaMonitoredItem, DataValue dataValue) {
		if (!nodeMapCallback.containsKey(uaMonitoredItem.getClientHandle())) {
			OpcUaClientNodeConfig config = nodeMapConfig.get(uaMonitoredItem.getClientHandle());
			final EdgeChannel channel = opcUaClientService.getDataAddress().createOrGetDataChannel(config.readChannel,
					IPublishSubscribeChannel.class, "status of Watson connection", config.fatherOfChannels,
					config.scopeOfChannels,
					ConfigHelper.mergeTags(config.tags, opcUaClientService.getConfiguration().getTags()),
					opcUaClientService);
			nodeMapCallback.put(uaMonitoredItem.getClientHandle(), channel);
		}
		final EdgeChannel outputChannel = nodeMapCallback.get(uaMonitoredItem.getClientHandle());
		final IndustrialPayload industrialPayload = new IndustrialPayload();
		final Object value = dataValue.getValue().getValue();
		if (dataValue.getStatusCode().isGood()) {
			industrialPayload.setQuality(Quality.GOOD);
		} else if (dataValue.getStatusCode().isOverflowSet()) {
			industrialPayload.setQuality(Quality.OVERFLOW);
		} else if (dataValue.getStatusCode().isSecurityError()) {
			industrialPayload.setQuality(Quality.SECURITY_ERROR);
		} else if (dataValue.getStatusCode().isUncertain()) {
			industrialPayload.setQuality(Quality.UNKNOW);
		} else if (dataValue.getStatusCode().isBad()) {
			industrialPayload.setQuality(Quality.BAD);
		} else {
			industrialPayload.setQuality(Quality.UNKNOW);
		}
		industrialPayload.setProductionTime(dataValue.getSourceTime().getUtcTime());
		if (value instanceof String) {
			industrialPayload.setDataType(DataType.String);
			industrialPayload.setValue(value.toString());
		} else if (value instanceof Integer) {
			industrialPayload.setDataType(DataType.Integer);
			industrialPayload.setValue(value.toString());
		} else if (value instanceof Boolean) {
			industrialPayload.setDataType(DataType.Boolean);
			industrialPayload.setValue(value.toString());
		} else if (value instanceof Double) {
			industrialPayload.setDataType(DataType.Double);
			industrialPayload.setValue(value.toString());
		} else if (value instanceof Float) {
			industrialPayload.setDataType(DataType.Float);
			industrialPayload.setValue(value.toString());
		} else if (value instanceof java.util.UUID) {
			industrialPayload.setDataType(DataType.UUID);
			industrialPayload.setValue(value.toString());
		} else if (value instanceof Short) {
			industrialPayload.setDataType(DataType.Short);
			industrialPayload.setValue(value.toString());
		} else if (value instanceof Long) {
			industrialPayload.setDataType(DataType.Long);
			industrialPayload.setValue(value.toString());
		} else if (value instanceof Byte) {
			industrialPayload.setDataType(DataType.Byte);
			industrialPayload.setValue(value.toString());
		} else if (value instanceof org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte) {
			industrialPayload.setDataType(DataType.UByte);
			industrialPayload.setValue(value.toString());
		} else if (value instanceof org.eclipse.milo.opcua.stack.core.types.builtin.ByteString) {
			industrialPayload.setDataType(DataType.ByteString);
			industrialPayload.setValue(value.toString());
		} else if (value instanceof org.eclipse.milo.opcua.stack.core.types.builtin.DateTime) {
			industrialPayload.setDataType(DataType.DateTime);
			industrialPayload.setValue(value.toString());
		} else if (value instanceof org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort) {
			industrialPayload.setDataType(DataType.UShort);
			industrialPayload.setValue(value.toString());
		} else if (value instanceof org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger) {
			industrialPayload.setDataType(DataType.UInteger);
			industrialPayload.setValue(value.toString());
		} else if (value instanceof org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong) {
			industrialPayload.setDataType(DataType.ULong);
			industrialPayload.setValue(value.toString());
		} else if (value instanceof org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement) {
			industrialPayload.setDataType(DataType.XmlElement);
			industrialPayload.setValue(value.toString());
		} else {
			industrialPayload.setDataType(DataType.UNKNOW);
			industrialPayload.setValue(value.toString());
		}
		final IndustrialMessage message = new IndustrialMessage(industrialPayload);
		outputChannel.getChannel().send(message);
	}

	@Override
	public void close() throws Exception {
		opcUaClientService.getOpcUaClient()
				.deleteSubscriptions(Collections.singletonList(uaSubscription.getSubscriptionId()));

	}

}
