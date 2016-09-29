package ch.sailcom.server.service;

import java.util.Date;
import java.util.List;

import ch.sailcom.server.model.Booking;
import ch.sailcom.server.model.Trip;

public interface BookingService extends Service {

	List<Trip> getTrips();

	List<Booking> getBookings(int shipId, Date fromDate, int nofWeeks);

}
