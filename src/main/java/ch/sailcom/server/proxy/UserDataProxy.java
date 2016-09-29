package ch.sailcom.server.proxy;

import java.util.List;
import java.util.Map;

import ch.sailcom.server.dto.Ship;
import ch.sailcom.server.dto.User;
import ch.sailcom.server.dto.UserInfo;
import ch.sailcom.server.dto.UserPref;

public interface UserDataProxy {

	User getUser();

	UserInfo getUserInfo();

	List<Integer> getAvailableLakes();

	List<Integer> getAvailableHarbors();

	List<Integer> getAvailableShips();

	UserPref getUserPref();

	List<Integer> getFavoriteShips();

	List<Integer> like(Ship ship);

	List<Integer> unlike(Ship ship);

	Map<Integer, Integer> getRatedShips();

	Map<Integer, Integer> rate(Ship ship, int starCount);

}
