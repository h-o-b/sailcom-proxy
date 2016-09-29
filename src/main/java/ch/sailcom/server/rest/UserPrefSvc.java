package ch.sailcom.server.rest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.sailcom.server.model.UserPref;
import ch.sailcom.server.rest.util.Authenticated;
import ch.sailcom.server.rest.util.SvcUtil;
import ch.sailcom.server.service.StaticDataService;
import ch.sailcom.server.service.UserService;

/**
 * User Preference Service
 */
@Path("/userPref")
@Authenticated
public class UserPrefSvc {

	@Inject
	StaticDataService staticDataService;

	@Inject
	UserService userService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public UserPref getUserPreference() throws IOException {
		return userService.getUserPref();
	}

	@GET
	@Path("/favoriteShips")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Integer> getShips() throws IOException {
		return userService.getFavoriteShips();
	}

	@GET
	@Path("/ratedShips")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<Integer, Integer> getRatedShips() throws IOException {
		return userService.getRatedShips();
	}

	@GET
	@Path("/ships/{shipId}/like")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Integer> likeShip(@PathParam("shipId") Integer shipId) throws IOException {
		if (shipId == null) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(SvcUtil.getErrorEntity("shipId parameter is mandatory")).build());
		}
		return userService.like(staticDataService.getShip(shipId));
	}

	@GET
	@Path("/ships/{shipId}/unlike")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Integer> unlikeShip(@PathParam("shipId") Integer shipId) throws IOException {
		if (shipId == null) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(SvcUtil.getErrorEntity("shipId parameter is mandatory")).build());
		}
		return userService.unlike(staticDataService.getShip(shipId));
	}

	@GET
	@Path("/ships/{shipId}/rate/{starCount}")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<Integer, Integer> rateShip(@PathParam("shipId") Integer shipId, @PathParam("starCount") Integer starCount) throws IOException {
		if (shipId == null) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(SvcUtil.getErrorEntity("shipId parameter is mandatory")).build());
		} else if (starCount == null) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(SvcUtil.getErrorEntity("starCount parameter is mandatory")).build());
		}
		return userService.rate(staticDataService.getShip(shipId), starCount);
	}

}
