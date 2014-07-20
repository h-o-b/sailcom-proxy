package ch.sailcom.mobile.server.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import ch.sailcom.mobile.Harbor;
import ch.sailcom.mobile.Lake;
import ch.sailcom.mobile.Ship;

public class StaticDataSvc {

	private static final String STATIC_DATA_URL = "http://www.sailcomnet.ch/liste.php";
	private static final String MAIN_DIV = "Hauptteil";

	public static StaticData getStaticData() throws IOException {

		Document doc = Jsoup.connect(STATIC_DATA_URL).get();

		Element main = doc.select("div#" + MAIN_DIV).first();
		Element tab = main.select("table").first();
		Elements rows = tab.select("tr");

		StaticData sd = new StaticData();
		Map<String, Lake> lakeByName = new HashMap<String, Lake>();

		/*
		 * <tr onclick="window.location.href='/boot.php?id=132';">
		 *   <td --0-- class='norm'>
		 *     <a href='/boot.php?id=132'>
		 *       <img src='/fotos/thumbs/132.jpg' width='95' height='55' border='0'>
		 *     </a>
		 *   </td>
		 *   <td --1-- class='norm'>
		 *     <a href='/boot.php?id=132'>mOcean 'emotion'</a><br>
		 *     ZG 615<br>
		 *     offenes Kielboot
		 *   </td>
		 *   <td --2-- class='norm'>
		 *     Ägerisee<br>
		 *     <a href='hafen.php?id=46'>Oberägeri</a><br>
		 *     Oberägeri - Bojenfeld
		 *   </td>
		 *   <td --3-- class='norm'>
		 *     7&nbsp;Pers.<br>
		 *     37 m²<br>
		 *     L:&nbsp;7.97&nbsp;m
		 *   </td>
		 *   </td>
		 *   <td --4-- class='norm'>
		 *     205.-<br>
		 *     245.-
		 *     *
		 *   </td>
		 * </tr>
		 * 
		 */

		for (int i = 1; i < rows.size(); i++) {

			Elements cells = rows.get(i).select("td");
			Element c = null;
			List<TextNode> tn = null;

			Ship s = new Ship();
			Harbor h = null;
			Lake l = null;

			c = cells.get(0);
			s.hasImg = c.select("img").size() > 0;

			c = cells.get(1);
			s.id = Integer.parseInt(c.select("a").first().attr("href").split("=")[1]);
			s.name = c.select("a").first().html();
			tn = c.textNodes();
			s.nrPlate = tn.get(0).getWholeText();
			s.type = tn.get(1).getWholeText();

			c = cells.get(2);
			tn = c.textNodes();
			s.harborId = Integer.parseInt(c.select("a").first().attr("href").split("=")[1]);

			/* Get/Init Harbor */
			h = sd.harborsById.get(s.harborId);
			if (h != null) {
				l = sd.lakesById.get(h.lakeId);
			} else {
				/* Get/Init Lake */
				String lakeName = tn.get(0).getWholeText();
				l = lakeByName.get(lakeName);
				if (l == null) {
					l = new Lake();
					l.id = sd.lakes.size() + 1;
					l.name = lakeName;
					sd.lakes.add(l);
					sd.lakesById.put(l.id, l);
					lakeByName.put(l.name, l);
				}
				/* Init Harbor */
				h = new Harbor();
				h.id = s.harborId;
				h.name = c.select("a").first().html();
				h.lakeId = l.id;
				sd.harborsById.put(h.id, h);
				sd.harbors.add(h);
			}

			s.lakeId = l.id;
			s.harborId = h.id;
			s.location = tn.size() > 1 ? tn.get(1).getWholeText() : "";

			c = cells.get(3);
			tn = c.textNodes();
			String pax = tn.get(0).getWholeText();
			s.pax = Integer.parseInt(pax.substring(0, pax.length() - 6));
			s.sailSize = tn.get(1).getWholeText();
			s.length = tn.get(2).getWholeText().substring(3);

			sd.ships.add(s);
			sd.shipsById.put(s.id, s);
		}

		return sd;

	}

}
