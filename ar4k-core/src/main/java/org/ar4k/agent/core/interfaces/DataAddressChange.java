package org.ar4k.agent.core.interfaces;

public interface DataAddressChange {

  void onDataAddressUpdate(EdgeChannel updatedChannel);

  void onDataAddressCreate(EdgeChannel createdChannel);

  void onDataAddressDelete(String deletedChannel);

}
