package ch.sailcom.fleet.proxy;

import java.util.List;

import ch.sailcom.fleet.domain.Harbor;
import ch.sailcom.fleet.domain.Lake;
import ch.sailcom.fleet.domain.Ship;

public interface FleetProxy {

	List<Lake> getLakes();

	List<Harbor> getHarbors();

	List<Ship> getShips();

}
