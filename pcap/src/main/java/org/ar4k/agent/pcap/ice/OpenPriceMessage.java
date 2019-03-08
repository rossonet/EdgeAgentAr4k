package org.ar4k.agent.pcap.ice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class OpenPriceMessage implements IceObject {

  private static final long serialVersionUID = 2185897645171406482L;
  private Integer marketID;
  private Long openPrice;
  private Long dateTime;

  @Override
  public char getTypeCode() {
    return 'N';
  }

  @Override
  public void valueOf(byte[] bytes) throws IOException {
    InputStream targetStream = new ByteArrayInputStream(bytes);
    marketID = Helper.popInteger(targetStream);
    openPrice = Helper.popLong(targetStream);
    dateTime = Helper.popLong(targetStream);
  }

  @Override
  public String toString() {
    String rit = "-- OpenPrice Message --\nmarketID:" + marketID + "\n";
    rit += "openPrice:" + openPrice + "\n";
    rit += "dateTime:" + dateTime + "\n";
    return rit;
  }

  public Integer getMarketID() {
    return marketID;
  }

  public Long getOpenPrice() {
    return openPrice;
  }

  public Long getDateTime() {
    return dateTime;
  }

}
