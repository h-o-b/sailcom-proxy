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
		writer.println("    <td><a href='harbors'>Harbors</a>, <a href='harbors/my'>My Harbors</a>, <a href='harbors/14'>Single Harbor (La Neveville)</a></td>");
		writer.println("  </tr>");
		writer.println("  <tr>");
		writer.println("    <td>Ships</td>");
		writer.println("    <td><a href='ships'>Ships</a>, <a href='ships/my'>My Ships</a>, <a href='ships/147'>Single Ship</a></td>");
		writer.println("  </tr>");
		writer.println("</table>");

		writer.println("<h2>Trips</h2>");
		writer.println("<table>");
		writer.println("  <tr>");
		writer.println("    <td>Trips</td>");
		writer.println("    <td><a href='trips'>My Trips</a></td>");
		writer.println("  </tr>");
		writer.println("</table>");

		writer.println("<h2>Bookings</h2>");
		writer.println("<table>");
		writer.println("  <tr>");
		writer.println("    <td>Bookings</td>");
		writer.println("    <td><a href='bookings?shipId=112&date=01.09.2014&nofWeeks=2'>Bookings for September 2014 (Ship 112)</a></td>");
		writer.println("  </tr>");
		writer.println("  <tr>");
		writer.println("    <td>&nbsp;</td>");
		writer.println("    <td><a href='bookings?shipId=161'>Bookings around Today (Ship 161)</a></td>");
		writer.println("  </tr>");
		writer.println("  <tr>");
		writer.println("    <td>&nbsp;</td>");
		writer.println("    <td><a href='bookings?shipId=161&date=01.07.2014&nofWeeks=5'>Bookings for Juli 2014 (Ship 161)</a></td>");
		writer.println("  </tr>");
		writer.println("</table>");

		writer.println("<h2>Weather</h2>");
		writer.println("<table>");
		writer.println("  <tr>");
		writer.println("    <td>Weather overview</td>");
		writer.println("    <td><a href='weather/14'>La Neveville</a></td>");
		writer.println("  </tr>");
		writer.println("  <tr>");
		writer.println("    <td>Weather details</td>");
		writer.println("    <td><a href='weather/14?det=true'>La Neveville</a></td>");
		writer.println("  </tr>");
		writer.println("</table>");

		writer.println("</body>");
		writer.println("</html>");

//		Cookie c = new Cookie("c" + request.getCookies().length, "custom cookie");
//		response.addCookie(c);

		writer.close();

	}

}
