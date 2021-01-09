package org.ar4k.agent.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Scanner;

public final class IOUtils {

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
}
