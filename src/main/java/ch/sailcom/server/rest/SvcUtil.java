package ch.sailcom.server.rest;

import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import ch.sailcom.server.proxy.BookingProxy;
import ch.sailcom.server.proxy.SessionProxy;
import ch.sailcom.server.proxy.StaticDataProxy;
import ch.sailcom.server.proxy.WeatherProxy;
import ch.sailcom.server.proxy.impl.BookingProxyImpl;
import ch.sailcom.server.proxy.impl.SessionProxyImpl;
import ch.sailcom.server.proxy.impl.StaticDataProxyImpl;
import ch.sailcom.server.proxy.impl.WeatherProxyImpl;

public class SvcUtil {

	private static final String SESSION = "sailcomSession";
	private static final String STATIC_DATA = "sailcomStaticData";
	private static final String BOOKING = "sailcomBooking";
	private static final String WEATHER = "sailcomWeather";

	public static SessionProxy initSessionProxy(HttpServletRequest request) {
		HttpSession clientSession = request.getSession(true);
		if (clientSession.getAttribute(SESSION) == null) {
			clientSession.setAttribute(SESSION, new SessionProxyImpl());
			clientSession.setAttribute(STATIC_DATA, new StaticDataProxyImpl());
			clientSession.setAttribute(BOOKING, new BookingProxyImpl());
			clientSession.setAttribute(WEATHER, new WeatherProxyImpl());
		}
		return (SessionProxy) clientSession.getAttribute(SESSION);
	}

	private static SessionProxy ensureSessionProxy(HttpServletRequest request) {
		HttpSession clientSession = request.getSession(true);
		SessionProxy serverSession = (SessionProxy) clientSession.getAttribute(SESSION);
		if (serverSession == null) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(SvcUtil.getErrorMessage("no server session")).build());
		} else if (!serverSession.isLoggedIn()) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(SvcUtil.getErrorMessage("no authenticated server session")).build());
		}
		if (!serverSession.isConnected()) {
			serverSession.connect();
		}
		return serverSession;
	}

	public static SessionProxy getSessionProxy(HttpServletRequest request) {
		return ensureSessionProxy(request);
	}

	public static StaticDataProxy getStaticDataProxy(HttpServletRequest request) {
		ensureSessionProxy(request);
		HttpSession clientSession = request.getSession(true);
		return (StaticDataProxy) clientSession.getAttribute(STATIC_DATA);
	}

	public static BookingProxy getBookingProxy(HttpServletRequest request) {
		ensureSessionProxy(request);
		HttpSession clientSession = request.getSession(true);
		return (BookingProxy) clientSession.getAttribute(BOOKING);
	}

	public static WeatherProxy getWeatherProxy(HttpServletRequest request) {
		ensureSessionProxy(request);
		HttpSession clientSession = request.getSession(true);
		return (WeatherProxy) clientSession.getAttribute(WEATHER);
	}

	public static String getErrorMessage(String msg) {
		return "{ \"error\": \"" + msg + "\"}";
	}

}
