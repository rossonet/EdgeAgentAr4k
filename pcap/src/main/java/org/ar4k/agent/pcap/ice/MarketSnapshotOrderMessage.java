package org.ar4k.agent.pcap.ice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MarketSnapshotOrderMessage implements IceObject {

  private static final long serialVersionUID = 3442141385810624265L;

  private Integer marketID;
  private Long orderID;
  private Short orderSequenceID;
  private char side;
  private Long price;
  private Integer quantity;
  private char isImplied;
  private char isRFQ;
  private Long orderEntryDateTime;
  private Integer sequenceWithinMillis;

  @Override
  public char getTypeCode() {
    return 'D';
  }

  @Override
  public void valueOf(byte[] bytes) throws IOException {
    InputStream targetStream = new ByteArrayInputStream(bytes);
    marketID = Helper.popInteger(targetStream);
    orderID = Helper.popLong(targetStream);
    orderSequenceID = Helper.popShort(targetStream);
    side = Helper.popChar(targetStream);
    price = Helper.popLong(targetStream);
    quantity = Helper.popInteger(targetStream);
    isImplied = Helper.popChar(targetStream);
    isRFQ = Helper.popChar(targetStream);
    orderEntryDateTime = Helper.popLong(targetStream);
    sequenceWithinMillis = Helper.popInteger(targetStream);
  }

  @Override
  public String toString() {
    String rit = "-- Market Snapshot Order Message --\nmarketID:" + marketID + "\n";
    rit += "orderID:" + orderID + "\n";
    rit += "orderSequenceID:" + orderSequenceID + "\n";
    rit += "side:" + side + "\n";
    rit += "price:" + price + "\n";
    rit += "quantity:" + quantity + "\n";
    rit += "isImplied:" + isImplied + "\n";
    rit += "isRFQ:" + isRFQ + "\n";
    rit += "orderEntryDateTime:" + orderEntryDateTime + "\n";
    rit += "sequenceWithinMillis:" + sequenceWithinMillis + "\n";
    return rit;
  }

  public Integer getMarketID() {
    return marketID;
  }

  public Long getOrderID() {
    return orderID;
  }

  public Short getOrderSequenceID() {
    return orderSequenceID;
  }

  public char getSide() {
    return side;
  }

  public Long getPrice() {
    return price;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public char getIsImplied() {
    return isImplied;
  }

  public char getIsRFQ() {
    return isRFQ;
  }

  public Long getOrderEntryDateTime() {
    return orderEntryDateTime;
  }

  public Integer getSequenceWithinMillis() {
    return sequenceWithinMillis;
  }

}
