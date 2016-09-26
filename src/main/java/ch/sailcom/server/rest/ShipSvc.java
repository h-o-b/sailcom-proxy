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

import ch.sailcom.server.dto.Ship;
import ch.sailcom.server.proxy.StaticDataProxy;
import ch.sailcom.server.rest.util.Authenticated;

/**
 * Ship Service
 */
@Path("/ships")
@Authenticated
public class ShipSvc {

	@Context
	HttpServletRequest request;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Ship> getAllShips() throws IOException {
		return SvcUtil.getSessionProxy(request).getProxy(StaticDataProxy.class).getShips();
	}

	@GET
	@Path("/{shipId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Ship getShip(@PathParam("shipId") Integer shipId) throws IOException {
		return SvcUtil.getSessionProxy(request).getProxy(StaticDataProxy.class).getShip(shipId);
	}

}
