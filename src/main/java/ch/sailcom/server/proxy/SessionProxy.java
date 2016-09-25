package ch.sailcom.server.proxy;

import ch.sailcom.server.dto.User;

public interface SessionProxy {

	boolean isConnected();

	void connect();

	boolean isLoggedIn();

	boolean login(String userNr, String pwd);

	String getSessionId();

	User getUser();

	void logout();

}
