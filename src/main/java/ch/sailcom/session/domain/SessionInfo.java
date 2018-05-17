package ch.sailcom.session.domain;

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
