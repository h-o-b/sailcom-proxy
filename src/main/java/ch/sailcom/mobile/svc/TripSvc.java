package ch.sailcom.mobile.svc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.sailcom.mobile.server.ServerSession;
import ch.sailcom.mobile.server.impl.NoSessionException;

/**
 * Servlet implementation class TripSvc
 */
@WebServlet("/trips")
public class TripSvc extends HttpServlet {

	private static final long serialVersionUID = 1L;

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

			response.setContentType("application/json");

			PrintWriter writer = response.getWriter();
			writer.println(SvcUtil.toJson(serverSession.getTrips()));
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
