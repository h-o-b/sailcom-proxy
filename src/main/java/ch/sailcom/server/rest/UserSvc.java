package ch.sailcom.server.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ch.sailcom.server.dto.User;
import ch.sailcom.server.dto.UserInfo;
import ch.sailcom.server.dto.UserPreference;
import ch.sailcom.server.proxy.StaticDataProxy;
import ch.sailcom.server.proxy.UserDataProxy;
import ch.sailcom.server.rest.util.Authenticated;

/**
 * Ship Service
 */
@Path("/user")
@Authenticated
public class UserSvc {

	@Inject
	StaticDataProxy staticDataProxy;

	@Inject
	UserDataProxy userDataProxy;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser() throws IOException {
		return userDataProxy.getUser();
	}

	@GET
	@Path("/info")
	@Produces(MediaType.APPLICATION_JSON)
	public UserInfo getUserInfo() throws IOException {
		return userDataProxy.getUserInfo();
	}

	@GET
	@Path("/pref")
	@Produces(MediaType.APPLICATION_JSON)
	public UserPreference getUserPreference() throws IOException {
		return userDataProxy.getUserPreference();
	}

	@GET
	@Path("/lakes")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> getLakes() throws IOException {
		return userDataProxy.getLakes();
	}

	@GET
	@Path("/harbors")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> getHarbors() throws IOException {
		return userDataProxy.getHarbors();
	}

	@GET
	@Path("/ships")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> getShips() throws IOException {
		return userDataProxy.getShips();
	}

	@GET
	@Path("/ships/{shipId}/like")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> likeShip(@PathParam("shipId") Integer shipId) throws IOException {
		return userDataProxy.like(staticDataProxy.getShip(shipId));
	}

	@GET
	@Path("/ships/{shipId}/unlike")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> unlikeShip(@PathParam("shipId") Integer shipId) throws IOException {
		return userDataProxy.unlike(staticDataProxy.getShip(shipId));
	}

	@GET
	@Path("/ships/{shipId}/rate/{starCount}")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<Integer, Integer> rateShip(@PathParam("shipId") Integer shipId, @PathParam("starCount") Integer starCount) throws IOException {
		return userDataProxy.rate(staticDataProxy.getShip(shipId), starCount);
	}

}
