/*
 * Copyright (c) 2016-present, Takayuki Maruyama
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.ar4k.agent.mattermost.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;

import org.ar4k.agent.mattermost.model.CommandMethod.CommandMethodDeserializer;
import org.ar4k.agent.mattermost.model.serialize.HasCodeSerializer;

/**
 * The type of command execute request method.
 * 
 * @author Takayuki Maruyama
 */
@JsonSerialize(using = HasCodeSerializer.class)
@JsonDeserialize(using = CommandMethodDeserializer.class)
public enum CommandMethod implements HasCode<CommandMethod> {

  POST("P"), GET("G");
  private final String code;

  CommandMethod(String code) {
    this.code = code;
  }

  @Override
  public String getCode() {
    return code;
  }

  /**
   * Get an enum constant for provided {@code code}.
   */
  public static CommandMethod of(String code) {
    for (CommandMethod method : CommandMethod.values()) {
      if (method.getCode().equals(code)) {
        return method;
      }
    }
    return null;
  }

  public static class CommandMethodDeserializer extends JsonDeserializer<CommandMethod> {

    @Override
    public CommandMethod deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      String code = p.getText();
      return CommandMethod.of(code);
    }
  }
}
