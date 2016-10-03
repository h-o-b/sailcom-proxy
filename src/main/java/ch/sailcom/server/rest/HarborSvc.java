package ch.sailcom.server.rest;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ch.sailcom.server.model.Harbor;
import ch.sailcom.server.rest.util.Authenticated;
import ch.sailcom.server.rest.util.SvcUtil;
import ch.sailcom.server.service.StaticDataService;

/**
 * Harbor Service
 */
@Path("/harbors")
@Authenticated
public class HarborSvc {

	@Inject
	StaticDataService staticDataService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Harbor> getAllHarbors() throws IOException {
		return staticDataService.getHarbors();
	}

	@GET
	@Path("/{harborId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Harbor getHarbor(@PathParam("harborId") Integer harborId) throws IOException {
		SvcUtil.check(harborId != null, "harborId parameter is mandatory");
		return staticDataService.getHarbor(harborId);
	}

}
