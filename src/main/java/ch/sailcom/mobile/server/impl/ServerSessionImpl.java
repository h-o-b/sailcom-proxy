package ch.sailcom.mobile.server.impl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import ch.sailcom.mobile.Booking;
import ch.sailcom.mobile.Lake;
import ch.sailcom.mobile.Harbor;
import ch.sailcom.mobile.Ship;
import ch.sailcom.mobile.Trip;
import ch.sailcom.mobile.User;
import ch.sailcom.mobile.WeatherInfo;
import ch.sailcom.mobile.server.ServerSession;

public class ServerSessionImpl implements ServerSession {

	/* ClientSession Attributes */
	private static final String SESSION_COOKIE_ATTR = "sessionCookie";
	private static final String IS_AUTHENTICATED_ATTR = "isAuthenticated";
	private static final String USER_ATTR = "user";

	/* Login Form */
	private static final String SERVER_SESSION_COOKIE = "PHPSESSID";
	private static final String LOGIN_FORM_URL = "https://www.sailcomnet.ch/net/index.php";
	private static final String LOGIN_INFO_URL = "https://www.sailcomnet.ch/net/res_neu.php";
	private static final String USER_NAME_FLD = "txtMitgliedernummer";
	private static final String PWD_FLD = "txtPasswort";

	/* Logout Page */
	private static final String LOGOUT_PAGE_URL = "https://www.sailcomnet.ch/index.php?logout=1";

	private static CookieManager cookieManager = null;
	private static CookieStore cookieStore = null;

	private HttpSession clientSession = null;
	private StaticData staticData = null;

	public ServerSessionImpl(HttpSession clientSession) {
		this.clientSession = clientSession;
	}

	private boolean hasStaticData() {
		return staticData != null;
	}

	private void loadStaticData() {
		if (hasStaticData()) {
			return;
		}
		try {
			staticData = StaticDataSvc.getStaticData();
			try {
				List<Integer> myShips = MyShipSvc.getShips(this);
				for (Integer shipId : myShips) {
					Ship ship = getShip(shipId);
					staticData.myShips.add(ship);
					ship.isMine = true;
					Harbor harbor = staticData.harborsById.get(ship.harborId);
					if (!staticData.myHarbors.contains(harbor)) {
						staticData.myHarbors.add(harbor);
						harbor.isMine = true;
						Lake lake = staticData.lakesById.get(harbor.lakeId);
						if (!staticData.myLakes.contains(lake)) {
							staticData.myLakes.add(lake);
							lake.isMine = true;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Lake> getLakes() {
		if (!hasStaticData()) {
			loadStaticData();
		}
		return staticData.lakes;
	}

	@Override
	public List<Lake> getMyLakes() {
		if (!hasStaticData()) {
			loadStaticData();
		}
		return staticData.myLakes;
	}

	@Override
	public Lake getLake(int lakeId) {
		if (!hasStaticData()) {
			loadStaticData();
		}
		return staticData.lakesById.get(lakeId);
	}

	@Override
	public List<Harbor> getHarbors() {
		if (!hasStaticData()) {
			loadStaticData();
		}
		return staticData.harbors;
	}

	@Override
	public List<Harbor> getMyHarbors() {
		if (!hasStaticData()) {
			loadStaticData();
		}
		return staticData.myHarbors;
	}

	@Override
	public Harbor getHarbor(int harborId) {
		if (!hasStaticData()) {
			loadStaticData();
		}
		return staticData.harborsById.get(harborId);
	}

	@Override
	public List<Ship> getShips() {
		if (!hasStaticData()) {
			loadStaticData();
		}
		this.getMyShips();
		return staticData.ships;
	}

	@Override
	public List<Ship> getMyShips() {
		if (!hasStaticData()) {
			loadStaticData();
		}
		return staticData.myShips;
	}

	@Override
	public Ship getShip(int shipId) {
		if (!hasStaticData()) {
			loadStaticData();
		}
		return staticData.shipsById.get(shipId);
	}

	@Override
	public Ship getShip(String shipName) {
		if (!hasStaticData()) {
			loadStaticData();
		}
		return staticData.shipsByName.get(shipName);
	}

	@Override
	public boolean isConnected() {
		return clientSession.getAttribute(SESSION_COOKIE_ATTR) != null;
	}

	@Override
	public void connect() {

		if (cookieManager == null) {
			cookieManager = new CookieManager();
			cookieStore = cookieManager.getCookieStore();
			CookieHandler.setDefault(cookieManager);
		}

		/* Fetch Session Cookie */
		try {

			URL loginPage = new URL(LOGIN_FORM_URL);
			HttpsURLConnection loginConnection = (HttpsURLConnection) loginPage.openConnection();
			loginConnection.connect();
			loginConnection.getContent();

			List<HttpCookie> cookies = cookieStore.getCookies();
			HttpCookie sessionCookie = null;
			for (HttpCookie cookie : cookies) {
				if (cookie.getName().equals(SERVER_SESSION_COOKIE)) {
					sessionCookie = cookie;
				}
			}
			clientSession.setAttribute(SESSION_COOKIE_ATTR, sessionCookie);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean isLoggedIn() {
		return clientSession.getAttribute(IS_AUTHENTICATED_ATTR) != null;
	}

	@Override
	public boolean login(String userNr, String pwd) {
		
		clientSession.setAttribute(IS_AUTHENTICATED_ATTR, null);
		clientSession.setAttribute(USER_ATTR, null);

		try {
			URL loginForm = new URL(LOGIN_FORM_URL);
			HttpsURLConnection loginFormConnection = (HttpsURLConnection) loginForm.openConnection();

			loginFormConnection.setRequestMethod("POST");
			loginFormConnection.setInstanceFollowRedirects(false);
			String urlParameters = USER_NAME_FLD + "=" + userNr + "&" + PWD_FLD + "=" + pwd;
 
			// Send post request
			loginFormConnection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(loginFormConnection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = loginFormConnection.getResponseCode();
			if (responseCode == HttpServletResponse.SC_MOVED_TEMPORARILY) {

				String redirectUrl = loginFormConnection.getHeaderField("Location");
				if (redirectUrl.startsWith("error.php")) {
					return false;
				}

				Document doc = Jsoup.connect(LOGIN_INFO_URL).get();

				// <div id="login">&nbsp;<b>82219 - Hannes Brunner</b>&nbsp;&nbsp;&nbsp;(IP: 81.221.99.86)</div>
				Element loginInfo = doc.getElementById("login");
				if (loginInfo == null) {
					return false;
				}

				String userInfo = loginInfo.html();
				User user = new User();

				userInfo = userInfo.substring(userInfo.indexOf("<b>") + 3);
				userInfo = userInfo.substring(0, userInfo.indexOf("</b>"));
				user.id = userInfo.split(" - ")[0].trim();
				user.name = userInfo.split(" - ")[1].trim();
				user.role = "user";
				user.ip = loginInfo.html();
				user.ip = user.ip.substring(user.ip.indexOf("(IP:"));
				user.ip = user.ip.substring(4, user.ip.length() - 1);

				clientSession.setAttribute(IS_AUTHENTICATED_ATTR, "yes");
				clientSession.setAttribute(USER_ATTR, user);

				return true;

			}

		} catch (IOException e) {
			
			e.printStackTrace();
			throw new RuntimeException(e);

		}

		return false;

	}

	@Override
	public String getSessionId() {
		return ((HttpCookie) clientSession.getAttribute(SESSION_COOKIE_ATTR)).getValue();
	}
	
	@Override
	public User getUser() {
		return (User) clientSession.getAttribute(USER_ATTR);
	}
	
	@Override
	public void logout() {
		
		clientSession.setAttribute(IS_AUTHENTICATED_ATTR, null);

		try {
			URL loginPage = new URL(LOGOUT_PAGE_URL);
		    HttpsURLConnection loginConnection = (HttpsURLConnection) loginPage.openConnection();
			loginConnection.connect();
			loginConnection.getContent();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<Trip> getTrips() {
		try {
			return TripSvc.getTrips(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Booking> getBookings(int shipId, Date fromDate, int nofWeeks) {
		try {
			return BookSvc.getBookings(this, shipId, fromDate, nofWeeks);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<WeatherInfo> getWeatherInfo(int harborId, boolean isDet) {
		try {
			return WeatherSvc.getWeatherInfo(this, harborId, isDet);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
