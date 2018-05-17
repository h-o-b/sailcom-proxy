package ch.sailcom.booking.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import ch.sailcom.booking.domain.Booking;
import ch.sailcom.booking.domain.Trip;
import ch.sailcom.booking.proxy.BookingProxy;
import ch.sailcom.booking.service.BookingService;

@SessionScoped
public class BookingServiceImpl implements BookingService, Serializable {

	private static final long serialVersionUID = 7118098844484726319L;

	@Inject
	private BookingProxy bookingProxy;

	@Override
	public List<Trip> getTrips() {
		return this.bookingProxy.getTrips();
	}

	@Override
	public List<Booking> getBookings(int shipId, Date fromDate, int nofWeeks) {
		return this.bookingProxy.getBookings(shipId, fromDate, nofWeeks);
	}

}
