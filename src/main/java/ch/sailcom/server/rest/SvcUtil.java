package ch.sailcom.server.rest;

import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.sailcom.server.proxy.SessionProxy;
import ch.sailcom.server.proxy.impl.SessionProxyImpl;

public class SvcUtil {

	private static Logger LOGGER = LoggerFactory.getLogger(SvcUtil.class);

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

	public static SessionProxy ensureSessionProxy(HttpServletRequest request) {
		HttpSession clientSession = request.getSession(true);
		SessionProxy serverSession = (SessionProxy) clientSession.getAttribute(SESSION);
		if (serverSession == null) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(SvcUtil.getErrorMessage("no server session")).build());
		} else if (!serverSession.isLoggedIn()) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(SvcUtil.getErrorMessage("no authenticated server session")).build());
		}
		return serverSession;
	}

	public static String getErrorMessage(String msg) {
		return "{ \"error\": \"" + msg + "\"}";
	}

}
