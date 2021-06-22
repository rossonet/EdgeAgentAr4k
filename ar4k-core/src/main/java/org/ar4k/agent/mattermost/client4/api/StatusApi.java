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

import java.util.Collection;

import org.ar4k.agent.mattermost.client4.ApiResponse;
import org.ar4k.agent.mattermost.model.Status;
import org.ar4k.agent.mattermost.model.StatusList;

public interface StatusApi {

	default ApiResponse<Status> getUserStatus(String userId) {
		return getUserStatus(userId, null);
	}

	ApiResponse<Status> getUserStatus(String userId, String etag);

	default ApiResponse<StatusList> getUsersStatusesByIds(Collection<String> userIds) {
		return getUsersStatusesByIds(userIds.toArray(new String[0]));
	}

	ApiResponse<StatusList> getUsersStatusesByIds(String... userIds);

	ApiResponse<Status> updateUserStatus(String userId, Status userStatus);

}
