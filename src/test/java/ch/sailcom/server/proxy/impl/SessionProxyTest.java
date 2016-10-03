package ch.sailcom.server.proxy.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.sailcom.server.proxy.impl.util.ProxyTest;
import ch.sailcom.server.rest.dto.SessionInfo;

public class SessionProxyTest extends ProxyTest {

	@Test
	public void testLogin() {
		SessionInfo sessionInfo = login();
		assertEquals("82219", sessionInfo.user.id);
		assertEquals("Hannes Brunner", sessionInfo.user.name);
	}

}
