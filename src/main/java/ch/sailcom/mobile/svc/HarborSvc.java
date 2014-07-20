package ch.sailcom.mobile.svc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.sailcom.mobile.Harbor;
import ch.sailcom.mobile.server.ServerSession;
import ch.sailcom.mobile.server.impl.NoSessionException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class HarborSvc
 */
@WebServlet("/harbors/*")
public class HarborSvc extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Gson gs = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HarborSvc() {
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

			if (request.getPathInfo() == null) {
				PrintWriter writer = response.getWriter();
				writer.println(gs.toJson(serverSession.getHarbors()));
				writer.flush();
				writer.close();
			} else if (request.getPathInfo().equals("/my")) {
				PrintWriter writer = response.getWriter();
				writer.println(gs.toJson(serverSession.getMyHarbors()));
				writer.flush();
				writer.close();
			} else if (SvcUtil.isId(request.getPathInfo().substring(1))) {
				int harborId = Integer.parseInt(request.getPathInfo().substring(1));
				Harbor h = serverSession.getHarbor(harborId);
				if (h != null) {
					PrintWriter writer = response.getWriter();
					writer.println(gs.toJson(h));
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
