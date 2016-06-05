package ch.sailcom.mobile.svc;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ch.sailcom.mobile.Trip;
import ch.sailcom.mobile.server.ServerSession;

/**
 * Trip Service
 */
@Path("/trips")
public class TripSvc {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Trip> getMyTrips(
		@Context HttpServletRequest request,
		@Context HttpServletResponse response
	) throws IOException {

		HttpSession clientSession = request.getSession(true);
		ServerSession serverSession = SvcUtil.getServerSession(clientSession);

		if (!serverSession.isLoggedIn()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		if (!serverSession.isConnected()) {
			serverSession.connect();
		}

		return serverSession.getTrips();

	}

}
