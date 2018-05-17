package ch.sailcom.weather.api.rest;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import ch.sailcom.session.api.rest.Authenticated;
import ch.sailcom.session.api.rest.SvcUtil;
import ch.sailcom.weather.domain.WeatherInfo;
import ch.sailcom.weather.service.WeatherService;

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
		SvcUtil.check(harborId != null, "harborId parameter is mandatory");
		return weatherService.getWeatherInfo(harborId, isDet == null ? false : isDet);
	}

}
