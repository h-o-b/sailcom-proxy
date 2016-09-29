package ch.sailcom.server.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.sailcom.server.model.Harbor;
import ch.sailcom.server.model.Lake;
import ch.sailcom.server.model.Ship;
import ch.sailcom.server.proxy.StaticDataProxy;
import ch.sailcom.server.service.StaticDataService;

public class StaticDataServiceImpl implements StaticDataService {

	public List<Lake> lakes;
	private Map<Integer, Lake> lakesById = new HashMap<Integer, Lake>();

	public List<Harbor> harbors;
	private Map<Integer, Harbor> harborsById = new HashMap<Integer, Harbor>();

	public List<Ship> ships;
	private Map<Integer, Ship> shipsById = new HashMap<Integer, Ship>();
	private Map<String, Ship> shipsByName = new HashMap<String, Ship>();

	public StaticDataServiceImpl(StaticDataProxy staticDataProxy) {
		this.lakes = staticDataProxy.getLakes();
		this.lakes.forEach((lake) -> {
			this.lakesById.put(lake.id, lake);
		});
		this.harbors = staticDataProxy.getHarbors();
		this.harbors.forEach((harbor) -> {
			this.harborsById.put(harbor.id, harbor);
		});
		this.ships = staticDataProxy.getShips();
		this.ships.forEach((ship) -> {
			this.shipsById.put(ship.id, ship);
			String fullName = ship.name + "@" + this.getHarbor(ship.harborId).name;
			this.shipsByName.put(fullName, ship);
		});
	}

	@Override
	public Lake getLake(int lakeId) {
		return this.lakesById.get(lakeId);
	}

	@Override
	public List<Lake> getLakes() {
		return this.lakes;
	}

	@Override
	public Harbor getHarbor(int harborId) {
		return this.harborsById.get(harborId);
	}

	@Override
	public List<Harbor> getHarbors() {
		return this.harbors;
	}

	@Override
	public Ship getShip(int shipId) {
		return this.shipsById.get(shipId);
	}

	@Override
	public Ship getShip(String fullName) {
		return this.shipsByName.get(fullName);
	}

	@Override
	public List<Ship> getShips() {
		return this.ships;
	}

}
