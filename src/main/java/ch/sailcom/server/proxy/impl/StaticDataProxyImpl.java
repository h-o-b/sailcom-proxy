package ch.sailcom.server.proxy.impl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.sailcom.server.dto.Harbor;
import ch.sailcom.server.dto.Lake;
import ch.sailcom.server.dto.Ship;
import ch.sailcom.server.dto.StaticData;
import ch.sailcom.server.proxy.StaticDataProxy;

public class StaticDataProxyImpl implements StaticDataProxy {

	private static Logger LOGGER = LoggerFactory.getLogger(StaticDataProxyImpl.class);

	private static final String STATIC_DATA_URL = "http://www.sailcomnet.ch/liste.php";
	private static final String MAIN_DIV = "Hauptteil";

	private static final String MY_SHIPS_URL = "https://www.sailcomnet.ch/net/res_neu.php";

	private static StaticData staticData = null;

	private static StaticData getStaticData() throws IOException {

		LOGGER.info("getStaticData.1");
		Document doc = Jsoup.parse(new URL(STATIC_DATA_URL).openStream(), "ISO-8859-1", STATIC_DATA_URL);
		LOGGER.info("getStaticData.2");

		Element main = doc.select("div#" + MAIN_DIV).first();
		Element tab = main.select("table").first();
		Elements rows = tab.select("tr");

		StaticData sd = new StaticData();

		// @formatter:off
		/*
		 * <tr onclick="window.location.href='/boot.php?id=132';">
		 *   <td --0-- class='norm'> 
		 *     <a href='/boot.php?id=132'> <img src='/fotos/thumbs/132.jpg' width='95' height='55' border='0'> </a>
		 *   </td>
		 *   <td --1-- class='norm'>
		 *     <a href='/boot.php?id=132'>mOcean 'emotion'</a>
		 *     <br>
		 *     ZG 615
		 *     <br>
		 *     offenes Kielboot
		 *   </td>
		 *   <td --2-- class='norm'>
		 *     Ägerisee
		 *     <br>
		 *     <a href='hafen.php?id=46'>Oberägeri</a>
		 *     <br>
		 *     Oberägeri - Bojenfeld
		 *   </td>
		 *   <td --3-- class='norm'>
		 *     7&nbsp;Pers.
		 *     <br>
		 *     37 m²
		 *     <br>
		 *     L:&nbsp;7.97&nbsp;m
		 *   </td>
		 *   </td>
		 *   <td --4-- class='norm'>
		 *     205.-
		 *     <br>
		 *     245.- *
		 *   </td>
		 * </tr>
		 * 
		 */
		// @formatter:on

		// Skip Header Row
		for (int i = 1; i < rows.size(); i++) {

			Elements cells = rows.get(i).select("td");
			Element c = null;
			List<TextNode> tn = null;

			Ship ship = new Ship();
			Harbor harbor = null;
			Lake lake = null;

			c = cells.get(0);
			ship.hasImg = c.select("img").size() > 0;

			c = cells.get(1);
			ship.id = Integer.parseInt(c.select("a").first().attr("href").split("=")[1]);
			ship.name = c.select("a").first().text();
			String[] descnParts = c.html().split("<br>");
			if (descnParts.length == 3) {
				ship.plate = descnParts[1].trim();
				ship.type = descnParts[2].trim();
			} else {
				ship.plate = c.html().trim();
				ship.type = "";
			}

			c = cells.get(2);
			tn = c.textNodes();
			ship.harborId = Integer.parseInt(c.select("a").first().attr("href").split("=")[1]);

			/* Get/Add Harbor */
			harbor = sd.harborsById.get(ship.harborId);
			if (harbor != null) {
				lake = sd.lakesById.get(harbor.lakeId);
			} else {
				/* Get/Add Lake */
				String lakeName = tn.get(0).text();
				lake = sd.lakesByName.get(lakeName);
				if (lake == null) {
					lake = sd.addLake(lakeName);
				}
				harbor = sd.addHarbor(ship.harborId, c.select("a").first().text(), lake.id);
			}

			ship.lakeId = lake.id;
			ship.harborId = harbor.id;
			ship.location = tn.size() > 1 ? tn.get(1).getWholeText() : "";

			c = cells.get(3);
			tn = c.textNodes();
			String pax = tn.get(0).getWholeText();
			ship.pax = Integer.parseInt(pax.substring(0, pax.length() - 6));
			ship.sailSize = tn.get(1).getWholeText().trim();
			ship.length = tn.get(2).getWholeText().substring(3).trim();

			sd.addShip(ship);
		}
		LOGGER.info("getStaticData.3");

		return sd;

	}

	private static List<Integer> getAvailableShips() throws IOException {

		List<Integer> ships = new ArrayList<Integer>();

		// @formatter:off
		/*
		 * https://www.sailcomnet.ch/net/res_neu.php
		 * 
		 * <...>
		 *   <form method="post" action="/net/res_neu.php" name="form1">
		 *     <table cellpadding="3" cellspacing="0" style="border-collapse: collapse;" >
		 *       <tr>
		 *         <td colspan="2">Bitte beachte: Auf der untenstehenden Liste erscheinen nur diejenigen Boote, die Du eingesegelt hast und welche zur Zeit zur Verfügung stehen (nicht ausgewassert).</td>
		 *       </tr>
		 *       <tr>
		 *         <td><b>Boot:</b></td>
		 *         <td>
		 *           <select size="1" name="boot" onChange='searchSuggest();'>
		 *             <option value='112' selected>Vierwaldstättersee, Brunnen - Föhnhafen, Albin Viggen</option>
		 *           </select>
		 *         </td>
		 *       </tr>
		 */
		// @formatter:on

		LOGGER.info("getAvailableShips.1");
		String url = MY_SHIPS_URL;
		Document doc = Jsoup.connect(url).get();
		LOGGER.info("getAvailableShips.2");

		if (doc.select("input[name=txtMitgliedernummer]").first() != null) {
			throw new NoSessionException();
		}

		// Loop ship options
		Element select = doc.select("select[name=boot]").first();
		Elements shipOptions = select.select("option");
		for (int s = 0; s < shipOptions.size(); s++) {
			Element ship = shipOptions.get(s);
			int shipId = Integer.parseInt(ship.attr("value"));
			ships.add(shipId);
		}

		return ships;

	}

	private static synchronized void loadStaticData() {

		if (staticData != null) {
			return;
		}

		LOGGER.info("Loading static data ...");
		try {
			staticData = getStaticData();
			LOGGER.info("Loading static data 1");
			List<Integer> myShips = getAvailableShips();
			LOGGER.info("Loading static data 2");
			for (Integer shipId : myShips) {
				Ship ship = staticData.shipsById.get(shipId);
				staticData.myShips.add(ship);
				ship.isAvailable = true;
				Harbor harbor = staticData.harborsById.get(ship.harborId);
				if (!staticData.myHarbors.contains(harbor)) {
					staticData.myHarbors.add(harbor);
					harbor.isAvailable = true;
					Lake lake = staticData.lakesById.get(harbor.lakeId);
					if (!staticData.myLakes.contains(lake)) {
						staticData.myLakes.add(lake);
						lake.isAvailable = true;
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		LOGGER.info("Loading static data done");

	}

	@Override
	public List<Lake> getLakes() {
		loadStaticData();
		return staticData.lakes;
	}

	@Override
	public List<Lake> getMyLakes() {
		loadStaticData();
		return staticData.myLakes;
	}

	@Override
	public Lake getLake(int lakeId) {
		loadStaticData();
		return staticData.lakesById.get(lakeId);
	}

	@Override
	public List<Harbor> getHarbors() {
		loadStaticData();
		return staticData.harbors;
	}

	@Override
	public List<Harbor> getMyHarbors() {
		loadStaticData();
		return staticData.myHarbors;
	}

	@Override
	public Harbor getHarbor(int harborId) {
		loadStaticData();
		return staticData.harborsById.get(harborId);
	}

	@Override
	public List<Ship> getShips() {
		loadStaticData();
		this.getMyShips();
		return staticData.ships;
	}

	@Override
	public List<Ship> getMyShips() {
		loadStaticData();
		return staticData.myShips;
	}

	@Override
	public Ship getShip(int shipId) {
		loadStaticData();
		return staticData.shipsById.get(shipId);
	}

	@Override
	public Ship getShip(String shipName) {
		loadStaticData();
		return staticData.shipsByName.get(shipName);
	}

}
