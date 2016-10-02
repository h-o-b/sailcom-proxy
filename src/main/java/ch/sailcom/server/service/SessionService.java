package ch.sailcom.server.service;

import ch.sailcom.server.model.User;

public interface SessionService extends Service {

	String getSessionId();

	boolean isLoggedIn();

	boolean login(String user, String pwd);

	User getUser();

	void logout();

}
