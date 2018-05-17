package ch.sailcom.session.proxy.impl;

import ch.sailcom.session.domain.SessionInfo;
import ch.sailcom.session.proxy.impl.SessionProxyImpl;

public abstract class ProxyTest {

	private static final String USER = "82219";
	private static final String PWD = "y|[^rZ6g";

	private static SessionProxyImpl sessionProxy;

	public static SessionInfo login() {
		sessionProxy = new SessionProxyImpl();
		sessionProxy.init();
		if (sessionProxy.login(USER, Obfuscator.decrypt(PWD))) {
			return new SessionInfo(sessionProxy.getSessionId(), sessionProxy.getUser());
		}
		return null;
	}

	public static void logout() {
		sessionProxy.logout();
	}

}
