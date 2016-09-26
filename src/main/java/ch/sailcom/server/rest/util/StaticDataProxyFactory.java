package ch.sailcom.server.rest.util;

import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.api.Factory;

import ch.sailcom.server.proxy.SessionProxy;
import ch.sailcom.server.proxy.StaticDataProxy;
import ch.sailcom.server.rest.SvcUtil;

public class StaticDataProxyFactory implements Factory<StaticDataProxy> {

	private final HttpServletRequest request;

	@Inject
	public StaticDataProxyFactory(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public StaticDataProxy provide() {
		SessionProxy session = SvcUtil.getSessionProxy(request);
		if (session == null) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(SvcUtil.getErrorMessage("no server session")).build());
		}
		return session.getProxy(StaticDataProxy.class);
	}

	@Override
	public void dispose(StaticDataProxy t) {
	}

}
