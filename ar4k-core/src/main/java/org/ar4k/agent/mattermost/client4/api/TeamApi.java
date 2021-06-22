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

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

import org.ar4k.agent.mattermost.client4.ApiResponse;
import org.ar4k.agent.mattermost.client4.Pager;
import org.ar4k.agent.mattermost.model.Role;
import org.ar4k.agent.mattermost.model.Team;
import org.ar4k.agent.mattermost.model.TeamExists;
import org.ar4k.agent.mattermost.model.TeamInviteInfo;
import org.ar4k.agent.mattermost.model.TeamList;
import org.ar4k.agent.mattermost.model.TeamMember;
import org.ar4k.agent.mattermost.model.TeamMemberList;
import org.ar4k.agent.mattermost.model.TeamPatch;
import org.ar4k.agent.mattermost.model.TeamSearch;
import org.ar4k.agent.mattermost.model.TeamStats;
import org.ar4k.agent.mattermost.model.TeamUnread;

public interface TeamApi {

	ApiResponse<Team> createTeam(Team team);

	default ApiResponse<Team> getTeam(String teamId) {
		return getTeam(teamId, null);
	}

	ApiResponse<Team> getTeam(String teamId, String etag);

	default ApiResponse<TeamList> getAllTeams() {
		return getAllTeams(Pager.defaultPager());
	}

	default ApiResponse<TeamList> getAllTeams(Pager pager) {
		return getAllTeams(pager, null);
	}

	ApiResponse<TeamList> getAllTeams(Pager pager, String etag);

	default ApiResponse<Team> getTeamByName(String name) {
		return getTeamByName(name, null);
	}

	ApiResponse<Team> getTeamByName(String name, String etag);

	ApiResponse<TeamList> searchTeams(TeamSearch search);

	default ApiResponse<TeamExists> teamExists(String name) {
		return teamExists(name, null);
	}

	ApiResponse<TeamExists> teamExists(String name, String etag);

	default ApiResponse<TeamList> getTeamsForUser(String userId) {
		return getTeamsForUser(userId, null);
	}

	ApiResponse<TeamList> getTeamsForUser(String userId, String etag);

	default ApiResponse<TeamMember> getTeamMember(String teamId, String userId) {
		return getTeamMember(teamId, userId, null);
	}

	ApiResponse<TeamMember> getTeamMember(String teamId, String userId, String etag);

	default ApiResponse<Boolean> updateTeamMemberRoles(String teamId, String userId, Collection<Role> newRoles) {
		return updateTeamMemberRoles(teamId, userId, newRoles.toArray(new Role[0]));
	}

	ApiResponse<Boolean> updateTeamMemberRoles(String teamId, String userId, Role... newROles);

	ApiResponse<Team> updateTeam(Team team);

	ApiResponse<Team> patchTeam(String teamId, TeamPatch patch);

	default ApiResponse<Boolean> deleteTeam(String teamId) {
		return deleteTeam(teamId, false);
	}

	ApiResponse<Boolean> deleteTeam(String teamId, boolean permanent);

	default ApiResponse<TeamMemberList> getTeamMembers(String teamId) {
		return getTeamMembers(teamId, Pager.defaultPager());
	}

	default ApiResponse<TeamMemberList> getTeamMembers(String teamId, Pager pager) {
		return getTeamMembers(teamId, pager, null);
	}

	ApiResponse<TeamMemberList> getTeamMembers(String teamId, Pager pager, String etag);

	default ApiResponse<TeamMemberList> getTeamMembersForUser(String userId) {
		return getTeamMembersForUser(userId, null);
	}

	ApiResponse<TeamMemberList> getTeamMembersForUser(String userId, String etag);

	default ApiResponse<TeamMemberList> getTeamMembersByIds(String teamId, Collection<String> userIds) {
		return getTeamMembersByIds(teamId, userIds.toArray(new String[0]));
	}

	ApiResponse<TeamMemberList> getTeamMembersByIds(String teamId, String... userIds);

	ApiResponse<TeamMember> addTeamMember(TeamMember teamMemberToAdd);

	@Deprecated // API Change on Mattermost 4.0
	ApiResponse<TeamMember> addTeamMember(String teamId, String userId, String hash, String dataToHash,
			String inviteId);

	@Deprecated
	ApiResponse<TeamMember> addTeamMember(String hash, String dataToHash, String inviteId);

	ApiResponse<TeamMember> addTeamMemberFromInvite(String token, String inviteId);

	default ApiResponse<TeamMemberList> addTeamMembers(String teamId, Collection<String> userIds) {
		return addTeamMembers(teamId, userIds.toArray(new String[0]));
	}

	ApiResponse<TeamMemberList> addTeamMembers(String teamId, String... userIds);

	default ApiResponse<Boolean> removeTeamMember(TeamMember teamMember) {
		return removeTeamMember(teamMember.getTeamId(), teamMember.getUserId());
	}

	ApiResponse<Boolean> removeTeamMember(String teamId, String userId);

	default ApiResponse<TeamStats> getTeamStats(String teamId) {
		return getTeamStats(teamId, null);
	}

	ApiResponse<TeamStats> getTeamStats(String teamId, String etag);

	ApiResponse<Path> getTeamIcon(String teamId) throws IOException;

	ApiResponse<Boolean> setTeamIcon(String teamId, Path iconFilePath);

	ApiResponse<Boolean> removeTeamIcon(String teamId);

	ApiResponse<TeamUnread> getTeamUnread(String teamId, String userId);

	ApiResponse<byte[]> importTeam(byte[] data, int filesize, String importFrom, String fileName, String teamId);

	ApiResponse<Boolean> inviteUsersToTeam(String teamId, Collection<String> userEmails);

	ApiResponse<TeamInviteInfo> getInviteInfo(String inviteId);

}
