package ch.sailcom.server.proxy.impl;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.sailcom.server.model.User;
import ch.sailcom.server.model.UserPref;
import ch.sailcom.server.proxy.UserPrefProxy;

@SessionScoped
public class UserPrefProxyImpl implements UserPrefProxy, Serializable {

	private static final long serialVersionUID = -7964478087782131052L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserPrefProxyImpl.class);

	// private static final String DB_FILE = "/volume1/@appstore/Tomcat7/src/webapps/sailcom-proxy/sailcom-proxy.db";
	private static final String DB_FILE = "d:/data/sailcom/sailcom-proxy.db";
	private static final String USER_INFO_MAP = "userInfoMap";

	private static DB database;
	private static ConcurrentMap<String, UserPref> userPrefMap;

	@PostConstruct
	@SuppressWarnings("unchecked")
	void init() {

		if (database != null) {
			return;
		}

		File dbFile = new File(DB_FILE);
		LOGGER.debug("Opening database file {}", dbFile.getAbsolutePath());

		database = DBMaker.fileDB(dbFile).transactionEnable().closeOnJvmShutdown().make();
		LOGGER.debug("Opening database hashMap {}", USER_INFO_MAP);

		userPrefMap = (ConcurrentMap<String, UserPref>) database.hashMap(USER_INFO_MAP).createOrOpen();
		LOGGER.debug("Opening database done");

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

		LOGGER.debug("getUserPref({}): {}", user.id, userPref);
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
