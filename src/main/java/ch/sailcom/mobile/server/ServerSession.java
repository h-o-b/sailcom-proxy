package ch.sailcom.mobile.server;

import java.util.Date;
import java.util.List;

import ch.sailcom.mobile.Booking;
import ch.sailcom.mobile.Lake;
import ch.sailcom.mobile.Harbor;
import ch.sailcom.mobile.Ship;

public interface ServerSession {

	boolean isConnected();
	void connect();

	boolean isLoggedIn();
	void login(String user, String pwd);
	void logout();

	List<Lake> getLakes();
	List<Lake> getMyLakes();
	Lake getLake(int lakeId);

	List<Harbor> getHarbors();
	List<Harbor> getMyHarbors();
	Harbor getHarbor(int harborId);

	List<Ship> getShips();
	List<Ship> getMyShips();
	Ship getShip(int shipId);

	List<Booking> getBookings(int shipId, Date fromDate, int nofWeeks);

}
