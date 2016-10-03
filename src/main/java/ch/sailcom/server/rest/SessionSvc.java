package ch.sailcom.server.rest;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.sailcom.server.rest.dto.SessionInfo;
import ch.sailcom.server.rest.util.Authenticated;
import ch.sailcom.server.rest.util.SvcUtil;
import ch.sailcom.server.service.SessionService;

/**
 * Session Service
 */
@Path("/session")
public class SessionSvc {

	private static Logger LOGGER = LoggerFactory.getLogger(SessionSvc.class);

	@Inject
	SessionService sessionService;

	@GET
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public SessionInfo login(@QueryParam("user") String user, @QueryParam("pwd") String pwd) throws IOException {

		try {

			if (user == null) {
				throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(SvcUtil.getErrorEntity("user parameter is mandatory")).build());
			} else if (pwd == null) {
				throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(SvcUtil.getErrorEntity("pwd parameter is mandatory")).build());
			}

			if (sessionService.isLoggedIn()) {
				return new SessionInfo(sessionService.getSessionId(), sessionService.getUser());
			}

			LOGGER.info("User {} logging in ...", user);
			if (!sessionService.login(user, pwd)) {
				LOGGER.info("User {} login failed", user);
				throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(SvcUtil.getErrorEntity("login denied")).build());
			}

			LOGGER.info("User {} successfully logged in", user);
			return new SessionInfo(sessionService.getSessionId(), sessionService.getUser());

		} catch (Exception e) {
			LOGGER.error("login crashed", e);
			throw e;
		}

	}

	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	@Authenticated
	public void logout() throws IOException {

		try {
			if (sessionService.isLoggedIn()) {
				sessionService.logout();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(SvcUtil.getErrorEntity("logout failed")).build());
		}

	}

}
