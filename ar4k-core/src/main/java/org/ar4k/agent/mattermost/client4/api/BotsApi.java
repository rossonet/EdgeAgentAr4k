/*
 * Copyright (c) 2019 Takayuki Maruyama
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
import org.ar4k.agent.mattermost.client4.Pager;
import org.ar4k.agent.mattermost.client4.model.GetBotsOption;
import org.ar4k.agent.mattermost.model.Bot;
import org.ar4k.agent.mattermost.model.BotPatch;
import org.ar4k.agent.mattermost.model.Bots;

public interface BotsApi {

	ApiResponse<Bot> createBot(BotPatch bot);

	ApiResponse<Bot> patchBot(String botUserId, BotPatch patch);

	default ApiResponse<Bot> getBot(String botUserId) {
		return getBot(botUserId, false);
	}

	ApiResponse<Bot> getBot(String botUserId, boolean includeDeleted);

	default ApiResponse<Bots> getBots() {
		return getBots(Pager.defaultPager(), GetBotsOption.defaultInstance());
	}

	default ApiResponse<Bots> getBots(GetBotsOption option) {
		return getBots(Pager.defaultPager(), option);
	}

	ApiResponse<Bots> getBots(Pager pager, GetBotsOption option);

	ApiResponse<Bot> disableBot(String botUserId);

	ApiResponse<Bot> enableBot(String botUserId);

	ApiResponse<Bot> assignBotToUser(String botUserId, String ownerUserId);
}
