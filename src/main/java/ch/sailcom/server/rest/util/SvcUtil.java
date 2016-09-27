package ch.sailcom.server.rest.util;

import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ch.sailcom.server.proxy.SessionProxy;
import ch.sailcom.server.proxy.impl.SessionProxyImpl;

public class SvcUtil {

	private static final String SESSION = "sailcomSession";

	public static SessionProxy getSessionProxy(HttpServletRequest request) {
		HttpSession clientSession = request.getSession();
		SessionProxy session = clientSession == null ? null : (SessionProxy) clientSession.getAttribute(SESSION);
		return session;
	}

	public static SessionProxy initSessionProxy(HttpServletRequest request) {
		HttpSession clientSession = request.getSession(true);
		if (clientSession.getAttribute(SESSION) == null) {
			clientSession.setAttribute(SESSION, new SessionProxyImpl());
		}
		return (SessionProxy) clientSession.getAttribute(SESSION);
	}

	public static void ensureSessionProxy(HttpServletRequest request) {
		SessionProxy serverSession = SvcUtil.getSessionProxy(request);
		if (serverSession == null) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(SvcUtil.getErrorEntity("no server session")).build());
		} else if (!serverSession.isLoggedIn()) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(SvcUtil.getErrorEntity("server session not authenticated")).build());
		}
	}

	public static String getErrorEntity(String msg) {
		return "{ \"error\": \"" + msg + "\"}";
	}

}
