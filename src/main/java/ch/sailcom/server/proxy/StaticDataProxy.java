package ch.sailcom.server.proxy;

import java.util.List;

import ch.sailcom.server.model.Harbor;
import ch.sailcom.server.model.Lake;
import ch.sailcom.server.model.Ship;

public interface StaticDataProxy {

	List<Lake> getLakes();

	List<Harbor> getHarbors();

	List<Ship> getShips();

}
