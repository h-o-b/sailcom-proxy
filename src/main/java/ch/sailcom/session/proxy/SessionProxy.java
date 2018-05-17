package ch.sailcom.session.proxy;

import ch.sailcom.session.domain.User;

public interface SessionProxy {

	String getSessionId();

	boolean isLoggedIn();

	boolean login(String user, String pwd);

	User getUser();

	void logout();

}
