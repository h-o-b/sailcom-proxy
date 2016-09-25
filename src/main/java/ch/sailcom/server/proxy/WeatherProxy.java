package ch.sailcom.server.proxy;

import java.util.List;

import ch.sailcom.server.dto.WeatherInfo;

public interface WeatherProxy {

	List<WeatherInfo> getWeatherInfo(int harborId, boolean isDet);

}
