package org.ar4k.agent.pcap.ice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TradeMessage implements IceObject {

  private static final long serialVersionUID = 4469647595367580697L;
  private Integer marketID;
  private Long orderID;
  private Long tradeID;
  private char isSystemPricedLeg;
  private Long price;
  private Integer quantity;
  private char oldOffMarketTradeType;
  private Long transactDateTime;
  private char systemPricedLegType;
  private char isImpliedSpreadAtMarketOpen;
  private char isAdjustedTrade;
  private char aggressorSide;
  private String extraFlags;
  private String offMarketTradeType;
  private Integer sequenceWithinMillis;

  @Override
  public char getTypeCode() {
    return 'G';
  }

  @Override
  public void valueOf(byte[] bytes) throws IOException {
    InputStream targetStream = new ByteArrayInputStream(bytes);
    marketID = Helper.popInteger(targetStream);
    orderID = Helper.popLong(targetStream);
    tradeID = Helper.popLong(targetStream);
    isSystemPricedLeg = Helper.popChar(targetStream);
    price = Helper.popLong(targetStream);
    quantity = Helper.popInteger(targetStream);
    oldOffMarketTradeType = Helper.popChar(targetStream);
    transactDateTime = Helper.popLong(targetStream);
    systemPricedLegType = Helper.popChar(targetStream);
    isImpliedSpreadAtMarketOpen = Helper.popChar(targetStream);
    isAdjustedTrade = Helper.popChar(targetStream);
    aggressorSide = Helper.popChar(targetStream);
    extraFlags = Helper.popString(targetStream, 1);
    offMarketTradeType = Helper.popString(targetStream, 3);
    sequenceWithinMillis = Helper.popInteger(targetStream);
  }

  @Override
  public String toString() {
    String rit = "-- Trade Message --\nmarketID:" + marketID + "\n";
    rit += "orderID:" + orderID + "\n";
    rit += "tradeID:" + tradeID + "\n";
    rit += "isSystemPricedLeg:" + isSystemPricedLeg + "\n";
    rit += "price:" + price + "\n";
    rit += "quantity:" + quantity + "\n";
    rit += "oldOffMarketTradeType:" + oldOffMarketTradeType + "\n";
    rit += "transactDateTime:" + transactDateTime + "\n";
    rit += "systemPricedLegType:" + systemPricedLegType + "\n";
    rit += "isImpliedSpreadAtMarketOpen:" + isImpliedSpreadAtMarketOpen + "\n";
    rit += "isAdjustedTrade:" + isAdjustedTrade + "\n";
    rit += "aggressorSide:" + aggressorSide + "\n";
    rit += "extraFlags:" + extraFlags + "\n";
    rit += "offMarketTradeType:" + offMarketTradeType + "\n";
    rit += "sequenceWithinMillis:" + sequenceWithinMillis + "\n";
    return rit;
  }

  public Integer getMarketID() {
    return marketID;
  }

  public Long getOrderID() {
    return orderID;
  }

  public Integer getSequenceWithinMillis() {
    return sequenceWithinMillis;
  }

  public Long getTradeID() {
    return tradeID;
  }

  public char getIsSystemPricedLeg() {
    return isSystemPricedLeg;
  }

  public Long getPrice() {
    return price;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public char getOldOffMarketTradeType() {
    return oldOffMarketTradeType;
  }

  public Long getTransactDateTime() {
    return transactDateTime;
  }

  public char getSystemPricedLegType() {
    return systemPricedLegType;
  }

  public char getIsImpliedSpreadAtMarketOpen() {
    return isImpliedSpreadAtMarketOpen;
  }

  public char getIsAdjustedTrade() {
    return isAdjustedTrade;
  }

  public char getAggressorSide() {
    return aggressorSide;
  }

  public String getExtraFlags() {
    return extraFlags;
  }

  public String getOffMarketTradeType() {
    return offMarketTradeType;
  }

}
