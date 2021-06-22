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

package org.ar4k.agent.mattermost.client4.api;

import org.ar4k.agent.mattermost.client4.ApiResponse;
import org.ar4k.agent.mattermost.model.Command;
import org.ar4k.agent.mattermost.model.CommandList;
import org.ar4k.agent.mattermost.model.CommandResponse;

public interface CommandsApi {

	ApiResponse<Command> createCommand(Command cmd);

	ApiResponse<Command> updateCommand(Command cmd);

	ApiResponse<Boolean> deleteCommand(String commandId);

	default ApiResponse<CommandList> listCommands(String teamId) {
		return listCommands(teamId, false);
	}

	ApiResponse<CommandList> listCommands(String teamId, boolean customOnly);

	ApiResponse<CommandResponse> executeCommand(String channelId, String command);

	ApiResponse<CommandList> listAutocompleteCommands(String teamId);

	ApiResponse<String> regenCommandToken(String commandId);

}
