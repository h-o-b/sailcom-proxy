package ch.sailcom.server.rest;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.sailcom.server.proxy.BookingProxy;
import ch.sailcom.server.proxy.SessionProxy;
import ch.sailcom.server.proxy.StaticDataProxy;
import ch.sailcom.server.proxy.UserDataProxy;
import ch.sailcom.server.proxy.impl.SessionProxyImpl;
import ch.sailcom.server.rest.util.BookingProxyFactory;
import ch.sailcom.server.rest.util.Encryptor;
import ch.sailcom.server.rest.util.StaticDataProxyFactory;
import ch.sailcom.server.rest.util.UserDataProxyFactory;

public class App extends ResourceConfig {

	private static Logger LOGGER = LoggerFactory.getLogger(App.class);

	private static final String USER = "82219";
	private static final String PWD = "y|[^rZ6g";

	public App() {

		LOGGER.info("register proxy factories");
		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bindFactory(StaticDataProxyFactory.class).to(StaticDataProxy.class).proxy(true).proxyForSameScope(false).in(RequestScoped.class);
				bindFactory(UserDataProxyFactory.class).to(UserDataProxy.class).proxy(true).proxyForSameScope(false).in(RequestScoped.class);
				bindFactory(BookingProxyFactory.class).to(BookingProxy.class).proxy(true).proxyForSameScope(false).in(RequestScoped.class);
			}
		});

		LOGGER.info("login");
		SessionProxy session = new SessionProxyImpl();
		session.login(USER, Encryptor.decrypt(PWD));

		LOGGER.info("fetch static data");
		session.getProxy(StaticDataProxy.class).getStaticData();

		LOGGER.info("logout");
		session.logout();

	}

}
