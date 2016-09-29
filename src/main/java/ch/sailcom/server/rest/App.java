package ch.sailcom.server.rest;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.sailcom.server.rest.util.BookingServiceFactory;
import ch.sailcom.server.rest.util.Encryptor;
import ch.sailcom.server.rest.util.StaticDataServiceFactory;
import ch.sailcom.server.rest.util.UserServiceFactory;
import ch.sailcom.server.rest.util.WeatherServiceFactory;
import ch.sailcom.server.service.BookingService;
import ch.sailcom.server.service.SessionService;
import ch.sailcom.server.service.StaticDataService;
import ch.sailcom.server.service.UserService;
import ch.sailcom.server.service.WeatherService;
import ch.sailcom.server.service.impl.SessionServiceImpl;

public class App extends ResourceConfig {

	private static Logger LOGGER = LoggerFactory.getLogger(App.class);

	private static final String USER = "82219";
	private static final String PWD = "y|[^rZ6g";

	public App() {

		LOGGER.info("register proxy factories");
		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bindFactory(StaticDataServiceFactory.class).to(StaticDataService.class).proxy(true).proxyForSameScope(false).in(RequestScoped.class);
				bindFactory(UserServiceFactory.class).to(UserService.class).proxy(true).proxyForSameScope(false).in(RequestScoped.class);
				bindFactory(BookingServiceFactory.class).to(BookingService.class).proxy(true).proxyForSameScope(false).in(RequestScoped.class);
				bindFactory(WeatherServiceFactory.class).to(WeatherService.class).proxy(true).proxyForSameScope(false).in(RequestScoped.class);
			}
		});

		LOGGER.info("login");
		SessionService session = new SessionServiceImpl();
		session.login(USER, Encryptor.decrypt(PWD));

		LOGGER.info("fetch static data");
		session.getService(StaticDataService.class).getShips();

		LOGGER.info("logout");
		session.logout();

	}

}
