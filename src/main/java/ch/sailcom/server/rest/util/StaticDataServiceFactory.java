package ch.sailcom.server.rest.util;

import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.api.Factory;

import ch.sailcom.server.service.SessionService;
import ch.sailcom.server.service.StaticDataService;

public class StaticDataServiceFactory implements Factory<StaticDataService> {

	private final HttpServletRequest request;

	@Inject
	public StaticDataServiceFactory(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public StaticDataService provide() {
		SessionService session = SvcUtil.getSessionService(request);
		if (session == null) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(SvcUtil.getErrorEntity("no server session")).build());
		}
		return session.getService(StaticDataService.class);
	}

	@Override
	public void dispose(StaticDataService t) {
	}

}
