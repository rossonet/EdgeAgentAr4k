package org.ar4k.agent.industrial;

import java.util.EnumSet;

import org.ar4k.agent.industrial.Enumerator.DataChangeTrigger;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class DataChangeTriggerValidator implements IParameterValidator {

  @Override
  public void validate(String name, String value) throws ParameterException {
    try {
      DataChangeTrigger.valueOf(value);
    } catch (java.lang.IllegalArgumentException aa) {
      StringBuilder b = new StringBuilder();
      EnumSet.allOf(DataChangeTrigger.class).forEach(v -> {
        b.append(v.toString() + " ");
      });
      throw new ParameterException("Parameter " + name + " should be in " + b.toString());
    }
  }
}
