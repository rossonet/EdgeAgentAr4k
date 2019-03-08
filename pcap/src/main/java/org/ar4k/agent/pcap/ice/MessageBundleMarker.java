package org.ar4k.agent.pcap.ice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MessageBundleMarker implements IceObject {

  private static final long serialVersionUID = 6599708595149809467L;
  private char startOrEnd;
  private Long tradeTransactionID;
  private char isTransactionEnd;

  @Override
  public char getTypeCode() {
    return 'T';
  }

  @Override
  public void valueOf(byte[] bytes) throws IOException {
    InputStream targetStream = new ByteArrayInputStream(bytes);
    startOrEnd = Helper.popChar(targetStream);
    tradeTransactionID = Helper.popLong(targetStream);
    isTransactionEnd = Helper.popChar(targetStream);
  }

  @Override
  public String toString() {
    String rit = "-- Message Bundle Marker --\nStartOrEnd:" + startOrEnd + "\n";
    rit += "TradeTransactionID:" + tradeTransactionID + "\n";
    rit += "IsTransactionEnd:" + isTransactionEnd + "\n";
    return rit;
  }

  public char getStartOrEnd() {
    return startOrEnd;
  }

  public Long getTradeTransactionID() {
    return tradeTransactionID;
  }

  public char getIsTransactionEnd() {
    return isTransactionEnd;
  }

}
