package ch.sailcom.server.proxy.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.sailcom.server.dto.Ship;
import ch.sailcom.server.dto.User;
import ch.sailcom.server.dto.UserInfo;
import ch.sailcom.server.dto.UserPref;
import ch.sailcom.server.proxy.UserDataProxy;

public class UserDataProxyImpl implements UserDataProxy {

	private static Logger LOGGER = LoggerFactory.getLogger(UserDataProxyImpl.class);

	private static final String MY_SHIPS_URL = "https://www.sailcomnet.ch/net/res_neu.php";
	// private static final String DB_FILE = "/volume1/@appstore/Tomcat7/src/webapps/sailcom-proxy/sailcom-proxy.db";
	private static final String DB_FILE = "d:/data/sailcom/sailcom-proxy.db";
	private static final String USER_INFO_MAP = "userInfoMap";

	private static DB database;
	private static ConcurrentMap<String, UserPref> userPrefMap;

	private final StaticData staticData;
	private final User user;
	private UserData userData = null;

	@SuppressWarnings("unchecked")
	private void openDatabase() {
		File dbFile = new File(DB_FILE);
		LOGGER.debug("Opening database file {}", dbFile.getAbsolutePath());
		database = DBMaker.fileDB(dbFile).transactionEnable().closeOnJvmShutdown().make();
		LOGGER.debug("Opening database hashMap {}", USER_INFO_MAP);
		userPrefMap = (ConcurrentMap<String, UserPref>) database.hashMap(USER_INFO_MAP).createOrOpen();
		LOGGER.debug("Opening database done");
	}

	public UserDataProxyImpl(StaticData staticData, User user) {
		this.staticData = staticData;
		this.user = user;
		if (database == null) {
			this.openDatabase();
		}
	}

	public synchronized void updateUserPref(User user, UserPref userPref) {
		LOGGER.debug("Update user info for {} - {}", user.id, user.name);
		userPrefMap.put(user.id, userPref);
		database.commit();
		LOGGER.debug("Update user info done");
	}

	private List<Integer> loadAvailableShips() throws IOException {

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

		LOGGER.debug("getAvailableShips.1");
		String url = MY_SHIPS_URL;
		Document doc = Jsoup.connect(url).get();

		LOGGER.debug("getAvailableShips.2");
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

		try {

			LOGGER.debug("loadUserData: loadAvailableShips");
			List<Integer> myShips = loadAvailableShips();
			for (Integer shipId : myShips) {
				this.userData.addAvailableShip(shipId);
			}

			LOGGER.debug("loadUserData: loadUserPref");
			UserPref userPref = userPrefMap.get(this.getUser().id);
			if (userPref != null) {
				for (Integer id : userPref.favoriteShips) {
					this.userData.favoriteShipsById.put(id, this.staticData.getShip(id));
				}
				this.userData.ratedShipsById = userPref.starredShips;
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		LOGGER.debug("loadUserData.3");

	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public UserInfo getUserInfo() {
		loadUserData();
		return this.userData.getUserInfo();
	}

	@Override
	public List<Integer> getAvailableLakes() {
		loadUserData();
		return new ArrayList<Integer>(userData.availableLakesById.keySet());
	}

	@Override
	public List<Integer> getAvailableHarbors() {
		loadUserData();
		return new ArrayList<Integer>(userData.availableHarborsById.keySet());
	}

	@Override
	public List<Integer> getAvailableShips() {
		loadUserData();
		return new ArrayList<Integer>(userData.availableShipsById.keySet());
	}

	@Override
	public UserPref getUserPref() {
		return this.userData.getUserPref();
	}

	@Override
	public List<Integer> getFavoriteShips() {
		loadUserData();
		return new ArrayList<Integer>(userData.favoriteShipsById.keySet());
	}

	@Override
	public List<Integer> like(Ship ship) {
		List<Integer> favorites = this.userData.like(ship);
		this.updateUserPref(this.getUser(), this.getUserPref());
		return favorites;
	}

	@Override
	public List<Integer> unlike(Ship ship) {
		List<Integer> favorites = this.userData.unlike(ship);
		this.updateUserPref(this.getUser(), this.getUserPref());
		return favorites;
	}

	@Override
	public Map<Integer, Integer> getRatedShips() {
		loadUserData();
		return userData.ratedShipsById;
	}

	@Override
	public Map<Integer, Integer> rate(Ship ship, int starCount) {
		Map<Integer, Integer> ratings = this.userData.rate(ship, starCount);
		this.updateUserPref(this.getUser(), this.getUserPref());
		return ratings;
	}

}
