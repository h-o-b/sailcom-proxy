package ch.sailcom.server.proxy;

import java.util.Date;
import java.util.List;

import ch.sailcom.server.model.Booking;
import ch.sailcom.server.model.Trip;

public interface BookingProxy {

	List<Trip> getTrips();

	List<Booking> getBookings(int shipId, Date fromDate, int nofWeeks);

}
