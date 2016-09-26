package ch.sailcom.server.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ch.sailcom.server.dto.Ship;
import ch.sailcom.server.dto.User;
import ch.sailcom.server.dto.UserInfo;
import ch.sailcom.server.proxy.StaticDataProxy;
import ch.sailcom.server.proxy.UserDataProxy;
import ch.sailcom.server.rest.util.Authenticated;

/**
 * Ship Service
 */
@Path("/user")
@Authenticated
public class UserSvc {

	@Context
	HttpServletRequest request;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser() throws IOException {
		return SvcUtil.getSessionProxy(request).getProxy(UserDataProxy.class).getUser();
	}

	@GET
	@Path("/info")
	@Produces(MediaType.APPLICATION_JSON)
	public UserInfo getUserInfo() throws IOException {
		return SvcUtil.getSessionProxy(request).getProxy(UserDataProxy.class).getUserInfo();
	}

	@GET
	@Path("/lakes")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> getLakes() throws IOException {
		return SvcUtil.getSessionProxy(request).getProxy(UserDataProxy.class).getLakes();
	}

	@GET
	@Path("/harbors")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> getHarbors() throws IOException {
		return SvcUtil.getSessionProxy(request).getProxy(UserDataProxy.class).getHarbors();
	}

	@GET
	@Path("/ships")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> getShips() throws IOException {
		return SvcUtil.getSessionProxy(request).getProxy(UserDataProxy.class).getShips();
	}

	@GET
	@Path("/ships/{shipId}/like")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> likeShip(@PathParam("shipId") Integer shipId) throws IOException {
		Ship ship = SvcUtil.getSessionProxy(request).getProxy(StaticDataProxy.class).getShip(shipId);
		return SvcUtil.getSessionProxy(request).getProxy(UserDataProxy.class).getUserData().like(ship);
	}

	@GET
	@Path("/ships/{shipId}/unlike")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> unlikeShip(@PathParam("shipId") Integer shipId) throws IOException {
		Ship ship = SvcUtil.getSessionProxy(request).getProxy(StaticDataProxy.class).getShip(shipId);
		return SvcUtil.getSessionProxy(request).getProxy(UserDataProxy.class).getUserData().unlike(ship);
	}

	@GET
	@Path("/ships/{shipId}/rate/{starCount}")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<Integer, Integer> rateShip(@PathParam("shipId") Integer shipId, @PathParam("starCount") Integer starCount) throws IOException {
		Ship ship = SvcUtil.getSessionProxy(request).getProxy(StaticDataProxy.class).getShip(shipId);
		return SvcUtil.getSessionProxy(request).getProxy(UserDataProxy.class).getUserData().rate(ship, starCount);
	}

}
