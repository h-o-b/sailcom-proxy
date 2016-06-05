package ch.sailcom.mobile.svc;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ch.sailcom.mobile.Ship;
import ch.sailcom.mobile.server.ServerSession;

/**
 * Ship Service
 */
@Path("/ships")
public class ShipSvc {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Ship> getAllShips(
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

		return serverSession.getShips();

	}

	@GET
	@Path("/my")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Ship> getMyShips(
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

		return serverSession.getMyShips();

	}

	@GET
	@Path("/{shipId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Ship getShip(
		@Context HttpServletRequest request,
		@Context HttpServletResponse response,
		@PathParam("shipId") Integer shipId
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

		return serverSession.getShip(shipId);

	}

}
