package org.ar4k.agent.industrial.validators;

import java.util.EnumSet;

import org.ar4k.agent.industrial.Enumerator.AuthMode;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/*
* @author Andrea Ambrosini Rossonet s.c.a r.l. andrea.ambrosini@rossonet.com
*
*         Validatore valori da linea di comando per la modalità di autenticazione opcua.
*
*/
public class AuthModeValidator implements IParameterValidator {

	@Override
	public void validate(String name, String value) throws ParameterException {
		try {
			AuthMode.valueOf(value);
		} catch (java.lang.IllegalArgumentException aa) {
			StringBuilder b = new StringBuilder();
			EnumSet.allOf(AuthMode.class).forEach(v -> {
				b.append(v.toString() + " ");
			});
			throw new ParameterException("Parameter " + name + " should be in " + b.toString());
		}
	}
}
