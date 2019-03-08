package org.ar4k.agent.pcap.ice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MarketStatisticsMessage implements IceObject {

  private static final long serialVersionUID = -3813884420628020447L;
  private Integer marketID;
  private Integer volume;
  private Integer blockVolume;
  private Integer eFSVolume;
  private Integer eFPVolume;
  private Long high;
  private Long low;
  private Long vWAP;
  private Long dateTime;

  @Override
  public char getTypeCode() {
    return 'J';
  }

  @Override
  public void valueOf(byte[] bytes) throws IOException {
    InputStream targetStream = new ByteArrayInputStream(bytes);
    marketID = Helper.popInteger(targetStream);
    volume = Helper.popInteger(targetStream);
    blockVolume = Helper.popInteger(targetStream);
    eFSVolume = Helper.popInteger(targetStream);
    eFPVolume = Helper.popInteger(targetStream);
    high = Helper.popLong(targetStream);
    low = Helper.popLong(targetStream);
    vWAP = Helper.popLong(targetStream);
    dateTime = Helper.popLong(targetStream);
  }

  @Override
  public String toString() {
    String rit = "-- Market Statistics Message --\nmarketID:" + marketID + "\n";
    rit += "volume:" + volume + "\n";
    rit += "blockVolume:" + blockVolume + "\n";
    rit += "eFSVolume:" + eFSVolume + "\n";
    rit += "eFPVolume:" + eFPVolume + "\n";
    rit += "high:" + high + "\n";
    rit += "low:" + low + "\n";
    rit += "vWAP:" + vWAP + "\n";
    rit += "dateTime:" + dateTime + "\n";
    return rit;
  }

  public Integer getMarketID() {
    return marketID;
  }

  public Integer getVolume() {
    return volume;
  }

  public Integer getBlockVolume() {
    return blockVolume;
  }

  public Integer geteFSVolume() {
    return eFSVolume;
  }

  public Integer geteFPVolume() {
    return eFPVolume;
  }

  public Long getHigh() {
    return high;
  }

  public Long getLow() {
    return low;
  }

  public Long getvWAP() {
    return vWAP;
  }

  public Long getDateTime() {
    return dateTime;
  }

}
