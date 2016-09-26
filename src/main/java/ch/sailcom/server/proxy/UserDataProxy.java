package ch.sailcom.server.proxy;

import java.util.List;

import ch.sailcom.server.dto.Harbor;
import ch.sailcom.server.dto.Lake;
import ch.sailcom.server.dto.Ship;
import ch.sailcom.server.dto.UserData;

public interface UserDataProxy {

	// User getUser();

	UserData getUserData();

	List<Lake> getMyLakes();

	List<Harbor> getMyHarbors();

	List<Ship> getMyShips();

}
