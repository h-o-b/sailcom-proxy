package ch.sailcom.weather.proxy;

import java.util.List;

import ch.sailcom.weather.domain.WeatherInfo;

public interface WeatherProxy {

	List<WeatherInfo> getWeatherInfo(int harborId, boolean isDet);

}
