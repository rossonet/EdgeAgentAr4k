package org.ar4k.agent.core.data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.ar4k.agent.core.Homunculus;
import org.ar4k.agent.core.data.channels.INoDataChannel;
import org.ar4k.agent.core.interfaces.DataAddressChange;
import org.ar4k.agent.core.interfaces.DataServiceOwner;
import org.ar4k.agent.core.interfaces.EdgeChannel;
import org.ar4k.agent.core.interfaces.EdgeManagedNamespace;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.springframework.messaging.MessageChannel;

public class DataAddress implements AutoCloseable {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(DataAddress.class.toString());

	protected final Homunculus homunculus;

	protected DataServiceOwner serviceOwner;

	public DataAddress(Homunculus homunculus, DataServiceOwner serviceOwner) {
		dataChannels.clear();
		this.homunculus = homunculus;
		this.serviceOwner = serviceOwner;
	}

	protected final Collection<EdgeChannel> dataChannels = new HashSet<>();

	protected String defaultScope = "ar4k-system";
	protected String levelSeparator = "/";

	protected transient Set<DataAddressChange> callbacks = new HashSet<>();

	protected transient Set<EdgeManagedNamespace> managedNameSpace = new HashSet<>();

	public Collection<EdgeChannel> getDataChannels() {
		return dataChannels;
	}

	public Collection<String> getKnowTags() {
		final Set<String> result = new HashSet<>();
		for (final EdgeChannel c : getDataChannels()) {
			result.addAll(c.getTags());
		}
		return result;
	}

	private void initializeExternalNameSpace(EdgeManagedNamespace nameSpace) {
		// TODO chiamate per configurare i managed name spaces
	}

	private void updateExternalNameSpace(EdgeManagedNamespace nameSpace) {
		// TODO chiamate per configurare i managed name spaces

	}

	public Collection<EdgeChannel> getDataChannels(DataChannelFilter filter) {
		final List<EdgeChannel> result = new ArrayList<>();
		for (final EdgeChannel singleChannel : getDataChannels()) {
			if (filter.filtersMatch(singleChannel)) {
				result.add(singleChannel);
			}
		}
		return result;
	}

	public EdgeChannel getChannel(String channelId) {
		EdgeChannel r = null;
		for (final EdgeChannel c : getDataChannels()) {
			if (c.getBrowseName() != null && c.getBrowseName().equals(channelId)) {
				r = c;
				break;
			}
		}
		return r;
	}

	public void callAddressSpaceRefresh(EdgeChannel nodeUpdated) {
		for (final DataAddressChange target : callbacks) {
			target.onDataAddressUpdate(nodeUpdated);
		}
		for (final EdgeManagedNamespace nameSpace : managedNameSpace) {
			updateExternalNameSpace(nameSpace);
		}
	}

	public void addCallbackOnChange(DataAddressChange callback) {
		callbacks.add(callback);
	}

	public void removeCallbackOnChange(DataAddressChange callback) {
		callbacks.remove(callback);
	}

	public void addManagedNamespace(EdgeManagedNamespace managedObject) {
		managedNameSpace.add(managedObject);
		initializeExternalNameSpace(managedObject);
	}

	public void removeManagedNamespace(EdgeManagedNamespace managedObject) {
		managedNameSpace.remove(managedObject);
	}

	public EdgeChannel createOrGetDataChannel(String nodeId, Class<? extends EdgeChannel> channelType,
			String description, String father, String scope, Collection<String> tags, DataServiceOwner serviceOwner) {
		return createOrGetDataChannel(nodeId, channelType, description, getChannel(father), scope, tags, serviceOwner);
	}

	public EdgeChannel createOrGetDataChannel(String nodePartialId, Class<? extends EdgeChannel> channelType,
			String description, EdgeChannel father, String scope, Collection<String> tags,
			DataServiceOwner serviceOwner) {
		String nodeId = serviceOwner.getServiceName() + levelSeparator + nodePartialId;
		EdgeChannel returnChannel = null;
		if (getChannel(nodeId) != null) {
			returnChannel = getChannel(nodeId);
			this.dataChannels.add(returnChannel);
			returnChannel.setDataAddress(this);
			if (channelType != null && !channelType.equals(getChannel(nodeId).getClass())) {
				logger.warn("GET A DATA CHANNEL OF DIFFERENT TYPE: " + channelType.getName() + " -> "
						+ getChannel(nodeId).getClass().getName());
			}
		} else {
			try {
				if (channelType == null) {
					channelType = INoDataChannel.class;
				}
				final Constructor<?> ctor = channelType.getConstructor(DataServiceOwner.class);
				logger.info("create channel " + nodeId + " of type " + channelType);
				returnChannel = (EdgeChannel) ctor.newInstance(serviceOwner);
				returnChannel.setBrowseName(nodeId);
				returnChannel.setDescription(description);
				this.dataChannels.add(returnChannel);
				returnChannel.setDataAddress(this);
				if (father != null) {
					returnChannel.setFatherOfScope(scope, father);
				} else if (homunculus.getDataAddress() != null
						&& homunculus.getDataAddress().getSystemChannel() != null) {
					returnChannel.setFatherOfScope(scope, homunculus.getDataAddress().getSystemChannel());
				}
				logger.info(returnChannel.getBrowseName() + " [" + returnChannel.getDescription() + "] started");
				for (final DataAddressChange target : callbacks) {
					target.onDataAddressCreate(returnChannel);
				}
			} catch (final Exception a) {
				logger.logException(a);
			}
		}
		if (tags != null) {
			for (final String t : tags) {
				returnChannel.addTag(t);
			}
		}
		returnChannel.setBaseName(nodePartialId);
		return returnChannel;
	}

	public void removeDataChannel(EdgeChannel dataChannel, boolean clearList) {
		try {
			logger.info("Try to remove dataChannel " + dataChannel.getBrowseName());
			dataChannel.close();
		} catch (final Exception e) {
			logger.logException(e);
		}
		if (clearList) {
			this.dataChannels.remove(dataChannel);
		}
		for (final DataAddressChange target : callbacks) {
			target.onDataAddressDelete(dataChannel.toString());
		}
		logger.info(dataChannel.getBrowseName() + " [" + dataChannel.getDescription() + "] removed");
	}

	public void removeDataChannel(String dataChannel, boolean clearList) {
		removeDataChannel(getChannel(dataChannel), clearList);
	}

	public void clearDataChannels() {
		for (final EdgeChannel target : this.dataChannels) {
			removeDataChannel(target, false);
		}
		this.dataChannels.clear();
	}

	public Collection<String> listChannels() {
		final Collection<String> result = new ArrayList<>();
		for (final EdgeChannel c : getDataChannels()) {
			result.add(c.getBrowseName() + " [" + c.getDescription() + "] " + c.getChannelType() + " " + c.getStatus());
		}
		return result;
	}

	public Collection<String> listSpringIntegrationChannels() {
		final Collection<String> result = new ArrayList<>();
		for (final Entry<String, MessageChannel> s : Homunculus.getApplicationContext()
				.getBeansOfType(MessageChannel.class).entrySet()) {
			result.add(s.getKey() + " -> " + s.getValue());
		}
		return result;
	}

	public String getDefaultScope() {
		return defaultScope;
	}

	public void setDefaultScope(String defaultScope) {
		this.defaultScope = defaultScope;
	}

	public String getLevelSeparator() {
		return levelSeparator;
	}

	public void setLevelSeparator(String levelSeparator) {
		this.levelSeparator = levelSeparator;
	}

	@Override
	public void close() throws Exception {
		clearDataChannels();
		callbacks.clear();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DataAddress [");
		builder.append("serviceOwner=");
		builder.append(serviceOwner.getServiceName());
		builder.append(", defaultScope=");
		builder.append(defaultScope);
		builder.append(", levelSeparator=");
		builder.append(levelSeparator);
		builder.append("]");
		return builder.toString();
	}

}
