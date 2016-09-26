package ch.sailcom.server.proxy;

import ch.sailcom.server.dto.User;

public interface SessionProxy {

	String getSessionId();

	boolean isLoggedIn();

	boolean login(String user, String pwd);

	User getUser();

	<T> T getProxy(Class<T> proxyClass);

	void logout();

}
