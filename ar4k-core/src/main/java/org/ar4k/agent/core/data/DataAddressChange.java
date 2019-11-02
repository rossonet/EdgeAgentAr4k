package org.ar4k.agent.core.data;

public interface DataAddressChange {

  void onDataAddressUpdate(Ar4kChannel updatedChannel);

  void onDataAddressCreate(Ar4kChannel createdChannel);

  void onDataAddressDelete(String deletedChannel);

}
