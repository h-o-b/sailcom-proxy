package ch.sailcom.mobile.svc;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ch.sailcom.mobile.WeatherInfo;
import ch.sailcom.mobile.server.ServerSession;

/**
 * Weather Service
 */
@Path("/weather")
public class WeatherSvc {

	@GET
	@Path("/{harborId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<WeatherInfo> getWeatherInfo(
		@Context HttpServletRequest request,
		@Context HttpServletResponse response,
		@PathParam("harborId") Integer harborId,
		@QueryParam("det") Boolean isDet
	) throws IOException {

		HttpSession clientSession = request.getSession(true);
		ServerSession serverSession = SvcUtil.getServerSession(clientSession);

		if (!serverSession.isLoggedIn()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		if (!serverSession.isConnected()) {
			serverSession.connect();
		}

		return serverSession.getWeatherInfo(harborId, isDet == null ? false : isDet);

	}

}
