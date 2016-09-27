package ch.sailcom.server.rest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.sailcom.server.dto.WeatherInfo;
import ch.sailcom.server.proxy.WeatherProxy;
import ch.sailcom.server.rest.util.Authenticated;
import ch.sailcom.server.rest.util.SvcUtil;

/**
 * Weather Service
 */
@Path("/weather")
@Authenticated
public class WeatherSvc {

	@Context
	HttpServletRequest request;

	@GET
	@Path("/harbor/{harborId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<WeatherInfo> getWeatherInfo(@PathParam("harborId") Integer harborId, @QueryParam("det") Boolean isDet) throws IOException {
		if (harborId == null) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(SvcUtil.getErrorEntity("harborId parameter is mandatory")).build());
		}
		return SvcUtil.getSessionProxy(request).getProxy(WeatherProxy.class).getWeatherInfo(harborId, isDet == null ? false : isDet);
	}

}
