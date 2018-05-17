package ch.sailcom.fleet.api.rest;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ch.sailcom.fleet.domain.Harbor;
import ch.sailcom.fleet.domain.Lake;
import ch.sailcom.fleet.domain.Ship;
import ch.sailcom.fleet.service.FleetService;
import ch.sailcom.session.api.rest.SvcUtil;

/**
 * Fleet Service
 */
@Path("/")
public class FleetSvc {

	@Inject
	FleetService staticDataService;

	@GET
	@Path("lakes")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Lake> getAllLakes() throws IOException {
		return staticDataService.getLakes();
	}

	@GET
	@Path("lakes/{lakeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Lake getLake(@PathParam("lakeId") Integer lakeId) throws IOException {
		SvcUtil.check(lakeId != null, "lakeId parameter is mandatory");
		return staticDataService.getLake(lakeId);
	}

	@GET
	@Path("harbors")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Harbor> getAllHarbors() throws IOException {
		return staticDataService.getHarbors();
	}

	@GET
	@Path("harbors/{harborId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Harbor getHarbor(@PathParam("harborId") Integer harborId) throws IOException {
		SvcUtil.check(harborId != null, "harborId parameter is mandatory");
		return staticDataService.getHarbor(harborId);
	}

	@GET
	@Path("ships")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Ship> getShips() throws IOException {
		return staticDataService.getShips();
	}

	@GET
	@Path("ships/{shipId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Ship getShip(@PathParam("shipId") Integer shipId) throws IOException {
		SvcUtil.check(shipId != null, "shipId parameter is mandatory");
		return staticDataService.getShip(shipId);
	}

}
