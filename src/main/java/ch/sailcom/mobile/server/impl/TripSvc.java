package ch.sailcom.mobile.server.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ch.sailcom.mobile.Ship;
import ch.sailcom.mobile.Trip;
import ch.sailcom.mobile.server.ServerSession;

public class TripSvc {

	private static final String TRIPS_BASE_URL = "https://www.sailcomnet.ch/net/res_edit.php?lng=de";
	private static final DateFormat tf = new SimpleDateFormat("dd.MM.yy HH:mm"); 
	private static final DateFormat jtf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); 

	public static List<Trip> getTrips(ServerSession session) throws IOException, ParseException {

		/*
		 * https://www.sailcomnet.ch/net/res_edit.php?lng=de
		 *
		 * <div id="Hauptteil">
		 *   <p style='text-align: center'><span class='titel'>Reservation ändern / annullieren</span></p>
		 *   <form method='post' action='/net/res_edit.php' name='form1'>
		 *   <table cellpadding="3" cellspacing="0" style="border-collapse: collapse;" width="100%">
		 *     <tr>
		 *       <th class="norm">von<br>bis</th>
		 *       <th class="norm">Boot</th>
		 *       <th class="norm" align='right'>Betrag</th>
		 *       <th class="norm">Bemerkungen</th>
		 *       <th class='norm'>&nbsp;</th>
		 *     </tr>
		 *     <tr onmouseover='style.backgroundColor="#DEEFFF"'; onmouseout='style.backgroundColor=""'; onclick="window.location.href='/net/res_edit.php?nid=84957';">
		 *     0 <td class='norm1' width='80' style='text-align: left;' valign='top'>
		 *         Sa.&nbsp;06.09.14<br>
		 *         14:00&nbsp;h bis<br>
		 *         Sa.&nbsp;06.09.14<br>
		 *         16:00&nbsp;h
		 *       </td>
		 *     1 <td class='norm' width='100' valign='top'>
		 *         <b>Albin Viggen</b>, Brunnen - Föhnhafen
		 *       </td>
		 *     2 <td class='norm' align='right' width='110' valign='top'>
		 *         90.00<br>
		 *         + 20 % Amtsträger-Beitrag<br>
		 *         - 20 % <br>
		 *         <b>= CHF&nbsp;90.00</b>
		 *       </td>
		 *     3 <td class='norm' valign='top'>
		 *       </td>
		 *     4 <td class='norm' style='text-align: center; background-color: #E4EBF3;'>
		 *         <a href='res_edit.php?nid=84957'><img src='/grafiken/edit.gif' border='0' title='bearbeiten'></a></a>
		 *       </td>
		 *     </tr>
		 *   </table>
		 * </div>
		 * 
		 */

		String url = TRIPS_BASE_URL;
		Document doc = Jsoup.connect(url).get();

		if (doc.select("input[name=txtMitgliedernummer]").first() != null) {
			throw new NoSessionException();
		}

		List<Trip> trips = new ArrayList<Trip>();
		Element div = doc.getElementById("Hauptteil");
		Element tab = div.getElementsByTag("tbody").first();

		// Loop trip rows
		Elements tripRows = tab.select("> tr");

		for (int t = 1; t < tripRows.size(); t++) {
			Element tripRow = tripRows.get(t);
			Elements tripCells = tripRow.select("> td");
			Element shipCell = tripCells.get(1);
			Element durCell = tripCells.get(0);
			Element idCell = tripCells.get(4);

			Trip trip = new Trip();
			String fullName = shipCell.text().replace(", ", "@");
			Ship ship = session.getShip(fullName);
			if (ship != null) {

				trip.tripId = idCell.getElementsByTag("a").first().attr("href");
				trip.tripId = trip.tripId.substring(trip.tripId.indexOf("?nid=") + 5);

				trip.shipId = ship.id;
				trip.harborId = ship.harborId;
				trip.lakeId = ship.lakeId;
				String dur[] = durCell.text().replace("\u00a0", " ").split(" ");

				trip.timeFrom = dur[2];
				trip.dateFrom = jtf.format(tf.parse(dur[1] + " " + trip.timeFrom));
				trip.timeTo = dur[7];
				trip.dateTo = jtf.format(tf.parse(dur[6] + " " + trip.timeTo));

				trips.add(trip);
			}

		}

		return trips;

	}

}
