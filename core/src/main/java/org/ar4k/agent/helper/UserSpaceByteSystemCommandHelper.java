package org.ar4k.agent.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import org.ar4k.agent.exception.Ar4kException;
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
          so.write(c);
          so.flush();
        }
      } catch (IOException e) {
        errori++;
      }
      try {
        if (rerr != null && rerr.ready()) {
          char[] c = new char[1];
          rerr.read(c, 0, 1);
          so.write(c);
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
    System.out.println("close the readers after " + String.valueOf(errori) + " errors. wait...");
    if (pr != null) {
      pr.destroyForcibly();
      pr = null;
    }
    if (rin != null) {
      try {
        rin.close();
        rin = null;
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    if (rerr != null) {
      try {
        rerr.close();
        rerr = null;
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    if (rout != null) {
      try {
        rout.close();
        rout = null;
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    if (es != null) {
      try {
        es.close();
        es = null;
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    if (is != null) {
      try {
        is.close();
        is = null;
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    if (os != null) {
      try {
        os.close();
        os = null;
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    try {
      System.in.reset();
      throw new Ar4kException("running shell terminated");
    } catch (Ar4kException | IOException e) {
      errori++;
      logger.warn(e.getMessage());
      if (errori > 0) {
        logger.info("running shell terminated");
      }
    }

    if (si != null) {
      try {
        si.close();
        si = null;
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    if (so != null) {
      try {
        so.close();
        so = null;
      } catch (IOException e) {
        errori++;
        logger.warn(e.getMessage());
      }
    }
    return "session terminated with " + String.valueOf(errori) + " errors";
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
