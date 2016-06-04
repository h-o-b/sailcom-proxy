package ch.sailcom.mobile.svc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.sailcom.mobile.WeatherInfo;
import ch.sailcom.mobile.server.ServerSession;
import ch.sailcom.mobile.server.impl.NoSessionException;

/**
 * Servlet implementation class WeatherSvc
 */
@WebServlet("/weather/*")
public class WeatherSvc extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {

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

			if (SvcUtil.isId(request.getPathInfo().substring(1))) {
				int harborId = Integer.parseInt(request.getPathInfo().substring(1));
				List<WeatherInfo> info = serverSession.getWeatherInfo(harborId, false);
				if (info != null) {
					PrintWriter writer = response.getWriter();
					writer.println(SvcUtil.toJson(info));
					writer.flush();
					writer.close();
				} else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				}
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}

		} catch (NoSessionException e) {

			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	
		} catch (IOException e) {

			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	
		}

	}

}
