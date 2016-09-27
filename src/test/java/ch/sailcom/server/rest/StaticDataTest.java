package ch.sailcom.server.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.ws.rs.core.Application;

import org.junit.Before;
import org.junit.Test;

import ch.sailcom.server.dto.Harbor;
import ch.sailcom.server.dto.Lake;
import ch.sailcom.server.dto.SessionInfo;
import ch.sailcom.server.dto.Ship;
import ch.sailcom.server.rest.util.SailcomTest;
import ch.sailcom.server.rest.util.TestAppBase;

public class StaticDataTest extends SailcomTest {

	class SessionTestApp extends TestAppBase {
		SessionTestApp() {
			register(SessionSvc.class);
			register(LakeSvc.class);
			register(HarborSvc.class);
			register(ShipSvc.class);
		}
	}

	@Override
	protected Application configure() {
		return new SessionTestApp();
	}

	@Before
	public void openSession() {
		final SessionInfo sessionInfo = login();
		assertEquals("user id", "82219", sessionInfo.user.id);
		assertEquals("user name", "Hannes Brunner", sessionInfo.user.name);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testLakes() {
		List<Lake> lakes = target("lakes").request().get(List.class);
		assertEquals("17 lakes", 17, lakes.size());
		Lake lake = target("lakes/2").request().get(Lake.class);
		assertEquals("lake id", 2, lake.id);
		assertNotNull("lake name", lake.name);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testHarbors() {
		List<Harbor> harbors = target("harbors").request().get(List.class);
		assertEquals("53 harbors", 53, harbors.size());
		Harbor harbor = target("harbors/14").request().get(Harbor.class);
		assertEquals("harbor id", 14, harbor.id);
		assertEquals("harbor name", "La Neuveville", harbor.name);
		assertNotNull("lake id", harbor.lakeId);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testShips() {
		List<Ship> ships = target("ships").request().get(List.class);
		assertEquals("99 ships", 99, ships.size());
		Ship ship = target("ships/197").request().get(Ship.class);
		assertEquals("ship id", 197, ship.id);
		assertEquals("ship name", "mOcean SUI 32", ship.name);
		assertEquals("harbor id", 31, ship.harborId);
		assertNotNull("lake id", ship.lakeId);
	}

}
