package ch.sailcom.server.proxy.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.sailcom.server.dto.StaticData;
import ch.sailcom.server.dto.User;
import ch.sailcom.server.dto.UserData;
import ch.sailcom.server.dto.UserInfo;
import ch.sailcom.server.proxy.UserDataProxy;

public class UserDataProxyImpl implements UserDataProxy {

	private static Logger LOGGER = LoggerFactory.getLogger(UserDataProxyImpl.class);

	private static final String MY_SHIPS_URL = "https://www.sailcomnet.ch/net/res_neu.php";

	private final StaticData staticData;
	private final User user;
	private UserData userData = null;

	public UserDataProxyImpl(StaticData staticData, User user) {
		this.staticData = staticData;
		this.user = user;
	}

	private List<Integer> getAvailableShips() throws IOException {

		List<Integer> ships = new ArrayList<Integer>();

		// @formatter:off
		/*
		 * https://www.sailcomnet.ch/net/res_neu.php
		 * 
		 * <...>
		 *   <form method="post" action="/net/res_neu.php" name="form1">
		 *     <table cellpadding="3" cellspacing="0" style="border-collapse: collapse;" >
		 *       <tr>
		 *         <td colspan="2">Bitte beachte: Auf der untenstehenden Liste erscheinen nur diejenigen Boote, die Du eingesegelt hast und welche zur Zeit zur Verfügung stehen (nicht ausgewassert).</td>
		 *       </tr>
		 *       <tr>
		 *         <td><b>Boot:</b></td>
		 *         <td>
		 *           <select size="1" name="boot" onChange='searchSuggest();'>
		 *             <option value='112' selected>Vierwaldstättersee, Brunnen - Föhnhafen, Albin Viggen</option>
		 *           </select>
		 *         </td>
		 *       </tr>
		 */
		// @formatter:on

		LOGGER.info("getAvailableShips.1");
		String url = MY_SHIPS_URL;
		Document doc = Jsoup.connect(url).get();
		LOGGER.info("getAvailableShips.2");

		if (doc.select("input[name=txtMitgliedernummer]").first() != null) {
			throw new NoSessionException();
		}

		// Loop ship options
		Element select = doc.select("select[name=boot]").first();
		Elements shipOptions = select.select("option");
		for (int s = 0; s < shipOptions.size(); s++) {
			Element ship = shipOptions.get(s);
			int shipId = Integer.parseInt(ship.attr("value"));
			ships.add(shipId);
		}

		return ships;

	}

	private synchronized void loadUserData() {

		if (this.userData != null) {
			return;
		}

		this.userData = new UserData(this.staticData, this.getUser());

		LOGGER.info("loadUserData.1");
		try {
			List<Integer> myShips = getAvailableShips();
			LOGGER.info("loadUserData.2");
			for (Integer shipId : myShips) {
				this.userData.addShip(shipId);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		LOGGER.info("loadUserData.3");

	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public UserInfo getUserInfo() {
		return this.getUserData().getUserInfo();
	}

	@Override
	public UserData getUserData() {
		loadUserData();
		return userData;
	}

	@Override
	public List<Integer> getLakes() {
		loadUserData();
		return new ArrayList<Integer>(userData.availableLakesById.keySet());
	}

	@Override
	public List<Integer> getHarbors() {
		loadUserData();
		return new ArrayList<Integer>(userData.availableHarborsById.keySet());
	}

	@Override
	public List<Integer> getShips() {
		loadUserData();
		return new ArrayList<Integer>(userData.availableShipsById.keySet());
	}

}
