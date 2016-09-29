package ch.sailcom.server.proxy.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.sailcom.server.dto.Harbor;
import ch.sailcom.server.dto.Lake;
import ch.sailcom.server.dto.Ship;
import ch.sailcom.server.dto.User;
import ch.sailcom.server.dto.UserInfo;
import ch.sailcom.server.dto.UserPref;

public class UserData {

	private final StaticData staticData;
	private final User user;

	public Map<Integer, Lake> availableLakesById = new HashMap<Integer, Lake>();
	public Map<Integer, Harbor> availableHarborsById = new HashMap<Integer, Harbor>();
	public Map<Integer, Ship> availableShipsById = new HashMap<Integer, Ship>();

	public Map<Integer, Ship> favoriteShipsById = new HashMap<Integer, Ship>();
	public Map<Integer, Integer> ratedShipsById = new HashMap<Integer, Integer>();

	public UserData(StaticData staticData, User user) {
		this.staticData = staticData;
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}

	public UserInfo getUserInfo() {
		UserInfo userInfo = new UserInfo();
		userInfo.availableLakes = new ArrayList<Integer>(this.availableLakesById.keySet());
		userInfo.availableHarbors = new ArrayList<Integer>(this.availableHarborsById.keySet());
		userInfo.availableShips = new ArrayList<Integer>(this.availableShipsById.keySet());
		return userInfo;
	}

	public UserPref getUserPref() {
		UserPref userPref = new UserPref();
		userPref.favoriteShips = new ArrayList<Integer>(this.favoriteShipsById.keySet());
		userPref.starredShips = new HashMap<Integer, Integer>(this.ratedShipsById);
		return userPref;
	}

	public void addAvailableShip(int shipId) {
		Ship ship = this.staticData.getShip(shipId);
		this.availableShipsById.put(shipId, ship);
		Harbor harbor = this.staticData.getHarbor(ship.harborId);
		if (!this.availableHarborsById.containsKey(harbor.id)) {
			this.availableHarborsById.put(harbor.id, harbor);
			Lake lake = this.staticData.getLake(harbor.lakeId);
			if (!this.availableLakesById.containsKey(lake.id)) {
				this.availableLakesById.put(lake.id, lake);
			}
		}
	}

	public List<Integer> like(Ship ship) {
		this.favoriteShipsById.put(ship.id, ship);
		return new ArrayList<Integer>(this.favoriteShipsById.keySet());
	}

	public List<Integer> unlike(Ship ship) {
		this.favoriteShipsById.remove(ship.id);
		return new ArrayList<Integer>(this.favoriteShipsById.keySet());
	}

	public Map<Integer, Integer> rate(Ship ship, int starCount) {
		if (starCount > 0) {
			this.ratedShipsById.put(ship.id, starCount);
		} else {
			this.ratedShipsById.remove(ship.id);
		}
		return this.ratedShipsById;
	}

}
