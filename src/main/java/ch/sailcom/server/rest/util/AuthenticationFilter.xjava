package ch.sailcom.server.rest.util;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

@Authenticated
@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

	@Context
	private HttpServletRequest servletRequest;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		SvcUtil.ensureSessionService(servletRequest);
	}

}
