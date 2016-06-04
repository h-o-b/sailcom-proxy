package ch.sailcom.mobile.server;

import java.util.Date;
import java.util.List;

import ch.sailcom.mobile.Booking;
import ch.sailcom.mobile.Lake;
import ch.sailcom.mobile.Harbor;
import ch.sailcom.mobile.Ship;
import ch.sailcom.mobile.Trip;
import ch.sailcom.mobile.User;
import ch.sailcom.mobile.WeatherInfo;

public interface ServerSession {

	boolean isConnected();
	void connect();

	boolean isLoggedIn();
	boolean login(String userNr, String pwd);
	void logout();

	String getSessionId();
	User getUser();

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

	List<Trip> getTrips();
	List<Booking> getBookings(int shipId, Date fromDate, int nofWeeks);

	List<WeatherInfo> getWeatherInfo(int harborId, boolean isDet);

}
