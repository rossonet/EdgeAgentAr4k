package org.ar4k.agent.rpc.process.bash;

import static org.ar4k.agent.helper.IOUtils.pipe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

import javax.script.ScriptContext;

import org.ar4k.agent.helper.IOUtils;
import org.ar4k.agent.helper.StringUtils;

public class NativeShellRunner {

	public static final Integer RETURN_CODE_OK = 0;

	private NativeShell nativeShell;

	public NativeShellRunner(NativeShell nativeShell) {
		this.nativeShell = nativeShell;
	}

	public String getInstalledVersion() {
		try {
			return runAndGetOutput(nativeShell.getInstalledVersionCommand());
		} catch (final Throwable e) {
			return "Could not determine version";
		}
	}

	public String getMajorVersion() {
		try {
			return runAndGetOutput(nativeShell.getMajorVersionCommand());
		} catch (final Throwable e) {
			return "Could not determine version";
		}
	}

	public int run(String command, ScriptContext scriptContext) {
		final File commandAsTemporaryFile = commandAsTemporaryFile(command);
		final int exitValue = run(commandAsTemporaryFile, scriptContext);
		commandAsTemporaryFile.delete();
		return exitValue;
	}

	private int run(File command, ScriptContext scriptContext) {
		final ProcessBuilder processBuilder = nativeShell.createProcess(command);

		addBindingsAsEnvironmentVariables(scriptContext, processBuilder);

		return run(processBuilder, scriptContext.getReader(), scriptContext.getWriter(),
				scriptContext.getErrorWriter());
	}

	private String runAndGetOutput(String command) {
		final ProcessBuilder processBuilder = nativeShell.createProcess(command);
		final StringWriter processOutput = new StringWriter();
		final Reader closedInput = new Reader() {
			@Override
			public int read(char[] cbuf, int off, int len) throws IOException {
				return -1;
			}

			@Override
			public void close() throws IOException {

			}
		};
		run(processBuilder, closedInput, processOutput, new StringWriter());
		return processOutput.toString();
	}

	private static int run(ProcessBuilder processBuilder, Reader processInput, Writer processOutput,
			Writer processError) {
		try {
			final Process process = processBuilder.start();
			final Thread input = writeProcessInput(process.getOutputStream(), processInput);
			final Thread output = readProcessOutput(process.getInputStream(), processOutput);
			final Thread error = readProcessOutput(process.getErrorStream(), processError);

			input.start();
			output.start();
			error.start();

			process.waitFor();
			output.join();
			error.join();
			input.interrupt(); // TO______DO better thing to do?

			return process.exitValue();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void addBindingsAsEnvironmentVariables(ScriptContext scriptContext, ProcessBuilder processBuilder) {
		final Map<String, String> environment = processBuilder.environment();
		for (final Map.Entry<String, Object> binding : scriptContext.getBindings(ScriptContext.ENGINE_SCOPE)
				.entrySet()) {
			final String bindingKey = binding.getKey();
			final Object bindingValue = binding.getValue();

			if (bindingValue instanceof Object[]) {
				addArrayBindingAsEnvironmentVariable(bindingKey, (Object[]) bindingValue, environment);
			} else if (bindingValue instanceof Collection) {
				addCollectionBindingAsEnvironmentVariable(bindingKey, (Collection<?>) bindingValue, environment);
			} else if (bindingValue instanceof Map) {
				addMapBindingAsEnvironmentVariable(bindingKey, (Map<?, ?>) bindingValue, environment);
			} else {
				environment.put(bindingKey, StringUtils.toEmptyStringIfNull(binding.getValue()));
			}
		}
	}

	private void addMapBindingAsEnvironmentVariable(String bindingKey, Map<?, ?> bindingValue,
			Map<String, String> environment) {
		for (final Map.Entry<?, ?> entry : ((Map<?, ?>) bindingValue).entrySet()) {
			environment.put(bindingKey + "_" + entry.getKey(),
					(entry.getValue() == null ? "" : StringUtils.toEmptyStringIfNull(entry.getValue())));
		}
	}

	private void addCollectionBindingAsEnvironmentVariable(String bindingKey, Collection<?> bindingValue,
			Map<String, String> environment) {
		final Object[] bindingValueAsArray = bindingValue.toArray();
		addArrayBindingAsEnvironmentVariable(bindingKey, bindingValueAsArray, environment);
	}

	private void addArrayBindingAsEnvironmentVariable(String bindingKey, Object[] bindingValue,
			Map<String, String> environment) {
		for (int i = 0; i < bindingValue.length; i++) {
			environment.put(bindingKey + "_" + i,
					(bindingValue[i] == null ? "" : StringUtils.toEmptyStringIfNull(bindingValue[i].toString())));
		}
	}

	private static Thread readProcessOutput(final InputStream processOutput, final Writer contextWriter) {
		return new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					pipe(new BufferedReader(new InputStreamReader(processOutput)), new BufferedWriter(contextWriter));
				} catch (final IOException ignored) {
				}
			}
		});
	}

	private static Thread writeProcessInput(final OutputStream processOutput, final Reader contextWriter) {
		return new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					pipe(new BufferedReader(contextWriter), new OutputStreamWriter(processOutput));
				} catch (final IOException closed) {
					try {
						processOutput.close();
					} catch (final IOException ignored) {
					}
				}
			}
		});
	}

	private File commandAsTemporaryFile(String command) {
		try {
			final File commandAsFile = File.createTempFile("jsr223nativeshell-", nativeShell.getFileExtension());
			IOUtils.writeStringToFile(command, commandAsFile);
			return commandAsFile;
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

}
