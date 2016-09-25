package ch.sailcom.server.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ch.sailcom.server.dto.Ship;

/**
 * Ship Service
 */
@Path("/ships")
public class ShipSvc {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Ship> getAllShips(@Context HttpServletRequest request) throws IOException {
		return SvcUtil.getStaticDataProxy(request).getShips();
	}

	@GET
	@Path("/my")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Ship> getMyShips(@Context HttpServletRequest request) throws IOException {
		return SvcUtil.getStaticDataProxy(request).getMyShips();
	}

	@GET
	@Path("/{shipId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Ship getShip(@Context HttpServletRequest request, @PathParam("shipId") Integer shipId) throws IOException {
		return SvcUtil.getStaticDataProxy(request).getShip(shipId);
	}

}
