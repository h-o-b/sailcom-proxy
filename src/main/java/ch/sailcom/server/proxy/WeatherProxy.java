package ch.sailcom.server.proxy;

import java.util.List;

import ch.sailcom.server.model.WeatherInfo;

public interface WeatherProxy {

	List<WeatherInfo> getWeatherInfo(int harborId, boolean isDet);

}
