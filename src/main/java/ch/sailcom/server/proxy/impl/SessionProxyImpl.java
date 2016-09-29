package ch.sailcom.server.proxy.impl;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.sailcom.server.model.User;
import ch.sailcom.server.proxy.SessionProxy;

public class SessionProxyImpl implements SessionProxy {

	private static Logger LOGGER = LoggerFactory.getLogger(SessionProxyImpl.class);

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

	private HttpCookie sessionCookie = null;
	private User user = null;
	private Map<Class<?>, Object> proxyMap = new HashMap<Class<?>, Object>();

	public SessionProxyImpl() {
		synchronized (SERVER_SESSION_COOKIE) {
			if (cookieManager == null) {
				cookieManager = new CookieManager();
				cookieStore = cookieManager.getCookieStore();
				CookieHandler.setDefault(cookieManager);
			}
		}
	}

	@Override
	public String getSessionId() {
		return sessionCookie != null ? sessionCookie.getValue() : null;
	}

	private boolean isConnected() {
		return sessionCookie != null;
	}

	private void connect() {

		/* Fetch SessionInfo Cookie */
		try {

			LOGGER.debug("connect.1");
			URL loginPage = new URL(LOGIN_FORM_URL);
			HttpsURLConnection loginConnection = (HttpsURLConnection) loginPage.openConnection();
			loginConnection.connect();
			loginConnection.getContent();

			LOGGER.debug("connect.2");
			List<HttpCookie> cookies = cookieStore.getCookies();
			for (HttpCookie cookie : cookies) {
				LOGGER.debug("connect.cookie: " + cookie.getName());
				if (cookie.getName().equals(SERVER_SESSION_COOKIE)) {
					sessionCookie = cookie;
				}
			}

			LOGGER.debug("connect.3");

		} catch (IOException e) {
			LOGGER.error("connect crashed", e);
			throw new RuntimeException(e);
		}

	}

	@Override
	public boolean isLoggedIn() {
		return this.user != null;
	}

	@Override
	public boolean login(String userNr, String pwd) {

		this.user = null;

		if (!this.isConnected()) {
			this.connect();
		}

		try {

			LOGGER.debug("login.1");
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

			LOGGER.debug("login.2: " + responseCode);
			if (responseCode == HttpServletResponse.SC_MOVED_TEMPORARILY) {

				String redirectUrl = loginFormConnection.getHeaderField("Location");
				LOGGER.debug("login.3: " + redirectUrl);
				if (redirectUrl.startsWith("error.php")) {
					return false;
				}

				LOGGER.debug("login.3");
				Document doc = Jsoup.connect(LOGIN_INFO_URL).get();

				// <div id="login">&nbsp;<b>82219 - Hannes Brunner</b>&nbsp;&nbsp;&nbsp;(IP: 81.221.99.86)</div>
				Element loginInfo = doc.getElementById("login");
				if (loginInfo == null) {
					return false;
				}

				LOGGER.debug("login.4");
				String userInfo = loginInfo.html();
				userInfo = userInfo.substring(userInfo.indexOf("<b>") + 3);
				userInfo = userInfo.substring(0, userInfo.indexOf("</b>"));

				user = new User();
				user.id = userInfo.split(" - ")[0].trim();
				user.name = userInfo.split(" - ")[1].trim();
				user.role = "user";
				user.ip = loginInfo.html();
				user.ip = user.ip.substring(user.ip.indexOf("(IP:"));
				user.ip = user.ip.substring(4, user.ip.length() - 1);

				LOGGER.info("login.5");
				return true;

			}

		} catch (IOException e) {

			LOGGER.error("login crashed", e);
			throw new RuntimeException(e);

		}

		return false;

	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public void logout() {

		this.user = null;
		this.proxyMap.clear();

		try {
			URL loginPage = new URL(LOGOUT_PAGE_URL);
			HttpsURLConnection loginConnection = (HttpsURLConnection) loginPage.openConnection();
			loginConnection.connect();
			loginConnection.getContent();
		} catch (IOException e) {
			LOGGER.error("logout crashed", e);
			throw new RuntimeException(e);
		}

	}

}
