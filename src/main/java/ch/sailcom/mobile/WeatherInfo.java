package ch.sailcom.mobile;

import java.io.Serializable;

public class WeatherInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	public String date;
	public int seqNr;
	public String hour;

	public String weather;
	public String temperature;
	public String windDirection;
	public String windSpeed;
	public String precipitation;
	public String precipitationProbability;
	public String sunHours;

}
