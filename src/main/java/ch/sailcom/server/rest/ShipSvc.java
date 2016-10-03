package ch.sailcom.server.rest;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
		SvcUtil.check(shipId != null, "shipId parameter is mandatory");
		return staticDataService.getShip(shipId);
	}

}
