package org.ar4k.agent.pcap.ice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DeleteOrderMessage implements IceObject {
  private static final long serialVersionUID = 11672759811527154L;
  private Integer marketID;
  private Long orderID;
  private Long dateTime;
  private Integer sequenceWithinMillis;

  @Override
  public char getTypeCode() {
    return 'F';
  }

  @Override
  public void valueOf(byte[] bytes) throws IOException {
    InputStream targetStream = new ByteArrayInputStream(bytes);
    marketID = Helper.popInteger(targetStream);
    orderID = Helper.popLong(targetStream);
    dateTime = Helper.popLong(targetStream);
    sequenceWithinMillis = Helper.popInteger(targetStream);
  }

  @Override
  public String toString() {
    String rit = "-- Delete Order Message --\nmarketID:" + marketID + "\n";
    rit += "orderID:" + orderID + "\n";
    rit += "dateTime:" + dateTime + "\n";
    rit += "sequenceWithinMillis:" + sequenceWithinMillis + "\n";
    return rit;
  }

  public Integer getMarketID() {
    return marketID;
  }

  public Long getOrderID() {
    return orderID;
  }

  public Long getDateTime() {
    return dateTime;
  }

  public Integer getSequenceWithinMillis() {
    return sequenceWithinMillis;
  }

}
