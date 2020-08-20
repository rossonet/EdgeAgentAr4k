package org.ar4k.agent.core.data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.ar4k.agent.core.Anima;
import org.ar4k.agent.core.data.channels.INoDataChannel;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.springframework.messaging.MessageChannel;

public class DataAddress implements AutoCloseable {

	private static final EdgeLogger logger = (EdgeLogger) EdgeStaticLoggerBinder.getSingleton().getLoggerFactory()
			.getLogger(DataAddress.class.toString());

	protected final Anima anima;

	public DataAddress(Anima anima) {
		dataChannels.clear();
		this.anima = anima;
	}

	protected final Collection<Ar4kChannel> dataChannels = new HashSet<>();

	protected String defaultScope = "ar4k-ai";
	protected String levelSeparator = "/";

	protected transient Set<DataAddressChange> callbacks = new HashSet<>();

	protected transient Set<Ar4kManagedNamespace> managedNameSpace = new HashSet<>();

	public Collection<Ar4kChannel> getDataChannels() {
		return dataChannels;
	}

	public Collection<String> getKnowTags() {
		final Set<String> result = new HashSet<>();
		for (final Ar4kChannel c : getDataChannels()) {
			result.addAll(c.getTags());
		}
		return result;
	}

	private void initializeExternalNameSpace(Ar4kManagedNamespace nameSpace) {
		// TODO chiamate per configurare i managed name spaces
	}

	private void updateExternalNameSpace(Ar4kManagedNamespace nameSpace) {
		// TODO chiamate per configurare i managed name spaces

	}

	public Collection<Ar4kChannel> getDataChannels(DataChannelFilter filter) {
		final List<Ar4kChannel> result = new ArrayList<>();
		for (final Ar4kChannel singleChannel : getDataChannels()) {
			if (filter.filtersMatch(singleChannel)) {
				result.add(singleChannel);
			}
		}
		return result;
	}

	public Ar4kChannel getChannel(String channelId) {
		Ar4kChannel r = null;
		for (final Ar4kChannel c : getDataChannels()) {
			if (c.getBrowseName() != null && c.getBrowseName().equals(channelId)) {
				r = c;
				break;
			}
		}
		return r;
	}

	public void callAddressSpaceRefresh(Ar4kChannel nodeUpdated) {
		for (final DataAddressChange target : callbacks) {
			target.onDataAddressUpdate(nodeUpdated);
		}
		for (final Ar4kManagedNamespace nameSpace : managedNameSpace) {
			updateExternalNameSpace(nameSpace);
		}
	}

	public void addCallbackOnChange(DataAddressChange callback) {
		callbacks.add(callback);
	}

	public void removeCallbackOnChange(DataAddressChange callback) {
		callbacks.remove(callback);
	}

	public void addManagedNamespace(Ar4kManagedNamespace managedObject) {
		managedNameSpace.add(managedObject);
		initializeExternalNameSpace(managedObject);
	}

	public void removeManagedNamespace(Ar4kManagedNamespace managedObject) {
		managedNameSpace.remove(managedObject);
	}

	public Ar4kChannel createOrGetDataChannel(String nodeId, Class<? extends Ar4kChannel> channelType,
			String description, String father, String scope, Collection<String> tags) {
		return createOrGetDataChannel(nodeId, channelType, description, getChannel(father), scope, tags);
	}

	public Ar4kChannel createOrGetDataChannel(String nodeId, Class<? extends Ar4kChannel> channelType,
			String description, Ar4kChannel father, String scope, Collection<String> tags) {
		Ar4kChannel returnChannel = null;
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
				final Constructor<?> ctor = channelType.getConstructor();
				logger.info("create channel " + nodeId + " of type " + channelType);
				returnChannel = (Ar4kChannel) ctor.newInstance();
				returnChannel.setBrowseName(nodeId);
				returnChannel.setDescription(description);
				this.dataChannels.add(returnChannel);
				returnChannel.setDataAddress(this);
				if (father != null) {
					returnChannel.setFatherOfScope(scope, father);
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
		return returnChannel;
	}

	public void removeDataChannel(Ar4kChannel dataChannel, boolean clearList) {
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
		for (final Ar4kChannel target : this.dataChannels) {
			removeDataChannel(target, false);
		}
		this.dataChannels.clear();
	}

	public Collection<String> listChannels() {
		final Collection<String> result = new ArrayList<>();
		for (final Ar4kChannel c : getDataChannels()) {
			result.add(c.getBrowseName() + " [" + c.getDescription() + "] "
					+ (c.getChannel() != null ? c.getChannel().getBeanName() : c.getStatus())
					+ (c.getChannel() != null ? " -> " + c.getChannel().getFullChannelName() : ""));
		}
		return result;
	}

	public Collection<String> listSpringIntegrationChannels() {
		final Collection<String> result = new ArrayList<>();
		for (final Entry<String, MessageChannel> s : Anima.getApplicationContext().getBeansOfType(MessageChannel.class)
				.entrySet()) {
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
		final StringBuilder builder = new StringBuilder();
		builder.append("DataAddress [");
		if (anima != null)
			builder.append("anima=").append(anima.getAgentUniqueName()).append(", ");
		if (defaultScope != null)
			builder.append("defaultScope=").append(defaultScope).append(", ");
		if (levelSeparator != null)
			builder.append("levelSeparator=").append(levelSeparator).append(", ");
		builder.append("]");
		return builder.toString();
	}

}
