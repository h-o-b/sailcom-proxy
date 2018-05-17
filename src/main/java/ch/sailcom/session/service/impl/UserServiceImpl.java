package ch.sailcom.session.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import ch.sailcom.fleet.domain.Ship;
import ch.sailcom.fleet.service.FleetService;
import ch.sailcom.session.domain.User;
import ch.sailcom.session.domain.UserInfo;
import ch.sailcom.session.domain.UserPref;
import ch.sailcom.session.proxy.UserInfoProxy;
import ch.sailcom.session.proxy.UserPrefProxy;
import ch.sailcom.session.service.SessionService;
import ch.sailcom.session.service.UserService;

@SessionScoped
public class UserServiceImpl implements UserService, Serializable {

	private static final long serialVersionUID = -8398228446420294396L;

	@Inject
	private FleetService staticDataService;

	@Inject
	private SessionService sessionService;

	@Inject
	private UserInfoProxy userInfoProxy;

	private List<Integer> availableLakes = new ArrayList<Integer>();
	private List<Integer> availableHarbors = new ArrayList<Integer>();
	private List<Integer> availableShips = new ArrayList<Integer>();

	@Inject
	private UserPrefProxy userPrefProxy;

	private UserPref userPref;

	public UserServiceImpl() {
	}

	@PostConstruct
	private void init() {

		this.availableShips = this.userInfoProxy.getAvailableShips();

		this.availableHarbors = this.availableShips.stream().map((shipId) -> {
			return staticDataService.getShip(shipId).harborId;
		}).distinct().collect(Collectors.toList());

		this.availableLakes = this.availableHarbors.stream().map((harborId) -> {
			return staticDataService.getHarbor(harborId).lakeId;
		}).distinct().collect(Collectors.toList());

		this.userPref = this.userPrefProxy.getUserPref(this.getUser());

	}

	@Override
	public User getUser() {
		return this.sessionService.getUser();
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
		this.userPrefProxy.setUserPref(this.getUser(), this.userPref);
		return this.getFavoriteShips();
	}

	@Override
	public Set<Integer> unlike(Ship ship) {
		this.getUserPref().favoriteShips.remove(ship.id);
		this.userPrefProxy.setUserPref(this.getUser(), this.userPref);
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
		this.userPrefProxy.setUserPref(this.getUser(), this.userPref);
		return this.getRatedShips();
	}

}
