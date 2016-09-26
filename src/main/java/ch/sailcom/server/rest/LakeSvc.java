package ch.sailcom.server.rest;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ch.sailcom.server.dto.Lake;
import ch.sailcom.server.proxy.StaticDataProxy;
import ch.sailcom.server.rest.util.Authenticated;

/**
 * Lake Service
 */
@Path("/lakes")
@Authenticated
public class LakeSvc {

	@Context
	HttpServletRequest request;

	@Inject
	StaticDataProxy staticDataProxy;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Lake> getAllLakes() throws IOException {
		return staticDataProxy.getLakes();
		// return SvcUtil.getSessionProxy(request).getProxy(StaticDataProxy.class).getLakes();
	}

	@GET
	@Path("/{lakeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Lake getLake(@PathParam("lakeId") Integer lakeId) throws IOException {
		return SvcUtil.getSessionProxy(request).getProxy(StaticDataProxy.class).getLake(lakeId);
	}

}
