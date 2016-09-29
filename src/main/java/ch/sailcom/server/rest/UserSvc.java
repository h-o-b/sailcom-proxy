package ch.sailcom.server.rest;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ch.sailcom.server.dto.User;
import ch.sailcom.server.dto.UserInfo;
import ch.sailcom.server.proxy.UserDataProxy;
import ch.sailcom.server.rest.util.Authenticated;

/**
 * User Service
 */
@Path("/user")
@Authenticated
public class UserSvc {

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
	@Path("/availableLakes")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> getLakes() throws IOException {
		return userDataProxy.getAvailableLakes();
	}

	@GET
	@Path("/availableHarbors")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> getHarbors() throws IOException {
		return userDataProxy.getAvailableHarbors();
	}

	@GET
	@Path("/availableShips")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> getShips() throws IOException {
		return userDataProxy.getAvailableShips();
	}

}
