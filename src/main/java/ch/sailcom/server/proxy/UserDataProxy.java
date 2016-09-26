package ch.sailcom.server.proxy;

import java.util.List;
import java.util.Map;

import ch.sailcom.server.dto.Ship;
import ch.sailcom.server.dto.User;
import ch.sailcom.server.dto.UserData;
import ch.sailcom.server.dto.UserInfo;
import ch.sailcom.server.dto.UserPreference;

public interface UserDataProxy {

	User getUser();

	UserInfo getUserInfo();

	UserPreference getUserPreference();

	UserData getUserData();

	List<Integer> getLakes();

	List<Integer> getHarbors();

	List<Integer> getShips();

	List<Integer> like(Ship ship);

	List<Integer> unlike(Ship ship);

	Map<Integer, Integer> rate(Ship ship, int starCount);

}
