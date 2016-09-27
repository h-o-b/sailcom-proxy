package ch.sailcom.server.proxy.impl;

import java.io.IOException;
import java.net.URL;
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

	private static StaticData staticData = null;

	private static void loadStaticData() {

		if (staticData != null) {
			return;
		}

		LOGGER.debug("loadStaticData.1");
		Document doc;
		try {
			doc = Jsoup.parse(new URL(STATIC_DATA_URL).openStream(), "ISO-8859-1", STATIC_DATA_URL);
		} catch (IOException e) {
			LOGGER.error("Static data url crashed", e);
			throw new RuntimeException("Static data url crashed", e);
		}
		LOGGER.debug("loadStaticData.2");

		Element main = doc.select("div#" + MAIN_DIV).first();
		Element tab = main.select("table").first();
		Elements rows = tab.select("tr");

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

		staticData = new StaticData();

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
			harbor = staticData.getHarbor(ship.harborId);
			if (harbor != null) {
				lake = staticData.getLake(harbor.lakeId);
			} else {
				/* Get/Add Lake */
				String lakeName = tn.get(0).text();
				lake = staticData.getLake(lakeName);
				if (lake == null) {
					lake = staticData.addLake(lakeName);
				}
				harbor = staticData.addHarbor(ship.harborId, c.select("a").first().text(), lake.id);
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

			staticData.addShip(ship);
		}
		LOGGER.debug("loadStaticData.3");

	}

	@Override
	public StaticData getStaticData() {
		loadStaticData();
		return staticData;
	}

	@Override
	public List<Lake> getLakes() {
		loadStaticData();
		return staticData.lakes;
	}

	@Override
	public Lake getLake(int lakeId) {
		loadStaticData();
		return staticData.getLake(lakeId);
	}

	@Override
	public List<Harbor> getHarbors() {
		loadStaticData();
		return staticData.harbors;
	}

	@Override
	public Harbor getHarbor(int harborId) {
		loadStaticData();
		return staticData.getHarbor(harborId);
	}

	@Override
	public List<Ship> getShips() {
		loadStaticData();
		return staticData.ships;
	}

	@Override
	public Ship getShip(int shipId) {
		loadStaticData();
		return staticData.getShip(shipId);
	}

}
