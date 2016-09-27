package ch.sailcom.server.rest.util;

import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.api.Factory;

import ch.sailcom.server.proxy.SessionProxy;
import ch.sailcom.server.proxy.UserDataProxy;

public class UserDataProxyFactory implements Factory<UserDataProxy> {

	private final HttpServletRequest request;

	@Inject
	public UserDataProxyFactory(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public UserDataProxy provide() {
		SessionProxy session = SvcUtil.getSessionProxy(request);
		if (session == null) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(SvcUtil.getErrorEntity("no server session")).build());
		}
		return session.getProxy(UserDataProxy.class);
	}

	@Override
	public void dispose(UserDataProxy t) {
	}

}
