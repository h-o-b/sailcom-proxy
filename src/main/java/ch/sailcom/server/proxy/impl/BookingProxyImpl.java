package ch.sailcom.server.proxy.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ch.sailcom.server.dto.Booking;
import ch.sailcom.server.dto.Ship;
import ch.sailcom.server.dto.Trip;
import ch.sailcom.server.proxy.BookingProxy;
import ch.sailcom.server.proxy.StaticDataProxy;

public class BookingProxyImpl implements BookingProxy {

	private static final String TRIPS_BASE_URL = "https://www.sailcomnet.ch/net/res_edit.php?lng=de";
	private static final DateFormat tf = new SimpleDateFormat("dd.MM.yy HH:mm");
	private static final DateFormat jtf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	private static final String BOOKING_BASE_URL = "https://www.sailcomnet.ch/net/plan.php?lng=de";
	private static final DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
	private static final DateFormat jdf = new SimpleDateFormat("yyyy-MM-dd");

	private static final StaticDataProxy staticDataProxy = new StaticDataProxyImpl();

	private static List<Trip> getTrips(StaticDataProxy staticDataProxy) throws IOException, ParseException {

		// @formatter:off
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
		// @formatter:on

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
			Ship ship = staticDataProxy.getShip(fullName);
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

	public static List<Booking> getBookings(StaticDataProxy staticDataProxy, int shipId, Date fromDate, int nofWeeks) throws IOException, ParseException {

		// @formatter:off
		/*
		 * https://www.sailcomnet.ch/net/plan.php?lng=de&vonPlan=01.06.2014&boot=161&wochen=10
		 * 
		 * <table style="border-collapse: collapse;" border="0" cellpadding="2" cellspacing="0">
		 *
		 *   <tbody>
		 *
		 *     <!-- SKIP -->
		 *     <tr>
		 *       <td colspan="8" align="center"><a href="/boot.php?id=161" target="_blank">Detailseite des Bootes</a></td>
		 *     </tr>
		 *     <!-- SKIP -->
		 *     <tr>
		 *       <td class="norm1" align='center' style="background-color: #CBDEEF; font-size: 0.6em"><a href='#' onClick="searchSuggest('12.05.2014')"><b>Woche<br>...21</b></a></td>
		 *       <th class="norm" width="50">Mo</th>
		 *       <th class="norm" width="50">Di</th>
		 *       <th class="norm" width="50">Mi</th>
		 *       <th class="norm" width="50">Do</th>
		 *       <th class="norm" width="50">Fr</th>
		 *       <th class="norm" width="50" style="background-color: #7BAAD5;">Sa</th>
		 *       <th class="norm" width="50" style="background-color: #7BAAD5;">So</th>
		 *     </tr>
		 *
		 *     <tr>
		 *
		 *       <!-- WEEK HEADER -->
		 *       <td class='norm' style='text-align: center; background-color: #EEEEEE; vertical-align=middle;'>Woche<br>23</td>
		 *
		 *       <!-- PER DAY -->
		 *       <td class='norm1' style='vertical-align: top;background-color: #DEEFFF' onClick='document.form1.von.value="07.06.2014";document.form1.bis.value="07.06.2014";' onMouseOver=''>
		 *         <span style='font-size: 0.9em; color: #000000'>07.06.</span><br>
		 *
		 *         <!-- PER BOOKING -->
		 *         <a class='resdetail' href='#' onmouseover='this.style.cursor="pointer" ' onfocus='this.blur();' onclick="document.getElementById('83049').style.display = 'block' " >
		 *           <div id='Res'  style='background-color: #FFC0C0'>10:00-<br>24:00</div>
		 *         </a>
		 *         <div class='res' id='83049' style='display: none; position: absolute; left: 50px; top: 50px;'>
		 *           <div style='text-align: right' class='fenster'>
		 *             <a onmouseover='this.style.cursor="pointer" ' onfocus='this.blur();' onclick="document.getElementById('83049').style.display = 'none' ">Fenster schliessen</a>
		 *           </div>
		 *           <table cellspacing='0' cellpadding='5' border='0'>
		 *             <tr>
		 *               <td colspan='2' align='center'>Sa.&nbsp;07.06.2014 10:00&nbsp;h&nbsp;-&nbsp;So.&nbsp;08.06.2014 14:00&nbsp;h<br>Reserviert am ...</td>
		 *             </tr>
		 *             <tr>
		 *               <td valign='bottom'><img src='../fotos/mitglieder/11719.jpg' | src='/fotos/mitglieder/leer.jpg' border='1'></td>
		 *               <td valign='top'>
		 *                 <b>Thomas Wanner</b><br>
		 *                 Heidenstrasse 18<br>
		 *                 4142 Münchenstein<br>
		 *                 Tel. P: 061  312 29 59<br>
		 *                 Mobil: 079 747 84 45<br>
		 *                 <a href='mailto:th.wanner@bluewin.ch'>th.wanner@bluewin.ch</a>
		 *               </td>
		 *             </tr>
		 *           </table>
		 *         </div>
		 *
		 *       </td>
		 *
		 *     </tr>
		 *
		 *   </tbody>
		 *
		 * </table>
		 */
		// @formatter:on

		String url = BOOKING_BASE_URL + "&boot=" + shipId + "&vonPlan=" + df.format(fromDate) + "&wochen=" + nofWeeks;
		Document doc = Jsoup.connect(url).get();

		if (doc.select("input[name=txtMitgliedernummer]").first() != null) {
			throw new NoSessionException();
		}

		List<Booking> bookings = new ArrayList<Booking>();
		Element tab = doc.getElementsByTag("tbody").first();

		// Loop week rows
		Elements weeks = tab.select("> tr");
		for (int w = 2; w < weeks.size() - 1; w++) {

			// Loop day cells
			Element week = weeks.get(w);
			Elements days = week.select("> td");
			for (int d = 1; d < days.size(); d++) {

				Element day = days.get(d);

				// Date
				@SuppressWarnings("deprecation")
				String bookDate = day.select("span").first().text() + (1900 + fromDate.getYear());

				// Loop bookings
				Elements booksA = day.select("a.resdetail");
				Elements booksDiv = day.select("div.res");
				for (int b = 0; b < booksDiv.size(); b++) {

					Element a = booksA.get(b);
					Element book = booksDiv.get(b);
					int tripId = Integer.parseInt(book.id());

					Booking booking = new Booking();

					booking.shipId = shipId;
					booking.harborId = staticDataProxy.getShip(booking.shipId).harborId;
					booking.lakeId = staticDataProxy.getHarbor(booking.harborId).lakeId;

					booking.bookDate = jdf.format(df.parse(bookDate));
					Element bookTime = a.select("div#Res").first();
					booking.bookTimeFrom = bookTime.text().split("-")[0].trim();
					booking.bookTimeTo = bookTime.text().split("-")[1].trim();

					booking.tripId = tripId;
					booking.isMine = bookTime.attr("style").contains("#80FF80");

					String tripInfo = book.select("table").first().select("tr").get(0).select("td").get(0).text();
					Pattern p = Pattern.compile("[0-9][0-9]\\.[0-9][0-9]\\.[0-9][0-9][0-9][0-9] [0-9][0-9]:[0-9][0-9]");
					Matcher m = p.matcher(tripInfo);
					if (m.find()) { // from date
						booking.tripDateFrom = jdf.format(df.parse(m.group().split(" ")[0]));
						booking.tripTimeFrom = m.group().split(" ")[1];
					}
					if (m.find()) { // to date
						booking.tripDateTo = jdf.format(df.parse(m.group().split(" ")[0]));
						booking.tripTimeTo = m.group().split(" ")[1];
					}

					Element userInfo = book.select("table").first().select("tr").get(1).select("td").get(1);
					booking.userName = userInfo.select("b").first().text();
					String[] parts = userInfo.html().replace("<b>", "").replace("</b>", "").split("<br />"); // Jsoup transforms <br> to <br />
					boolean isComment = false;
					for (int i = 1; i < parts.length; i++) {
						if (parts[i].equals("")) {
							// ignore
						} else if (parts[i].startsWith("Tel")) {
							booking.userPhone = parts[i].substring(parts[i].indexOf(":") + 2);
						} else if (parts[i].startsWith("Mobil")) {
							booking.userMobile = parts[i].substring(parts[i].indexOf(":") + 2);
						} else if (parts[i].startsWith("<a")) {
							booking.userMail = userInfo.select("a").first().text();
							isComment = true;
						} else if (isComment) {
							booking.userComment = booking.userComment == null ? parts[i] : booking.userComment + "\\n" + parts[i];
						} else {
							booking.userAdress = booking.userAdress == null ? parts[i] : booking.userAdress + "\\n" + parts[i];
						}
					}

					bookings.add(booking);

				}

			}

		}

		return bookings;

	}

	@Override
	public List<Trip> getTrips() {
		try {
			return getTrips(staticDataProxy);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Booking> getBookings(int shipId, Date fromDate, int nofWeeks) {
		try {
			return getBookings(staticDataProxy, shipId, fromDate, nofWeeks);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}