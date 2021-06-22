// Generated by delombok at Sun Apr 18 22:20:18 CEST 2021
/*
 * Copyright (c) 2017-present, Takayuki Maruyama
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

import org.ar4k.agent.mattermost.model.TeamType.TeamTypeDeserializer;
import org.ar4k.agent.mattermost.model.serialize.HasCodeSerializer;

@JsonSerialize(using = HasCodeSerializer.class)
@JsonDeserialize(using = TeamTypeDeserializer.class)
public enum TeamType implements HasCode<TeamType> {
	OPEN("O"), INVITE("I");

	private final String code;

	private TeamType(String code) {
		this.code = code;
	}

	static class TeamTypeDeserializer extends JsonDeserializer<TeamType> {
		@Override
		public TeamType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
			String jsonValue = p.getText();
			return HasCode.of(TeamType::values, jsonValue, OPEN);
		}
	}

	@java.lang.SuppressWarnings("all")
	public String getCode() {
		return this.code;
	}
}
