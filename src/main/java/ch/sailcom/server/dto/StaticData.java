package ch.sailcom.server.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticData {

	public List<Lake> lakes = new ArrayList<Lake>();
	private Map<Integer, Lake> lakesById = new HashMap<Integer, Lake>();
	private Map<String, Lake> lakesByName = new HashMap<String, Lake>();

	public List<Harbor> harbors = new ArrayList<Harbor>();
	private Map<Integer, Harbor> harborsById = new HashMap<Integer, Harbor>();
	private Map<String, Harbor> harborsByName = new HashMap<String, Harbor>();

	public List<Ship> ships = new ArrayList<Ship>();
	private Map<Integer, Ship> shipsById = new HashMap<Integer, Ship>();
	private Map<String, Ship> shipsByName = new HashMap<String, Ship>();

	public Lake getLake(int lakeId) {
		return this.lakesById.get(lakeId);
	}

	public Lake getLake(String name) {
		return this.lakesByName.get(name);
	}

	public Lake addLake(String name) {
		Lake lake = new Lake();
		lake.id = this.lakes.size() + 1;
		lake.name = name;
		this.lakes.add(lake);
		this.lakesById.put(lake.id, lake);
		this.lakesByName.put(lake.name, lake);
		return lake;
	}

	public Harbor getHarbor(int harborId) {
		return this.harborsById.get(harborId);
	}

	public Harbor getHarbor(String name) {
		return this.harborsByName.get(name);
	}

	public Harbor addHarbor(int id, String name, int lakeId) {
		Harbor harbor = new Harbor();
		harbor.id = id;
		harbor.name = name;
		harbor.lakeId = lakeId;
		this.harbors.add(harbor);
		this.harborsById.put(harbor.id, harbor);
		this.harborsByName.put(harbor.name, harbor);
		return harbor;
	}

	public Ship getShip(int shipId) {
		return this.shipsById.get(shipId);
	}

	public Ship getShip(String name) {
		return this.shipsByName.get(name);
	}

	public void addShip(Ship s) {
		this.ships.add(s);
		this.shipsById.put(s.id, s);
		String fullName = s.name + "@" + this.getHarbor(s.harborId).name;
		this.shipsByName.put(fullName, s);
	}

}
