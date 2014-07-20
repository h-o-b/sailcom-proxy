package ch.sailcom.mobile.svc;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.sailcom.mobile.server.ServerSession;
import ch.sailcom.mobile.server.impl.NoSessionException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class HarborSvc
 */
@WebServlet("/bookings/*")
public class BookSvc extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Gson gs = null;
	private DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BookSvc() {
		super();
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		gs = new GsonBuilder().setPrettyPrinting().create();;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {

		HttpSession clientSession = request.getSession(true);
		ServerSession serverSession = SvcUtil.getServerSession(clientSession);

		try {

			if (!serverSession.isLoggedIn()) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
			if (!serverSession.isConnected()) {
				serverSession.connect();
			}

			/* Validate Service Input */
			if (request.getParameter("shipId") == null) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}

			int shipId = Integer.parseInt(request.getParameter("shipId"));
			if (serverSession.getShip(shipId) == null) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			int nofWeeks = request.getParameter("nofWeeks") == null ? 1 : Integer.parseInt(request.getParameter("nofWeeks"));
			Date currDate = null;
			if (request.getParameter("date") != null) {
				try {
					currDate = df.parse(request.getParameter("date"));
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

			response.setContentType("application/json");

			PrintWriter writer = response.getWriter();
			writer.println(gs.toJson(serverSession.getBookings(shipId, currDate, nofWeeks)));
			writer.flush();
			writer.close();

		} catch (NoSessionException e) {

			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	
		} catch (IOException e) {

			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	
		}

	}

}
