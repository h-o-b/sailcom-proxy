package ch.sailcom.session.service;

import ch.sailcom.common.service.Service;
import ch.sailcom.session.domain.User;

public interface SessionService extends Service {

	String getSessionId();

	boolean isLoggedIn();

	boolean login(String user, String pwd);

	User getUser();

	void logout();

}
