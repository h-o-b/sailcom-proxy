package ch.sailcom.server.proxy.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.sailcom.server.proxy.UserInfoProxy;

@SessionScoped
public class UserInfoProxyImpl implements UserInfoProxy, Serializable {

	private static final long serialVersionUID = -1981902868405012738L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoProxyImpl.class);

	private static final String MY_SHIPS_URL = "https://www.sailcomnet.ch/net/res_neu.php";

	private final List<Integer> availableShips = new ArrayList<Integer>();

	@PostConstruct
	private void loadAvailableShips() {

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

		LOGGER.debug("loadAvailableShips.1");
		Document doc;
		try {
			doc = Jsoup.connect(MY_SHIPS_URL).get();
		} catch (IOException e) {
			LOGGER.error("My ships url crashed", e);
			throw new RuntimeException("My ships url crashed", e);
		}

		LOGGER.debug("loadAvailableShips.2");
		if (doc.select("input[name=txtMitgliedernummer]").first() != null) {
			throw new NoSessionException();
		}

		// Loop ship options
		Element select = doc.select("select[name=boot]").first();
		Elements shipOptions = select.select("option");
		for (int s = 0; s < shipOptions.size(); s++) {
			Element ship = shipOptions.get(s);
			int shipId = Integer.parseInt(ship.attr("value"));
			availableShips.add(shipId);
		}

		LOGGER.debug("loadAvailableShips.3");

	}

	@Override
	public List<Integer> getAvailableShips() {
		return this.availableShips;
	}

}
