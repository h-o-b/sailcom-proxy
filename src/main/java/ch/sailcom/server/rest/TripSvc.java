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
import ch.sailcom.server.proxy.BookingProxy;
import ch.sailcom.server.rest.filter.Authenticated;

/**
 * Trip Service
 */
@Path("/trips")
@Authenticated
public class TripSvc {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Trip> getMyTrips(@Context HttpServletRequest request) throws IOException {
		return SvcUtil.getSessionProxy(request).getProxy(BookingProxy.class).getTrips();
	}

}
