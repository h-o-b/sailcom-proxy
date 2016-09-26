package ch.sailcom.server.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserData {

	private final StaticData staticData;
	// private final User user;

	public List<Lake> lakes = new ArrayList<Lake>();
	public Map<Integer, Lake> lakesById = new HashMap<Integer, Lake>();

	public List<Harbor> harbors = new ArrayList<Harbor>();
	public Map<Integer, Harbor> harborsById = new HashMap<Integer, Harbor>();

	public List<Ship> ships = new ArrayList<Ship>();
	public Map<Integer, Ship> shipsById = new HashMap<Integer, Ship>();

	public UserData(StaticData staticData/* , User user */) {
		this.staticData = staticData;
		// this.user = user;
	}

	// public User getUser() {
	// return this.user;
	// }

	public void addShip(int shipId) {
		Ship ship = this.staticData.getShip(shipId);
		this.ships.add(ship);
		this.shipsById.put(shipId, ship);
		Harbor harbor = this.staticData.getHarbor(ship.harborId);
		if (!this.harbors.contains(harbor)) {
			this.harbors.add(harbor);
			this.harborsById.put(harbor.id, harbor);
			Lake lake = this.staticData.getLake(harbor.lakeId);
			if (!this.lakes.contains(lake)) {
				this.lakes.add(lake);
				this.lakesById.put(lake.id, lake);
			}
		}
	}

	public Ship like(Ship ship) {
		// this.staticData.shipsById.get(shipId).isFavorite = true;
		return ship;
	}

	public Ship unlike(Ship ship) {
		// this.staticData.shipsById.get(shipId).isFavorite = false;
		return ship;
	}

}
