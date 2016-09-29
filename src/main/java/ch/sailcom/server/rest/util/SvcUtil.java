package ch.sailcom.server.rest.util;

import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ch.sailcom.server.service.SessionService;
import ch.sailcom.server.service.impl.SessionServiceImpl;

public class SvcUtil {

	private static final String SESSION = "sailcomSession";

	public static SessionService getSessionService(HttpServletRequest request) {
		HttpSession clientSession = request.getSession();
		SessionService session = clientSession == null ? null : (SessionService) clientSession.getAttribute(SESSION);
		return session;
	}

	public static SessionService initSessionService(HttpServletRequest request) {
		HttpSession clientSession = request.getSession(true);
		if (clientSession.getAttribute(SESSION) == null) {
			clientSession.setAttribute(SESSION, new SessionServiceImpl());
		}
		return (SessionService) clientSession.getAttribute(SESSION);
	}

	public static void ensureSessionService(HttpServletRequest request) {
		SessionService sessionService = getSessionService(request);
		if (sessionService == null) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(SvcUtil.getErrorEntity("no server session")).build());
		} else if (!sessionService.isLoggedIn()) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(SvcUtil.getErrorEntity("server session not authenticated")).build());
		}
	}

	public static String getErrorEntity(String msg) {
		return "{ \"error\": \"" + msg + "\"}";
	}

}
