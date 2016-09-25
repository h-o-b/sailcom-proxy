package ch.sailcom.server.proxy;

import java.util.Date;
import java.util.List;

import ch.sailcom.server.dto.Booking;
import ch.sailcom.server.dto.Trip;

public interface BookingProxy {

	List<Trip> getTrips();

	List<Booking> getBookings(int shipId, Date fromDate, int nofWeeks);

}
