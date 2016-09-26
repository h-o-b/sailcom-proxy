package ch.sailcom.server.rest;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Application;

import org.junit.Test;

import ch.sailcom.server.dto.SessionInfo;
import ch.sailcom.server.rest.util.SailcomTest;
import ch.sailcom.server.rest.util.TestAppBase;

public class SessionTest extends SailcomTest {

	class SessionTestApp extends TestAppBase {
		SessionTestApp() {
			register(SessionSvc.class);
		}
	}

	@Override
	protected Application configure() {
		return new SessionTestApp();
	}

	@Test
	public void testLogin() {
		final SessionInfo sessionInfo = login();
		assertEquals("82219", sessionInfo.user.id);
		assertEquals("Hannes Brunner", sessionInfo.user.name);
	}

}
