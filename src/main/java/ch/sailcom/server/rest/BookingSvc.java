package ch.sailcom.server.rest;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ch.sailcom.server.model.Booking;
import ch.sailcom.server.rest.util.Authenticated;
import ch.sailcom.server.rest.util.SvcUtil;
import ch.sailcom.server.service.BookingService;
import ch.sailcom.server.service.StaticDataService;

/**
 * Bookings Service
 */
@Path("/bookings")
@Authenticated
public class BookingSvc {

	private static final DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

	@Inject
	StaticDataService staticDataService;

	@Inject
	BookingService bookingService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Booking> getBookings(@QueryParam("shipId") Integer shipId, @QueryParam("date") String date, @QueryParam("nofWeeks") Integer nofWeeks) throws IOException {

		/* Validate Service Input */
		SvcUtil.check(shipId != null, "shipId parameter is mandatory");
		SvcUtil.check(staticDataService.getShip(shipId) != null, "ship not found");

		nofWeeks = nofWeeks == null ? 1 : nofWeeks;
		Date currDate = null;
		if (date != null && !date.equals("")) {
			try {
				currDate = df.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		} else {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			currDate = cal.getTime();
		}

		return bookingService.getBookings(shipId, currDate, nofWeeks);

	}

}
