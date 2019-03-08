package org.ar4k.agent.pcap.ice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MarketSnapshotMessage implements IceObject {

  private static final long serialVersionUID = -3813884420628020447L;
  private Integer marketID;
  private Short marketType;
  private char tradingStatus;
  private Integer volume;
  private Integer blockVolume;
  private Integer eFSVolume;
  private Integer eFPVolume;
  private Integer openInterest;
  private Long openingPrice;
  private Long settlementPriceWithDealPricePrecision;
  private Long high;
  private Long low;
  private Long vWAP;

  @Override
  public char getTypeCode() {
    return 'C';
  }

  @Override
  public void valueOf(byte[] bytes) throws IOException {
    InputStream targetStream = new ByteArrayInputStream(bytes);
    marketID = Helper.popInteger(targetStream);
    marketType = Helper.popShort(targetStream);
    tradingStatus = Helper.popChar(targetStream);
    volume = Helper.popInteger(targetStream);
    blockVolume = Helper.popInteger(targetStream);
    eFSVolume = Helper.popInteger(targetStream);
    eFPVolume = Helper.popInteger(targetStream);
    openInterest = Helper.popInteger(targetStream);
    openingPrice = Helper.popLong(targetStream);
    settlementPriceWithDealPricePrecision = Helper.popLong(targetStream);
    high = Helper.popLong(targetStream);
    low = Helper.popLong(targetStream);
    vWAP = Helper.popLong(targetStream);
  }

  @Override
  public String toString() {
    String rit = "-- Market Snapshot Message --\nmarketID:" + marketID + "\n";
    rit += "marketType:" + marketType + "\n";
    rit += "tradingStatus:" + tradingStatus + "\n";
    rit += "volume:" + volume + "\n";
    rit += "blockVolume:" + blockVolume + "\n";
    rit += "eFSVolume:" + eFSVolume + "\n";
    rit += "eFPVolume:" + eFPVolume + "\n";
    rit += "openInterest:" + openInterest + "\n";
    rit += "openingPrice:" + openingPrice + "\n";
    rit += "settlementPriceWithDealPricePrecision:" + settlementPriceWithDealPricePrecision + "\n";
    rit += "high:" + high + "\n";
    rit += "low:" + low + "\n";
    rit += "vWAP:" + vWAP + "\n";
    return rit;
  }

  public Integer getMarketID() {
    return marketID;
  }

  public Short getMarketType() {
    return marketType;
  }

  public char getTradingStatus() {
    return tradingStatus;
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

  public Integer getOpenInterest() {
    return openInterest;
  }

  public Long getOpeningPrice() {
    return openingPrice;
  }

  public Long getSettlementPriceWithDealPricePrecision() {
    return settlementPriceWithDealPricePrecision;
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
}
