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
import ch.sailcom.server.dto.StaticData;
import ch.sailcom.server.dto.User;
import ch.sailcom.server.dto.UserData;
import ch.sailcom.server.dto.UserInfo;
import ch.sailcom.server.dto.UserPreference;
import ch.sailcom.server.proxy.UserDataProxy;

public class UserDataProxyImpl implements UserDataProxy {

	private static Logger LOGGER = LoggerFactory.getLogger(UserDataProxyImpl.class);

	private static final String MY_SHIPS_URL = "https://www.sailcomnet.ch/net/res_neu.php";
	private static final String DB_FILE = "sailcom-proxy.db";
	private static final String USER_INFO_MAP = "userInfoMap";

	private static DB database;
	private static ConcurrentMap<String, UserPreference> userPrefMap;

	private final StaticData staticData;
	private final User user;
	private UserData userData = null;

	@SuppressWarnings("unchecked")
	private void openDatabase() {
		File dbFile = new File(DB_FILE);
		LOGGER.info("Opening database file {}", dbFile.getAbsolutePath());
		database = DBMaker.fileDB(dbFile).closeOnJvmShutdown().make();
		LOGGER.info("Opening database hashMap {}", USER_INFO_MAP);
		userPrefMap = (ConcurrentMap<String, UserPreference>) database.hashMap(USER_INFO_MAP).createOrOpen();
		LOGGER.info("Opening database done");
	}

	private void closeDatabase() {
		LOGGER.info("Closing database");
		database.close();
	}

	public UserDataProxyImpl(StaticData staticData, User user) {
		this.staticData = staticData;
		this.user = user;
		if (database == null) {
			this.openDatabase();
		}
	}

	public synchronized void updateUserPreference(User user, UserPreference userPreference) {
		LOGGER.info("Update user info for {} - {}", user.id, user.name);
		userPrefMap.put(user.id, userPreference);
		database.commit();
		LOGGER.info("Update user info done");
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

		LOGGER.info("loadUserData: getAvailableShips");
		try {
			List<Integer> myShips = getAvailableShips();
			for (Integer shipId : myShips) {
				this.userData.addShip(shipId);
			}
			LOGGER.info("loadUserData: userPreferences");
			UserPreference userPref = userPrefMap.get(this.getUser().id);
			if (userPref != null) {
				for (Integer id : userPref.favoriteShips) {
					this.userData.favoriteShipsById.put(id, this.staticData.getShip(id));
				}
				this.userData.starredShipsById = userPref.starredShips;
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
	public UserPreference getUserPreference() {
		return this.getUserData().getUserPreference();
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

	@Override
	public List<Integer> like(Ship ship) {
		List<Integer> favorites = this.getUserData().like(ship);
		this.updateUserPreference(this.getUser(), this.getUserPreference());
		return favorites;
	}

	@Override
	public List<Integer> unlike(Ship ship) {
		List<Integer> favorites = this.getUserData().unlike(ship);
		this.updateUserPreference(this.getUser(), this.getUserPreference());
		return favorites;
	}

	@Override
	public Map<Integer, Integer> rate(Ship ship, int starCount) {
		Map<Integer, Integer> ratings = this.getUserData().rate(ship, starCount);
		this.updateUserPreference(this.getUser(), this.getUserPreference());
		return ratings;
	}

}
