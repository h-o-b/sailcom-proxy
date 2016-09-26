package ch.sailcom.server.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserData {

	private final StaticData staticData;
	private final User user;

	public Map<Integer, Lake> availableLakesById = new HashMap<Integer, Lake>();

	public Map<Integer, Harbor> availableHarborsById = new HashMap<Integer, Harbor>();

	public Map<Integer, Ship> availableShipsById = new HashMap<Integer, Ship>();
	public Map<Integer, Ship> favoriteShipsById = new HashMap<Integer, Ship>();

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
		userInfo.favoriteShips = new ArrayList<Integer>(this.favoriteShipsById.keySet());
		return userInfo;
	}

	public void addShip(int shipId) {
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

}
