package ch.sailcom.server.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ch.sailcom.server.dto.Trip;

/**
 * Trip Service
 */
@Path("/trips")
public class TripSvc {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Trip> getMyTrips(@Context HttpServletRequest request) throws IOException {
		return SvcUtil.getBookingProxy(request).getTrips();
	}

}