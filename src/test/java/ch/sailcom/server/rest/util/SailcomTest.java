package ch.sailcom.server.rest.util;

import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.jdkhttp.JdkHttpServerTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;

import ch.sailcom.server.rest.dto.SessionInfo;

public class SailcomTest extends JerseyTest {

	private static final String USER_PARAM = "user";
	private static final String PWD_PARAM = "pwd";
	private static final String USER = "82219";
	private static final String PWD = "y|[^rZ6g";

	@Override
	protected TestContainerFactory getTestContainerFactory() {
		return new JdkHttpServerTestContainerFactory();
	}

	protected SessionInfo login() {
		return target("session/login").queryParam(USER_PARAM, USER).queryParam(PWD_PARAM, Encryptor.decrypt(PWD)).request().get(SessionInfo.class);
	}

}
