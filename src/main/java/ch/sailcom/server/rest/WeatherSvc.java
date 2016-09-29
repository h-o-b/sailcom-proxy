package ch.sailcom.server.rest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.sailcom.server.model.WeatherInfo;
import ch.sailcom.server.rest.util.Authenticated;
import ch.sailcom.server.rest.util.SvcUtil;
import ch.sailcom.server.service.WeatherService;

/**
 * Weather Service
 */
@Path("/weather")
@Authenticated
public class WeatherSvc {

	@Inject
	WeatherService weatherService;

	@GET
	@Path("/harbor/{harborId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<WeatherInfo> getWeatherInfo(@PathParam("harborId") Integer harborId, @QueryParam("det") Boolean isDet) throws IOException {
		if (harborId == null) {
			throw new WebApplicationException(Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity(SvcUtil.getErrorEntity("harborId parameter is mandatory")).build());
		}
		return weatherService.getWeatherInfo(harborId, isDet == null ? false : isDet);
	}

}
