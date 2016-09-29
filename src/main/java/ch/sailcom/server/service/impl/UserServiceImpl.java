package ch.sailcom.server.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import ch.sailcom.server.model.Ship;
import ch.sailcom.server.model.User;
import ch.sailcom.server.model.UserInfo;
import ch.sailcom.server.model.UserPref;
import ch.sailcom.server.proxy.UserInfoProxy;
import ch.sailcom.server.proxy.UserPrefProxy;
import ch.sailcom.server.service.StaticDataService;
import ch.sailcom.server.service.UserService;

public class UserServiceImpl implements UserService {

	private final User user;

	private final UserInfoProxy userInfoProxy;
	private List<Integer> availableLakes = new ArrayList<Integer>();
	private List<Integer> availableHarbors = new ArrayList<Integer>();
	private List<Integer> availableShips = new ArrayList<Integer>();

	private final UserPrefProxy userPrefProxy;
	private UserPref userPref;

	public UserServiceImpl(User user, StaticDataService staticData, UserInfoProxy userInfoProxy, UserPrefProxy userPrefProxy) {
		this.user = user;
		this.userInfoProxy = userInfoProxy;
		this.userPrefProxy = userPrefProxy;
		this.availableShips = this.userInfoProxy.getAvailableShips();
		this.availableHarbors = this.availableShips.stream().map((shipId) -> {
			return staticData.getShip(shipId).harborId;
		}).distinct().collect(Collectors.toList());
		this.availableLakes = this.availableHarbors.stream().map((harborId) -> {
			return staticData.getHarbor(harborId).lakeId;
		}).distinct().collect(Collectors.toList());
	}

	@Override
	public User getUser() {
		return this.user;
	}

	@Override
	public UserInfo getUserInfo() {
		UserInfo userInfo = new UserInfo();
		userInfo.availableLakes = this.availableLakes;
		userInfo.availableHarbors = this.availableHarbors;
		userInfo.availableShips = this.availableShips;
		return userInfo;
	}

	@Override
	public UserPref getUserPref() {
		if (this.userPref == null) {
			this.userPref = this.userPrefProxy.getUserPref(this.user);
		}
		return this.userPref;
	}

	@Override
	public List<Integer> getAvailableLakes() {
		return this.availableLakes;
	}

	@Override
	public List<Integer> getAvailableHarbors() {
		return this.availableHarbors;
	}

	@Override
	public List<Integer> getAvailableShips() {
		return this.availableShips;
	}

	@Override
	public Set<Integer> getFavoriteShips() {
		return this.getUserPref().favoriteShips;
	}

	@Override
	public Set<Integer> like(Ship ship) {
		this.getUserPref().favoriteShips.add(ship.id);
		this.userPrefProxy.setUserPref(this.user, this.userPref);
		return this.getFavoriteShips();
	}

	@Override
	public Set<Integer> unlike(Ship ship) {
		this.getUserPref().favoriteShips.remove(ship.id);
		this.userPrefProxy.setUserPref(this.user, this.userPref);
		return this.getFavoriteShips();
	}

	@Override
	public Map<Integer, Integer> getRatedShips() {
		return this.getUserPref().ratedShips;
	}

	@Override
	public Map<Integer, Integer> rate(Ship ship, int starCount) {
		if (starCount > 0) {
			this.getUserPref().ratedShips.put(ship.id, starCount);
		} else {
			this.getUserPref().ratedShips.remove(ship.id);
		}
		this.userPrefProxy.setUserPref(this.user, this.userPref);
		return this.getRatedShips();
	}

}
