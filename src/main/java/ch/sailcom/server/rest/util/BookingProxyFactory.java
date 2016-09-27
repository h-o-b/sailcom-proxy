package ch.sailcom.server.rest.util;

import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.api.Factory;

import ch.sailcom.server.proxy.BookingProxy;
import ch.sailcom.server.proxy.SessionProxy;

public class BookingProxyFactory implements Factory<BookingProxy> {

	private final HttpServletRequest request;

	@Inject
	public BookingProxyFactory(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public BookingProxy provide() {
		SessionProxy session = SvcUtil.getSessionProxy(request);
		if (session == null) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(SvcUtil.getErrorEntity("no server session")).build());
		}
		return session.getProxy(BookingProxy.class);
	}

	@Override
	public void dispose(BookingProxy t) {
	}

}
