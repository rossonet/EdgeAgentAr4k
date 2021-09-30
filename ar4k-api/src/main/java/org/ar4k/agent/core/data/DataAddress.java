package org.ar4k.agent.core.data;

import java.util.Collection;

import org.ar4k.agent.core.data.channels.EdgeChannel;

public interface DataAddress extends AutoCloseable {

	String getLevelSeparator();

	void callAddressSpaceRefresh(EdgeChannel abstractChannel);

	String getDefaultScope();

	Collection<? extends EdgeChannel> getDataChannels();

	void clearDataChannels();

	Collection<String> listChannels();

	EdgeChannel createOrGetDataChannel(String nodeId, Class<? extends EdgeChannel> channelType, String description,
			String father, String scope, Collection<String> tags, DataServiceOwner serviceOwner);

	void removeDataChannel(EdgeChannel dataChannel, boolean clearList);

	EdgeChannel createOrGetDataChannel(String nodePartialId, Class<? extends EdgeChannel> channelType,
			String description, EdgeChannel father, String scope, Collection<String> tags,
			DataServiceOwner serviceOwner);

	Collection<? extends EdgeChannel> getDataChannels(IDataChannelFilter filter);

	EdgeChannel getChannel(String channelId);

	void removeDataChannel(String dataChannel, boolean clearList);

	Collection<String> listSpringIntegrationChannels();

	Collection<String> getKnowTags();

	void addCallbackOnChange(DataAddressChange callback);

}
