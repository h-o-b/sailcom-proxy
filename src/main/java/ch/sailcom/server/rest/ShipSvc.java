package ch.sailcom.server.rest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.sailcom.server.model.Ship;
import ch.sailcom.server.rest.util.Authenticated;
import ch.sailcom.server.rest.util.SvcUtil;
import ch.sailcom.server.service.StaticDataService;

/**
 * Ship Service
 */
@Path("/ships")
@Authenticated
public class ShipSvc {

	@Inject
	StaticDataService staticDataService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Ship> getAllShips() throws IOException {
		return staticDataService.getShips();
	}

	@GET
	@Path("/{shipId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Ship getShip(@PathParam("shipId") Integer shipId) throws IOException {
		if (shipId == null) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(SvcUtil.getErrorEntity("shipId parameter is mandatory")).build());
		}
		return staticDataService.getShip(shipId);
	}

}
