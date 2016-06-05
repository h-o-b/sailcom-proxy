package ch.sailcom.mobile.svc;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ch.sailcom.mobile.Lake;
import ch.sailcom.mobile.server.ServerSession;

/**
 * Lake Service
 */
@Path("/lakes")
public class LakeSvc {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Lake> getAllLakes(
		@Context HttpServletRequest request,
		@Context HttpServletResponse response
	) throws IOException {

		HttpSession clientSession = request.getSession(true);
		ServerSession serverSession = SvcUtil.getServerSession(clientSession);

		if (!serverSession.isLoggedIn()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		if (!serverSession.isConnected()) {
			serverSession.connect();
		}

		return serverSession.getLakes();

	}

	@GET
	@Path("/my")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Lake> getMyLakes(
		@Context HttpServletRequest request,
		@Context HttpServletResponse response
	) throws IOException {

		HttpSession clientSession = request.getSession(true);
		ServerSession serverSession = SvcUtil.getServerSession(clientSession);

		if (!serverSession.isLoggedIn()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		if (!serverSession.isConnected()) {
			serverSession.connect();
		}

		return serverSession.getMyLakes();

	}

	@GET
	@Path("/{lakeId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Lake getLake(
		@Context HttpServletRequest request,
		@Context HttpServletResponse response,
		@PathParam("lakeId") Integer lakeId
	) throws IOException {

		HttpSession clientSession = request.getSession(true);
		ServerSession serverSession = SvcUtil.getServerSession(clientSession);

		if (!serverSession.isLoggedIn()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		if (!serverSession.isConnected()) {
			serverSession.connect();
		}

		return serverSession.getLake(lakeId);

	}

}
