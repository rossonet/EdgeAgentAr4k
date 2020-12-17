package org.ar4k.agent.opcua;

public class Enumerator {

  public enum SecurityMode {
    signAndEncrypt, sign, none
  }

  public enum CryptoMode {
    basic256Sha256, basic256, basic128Rsa15, none
  }

  public enum DeadbandType {
    none, absolute, percent
  }

  public enum DataChangeTrigger {
    status, statusOrValue, statusOrValueOrTimestamp
  }

}
