package ch.sailcom.server.proxy.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import ch.sailcom.server.dto.Harbor;
import ch.sailcom.server.dto.WeatherInfo;
import ch.sailcom.server.proxy.StaticDataProxy;
import ch.sailcom.server.proxy.WeatherProxy;

public class WeatherProxyImpl implements WeatherProxy {

	private static final String WEATHER_BASE_URL = "https://www.meteoblue.com/de/schweiz/wetter-";

	private static final StaticDataProxy staticDataProxy = new StaticDataProxyImpl();

	public static List<WeatherInfo> getWeatherInfo(StaticDataProxy staticDataProxy, int harborId, boolean isDet) throws IOException, ParseException {

		// @formatter:off
		/*
		 * https://www.meteoblue.com/de/schweiz/wetter-biel
		 * 
		 * <div id="day1" class="tab active">                             // day1 .. day7
		 *   <a href="/de/schweiz/wetter-unterengstringen">
		 *     <div class="tab_content">
		 *       <div class="day">
		 *         <div class="tab_day_short">So</div>
		 *         <div class="tab_day_long">Heute</div>                  // Heute, Morgen, 25.08
		 *       </div>
		 *       <div class="weather">
		 *         <div class="pictoicon">
		 *           <div class="picon p07_iday" title="Wechselhaft mit Schauern"></div>
		 *         </div>
		 *       </div>
		 *       <div class="temps">
		 *         <div class="tab_temp_max" style="background-color: #D8F7A1; color: #000000">18C</div>
		 *         <div class="tab_temp_min" style="background-color: #007229; color: #ffffff">10C</div>
		 *       </div>
		 *       <div class="data">
		 *         <div class="wind">
		 *           <span class="glyph winddir W"></span>7 km/h
		 *         </div>
		 *         <div class="tab_precip">
		 *           <span class="glyph rain"></span>2-5mm
		 *         </div>
		 *         <div title="Sonnenstunden" class="tab_sun">
		 *           <span class="glyph sun"></span>6 h
		 *         </div>
		 *       </div>
		 *       <div class="tab_predictability" title="Treffsicherheit: Sehr hoch">
		 *         <span class="glyph predictability"></span>
		 *         <div class="meter_outer">
		 *           <div class="meter_inner" style="width: 100%; background-color: #4eb400"></div>
		 *         </div>
		 *       </div>
		 *       <div style="clear: both"></div>
		 *     </div>
		 *   </a>
		 * </div>
		 */
		// @formatter:on

		Harbor h = staticDataProxy.getHarbor(harborId);
		String url = WEATHER_BASE_URL + h.name.toLowerCase();
		Document doc = Jsoup.connect(url).get();

		List<WeatherInfo> infos = new ArrayList<WeatherInfo>();

		for (int seqNr = 1; seqNr <= 7; seqNr++) {
			WeatherInfo info = new WeatherInfo();
			Element dayDiv = doc.getElementById("day" + seqNr);
			info.date = dayDiv.getElementsByClass("tab_day_long").first().text();
			info.seqNr = seqNr;
			// weather
			Element div = dayDiv.getElementsByClass("weather").first().getElementsByClass("picon").first();
			for (String c : div.classNames()) {
				if (c.endsWith("iday")) {
					info.weather = c;
				}
			}
			// temperature
			info.temperature = dayDiv.getElementsByClass("tab_temp_min").first().text() + " - " + dayDiv.getElementsByClass("tab_temp_max").first().text();
			// wind direction
			div = dayDiv.getElementsByClass("winddir").first();
			for (String c : div.classNames()) {
				if (!c.equals("glyph") && !c.equals("winddir")) {
					info.windDirection = c;
				}
			}
			// wind speed
			div = dayDiv.getElementsByClass("wind").first();
			info.windSpeed = div.text();
			// precipitation
			info.precipitation = dayDiv.getElementsByClass("tab_precip").first().text();
			// sun hours
			info.sunHours = dayDiv.getElementsByClass("tab_sun").first().text();
			infos.add(info);
		}

		return infos;

	}

	@Override
	public List<WeatherInfo> getWeatherInfo(int harborId, boolean isDet) {
		try {
			return getWeatherInfo(staticDataProxy, harborId, isDet);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
