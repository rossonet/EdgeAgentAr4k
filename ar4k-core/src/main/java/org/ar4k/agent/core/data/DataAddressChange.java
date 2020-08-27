package org.ar4k.agent.core.data;

public interface DataAddressChange {

  void onDataAddressUpdate(EdgeChannel updatedChannel);

  void onDataAddressCreate(EdgeChannel createdChannel);

  void onDataAddressDelete(String deletedChannel);

}
