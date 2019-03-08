package org.ar4k.agent.pcap.ice;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Helper {

  private Helper() {
    System.out.println("just static methods");
  }

  public static char popChar(InputStream inputStream) throws IOException {
    byte[] character = new byte[1];
    inputStream.read(character, 0, 1);
    return (char) character[0];
  }

  public static String popString(InputStream inputStream, int size) throws IOException {
    byte[] characters = new byte[size];
    inputStream.read(characters, 0, size);
    String ritorno = null;
    for (byte cb : characters) {
      ritorno += (char) cb;
    }
    return ritorno;
  }

  public static Integer popInteger(InputStream inputStream) throws IOException {
    byte[] integer = new byte[4];
    inputStream.read(integer, 0, 4);
    ByteBuffer wrapped = ByteBuffer.wrap(integer);
    return wrapped.getInt();
  }

  public static Long popLong(InputStream inputStream) throws IOException {
    byte[] integer = new byte[8];
    inputStream.read(integer, 0, 8);
    ByteBuffer wrapped = ByteBuffer.wrap(integer);
    return wrapped.getLong();
  }

  public static Short popShort(InputStream inputStream) throws IOException {
    byte[] integer = new byte[2];
    inputStream.read(integer, 0, 2);
    ByteBuffer wrapped = ByteBuffer.wrap(integer);
    return wrapped.getShort();
  }

}
