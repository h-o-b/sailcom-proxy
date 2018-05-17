package ch.sailcom.session.api.rest;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import ch.sailcom.session.service.SessionService;

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
