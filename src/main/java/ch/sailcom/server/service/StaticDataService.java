package ch.sailcom.server.service;

import java.util.List;

import ch.sailcom.server.model.Harbor;
import ch.sailcom.server.model.Lake;
import ch.sailcom.server.model.Ship;

public interface StaticDataService extends Service {

	List<Lake> getLakes();

	Lake getLake(int lakeId);

	List<Harbor> getHarbors();

	Harbor getHarbor(int harborId);

	List<Ship> getShips();

	Ship getShip(int shipId);

	Ship getShip(String fullName);

}
