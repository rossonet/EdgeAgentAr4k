package org.ar4k.agent.helper;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import org.ar4k.agent.logger.Ar4kLogger;
import org.ar4k.agent.logger.Ar4kStaticLoggerBinder;

public class ReflectionUtils {

  private static final Ar4kLogger logger = (Ar4kLogger) Ar4kStaticLoggerBinder.getSingleton().getLoggerFactory()
      .getLogger(ReflectionUtils.class.toString());

  private static ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();

  private static String getTaskName(long id, String name) {
    if (name == null) {
      return Long.toString(id);
    }
    return id + " (" + name + ")";
  }

  public static String logThreadInfo() {
    final StringBuilder sb = new StringBuilder();
    final int STACK_DEPTH = 20;
    boolean contention = threadBean.isThreadContentionMonitoringEnabled();
    long[] threadIds = threadBean.getAllThreadIds();
    sb.append("Process Thread Dump for all the JVM \n");
    sb.append(threadIds.length + " active threads\n");
    for (long tid : threadIds) {
      ThreadInfo info = threadBean.getThreadInfo(tid, STACK_DEPTH);
      if (info == null) {
        sb.append("  Inactive");
        continue;
      }
      sb.append("Thread " + getTaskName(info.getThreadId(), info.getThreadName()) + ":\n");
      Thread.State state = info.getThreadState();
      sb.append("  State: " + state + "\n");
      sb.append("  Blocked count: " + info.getBlockedCount() + "\n");
      sb.append("  Waited count: " + info.getWaitedCount() + "\n");
      if (contention) {
        sb.append("  Blocked time: " + info.getBlockedTime() + "\n");
        sb.append("  Waited time: " + info.getWaitedTime() + "\n");
      }
      if (state == Thread.State.WAITING) {
        sb.append("  Waiting on " + info.getLockName() + "\n");
      } else if (state == Thread.State.BLOCKED) {
        sb.append("  Blocked on " + info.getLockName() + "\n");
        sb.append("  Blocked by \n" + getTaskName(info.getLockOwnerId(), info.getLockOwnerName()));
      }
      sb.append("  Stack:\n");
      for (StackTraceElement frame : info.getStackTrace()) {
        sb.append("    " + frame.toString() + "\n");
      }
    }
    logger.info(sb.toString());
    return sb.toString();
  }

}