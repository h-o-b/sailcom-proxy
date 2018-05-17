package ch.sailcom.test.api.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation
 */
@WebServlet("/test/http/*")
public class HttpSvc extends HttpServlet {

	private static final long serialVersionUID = -3244607004176369657L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("application/json");

		PrintWriter writer = response.getWriter();

		writer.println("{");

		writer.println(" \"addr\": \"" + request.getLocalAddr() + "\",");
		Enumeration<String> attrs = request.getAttributeNames();
		int i = 0;
		while (attrs.hasMoreElements()) {
			String a = attrs.nextElement();
			writer.println(" \"attr[" + i + "]\": \"" + a + " = " + request.getAttribute(a) + "\",");
			i += 1;
		}
		writer.println(" \"contentLength\": \"" + request.getContentLength() + "\",");
		writer.println(" \"contextPath\": \"" + request.getContextPath() + "\",");
		writer.println(" \"contentType\": \"" + request.getContentType() + "\",");
		for (i = 0; i < request.getCookies().length; i++) {
			writer.println(" \"cookie[" + i + "]\": \"domain: " + request.getCookies()[i].getDomain() + ", path: " + request.getCookies()[i].getPath() + ", name: " + request.getCookies()[i].getName()
					+ ", value: " + request.getCookies()[i].getValue() + "\",");
		}
		writer.println(" \"encoding\": \"" + request.getCharacterEncoding() + "\",");
		writer.println(" \"method\": \"" + request.getMethod() + "\",");
		writer.println(" \"name\": \"" + request.getLocalName() + "\",");
		Enumeration<String> pars = request.getParameterNames();
		i = 0;
		while (pars.hasMoreElements()) {
			String p = pars.nextElement();
			writer.println(" \"param[" + i + "]\": \"" + p + " = " + request.getParameter(p) + "\",");
			i += 1;
		}
		writer.println(" \"pathInfo\": \"" + request.getPathInfo() + "\",");
		writer.println(" \"pathTranslated\": \"" + request.getPathTranslated() + "\",");
		writer.println(" \"port\": \"" + request.getLocalPort() + "\",");
		writer.println(" \"protocol\": \"" + request.getProtocol() + "\",");
		writer.println(" \"queryString\": \"" + request.getQueryString() + "\",");
		writer.println(" \"remoteAddr\": \"" + request.getRemoteAddr() + "\",");
		writer.println(" \"remoteHost\": \"" + request.getRemoteHost() + "\",");
		writer.println(" \"remotePort\": \"" + request.getRemotePort() + "\",");
		writer.println(" \"remoteUser\": \"" + request.getRemoteUser() + "\",");
		writer.println(" \"requestedSessionId\": \"" + request.getRequestedSessionId() + "\",");
		writer.println(" \"requestUri\": \"" + request.getRequestURI() + "\",");
		writer.println(" \"requestUrl\": \"" + request.getRequestURL().toString() + "\",");
		writer.println(" \"scheme\": \"" + request.getScheme() + "\",");
		writer.println(" \"serverName\": \"" + request.getServerName() + "\",");
		writer.println(" \"serverPort\": \"" + request.getServerPort() + "\",");
		writer.println(" \"servletPath\": \"" + request.getServletPath() + "\",");
		writer.println(" \"session\": \"" + request.getSession() + "\"");

		writer.println("}");

		writer.close();
	}

	protected void doGetHtml(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
			writer.println("<li>cookie: " + request.getCookies()[i].getDomain() + ", " + request.getCookies()[i].getPath() + ", " + request.getCookies()[i].getName() + ", "
					+ request.getCookies()[i].getValue() + ", " + "</li>");
		}
		writer.println("</ul>");

		writer.println("</body>");
		writer.println("</html>");

		// Cookie c = new Cookie("c" + request.getCookies().length, "custom cookie");
		// response.addCookie(c);

		writer.close();
	}

}
