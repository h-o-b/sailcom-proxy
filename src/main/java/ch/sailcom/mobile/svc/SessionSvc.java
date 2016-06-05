package ch.sailcom.mobile.svc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ch.sailcom.mobile.Session;
import ch.sailcom.mobile.server.ServerSession;
import ch.sailcom.mobile.server.impl.NoSessionException;

/**
 * Session Service
 */
@Path("/session")
public class SessionSvc {

	@GET
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public Session login(
		@Context HttpServletRequest request,
		@Context HttpServletResponse response,
		@QueryParam("user") String user,
		@QueryParam("pwd") String pwd
	) throws IOException {

		if (user == null || user.equals("")) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		} else if (pwd == null || pwd.equals("")) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		HttpSession clientSession = request.getSession(true);
		ServerSession serverSession = SvcUtil.getServerSession(clientSession);

		if (!serverSession.isConnected()) {
			serverSession.connect();
		}

		if (!serverSession.login(user, pwd)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		Session s = new Session();
		s.sessionId = serverSession.getSessionId();
		s.user = serverSession.getUser();

		return s;

	}

	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public void logout(
		@Context HttpServletRequest request,
		@Context HttpServletResponse response
	) throws IOException {

		HttpSession clientSession = request.getSession(true);
		ServerSession serverSession = SvcUtil.getServerSession(clientSession);

		try {
			if (serverSession.isLoggedIn()) {
				serverSession.logout();
			}
		} catch (NoSessionException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}

	}

}
