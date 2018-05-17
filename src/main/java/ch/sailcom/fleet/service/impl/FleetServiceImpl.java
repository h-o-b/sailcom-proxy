package ch.sailcom.fleet.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.sailcom.fleet.domain.Harbor;
import ch.sailcom.fleet.domain.Lake;
import ch.sailcom.fleet.domain.Ship;
import ch.sailcom.fleet.proxy.FleetProxy;
import ch.sailcom.fleet.service.FleetService;

@ApplicationScoped
public class FleetServiceImpl implements FleetService {

	private static Logger LOGGER = LoggerFactory.getLogger(FleetServiceImpl.class);

	@Inject
	private FleetProxy fleetProxy;

	private List<Lake> lakes;
	private Map<Integer, Lake> lakesById = new HashMap<Integer, Lake>();

	private List<Harbor> harbors;
	private Map<Integer, Harbor> harborsById = new HashMap<Integer, Harbor>();

	private List<Ship> ships;
	private Map<Integer, Ship> shipsById = new HashMap<Integer, Ship>();
	private Map<String, Ship> shipsByName = new HashMap<String, Ship>();

	public FleetServiceImpl() {
		LOGGER.debug("FleetServiceImpl()");
	}

	@PostConstruct
	private void loadFleet() {

		if (this.lakes != null) {
			return;
		}

		this.lakes = fleetProxy.getLakes();
		this.lakes.forEach((lake) -> {
			this.lakesById.put(lake.id, lake);
		});

		this.harbors = fleetProxy.getHarbors();
		this.harbors.forEach((harbor) -> {
			this.harborsById.put(harbor.id, harbor);
		});

		this.ships = fleetProxy.getShips();
		this.ships.forEach((ship) -> {
			this.shipsById.put(ship.id, ship);
			String fullName = ship.name + "@" + this.getHarbor(ship.harborId).name;
			this.shipsByName.put(fullName, ship);
		});

	}

	@Override
	public List<Lake> getLakes() {
		return this.lakes;
	}

	@Override
	public Lake getLake(int lakeId) {
		return this.lakesById.get(lakeId);
	}

	@Override
	public List<Harbor> getHarbors() {
		return this.harbors;
	}

	@Override
	public Harbor getHarbor(int harborId) {
		return this.harborsById.get(harborId);
	}

	@Override
	public List<Ship> getShips() {
		return this.ships;
	}

	@Override
	public Ship getShip(int shipId) {
		return this.shipsById.get(shipId);
	}

	@Override
	public Ship getShip(String fullName) {
		return this.shipsByName.get(fullName);
	}

}
