package ch.sailcom.server.proxy.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.sailcom.server.model.Harbor;
import ch.sailcom.server.model.Lake;
import ch.sailcom.server.model.Ship;
import ch.sailcom.server.proxy.impl.util.ProxyTest;
import ch.sailcom.server.rest.dto.SessionInfo;

public class StaticDataProxyTest extends ProxyTest {

	static StaticDataProxyImpl staticDataProxy;

	@BeforeClass
	public static void openSession() {
		SessionInfo sessionInfo = login();
		assertEquals("user id", "82219", sessionInfo.user.id);
		assertEquals("user name", "Hannes Brunner", sessionInfo.user.name);
		staticDataProxy = new StaticDataProxyImpl();
		staticDataProxy.loadStaticData();
	}

	@AfterClass
	public static void closeSession() {
		logout();
	}

	@Test
	public void testLakes() {
		List<Lake> lakes = staticDataProxy.getLakes();
		assertEquals("17 lakes", 17, lakes.size());
		Lake lake = lakes.stream().filter(l -> l.id == 2).findFirst().get();
		assertEquals("lake id", 2, lake.id);
		assertNotNull("lake name", lake.name);
	}

	@Test
	public void testHarbors() {
		List<Harbor> harbors = staticDataProxy.getHarbors();
		assertEquals("53 harbors", 53, harbors.size());
		Harbor harbor = harbors.stream().filter(h -> h.id == 14).findFirst().get();
		assertEquals("harbor id", 14, harbor.id);
		assertEquals("harbor name", "La Neuveville", harbor.name);
		assertNotNull("lake id", harbor.lakeId);
	}

	@Test
	public void testShips() {
		List<Ship> ships = staticDataProxy.getShips();
		assertEquals("99 ships", 99, ships.size());
		Ship ship = ships.stream().filter(s -> s.id == 197).findFirst().get();
		assertEquals("ship id", 197, ship.id);
		assertEquals("ship name", "mOcean SUI 32", ship.name);
		assertEquals("harbor id", 31, ship.harborId);
		assertNotNull("lake id", ship.lakeId);
	}

}
