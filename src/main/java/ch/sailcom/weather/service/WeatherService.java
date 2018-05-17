package ch.sailcom.weather.service;

import java.util.List;

import ch.sailcom.common.service.Service;
import ch.sailcom.weather.domain.WeatherInfo;

public interface WeatherService extends Service {

	List<WeatherInfo> getWeatherInfo(int harborId, boolean isDet);

}
