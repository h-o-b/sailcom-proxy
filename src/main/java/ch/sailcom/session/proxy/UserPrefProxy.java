package ch.sailcom.session.proxy;

import ch.sailcom.session.domain.User;
import ch.sailcom.session.domain.UserPref;

public interface UserPrefProxy {

	UserPref getUserPref(User user);

	void setUserPref(User user, UserPref userPref);

}
