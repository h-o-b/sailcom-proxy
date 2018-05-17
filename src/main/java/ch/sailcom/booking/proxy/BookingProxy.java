package ch.sailcom.booking.proxy;

import java.util.Date;
import java.util.List;

import ch.sailcom.booking.domain.Booking;
import ch.sailcom.booking.domain.Trip;

public interface BookingProxy {

	List<Trip> getTrips();

	List<Booking> getBookings(int shipId, Date fromDate, int nofWeeks);

}
