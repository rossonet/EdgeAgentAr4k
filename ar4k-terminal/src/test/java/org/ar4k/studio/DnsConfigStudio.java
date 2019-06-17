package org.ar4k.studio;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import org.ar4k.agent.config.Ar4kConfig;
import org.ar4k.agent.helper.ConfigHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

public class DnsConfigStudio {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void test() {
    System.out.println(dnsConfigDownload("d-ar4k.a.ar4k.net"));// .toString());
  }

  private Ar4kConfig dnsConfigDownload(String dnsConfig) {
    StringBuilder resultString = new StringBuilder();
    String hostPart = dnsConfig.split("\\.")[0];
    String domainPart = dnsConfig.replaceAll("^" + hostPart, "");
    System.out.println("Using H:" + hostPart + " D:" + domainPart);
    Set<String> errors = new HashSet<>();
    try {
      Lookup l = new Lookup(hostPart + "-max" + domainPart, Type.TXT, DClass.IN);
      l.setResolver(new SimpleResolver());
      l.run();
      if (l.getResult() == Lookup.SUCCESSFUL) {
        int chunkSize = Integer.valueOf(l.getAnswers()[0].rdataToString().replaceAll("^\"", "").replaceAll("\"$", ""));
        if (chunkSize > 0) {
          for (int c = 0; c < chunkSize; c++) {
            Lookup cl = new Lookup(hostPart + "-" + String.valueOf(c) + domainPart, Type.TXT, DClass.IN);
            cl.setResolver(new SimpleResolver());
            cl.run();
            if (cl.getResult() == Lookup.SUCCESSFUL) {
              resultString.append(cl.getAnswers()[0].rdataToString().replaceAll("^\"", "").replaceAll("\"$", ""));
            } else {
              errors.add(
                  "error in chunk " + hostPart + "-" + String.valueOf(c) + domainPart + " -> " + cl.getErrorString());
            }
          }
        } else {
          errors.add("error, size of data is " + l.getAnswers()[0].rdataToString());
        }
      } else {
        errors.add("no " + hostPart + "-max" + domainPart + " record found -> " + l.getErrorString());
      }
      if (!errors.isEmpty()) {
        System.out.println(errors.toString());
      }
    } catch (UnknownHostException | TextParseException e) {
      e.printStackTrace();
    }
    try {
      return (Ar4kConfig) ((errors.isEmpty() && resultString.length() > 0)
          ? ConfigHelper.fromBase64(resultString.toString())
          : null);
    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
      return null;
    }
  }

}
