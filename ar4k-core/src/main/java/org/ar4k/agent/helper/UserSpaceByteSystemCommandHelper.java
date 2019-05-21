package org.ar4k.agent.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;

public class UserSpaceByteSystemCommandHelper {

  private UserSpaceByteSystemCommandHelper() {
  }

  public static String runShellCommandLineByteToByte(String shellCommand, String endCharacter, Logger logger,
      InputStream input, OutputStream output) {
    Integer errori = 0;
    boolean running = true;
    Process pr = null;
    Runtime rt = Runtime.getRuntime();
    try {
      pr = rt.exec(shellCommand);
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    InputStream is = pr.getInputStream();
    OutputStream os = pr.getOutputStream();
    InputStream es = pr.getErrorStream();
    Reader rin = new InputStreamReader(is, StandardCharsets.UTF_8);
    Reader rerr = new InputStreamReader(es, StandardCharsets.UTF_8);
    Writer rout = new OutputStreamWriter(os, StandardCharsets.UTF_8);
    Reader si = new InputStreamReader(input, StandardCharsets.UTF_8);
    Writer so = new OutputStreamWriter(output, StandardCharsets.UTF_8);
    while (running && errori < 10) {
      try {
        if (rin != null && rin.ready()) {
          char[] c = new char[1];
          rin.read(c, 0, 1);
          filterOutCharacter(so, c);
          so.flush();
        }
      } catch (IOException e) {
        errori++;
      }
      try {
        if (rerr != null && rerr.ready()) {
          char[] c = new char[1];
          rerr.read(c, 0, 1);
          filterOutCharacter(so, c);
          so.flush();
        }
      } catch (IOException e) {
        errori++;
      }
      try {
        if (si.ready()) {
          char c[] = new char[1];
          si.read(c, 0, 1);
          if (Integer.parseInt(endCharacter) == Character.valueOf(c[0]).charValue()) {
            running = false;
            Thread.sleep(500L);
          }
          if (rout != null) {
            rout.write(c);
            rout.flush();
          }
        }
      } catch (IOException | InterruptedException e) {
        errori++;
      }
    }
    // System.out.println("close the readers after " + String.valueOf(errori) + "
    // errors. wait...");
    if (pr != null) {
      pr.destroyForcibly();
      pr = null;
      // System.out.println("process closed");
    }
    if (rin != null) {
      try {
        rin.close();
        rin = null;
        // System.out.println("reader input stream closed");
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    if (rerr != null) {
      try {
        rerr.close();
        rerr = null;
        // System.out.println("reader error stream closed");
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    if (rout != null) {
      try {
        rout.close();
        rout = null;
        // System.out.println("writer output stream closed");
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    if (es != null) {
      try {
        es.close();
        es = null;
        // System.out.println("error stream closed");
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    if (is != null) {
      try {
        is.close();
        is = null;
        // System.out.println("input stream closed");
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    if (os != null) {
      try {
        os.close();
        os = null;
        // System.out.println("output stream closed");
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    try {
      System.in.reset();
      // System.out.println("system in reseted");
    } catch (IOException e1) {
      errori++;
      logger.warn(e1.getMessage());
    }
    // System.out.println("shell closed");
    /*
     * // throw new Ar4kException("running shell terminated"); if (si != null) { try
     * { si.close(); si = null; System.out.println("reader input closed"); } catch
     * (IOException e) { errori++; logger.warn(e.getMessage()); } } if (so != null)
     * { try { so.close(); so = null; System.out.println("writer output closed"); }
     * catch (IOException e) { errori++; logger.warn(e.getMessage()); } }
     */
    return "session terminated with " + String.valueOf(errori) + " errors";
  }

  private static void filterOutCharacter(Writer so, char[] c) throws IOException {
    so.write(c);
  }

  @SuppressWarnings("unused")
  private boolean processIsTerminated(Process process) {
    try {
      process.exitValue();
    } catch (IllegalThreadStateException itse) {
      return false;
    }
    return true;
  }

}
