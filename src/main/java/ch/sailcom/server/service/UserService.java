package ch.sailcom.server.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.sailcom.server.model.Ship;
import ch.sailcom.server.model.User;
import ch.sailcom.server.model.UserInfo;
import ch.sailcom.server.model.UserPref;

public interface UserService extends Service {

	User getUser();

	UserInfo getUserInfo();

	List<Integer> getAvailableLakes();

	List<Integer> getAvailableHarbors();

	List<Integer> getAvailableShips();

	UserPref getUserPref();

	Set<Integer> getFavoriteShips();

	Set<Integer> like(Ship ship);

	Set<Integer> unlike(Ship ship);

	Map<Integer, Integer> getRatedShips();

	Map<Integer, Integer> rate(Ship ship, int starCount);

}
