package ch.sailcom.server.proxy;

import ch.sailcom.server.model.User;
import ch.sailcom.server.model.UserPref;

public interface UserPrefProxy {

	UserPref getUserPref(User user);

	void setUserPref(User user, UserPref userPref);

}
