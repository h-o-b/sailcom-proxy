package ch.sailcom.server.proxy;

import ch.sailcom.server.model.User;

public interface SessionProxy {

	String getSessionId();

	boolean isLoggedIn();

	boolean login(String user, String pwd);

	User getUser();

	void logout();

}
