package ch.sailcom.server.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticData {

	public List<Lake> lakes = new ArrayList<Lake>();
	public Map<Integer, Lake> lakesById = new HashMap<Integer, Lake>();
	public Map<String, Lake> lakesByName = new HashMap<String, Lake>();
	public List<Lake> myLakes = new ArrayList<Lake>();

	public List<Harbor> harbors = new ArrayList<Harbor>();
	public Map<Integer, Harbor> harborsById = new HashMap<Integer, Harbor>();
	public Map<String, Harbor> harborsByName = new HashMap<String, Harbor>();
	public List<Harbor> myHarbors = new ArrayList<Harbor>();

	public Map<Integer, Ship> shipsById = new HashMap<Integer, Ship>();
	public Map<String, Ship> shipsByName = new HashMap<String, Ship>();
	public List<Ship> ships = new ArrayList<Ship>();
	public List<Ship> myShips = new ArrayList<Ship>();

	public Lake addLake(String name) {
		Lake lake = new Lake();
		lake.id = this.lakes.size() + 1;
		lake.name = name;
		lake.isAvailable = false;
		this.lakes.add(lake);
		this.lakesById.put(lake.id, lake);
		this.lakesByName.put(lake.name, lake);
		return lake;
	}

	public Harbor addHarbor(int id, String name, int lakeId) {
		Harbor harbor = new Harbor();
		harbor.id = id;
		harbor.name = name;
		harbor.lakeId = lakeId;
		harbor.isAvailable = false;
		this.harbors.add(harbor);
		this.harborsById.put(harbor.id, harbor);
		this.harborsByName.put(harbor.name, harbor);
		return harbor;
	}

	public void addShip(Ship s) {
		this.ships.add(s);
		this.shipsById.put(s.id, s);
		String fullName = s.name + "@" + this.harborsById.get(s.harborId).name;
		this.shipsByName.put(fullName, s);
	}

}
