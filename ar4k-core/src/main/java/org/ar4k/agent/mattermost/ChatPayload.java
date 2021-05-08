package org.ar4k.agent.mattermost;

import java.io.Serializable;

public class ChatPayload implements Serializable {

	private static final long serialVersionUID = 8986007384292973697L;
	private String allowedDomains;
	private String authData;
	private String botDescription;
	private String channelId;
	private String companyName;
	private long createAt;
	private String creatorId;
	private long deleteAt;
	private String description;
	private String displayName;
	private long editAt;
	private String email;
	private String firstName;
	private String hashtags;
	private String header;
	private String id;
	private String inviteId;
	private String lastName;
	private String locale;
	private String message;
	private String name;
	private String nickname;
	private String originalId;
	private String parentId;
	private String position;
	private String purpose;
	private String roles;
	private String rootId;
	private String schemeId;
	private String teamId;
	private String type;
	private long updateAt;
	private String userId;
	private String username;
	private boolean directMessage = false;
	private boolean mentioned = false;

	public String getAllowedDomains() {
		return allowedDomains;
	}

	public String getAuthData() {
		return authData;
	}

	public String getBotDescription() {
		return botDescription;
	}

	public String getChannelId() {
		return channelId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public long getCreateAt() {
		return createAt;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public long getDeleteAt() {
		return deleteAt;
	}

	public String getDescription() {
		return description;
	}

	public String getDisplayName() {
		return displayName;
	}

	public long getEditAt() {
		return editAt;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getHashtags() {
		return hashtags;
	}

	public String getHeader() {
		return header;
	}

	public String getId() {
		return id;
	}

	public String getInviteId() {
		return inviteId;
	}

	public String getLastName() {
		return lastName;
	}

	public String getLocale() {
		return locale;
	}

	public String getMessage() {
		return message;
	}

	public String getName() {
		return name;
	}

	public String getNickname() {
		return nickname;
	}

	public String getOriginalId() {
		return originalId;
	}

	public String getParentId() {
		return parentId;
	}

	public String getPosition() {
		return position;
	}

	public String getPurpose() {
		return purpose;
	}

	public String getRoles() {
		return roles;
	}

	public String getRootId() {
		return rootId;
	}

	public String getSchemeId() {
		return schemeId;
	}

	public String getTeamId() {
		return teamId;
	}

	public String getType() {
		return type;
	}

	public long getUpdateAt() {
		return updateAt;
	}

	public String getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public void setAllowedDomains(String allowedDomains) {
		this.allowedDomains = allowedDomains;
	}

	public void setAuthData(String authData) {
		this.authData = authData;
	}

	public void setBotDescription(String botDescription) {
		this.botDescription = botDescription;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setCreateAt(long createAt) {
		this.createAt = createAt;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public void setDeleteAt(long deleteAt) {
		this.deleteAt = deleteAt;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setEditAt(long editAt) {
		this.editAt = editAt;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setHashtags(String hashtags) {
		this.hashtags = hashtags;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInviteId(String inviteId) {
		this.inviteId = inviteId;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setlocale(String locale) {
		this.locale = locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

	public void setSchemeId(String schemeId) {
		this.schemeId = schemeId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUpdateAt(long updateAt) {
		this.updateAt = updateAt;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ChatPayload [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (type != null) {
			builder.append("type=");
			builder.append(type);
			builder.append(", ");
		}
		if (message != null) {
			builder.append("message=");
			builder.append(message);
			builder.append(", ");
		}
		builder.append("createAt=");
		builder.append(createAt);
		builder.append(", editAt=");
		builder.append(editAt);
		builder.append(", updateAt=");
		builder.append(updateAt);
		builder.append(", deleteAt=");
		builder.append(deleteAt);
		builder.append(", mentioned=");
		builder.append(mentioned);
		builder.append(", directMessage=");
		builder.append(directMessage);
		builder.append(", ");
		if (channelId != null) {
			builder.append("channelId=");
			builder.append(channelId);
			builder.append(", ");
		}
		if (userId != null) {
			builder.append("userId=");
			builder.append(userId);
			builder.append(", ");
		}
		if (rootId != null) {
			builder.append("rootId=");
			builder.append(rootId);
			builder.append(", ");
		}
		if (parentId != null) {
			builder.append("parentId=");
			builder.append(parentId);
			builder.append(", ");
		}
		if (originalId != null) {
			builder.append("originalId=");
			builder.append(originalId);
			builder.append(", ");
		}
		if (hashtags != null) {
			builder.append("hashtags=");
			builder.append(hashtags);
			builder.append(", ");
		}
		if (creatorId != null) {
			builder.append("creatorId=");
			builder.append(creatorId);
			builder.append(", ");
		}
		if (displayName != null) {
			builder.append("displayName=");
			builder.append(displayName);
			builder.append(", ");
		}
		if (header != null) {
			builder.append("header=");
			builder.append(header);
			builder.append(", ");
		}
		if (name != null) {
			builder.append("name=");
			builder.append(name);
			builder.append(", ");
		}
		if (purpose != null) {
			builder.append("purpose=");
			builder.append(purpose);
			builder.append(", ");
		}
		if (schemeId != null) {
			builder.append("schemeId=");
			builder.append(schemeId);
			builder.append(", ");
		}
		if (teamId != null) {
			builder.append("teamId=");
			builder.append(teamId);
			builder.append(", ");
		}
		if (companyName != null) {
			builder.append("companyName=");
			builder.append(companyName);
			builder.append(", ");
		}
		if (description != null) {
			builder.append("description=");
			builder.append(description);
			builder.append(", ");
		}
		if (email != null) {
			builder.append("email=");
			builder.append(email);
			builder.append(", ");
		}
		if (allowedDomains != null) {
			builder.append("allowedDomains=");
			builder.append(allowedDomains);
			builder.append(", ");
		}
		if (inviteId != null) {
			builder.append("inviteId=");
			builder.append(inviteId);
			builder.append(", ");
		}
		if (firstName != null) {
			builder.append("firstName=");
			builder.append(firstName);
			builder.append(", ");
		}
		if (lastName != null) {
			builder.append("lastName=");
			builder.append(lastName);
			builder.append(", ");
		}
		if (authData != null) {
			builder.append("authData=");
			builder.append(authData);
			builder.append(", ");
		}
		if (nickname != null) {
			builder.append("nickname=");
			builder.append(nickname);
			builder.append(", ");
		}
		if (locale != null) {
			builder.append("locale=");
			builder.append(locale);
			builder.append(", ");
		}
		if (username != null) {
			builder.append("username=");
			builder.append(username);
			builder.append(", ");
		}
		if (botDescription != null) {
			builder.append("botDescription=");
			builder.append(botDescription);
			builder.append(", ");
		}
		if (position != null) {
			builder.append("position=");
			builder.append(position);
			builder.append(", ");
		}
		if (roles != null) {
			builder.append("roles=");
			builder.append(roles);
		}
		builder.append("]");
		return builder.toString();
	}

	public void setDirectMessage(boolean directMessage) {
		this.directMessage = directMessage;
	}

	public void setMentioned(boolean mentioned) {
		this.mentioned = mentioned;
	}

	public boolean isDirectMessage() {
		return directMessage;
	}

	public boolean isMentioned() {
		return mentioned;
	}

}
