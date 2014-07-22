package ch.sailcom.mobile.svc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LakeSvc
 */
@WebServlet("/test/*")
public class TestSvc extends HttpServlet {

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

		writer.println("<h1>SailCom Test Links</h1>");

		writer.println("<h2>Session Handling</h2>");
		writer.println("<table>");
		writer.println("  <tr>");
		writer.println("    <td>General</td>");
		writer.println("    <td><a href='somewhere'>404</a></td>");
		writer.println("  </tr>");
		writer.println("  <tr>");
		writer.println("    <td>Login</td>");
		writer.println("    <td><a href='login?user=82219&pwd=segeln'>Login (correct)</a>, <a href='login?user=82219&pwd=segel'>Login (with error)</a></td>");
		writer.println("  </tr>");
		writer.println("  <tr>");
		writer.println("    <td>Logout</td>");
		writer.println("    <td><a href='logout'>Logout</a></td>");
		writer.println("  </tr>");
		writer.println("  <tr>");
		writer.println("    <td></td>");
		writer.println("    <td></td>");
		writer.println("  </tr>");
		writer.println("</table>");

		
		writer.println("<h2>Static Data</h2>");
		writer.println("<table>");
		writer.println("  <tr>");
		writer.println("    <td>Lakes</td>");
		writer.println("    <td><a href='lakes'>All Lakes</a>, <a href='lakes/my'>My Lakes</a>, <a href='lakes/1'>Single Lake</a></td>");
		writer.println("  </tr>");
		writer.println("  <tr>");
		writer.println("    <td>Harbors</td>");
		writer.println("    <td><a href='harbors'>Harbors</a>, <a href='harbors/my'>My Harbors</a>, <a href='harbors/14'>Single Harbor</a></td>");
		writer.println("  </tr>");
		writer.println("  <tr>");
		writer.println("    <td>Ships</td>");
		writer.println("    <td><a href='ships'>Ships</a>, <a href='ships/my'>My Ships</a>, <a href='ships/147'>Single Ship</a></td>");
		writer.println("  </tr>");
		writer.println("  <tr>");
		writer.println("    <td></td>");
		writer.println("    <td></td>");
		writer.println("  </tr>");
		writer.println("</table>");

		writer.println("<h2>Bookings</h2>");
		writer.println("<table>");
		writer.println("  <tr>");
		writer.println("    <td>Bookings</td>");
		writer.println("    <td><a href='bookings?shipId=161'>Bookings for Today (Ship 161)</a>, <a href='bookings?shipId=161&date=01.07.2014&nofWeeks=5'>Bookings for Juli 14 (Ship 161)</a></td>");
		writer.println("  </tr>");
		writer.println("</table>");

		writer.println("</body>");
		writer.println("</html>");

//		Cookie c = new Cookie("c" + request.getCookies().length, "custom cookie");
//		response.addCookie(c);

		writer.close();
	}

}
