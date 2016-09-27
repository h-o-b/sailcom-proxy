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

import ch.sailcom.server.dto.Harbor;
import ch.sailcom.server.proxy.StaticDataProxy;
import ch.sailcom.server.rest.util.Authenticated;
import ch.sailcom.server.rest.util.SvcUtil;

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
		if (harborId == null) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(SvcUtil.getErrorEntity("harborId parameter is mandatory")).build());
		}
		return staticDataProxy.getHarbor(harborId);
	}

}
