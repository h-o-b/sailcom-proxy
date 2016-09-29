package ch.sailcom.server.service.impl;

import java.util.Date;
import java.util.List;

import ch.sailcom.server.model.Booking;
import ch.sailcom.server.model.Trip;
import ch.sailcom.server.proxy.BookingProxy;
import ch.sailcom.server.service.BookingService;

public class BookingServiceImpl implements BookingService {

	private final BookingProxy bookingProxy;

	public BookingServiceImpl(BookingProxy bookingProxy) {
		this.bookingProxy = bookingProxy;
	}

	@Override
	public List<Trip> getTrips() {
		return this.bookingProxy.getTrips();
	}

	@Override
	public List<Booking> getBookings(int shipId, Date fromDate, int nofWeeks) {
		return this.bookingProxy.getBookings(shipId, fromDate, nofWeeks);
	}

}
