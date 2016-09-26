package ch.sailcom.server.rest;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ch.sailcom.server.dto.Harbor;
import ch.sailcom.server.proxy.StaticDataProxy;
import ch.sailcom.server.rest.util.Authenticated;

/**
 * Harbor Service
 */
@Path("/harbors")
@Authenticated
public class HarborSvc {

	@Inject
	StaticDataProxy staticDataProxy;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Harbor> getAllHarbors() throws IOException {
		return staticDataProxy.getHarbors();
	}

	@GET
	@Path("/{harborId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Harbor getHarbor(@PathParam("harborId") Integer harborId) throws IOException {
		return staticDataProxy.getHarbor(harborId);
	}

}
