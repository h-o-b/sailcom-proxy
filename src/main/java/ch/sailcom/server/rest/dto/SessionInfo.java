package ch.sailcom.server.rest.dto;

import ch.sailcom.server.model.User;

public class SessionInfo {

	public String sessionId;
	public User user;

	SessionInfo() {
	}

	public SessionInfo(String sessionId, User user) {
		this.sessionId = sessionId;
		this.user = user;
	}

}
