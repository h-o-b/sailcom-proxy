package ch.sailcom.mobile.server.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ch.sailcom.mobile.Booking;
import ch.sailcom.mobile.server.ServerSession;

public class BookSvc {

	private static final String BOOKING_BASE_URL = "https://www.sailcomnet.ch/net/plan.php?lng=de";
	private static final DateFormat df = new SimpleDateFormat("dd.MM.yyyy"); 
	private static final DateFormat jdf = new SimpleDateFormat("yyyy-MM-dd"); 
	private static final DateFormat tf = new SimpleDateFormat("dd.MM.yyyy HH:mm"); 
	private static final DateFormat jtf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm"); 

	public static List<Booking> getBookings(ServerSession session, int shipId, Date fromDate, int nofWeeks) throws IOException, ParseException {

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
		 *           <div id='Res'  style='background-color: #FFC0C0'>10:00-<br>14:00</div>
		 *         </a>
		 *         <div class='res' id='83049' style='display: none; position: absolute; left: 50px; top: 50px;'>
		 *           <div style='text-align: right' class='fenster'>
		 *             <a onmouseover='this.style.cursor="pointer" ' onfocus='this.blur();' onclick="document.getElementById('83049').style.display = 'none' ">Fenster schliessen</a>
		 *           </div>
		 *           <table cellspacing='0' cellpadding='5' border='0'>
		 *             <tr>
		 *               <td colspan='2' align='center'>Sa.&nbsp;07.06.2014 10:00&nbsp;h&nbsp;-&nbsp;Sa.&nbsp;07.06.2014 14:00&nbsp;h</td>
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
					int bookId = Integer.parseInt(book.id());

					Booking booking = new Booking();

					booking.shipId = shipId;
					booking.harborId = session.getShip(booking.shipId).harborId;
					booking.lakeId = session.getHarbor(booking.harborId).lakeId;
					booking.date = jdf.format(df.parse(bookDate));

					booking.bookId = bookId;
					Element bookTime = a.select("div#Res").first();

					booking.timeFrom = bookTime.text().split("-")[0];
					booking.dateFrom = jtf.format(tf.parse(bookDate + " " + booking.timeFrom));
					booking.timeTo = bookTime.text().split("-")[1];
					booking.dateTo = jtf.format(tf.parse(bookDate + " " + booking.timeTo));

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

}
