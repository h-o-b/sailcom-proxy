package ch.sailcom.mobile.svc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LakeSvc
 */
@WebServlet("/http/*")
public class HttpSvc extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");

		PrintWriter writer = response.getWriter();

		writer.println("<html>");
		writer.println("<head><title>HTTP Test Page</title></head>");
		writer.println("<body>");

		writer.println("<h1>HTTP Request Parameters</h1>");
		writer.println("<ul>");
		writer.println("<li>encoding: " + request.getCharacterEncoding() + "</li>");
		writer.println("<li>contentLength: " + request.getContentLength() + "</li>");
		writer.println("<li>contentType: " + request.getContentType() + "</li>");
		writer.println("<li>protocol: " + request.getProtocol() + "</li>");
		writer.println("<li>addr: " + request.getLocalAddr() + "</li>");
		writer.println("<li>name: " + request.getLocalName() + "</li>");
		writer.println("<li>port: " + request.getLocalPort() + "</li>");
		writer.println("<li>contextPath: " + request.getContextPath() + "</li>");
		writer.println("<li>method: " + request.getMethod() + "</li>");
		writer.println("<li>pathInfo: " + request.getPathInfo() + "</li>");
		writer.println("<li>pathTranslated: " + request.getPathTranslated() + "</li>");
		writer.println("<li>queryString: " + request.getQueryString() + "</li>");
		writer.println("<li>remoteAddr: " + request.getRemoteAddr() + "</li>");
		writer.println("<li>remoteHost: " + request.getRemoteHost() + "</li>");
		writer.println("<li>remotePort: " + request.getRemotePort() + "</li>");
		writer.println("<li>remoteUser: " + request.getRemoteUser() + "</li>");
		writer.println("<li>requestedSessionId: " + request.getRequestedSessionId() + "</li>");
		writer.println("<li>requestUri: " + request.getRequestURI() + "</li>");
		writer.println("<li>requestUrl: " + request.getRequestURL().toString() + "</li>");
		writer.println("<li>scheme: " + request.getScheme() + "</li>");
		writer.println("<li>serverName: " + request.getServerName() + "</li>");
		writer.println("<li>serverPort: " + request.getServerPort() + "</li>");
		writer.println("<li>servletPath: " + request.getServletPath() + "</li>");
		writer.println("<li>session: " + request.getSession() + "</li>");
		Enumeration<String> attrs = request.getAttributeNames();
		while (attrs.hasMoreElements()) {
			String a = attrs.nextElement();
			writer.println("<li>attribute: " + a + " = " + request.getAttribute(a) + "</li>");
		}
		Enumeration<String> pars = request.getParameterNames();
		while (pars.hasMoreElements()) {
			String p = pars.nextElement();
			writer.println("<li>param: " + p + " = " + request.getParameter(p) + "</li>");
		}
		for (int i = 0; i < request.getCookies().length; i++) {
			writer.println("<li>cookie: " + request.getCookies()[i].getDomain() + ", " + request.getCookies()[i].getPath() + ", " + request.getCookies()[i].getName() + ", " + request.getCookies()[i].getValue() + ", " + "</li>");
		}
		writer.println("</ul>");

		writer.println("</body>");
		writer.println("</html>");

//		Cookie c = new Cookie("c" + request.getCookies().length, "custom cookie");
//		response.addCookie(c);

		writer.close();
	}

}
