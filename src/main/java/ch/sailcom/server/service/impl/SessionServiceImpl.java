package ch.sailcom.server.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.sailcom.server.model.User;
import ch.sailcom.server.proxy.SessionProxy;
import ch.sailcom.server.proxy.impl.BookingProxyImpl;
import ch.sailcom.server.proxy.impl.SessionProxyImpl;
import ch.sailcom.server.proxy.impl.StaticDataProxyImpl;
import ch.sailcom.server.proxy.impl.UserInfoProxyImpl;
import ch.sailcom.server.proxy.impl.UserPrefProxyImpl;
import ch.sailcom.server.proxy.impl.WeatherProxyImpl;
import ch.sailcom.server.service.BookingService;
import ch.sailcom.server.service.Service;
import ch.sailcom.server.service.SessionService;
import ch.sailcom.server.service.StaticDataService;
import ch.sailcom.server.service.UserService;
import ch.sailcom.server.service.WeatherService;

public class SessionServiceImpl implements SessionService {

	private static Logger LOGGER = LoggerFactory.getLogger(SessionServiceImpl.class);

	private final SessionProxy sessionProxy;

	private Map<Class<? extends Service>, Service> serviceMap = new HashMap<Class<? extends Service>, Service>();

	public SessionServiceImpl() {
		this.sessionProxy = new SessionProxyImpl();
	}

	@Override
	public String getSessionId() {
		return this.sessionProxy.getSessionId();
	}

	@Override
	public boolean isLoggedIn() {
		return this.getUser() != null;
	}

	@Override
	public boolean login(String user, String pwd) {

		LOGGER.debug("login({})", user);

		if (this.sessionProxy.login(user, pwd)) {

			StaticDataService staticDataService = new StaticDataServiceImpl(new StaticDataProxyImpl());
			this.serviceMap.put(StaticDataService.class, staticDataService);

			UserService userService = new UserServiceImpl(this.getUser(), staticDataService, new UserInfoProxyImpl(), new UserPrefProxyImpl());
			this.serviceMap.put(UserService.class, userService);

			BookingService bookingService = new BookingServiceImpl(new BookingProxyImpl(staticDataService));
			this.serviceMap.put(BookingService.class, bookingService);

			WeatherService weatherService = new WeatherServiceImpl(new WeatherProxyImpl(staticDataService));
			this.serviceMap.put(WeatherService.class, weatherService);

			return true;

		}

		return false;

	}

	@Override
	public User getUser() {
		return this.sessionProxy.getUser();
	}

	@Override
	public void logout() {
		this.sessionProxy.logout();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Service> T getService(Class<T> serviceClass) {
		return (T) this.serviceMap.get(serviceClass);
	}

}
