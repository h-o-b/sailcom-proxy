package ch.sailcom.server.proxy.impl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.sailcom.server.model.Harbor;
import ch.sailcom.server.model.Lake;
import ch.sailcom.server.model.Ship;
import ch.sailcom.server.proxy.StaticDataProxy;

@ApplicationScoped
public class StaticDataProxyImpl implements StaticDataProxy {

	private static Logger LOGGER = LoggerFactory.getLogger(StaticDataProxyImpl.class);

	private static final String STATIC_DATA_URL = "http://www.sailcomnet.ch/liste.php";
	private static final String MAIN_DIV = "Hauptteil";

	private final Map<Integer, Lake> lakesById = new HashMap<Integer, Lake>();
	private final Map<Integer, Harbor> harborsById = new HashMap<Integer, Harbor>();
	private final Map<Integer, Ship> shipsById = new HashMap<Integer, Ship>();

	public StaticDataProxyImpl() {
		LOGGER.debug("StaticDataProxyImpl()");
	}

	@PostConstruct
	void loadStaticData() {

		if (shipsById.size() > 0) {
			return;
		}

		Map<String, Lake> lakesByName = new HashMap<String, Lake>();

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

		// Skip Header Row
		for (int i = 1; i < rows.size(); i++) {

			Elements cells = rows.get(i).select("td");
			Element c = null;
			List<TextNode> tn = null;

			Lake lake = null;
			Harbor harbor = null;
			Ship ship = new Ship();

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
			harbor = harborsById.get(ship.harborId);
			if (harbor != null) {
				lake = lakesById.get(harbor.lakeId);
			} else {
				/* Get/Add Lake */
				String lakeName = tn.get(0).text();
				lake = lakesByName.get(lakeName);
				if (lake == null) {
					lake = new Lake();
					lake.id = lakesById.size() + 1;
					lake.name = lakeName;
					lakesById.put(lake.id, lake);
					lakesByName.put(lake.name, lake);
				}
				/* Add Harbor */
				String harborName = c.select("a").first().text();
				harbor = new Harbor();
				harbor.id = ship.harborId;
				harbor.name = harborName;
				harbor.lakeId = lake.id;
				harborsById.put(harbor.id, harbor);
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

			shipsById.put(ship.id, ship);

		}

		LOGGER.debug("loadStaticData.3");

	}

	@Override
	public List<Lake> getLakes() {
		return new ArrayList<Lake>(lakesById.values());
	}

	@Override
	public List<Harbor> getHarbors() {
		return new ArrayList<Harbor>(harborsById.values());
	}

	@Override
	public List<Ship> getShips() {
		return new ArrayList<Ship>(shipsById.values());
	}

}
