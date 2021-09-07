package org.ar4k.agent.helper;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.ar4k.agent.exception.EdgeException;
import org.ar4k.agent.logger.EdgeLogger;
import org.ar4k.agent.logger.EdgeStaticLoggerBinder;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

public final class IOUtils {

	private static final EdgeLogger logger = EdgeStaticLoggerBinder.getClassLogger(IOUtils.class);

	private static final int BUFFER_SIZE = 512;

	private IOUtils() {
		throw new UnsupportedOperationException("Just for static usage");
	}

	public static String toString(Reader reader) {
		@SuppressWarnings("resource")
		final Scanner s = new Scanner(reader).useDelimiter("\\A");
		return s.hasNext() ? s.next() : null;
	}

	public static String toString(InputStream is) {
		@SuppressWarnings("resource")
		final Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : null;
	}

	public static void writeStringToFile(String string, File file) throws FileNotFoundException {
		final PrintWriter out = new PrintWriter(file);
		out.print(string);
		out.close();
	}

	public static void pipe(Reader from, Writer to) throws IOException {
		final char[] buff = new char[1024];
		int n = from.read(buff);
		while (n != -1) {
			to.write(buff, 0, n);
			to.flush();
			n = from.read(buff);
		}
		from.close();
	}

	public static Process runCommand(String[] startCommand, String directory, StringBuilder resultCommand)
			throws IOException {
		Process process = directory != null ? Runtime.getRuntime().exec(startCommand, null, new File(directory))
				: Runtime.getRuntime().exec(startCommand);
		new Thread(new Runnable() {
			@Override
			public void run() {
				final BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
				final BufferedReader inputError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				String line = null;
				String lineError = null;
				try {
					while ((line = input.readLine()) != null)
						resultCommand.append(line + "\n");
					while ((lineError = inputError.readLine()) != null)
						resultCommand.append(lineError + "\n");
				} catch (final IOException e) {
					logger.warn("exec: " + Arrays.toString(startCommand) + "\n" + EdgeLogger.stackTraceToString(e, 4));
				}
			}
		}).start();
		return process;
	}

	public static void deleteDirectory(File file) {
		for (File subfile : file.listFiles()) {
			if (subfile.isDirectory()) {
				deleteDirectory(subfile);
			}
			subfile.delete();
		}
	}

	public static String readResourceFileToString(String path) {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource(path);
		return resourceAsString(resource);
	}

	public static String resourceAsString(Resource resource) {
		try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
			return FileCopyUtils.copyToString(reader);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static long downloadFileFromUrl(String filename, String url) throws MalformedURLException, IOException {
		final long result = 0L;
		FileOutputStream fileOutputStream = null;
		ReadableByteChannel readableByteChannel = null;
		FileChannel fileChannel = null;
		try {
			readableByteChannel = Channels.newChannel(new URL(url).openStream());
			fileOutputStream = new FileOutputStream(filename);
			fileChannel = fileOutputStream.getChannel();
			fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
			fileOutputStream.flush();
		} finally {
			if (fileChannel != null)
				fileChannel.close();
			if (fileOutputStream != null)
				fileOutputStream.close();
		}
		return result;
	}

	public static void extractTarGz(String path, InputStream in) throws IOException {
		final GzipCompressorInputStream gzipIn = new GzipCompressorInputStream(in);
		try (TarArchiveInputStream tarIn = new TarArchiveInputStream(gzipIn)) {
			TarArchiveEntry entry = null;
			while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
				/** If the entry is a directory, create the directory. **/
				if (entry.isDirectory()) {
					final File f = new File(path + "/" + entry.getName());
					final boolean created = f.mkdirs();
					if (!created) {
						throw new EdgeException("Unable to create directory " + f.getAbsolutePath()
								+ ", during extraction of archive contents.");
					}
				} else {
					int count;
					final byte data[] = new byte[BUFFER_SIZE];
					final FileOutputStream fos = new FileOutputStream(path + "/" + entry.getName(), false);
					try (BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE)) {
						while ((count = tarIn.read(data, 0, BUFFER_SIZE)) != -1) {
							dest.write(data, 0, count);
						}
					}
				}
			}
		}
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		final JSONObject json = new JSONObject(readTxtFromUrl(url));
		return json;
	}

	public static String readTxtFromUrl(String url) throws IOException, JSONException {
		final InputStream is = new URL(url).openStream();
		try {
			final BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			final String text = readAll(rd);
			return text;
		} finally {
			is.close();
		}
	}

	public static String resolveFileFromDns(final String hostPart, final String domainPart, final int retry)
			throws TextParseException, UnknownHostException {
		final StringBuilder resultString = new StringBuilder();
		final Set<String> errors = new HashSet<>();
		final Lookup l = new Lookup(hostPart + "-max" + domainPart, Type.TXT, DClass.IN);
		l.setResolver(new SimpleResolver());
		l.run();
		if (l.getResult() == Lookup.SUCCESSFUL) {
			final int chunkSize = Integer
					.parseInt(l.getAnswers()[0].rdataToString().replaceAll("^\"", "").replaceAll("\"$", ""));
			if (chunkSize > 0) {
				for (int c = 0; c < chunkSize; c++) {
					final Lookup cl = new Lookup(hostPart + "-" + String.valueOf(c) + domainPart, Type.TXT, DClass.IN);
					cl.setResolver(new SimpleResolver());
					cl.run();
					if (cl.getResult() == Lookup.SUCCESSFUL) {
						resultString
								.append(cl.getAnswers()[0].rdataToString().replaceAll("^\"", "").replaceAll("\"$", ""));
					} else {
						errors.add("error in chunk " + hostPart + "-" + String.valueOf(c) + domainPart + " -> "
								+ cl.getErrorString());
					}
				}
			} else {
				errors.add("error, size of data is " + l.getAnswers()[0].rdataToString());
			}
		} else {
			errors.add("no " + hostPart + "-max" + domainPart + " record found -> " + l.getErrorString());
			if (retry > 0) {
				return resolveFileFromDns(hostPart, domainPart, retry - 1);
			} else {
				return null;
			}
		}
		if (!errors.isEmpty()) {
			logger.error(errors.toString());
			return null;
		} else {
			return resultString.toString();
		}
	}

	private static String readAll(Reader rd) throws IOException {
		final StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
}
