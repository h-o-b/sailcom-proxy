package ch.sailcom.server.proxy.impl.util;

import ch.sailcom.server.proxy.SessionProxy;
import ch.sailcom.server.proxy.impl.SessionProxyImpl;
import ch.sailcom.server.rest.dto.SessionInfo;

public class ProxyTest {

	private static final String USER = "82219";
	private static final String PWD = "y|[^rZ6g";

	private static SessionProxy sessionProxy;

	public static SessionInfo login() {
		sessionProxy = new SessionProxyImpl();
		if (sessionProxy.login(USER, Obfuscator.decrypt(PWD))) {
			return new SessionInfo(sessionProxy.getSessionId(), sessionProxy.getUser());
		}
		return null;
	}

	public static void logout() {
		sessionProxy.logout();
	}

}
