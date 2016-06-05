package ch.sailcom.mobile.svc;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ch.sailcom.mobile.Booking;
import ch.sailcom.mobile.server.ServerSession;

/**
 * Bookings Service
 */
@Path("/bookings")
public class BookSvc {

	private DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Booking> getBookings(
		@Context HttpServletRequest request,
		@Context HttpServletResponse response,
		@QueryParam("shipId") Integer shipId,
		@QueryParam("date") String date,
		@QueryParam("nofWeeks") Integer nofWeeks
	) throws IOException {

		HttpSession clientSession = request.getSession(true);
		ServerSession serverSession = SvcUtil.getServerSession(clientSession);

		if (!serverSession.isLoggedIn()) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		if (!serverSession.isConnected()) {
			serverSession.connect();
		}

		/* Validate Service Input */
		if (shipId == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		} else if (serverSession.getShip(shipId) == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}

		nofWeeks = nofWeeks == null ? 1 : nofWeeks;
		Date currDate = null;
		if (date != null && !date.equals("")) {
			try {
				currDate = df.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		} else {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			currDate = cal.getTime();
		}

		return serverSession.getBookings(shipId, currDate, nofWeeks);

	}

}
