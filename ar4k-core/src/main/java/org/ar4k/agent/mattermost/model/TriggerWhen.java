// Generated by delombok at Sun Apr 18 22:20:18 CEST 2021
/*
 * Copyright (c) 2017 Takayuki Maruyama
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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;
import java.util.Arrays;

import org.ar4k.agent.mattermost.model.TriggerWhen.TriggerWhenDeserializer;
import org.ar4k.agent.mattermost.model.TriggerWhen.TriggerWhenSerializer;

@JsonSerialize(using = TriggerWhenSerializer.class)
@JsonDeserialize(using = TriggerWhenDeserializer.class)
public enum TriggerWhen {
	CONTAINS(1), STARTS_WITH(0);

	private final int code;

	public static TriggerWhen of(int code) {
		return Arrays.stream(values()).filter(e -> e.getCode() == code).findFirst().orElse(null);
	}

	static class TriggerWhenSerializer extends JsonSerializer<TriggerWhen> {
		@Override
		public void serialize(TriggerWhen value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			if (value != null) {
				gen.writeNumber(value.getCode());
			}
		}
	}

	static class TriggerWhenDeserializer extends JsonDeserializer<TriggerWhen> {
		@Override
		public TriggerWhen deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
			int jsonValue = p.getValueAsInt();
			return of(jsonValue);
		}
	}

	@java.lang.SuppressWarnings("all")
	public int getCode() {
		return this.code;
	}

	@java.lang.SuppressWarnings("all")
	private TriggerWhen(final int code) {
		this.code = code;
	}
}
