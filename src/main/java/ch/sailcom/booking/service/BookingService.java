package ch.sailcom.booking.service;

import java.util.Date;
import java.util.List;

import ch.sailcom.booking.domain.Booking;
import ch.sailcom.booking.domain.Trip;
import ch.sailcom.common.service.Service;

public interface BookingService extends Service {

	List<Trip> getTrips();

	List<Booking> getBookings(int shipId, Date fromDate, int nofWeeks);

}
