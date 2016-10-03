package ch.sailcom.server.rest;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ch.sailcom.server.model.Lake;
import ch.sailcom.server.rest.util.Authenticated;
import ch.sailcom.server.rest.util.SvcUtil;
import ch.sailcom.server.service.StaticDataService;

/**
 * Lake Service
 */
@Path("/lakes")
@Authenticated
public class LakeSvc {

	@Inject
	StaticDataService staticDataService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Lake> getAllLakes() throws IOException {
		return staticDataService.getLakes();
	}

	@GET
	@Path("/{lakeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Lake getLake(@PathParam("lakeId") Integer lakeId) throws IOException {
		SvcUtil.check(lakeId != null, "lakeId parameter is mandatory");
		return staticDataService.getLake(lakeId);
	}

}
