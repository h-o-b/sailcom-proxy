package ch.sailcom.session.proxy.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.sailcom.session.domain.SessionInfo;

public class SessionProxyTest extends ProxyTest {

	@Test
	public void testLogin() {
		SessionInfo sessionInfo = login();
		assertEquals("82219", sessionInfo.user.id);
		assertEquals("Hannes Brunner", sessionInfo.user.name);
	}

}
