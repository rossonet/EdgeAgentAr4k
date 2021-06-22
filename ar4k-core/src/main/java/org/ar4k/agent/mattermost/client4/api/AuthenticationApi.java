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
import org.ar4k.agent.mattermost.client4.model.SwitchAccountTypeResult;
import org.ar4k.agent.mattermost.model.SwitchRequest;
import org.ar4k.agent.mattermost.model.User;

public interface AuthenticationApi {

	ApiResponse<User> loginById(String id, String password);

	ApiResponse<User> login(String loginId, String password);

	ApiResponse<User> loginByLdap(String loginId, String password);

	ApiResponse<User> loginWithDevice(String loginId, String password, String deviceId);

	ApiResponse<Boolean> logout();

	ApiResponse<SwitchAccountTypeResult> switchAccountType(SwitchRequest switchRequest);
}
