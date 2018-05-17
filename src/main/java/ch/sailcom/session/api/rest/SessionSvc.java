package ch.sailcom.session.api.rest;

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

import ch.sailcom.session.domain.SessionInfo;
import ch.sailcom.session.service.SessionService;

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

			SvcUtil.check(user != null, "user parameter is mandatory");
			SvcUtil.check(pwd != null, "pwd parameter is mandatory");

			if (sessionService.isLoggedIn()) {
				return new SessionInfo(sessionService.getSessionId(), sessionService.getUser());
			}

			LOGGER.info("User {} logging in ...", user);
			SvcUtil.check(sessionService.login(user, pwd), "login denied", HttpURLConnection.HTTP_UNAUTHORIZED);

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
