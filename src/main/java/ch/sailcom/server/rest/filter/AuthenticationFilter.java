package ch.sailcom.server.rest.filter;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import ch.sailcom.server.proxy.SessionProxy;
import ch.sailcom.server.rest.SvcUtil;

@Authenticated
@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

	@Context
	private HttpServletRequest servletRequest;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		SessionProxy serverSession = SvcUtil.getSessionProxy(servletRequest);
		if (serverSession == null) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(SvcUtil.getErrorMessage("no server session")).build());
		} else if (!serverSession.isLoggedIn()) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity(SvcUtil.getErrorMessage("server session not authenticated")).build());
		}
	}

}
