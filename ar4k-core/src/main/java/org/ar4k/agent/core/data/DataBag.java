package org.ar4k.agent.core.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import org.ar4k.agent.core.data.channels.EdgeChannel;
import org.ar4k.agent.core.data.messages.InternalMessage;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.SubscribableChannel;

public class DataBag implements AutoCloseable, MessageHandler {

	private static transient final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(DataBag.class);

	private final File file;
	private final Collection<EdgeChannel> channels;
	private final long limit;
	private transient Timer timer = null;
	private transient BufferedWriter output;
	private transient FileInputStream fstream = null;
	private transient BufferedReader br = null;
	private transient long deltaTime = 0;
	private transient long completeTime = 0;
	private transient Queue<BagMessage> lastMessages = new LinkedBlockingQueue<>();
	private transient long packData = 0;

	public DataBag(File file, Collection<EdgeChannel> channels, long limit, long pollingTime) {
		this.file = file;
		this.channels = channels;
		this.limit = limit;
		for (final EdgeChannel sac : channels) {
			if (sac instanceof SubscribableChannel) {
				((SubscribableChannel) sac).subscribe(this);
			} else if (sac instanceof PollableChannel) {
				final TimerTask task = new TimerTask() {

					@Override
					public void run() {
						saveValueOnDisk((InternalMessage<?>) ((PollableChannel) sac).receive(limit / 2));

					}
				};
				if (timer == null) {
					timer = new Timer();
				}
				timer.schedule(task, pollingTime, pollingTime);
			}
		}
	}

	public DataBag(String dataBagFilePath) {
		this.file = new File(dataBagFilePath);
		this.channels = null;
		this.limit = 0;
	}

	protected void saveValueOnDisk(InternalMessage<?> received) {
		if (output == null) {
			try {
				output = new BufferedWriter(new FileWriter(file, true));
			} catch (final IOException e) {
				logger.logException(e);
			}
		}
		final BagMessage bagMessage = new BagMessage(received);
		try {
			final String line = bagMessage.getLine();
			if (line != null) {
				output.write(line);
				output.newLine();
			}
		} catch (final IOException e) {
			logger.logException(e);
		}
	}

	@Override
	public void close() {
		if (output != null) {
			try {
				output.flush();
				output.close();
			} catch (final IOException e) {
				logger.logException(e);
			}
		}
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}
		if (fstream != null) {
			try {
				fstream.close();
			} catch (final Exception e) {
				logger.logException(e);
			}
		}
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		saveValueOnDisk((InternalMessage<?>) message);
	}

	public long getLimit() {
		return limit;
	}

	public File getFile() {
		return file;
	}

	public Collection<EdgeChannel> getChannels() {
		return channels;
	}

	public void sincronize(final long time) {
		if (fstream != null) {
			try {
				fstream.close();
			} catch (final IOException e) {
				updateCompleteTime();
				logger.logException(e);
			}
		}
		try {
			fstream = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fstream));
			final long readAndCheckTime = readAndCheckTime();
			deltaTime = (new Date().getTime()) - readAndCheckTime;
			logger.info("synchronized dataBag with first value " + readAndCheckTime + ". Delta is " + deltaTime);
			completeTime = 0;
		} catch (final Exception e) {
			updateCompleteTime();
			logger.logException(e);
		}
	}

	private void updateCompleteTime() {
		if (completeTime == 0)
			completeTime = new Date().getTime();

	}

	private long readAndCheckTime() {
		try {
			final String readLine = br.readLine();
			// System.out.println("*** " + readLine);
			if (readLine != null) {
				final BagMessage m = new BagMessage(readLine);
				lastMessages.offer(m);
				if (m.getTime() > 0) {
					completeTime = 0;
				}
				return m.getTime();
			} else {
				updateCompleteTime();
				return 0;
			}
		} catch (final Exception ee) {
			logger.logException(ee);
			return 0;
		}
	}

	public boolean isComplete() {
		return completeTime != 0;
	}

	public List<Message<?>> getValues(long time) {
		try {
			final List<Message<?>> result = new ArrayList<>();
			while ((packData == 0 && completeTime == 0) || (packData != 0 && (packData < (time - deltaTime)))) {
				packData = readAndCheckTime();
			}
			while (!lastMessages.isEmpty()) {
				result.add(lastMessages.poll().getMessage());
			}
			return result;
		} catch (final Exception e) {
			logger.logException(e);
			return null;
		}
	}

	public long getCompleteTime() {
		return completeTime;
	}

}
