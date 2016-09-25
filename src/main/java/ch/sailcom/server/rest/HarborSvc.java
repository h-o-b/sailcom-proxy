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

import ch.sailcom.server.dto.Harbor;

/**
 * Harbor Service
 */
@Path("/harbors")
public class HarborSvc {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Harbor> getAllHarbors(@Context HttpServletRequest request) throws IOException {
		return SvcUtil.getStaticDataProxy(request).getHarbors();
	}

	@GET
	@Path("/my")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Harbor> getMyHarbors(@Context HttpServletRequest request) throws IOException {
		return SvcUtil.getStaticDataProxy(request).getMyHarbors();
	}

	@GET
	@Path("/{harborId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Harbor getHarbor(@Context HttpServletRequest request, @PathParam("harborId") Integer harborId) throws IOException {
		return SvcUtil.getStaticDataProxy(request).getHarbor(harborId);
	}

}
