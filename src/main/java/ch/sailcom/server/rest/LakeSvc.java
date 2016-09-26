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

import ch.sailcom.server.dto.Lake;
import ch.sailcom.server.proxy.StaticDataProxy;
import ch.sailcom.server.proxy.UserDataProxy;

/**
 * Lake Service
 */
@Path("/lakes")
public class LakeSvc {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Lake> getAllLakes(@Context HttpServletRequest request) throws IOException {
		return SvcUtil.getSessionProxy(request).getProxy(StaticDataProxy.class).getLakes();
	}

	@GET
	@Path("/my")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Lake> getMyLakes(@Context HttpServletRequest request) throws IOException {
		return SvcUtil.getSessionProxy(request).getProxy(UserDataProxy.class).getMyLakes();
	}

	@GET
	@Path("/{lakeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Lake getLake(@Context HttpServletRequest request, @PathParam("lakeId") Integer lakeId) throws IOException {
		return SvcUtil.getSessionProxy(request).getProxy(StaticDataProxy.class).getLake(lakeId);
	}

}
