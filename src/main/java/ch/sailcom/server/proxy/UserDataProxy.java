package ch.sailcom.server.proxy;

import java.util.List;

import ch.sailcom.server.dto.User;
import ch.sailcom.server.dto.UserData;
import ch.sailcom.server.dto.UserInfo;

public interface UserDataProxy {

	User getUser();

	UserInfo getUserInfo();

	UserData getUserData();

	List<Integer> getLakes();

	List<Integer> getHarbors();

	List<Integer> getShips();

}
