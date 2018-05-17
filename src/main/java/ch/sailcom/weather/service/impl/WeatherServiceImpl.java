package ch.sailcom.weather.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import ch.sailcom.weather.domain.WeatherInfo;
import ch.sailcom.weather.proxy.WeatherProxy;
import ch.sailcom.weather.service.WeatherService;

@SessionScoped
public class WeatherServiceImpl implements WeatherService, Serializable {

	private static final long serialVersionUID = -1962037195589388133L;

	@Inject
	private WeatherProxy weatherProxy;

	@Override
	public List<WeatherInfo> getWeatherInfo(int harborId, boolean isDet) {
		return weatherProxy.getWeatherInfo(harborId, isDet);
	}

}
