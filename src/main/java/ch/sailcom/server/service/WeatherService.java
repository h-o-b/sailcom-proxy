package ch.sailcom.server.service;

import java.util.List;

import ch.sailcom.server.model.WeatherInfo;

public interface WeatherService extends Service {

	List<WeatherInfo> getWeatherInfo(int harborId, boolean isDet);

}
