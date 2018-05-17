package ch.sailcom.session.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.sailcom.common.service.Service;
import ch.sailcom.fleet.domain.Ship;
import ch.sailcom.session.domain.User;
import ch.sailcom.session.domain.UserInfo;
import ch.sailcom.session.domain.UserPref;

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
