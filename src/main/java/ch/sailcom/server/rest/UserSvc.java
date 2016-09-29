package ch.sailcom.server.rest;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ch.sailcom.server.model.User;
import ch.sailcom.server.model.UserInfo;
import ch.sailcom.server.rest.util.Authenticated;
import ch.sailcom.server.service.UserService;

/**
 * User Service
 */
@Path("/user")
@Authenticated
public class UserSvc {

	@Inject
	UserService userService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser() throws IOException {
		return userService.getUser();
	}

	@GET
	@Path("/info")
	@Produces(MediaType.APPLICATION_JSON)
	public UserInfo getUserInfo() throws IOException {
		return userService.getUserInfo();
	}

	@GET
	@Path("/availableLakes")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> getLakes() throws IOException {
		return userService.getAvailableLakes();
	}

	@GET
	@Path("/availableHarbors")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> getHarbors() throws IOException {
		return userService.getAvailableHarbors();
	}

	@GET
	@Path("/availableShips")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> getShips() throws IOException {
		return userService.getAvailableShips();
	}

}
