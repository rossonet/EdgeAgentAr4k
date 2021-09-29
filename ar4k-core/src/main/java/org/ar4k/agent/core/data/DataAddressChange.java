package org.ar4k.agent.core.data;

import org.ar4k.agent.core.data.channels.EdgeChannel;

public interface DataAddressChange {

  void onDataAddressUpdate(EdgeChannel updatedChannel);

  void onDataAddressCreate(EdgeChannel createdChannel);

  void onDataAddressDelete(String deletedChannel);

}
