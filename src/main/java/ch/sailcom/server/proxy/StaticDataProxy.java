package ch.sailcom.server.proxy;

import java.util.List;

import ch.sailcom.server.dto.Harbor;
import ch.sailcom.server.dto.Lake;
import ch.sailcom.server.dto.Ship;
import ch.sailcom.server.dto.StaticData;

public interface StaticDataProxy {

	StaticData getStaticData();

	List<Lake> getLakes();

	Lake getLake(int lakeId);

	List<Harbor> getHarbors();

	Harbor getHarbor(int harborId);

	List<Ship> getShips();

	Ship getShip(int shipId);

}
