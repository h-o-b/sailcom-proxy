package ch.sailcom.mobile.svc;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ch.sailcom.mobile.server.ServerSession;
import ch.sailcom.mobile.server.impl.NoSessionException;

/**
 * Servlet implementation class LoginSvc
 */
@WebServlet("/login")
public class LoginSvc extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String USER_NAME_PAR = "user";
	private static final String PWD_PAR = "pwd";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginSvc() {
		super();
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		/* Validate Service Input */
		String user = request.getParameter(USER_NAME_PAR);
		String pwd = request.getParameter(PWD_PAR);

		if (user == null || user.equals("")) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		} else if (pwd == null || pwd.equals("")) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		/* Login */
		HttpSession clientSession = request.getSession(true);
		ServerSession serverSession = SvcUtil.getServerSession(clientSession);

		try {

			if (!serverSession.isConnected()) {
				serverSession.connect();
			}

			serverSession.login(user, pwd);

			if (!serverSession.isLoggedIn()) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}

		} catch (NoSessionException e) {

			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	
		}
	}

}
