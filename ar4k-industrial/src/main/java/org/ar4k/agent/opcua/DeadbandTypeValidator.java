package org.ar4k.agent.opcua;

import java.util.EnumSet;

import org.ar4k.agent.opcua.Enumerator.DeadbandType;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class DeadbandTypeValidator implements IParameterValidator {

  @Override
  public void validate(String name, String value) throws ParameterException {
    try {
      DeadbandType.valueOf(value);
    } catch (java.lang.IllegalArgumentException aa) {
      StringBuilder b = new StringBuilder();
      EnumSet.allOf(DeadbandType.class).forEach(v -> {
        b.append(v.toString() + " ");
      });
      throw new ParameterException("Parameter " + name + " should be in " + b.toString());
    }
  }
}
