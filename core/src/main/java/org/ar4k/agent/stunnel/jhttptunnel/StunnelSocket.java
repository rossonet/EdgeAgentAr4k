/* -*-mode:java; c-basic-offset:2; -*- */
/*
Copyright (c) 2004 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright 
     notice, this list of conditions and the following disclaimer in 
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package org.ar4k.agent.stunnel.jhttptunnel;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class StunnelSocket {
  Socket socket = null;
  private DataInputStream dataInputStream = null;
  private OutputStream os = null;

  StunnelSocket(Socket s) throws IOException {

    try {
      s.setTcpNoDelay(true);
    } catch (Exception e) {
      System.out.println(e + " tcpnodelay");
    }
    socket = s;
    BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
    dataInputStream = new DataInputStream(bis);
    os = s.getOutputStream();
  }

  InputStream getInputStream() {
    try {
      return dataInputStream;
    } catch (Exception e) {
    }
    return null;
  }

  public void close() {
    try {
      socket.shutdownOutput();
      dataInputStream.close();
      os.close();
      socket.close();
    } catch (IOException e) {
    }
  }

  public int read(byte[] buf, int s, int len) {
    try {
      return dataInputStream.read(buf, s, len);
    } catch (IOException e) {
      return -1;
    }
  }

  public int readByte() {
    try {
      int r = dataInputStream.readByte();
      return (r & 0xff);
    } catch (IOException e) {
      return (-1);
    }
  }

  public String readLine() {
    try {
      return (dataInputStream.readLine());
    } catch (IOException e) {
      return (null);
    }
  }

  public void write(byte[] foo, int start, int length) throws IOException {
    os.write(foo, start, length);
  }

  public void p(String s) throws IOException {
    os.write(s.getBytes());
  }

  public void print(String s) throws IOException {
    os.write(s.getBytes());
  }

  public void p(byte[] s) throws IOException {
    os.write(s);
  }

  public void print(byte[] s) throws IOException {
    os.write(s);
  }

  public void p(char c) throws IOException {
    os.write(c);
  }

  public void print(char c) throws IOException {
    os.write(c);
  }

  public void p(int c) throws IOException {
    os.write(Integer.toString(c).getBytes());
  }

  public void print(int c) throws IOException {
    os.write(Integer.toString(c).getBytes());
  }

  static final private byte[] _rn = "\r\n".getBytes();

  public void pn(String s) throws IOException {
    println(s);
  }

  public void println(String s) throws IOException {
    print(s);
    print(_rn);
  }

  public void flush() throws IOException {
    os.flush();
  }
}
