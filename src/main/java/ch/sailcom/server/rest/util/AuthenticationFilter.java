package ch.sailcom.server.rest.util;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import ch.sailcom.server.service.SessionService;

@Authenticated
@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

	@Inject
	SessionService sessionService;

	@Override
	public void filter(ContainerRequestContext arg0) throws IOException {
		SvcUtil.check(sessionService.isLoggedIn(), "server session not authenticated", HttpURLConnection.HTTP_UNAUTHORIZED);
	}

}
