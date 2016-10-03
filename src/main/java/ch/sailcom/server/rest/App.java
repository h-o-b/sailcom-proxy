package ch.sailcom.server.rest;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.sailcom.server.service.StaticDataService;

public class App extends ResourceConfig {

	private static Logger LOGGER = LoggerFactory.getLogger(App.class);

	@Inject
	StaticDataService staticDataService;

	public App() {
	}

	@PostConstruct
	void init() {
		LOGGER.info("Fetch static data");
		staticDataService.getShips();
	}

}
