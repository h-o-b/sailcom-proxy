package ch.sailcom.server.rest;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ch.sailcom.server.dto.Trip;
import ch.sailcom.server.proxy.BookingProxy;
import ch.sailcom.server.rest.util.Authenticated;

/**
 * Trip Service
 */
@Path("/trips")
@Authenticated
public class TripSvc {

	@Inject
	BookingProxy bookingProxy;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Trip> getMyTrips() throws IOException {
		return bookingProxy.getTrips();
	}

}
