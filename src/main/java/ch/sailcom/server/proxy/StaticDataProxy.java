package ch.sailcom.server.proxy;

import java.util.List;

import ch.sailcom.server.dto.Harbor;
import ch.sailcom.server.dto.Lake;
import ch.sailcom.server.dto.Ship;

public interface StaticDataProxy {

	List<Lake> getLakes();

	List<Lake> getMyLakes();

	Lake getLake(int lakeId);

	List<Harbor> getHarbors();

	List<Harbor> getMyHarbors();

	Harbor getHarbor(int harborId);

	List<Ship> getShips();

	List<Ship> getMyShips();

	Ship getShip(int shipId);

	Ship getShip(String shipName);

}
