package ch.sailcom.server.proxy.impl;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentMap;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.sailcom.server.model.User;
import ch.sailcom.server.model.UserPref;
import ch.sailcom.server.proxy.UserPrefProxy;

public class UserPrefProxyImpl implements UserPrefProxy {

	private static Logger LOGGER = LoggerFactory.getLogger(UserPrefProxyImpl.class);

	// private static final String DB_FILE = "/volume1/@appstore/Tomcat7/src/webapps/sailcom-proxy/sailcom-proxy.db";
	private static final String DB_FILE = "d:/data/sailcom/sailcom-proxy.db";
	private static final String USER_INFO_MAP = "userInfoMap";

	private static DB database;
	private static ConcurrentMap<String, UserPref> userPrefMap;

	@SuppressWarnings("unchecked")
	private void openDatabase() {
		File dbFile = new File(DB_FILE);
		LOGGER.debug("Opening database file {}", dbFile.getAbsolutePath());
		database = DBMaker.fileDB(dbFile).transactionEnable().closeOnJvmShutdown().make();
		LOGGER.debug("Opening database hashMap {}", USER_INFO_MAP);
		userPrefMap = (ConcurrentMap<String, UserPref>) database.hashMap(USER_INFO_MAP).createOrOpen();
		LOGGER.debug("Opening database done");
	}

	public UserPrefProxyImpl() {
		if (database == null) {
			this.openDatabase();
		}
	}

	@Override
	public synchronized UserPref getUserPref(User user) {

		LOGGER.debug("getUserPref({})", user.id);
		UserPref userPref = userPrefMap.get(user.id);

		LOGGER.debug("getUserPref({}): {}", user.id, userPref);
		if (userPref == null) {
			userPref = new UserPref();
			userPref.favoriteShips = new HashSet<Integer>();
			userPref.ratedShips = new HashMap<Integer, Integer>();
		}

		return userPref;

	}

	@Override
	public void setUserPref(User user, UserPref userPref) {
		LOGGER.debug("setUserPref({}, {})", user.id, userPref);
		userPrefMap.put(user.id, userPref);
		database.commit();
		LOGGER.debug("setUserPref.done({}, {})", user.id, userPref);
	}

}
