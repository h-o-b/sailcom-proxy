package ch.sailcom.server.service.impl;

import java.util.List;

import ch.sailcom.server.model.WeatherInfo;
import ch.sailcom.server.proxy.WeatherProxy;
import ch.sailcom.server.service.WeatherService;

public class WeatherServiceImpl implements WeatherService {

	private final WeatherProxy weatherProxy;

	public WeatherServiceImpl(WeatherProxy weatherProxy) {
		this.weatherProxy = weatherProxy;
	}

	@Override
	public List<WeatherInfo> getWeatherInfo(int harborId, boolean isDet) {
		return weatherProxy.getWeatherInfo(harborId, isDet);
	}

}
