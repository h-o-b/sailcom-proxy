package ch.sailcom.mobile.server.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.sailcom.mobile.Harbor;
import ch.sailcom.mobile.Lake;
import ch.sailcom.mobile.Ship;

public class StaticData {

	public Map<Integer, Lake> lakesById = new HashMap<Integer, Lake>();
	public List<Lake> lakes = new ArrayList<Lake>();
	public List<Lake> myLakes = new ArrayList<Lake>();

	public Map<Integer, Harbor> harborsById = new HashMap<Integer, Harbor>();
	public List<Harbor> harbors = new ArrayList<Harbor>();
	public List<Harbor> myHarbors = new ArrayList<Harbor>();

	public Map<Integer, Ship> shipsById = new HashMap<Integer, Ship>();
	public List<Ship> ships = new ArrayList<Ship>();
	public List<Ship> myShips = new ArrayList<Ship>();

}
