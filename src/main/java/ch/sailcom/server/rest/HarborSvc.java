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
import ch.sailcom.server.proxy.StaticDataProxy;
import ch.sailcom.server.rest.util.Authenticated;

/**
 * Harbor Service
 */
@Path("/harbors")
@Authenticated
public class HarborSvc {

	@Context
	HttpServletRequest request;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Harbor> getAllHarbors() throws IOException {
		return SvcUtil.getSessionProxy(request).getProxy(StaticDataProxy.class).getHarbors();
	}

	@GET
	@Path("/{harborId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Harbor getHarbor(@PathParam("harborId") Integer harborId) throws IOException {
		return SvcUtil.getSessionProxy(request).getProxy(StaticDataProxy.class).getHarbor(harborId);
	}

}
