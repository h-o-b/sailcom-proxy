package ch.sailcom.server.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import ch.sailcom.server.model.Booking;
import ch.sailcom.server.model.Trip;
import ch.sailcom.server.proxy.BookingProxy;
import ch.sailcom.server.service.BookingService;

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
