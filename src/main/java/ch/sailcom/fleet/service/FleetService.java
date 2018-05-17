package ch.sailcom.fleet.service;

import java.util.List;

import ch.sailcom.common.service.Service;
import ch.sailcom.fleet.domain.Harbor;
import ch.sailcom.fleet.domain.Lake;
import ch.sailcom.fleet.domain.Ship;

public interface FleetService extends Service {

	List<Lake> getLakes();

	Lake getLake(int lakeId);

	List<Harbor> getHarbors();

	Harbor getHarbor(int harborId);

	List<Ship> getShips();

	Ship getShip(int shipId);

	Ship getShip(String fullName);

}
