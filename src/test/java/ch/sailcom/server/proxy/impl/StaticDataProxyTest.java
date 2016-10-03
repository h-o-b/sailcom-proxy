package ch.sailcom.server.proxy.impl;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.sailcom.server.model.Harbor;
import ch.sailcom.server.model.Lake;
import ch.sailcom.server.model.Ship;

public class StaticDataProxyTest extends ProxyTest {

	static StaticDataProxyImpl staticDataProxy;

	static final int LAKE_COUNT = 17;
	static final String LAKE_NAME = "Lungernsee";
	static final int HARBOR_COUNT = 53;
	static final int HARBOR_ID = 54;
	static final String HARBOR_NAME = "Lungern";
	static final int SHIP_COUNT = 99;
	static final int SHIP_ID = 147;
	static final String SHIP_NAME = "Laser II";

	static int lakeId = 0;

	@BeforeClass
	public static void openSession() {
		staticDataProxy = new StaticDataProxyImpl();
		staticDataProxy.init();
	}

	@AfterClass
	public static void closeSession() {
	}

	@Test
	public void testLakes() {
		List<Lake> lakes = staticDataProxy.getLakes();
		assertEquals(LAKE_COUNT + " lakes", LAKE_COUNT, lakes.size());
		Lake lake = lakes.stream().filter(l -> LAKE_NAME.equals(l.name)).peek(l -> lakeId = l.id).findFirst().get();
		assertEquals("lake id", lakeId, lake.id);
		assertEquals("lake name", LAKE_NAME, lake.name);
	}

	@Test
	public void testHarbors() {
		List<Harbor> harbors = staticDataProxy.getHarbors();
		assertEquals(HARBOR_COUNT + " harbors", HARBOR_COUNT, harbors.size());
		Harbor harbor = harbors.stream().filter(h -> h.id == HARBOR_ID).findFirst().get();
		assertEquals("harbor id", HARBOR_ID, harbor.id);
		assertEquals("harbor name", HARBOR_NAME, harbor.name);
		assertEquals("lake id", lakeId, harbor.lakeId);
	}

	@Test
	public void testShips() {
		List<Ship> ships = staticDataProxy.getShips();
		assertEquals(SHIP_COUNT + " ships", SHIP_COUNT, ships.size());
		Ship ship = ships.stream().filter(s -> s.id == SHIP_ID).findFirst().get();
		assertEquals("ship id", SHIP_ID, ship.id);
		assertEquals("ship name", SHIP_NAME, ship.name);
		assertEquals("harbor id", HARBOR_ID, ship.harborId);
		assertEquals("plate", "OW -", ship.plate);
		assertEquals("type", "Jolle", ship.type);
		assertEquals("location", "beim Campingplatz Lungern", ship.location);
		assertEquals("pax", 3, ship.pax);
		assertEquals("sailSize", "11 m²", ship.sailSize);
		assertEquals("length", "4.39", ship.length.substring(0, 4));
	}

}
