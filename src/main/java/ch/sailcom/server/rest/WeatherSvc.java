package ch.sailcom.server.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ch.sailcom.server.dto.WeatherInfo;
import ch.sailcom.server.proxy.WeatherProxy;

/**
 * Weather Service
 */
@Path("/weather")
public class WeatherSvc {

	@GET
	@Path("/{harborId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<WeatherInfo> getWeatherInfo(@Context HttpServletRequest request, @PathParam("harborId") Integer harborId, @QueryParam("det") Boolean isDet) throws IOException {
		return SvcUtil.getSessionProxy(request).getProxy(WeatherProxy.class).getWeatherInfo(harborId, isDet == null ? false : isDet);
	}

}
