package ch.sailcom.server.rest;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;

import ch.sailcom.server.proxy.BookingProxy;
import ch.sailcom.server.proxy.StaticDataProxy;
import ch.sailcom.server.proxy.UserDataProxy;
import ch.sailcom.server.rest.util.BookingProxyFactory;
import ch.sailcom.server.rest.util.StaticDataProxyFactory;
import ch.sailcom.server.rest.util.UserDataProxyFactory;

public class App extends ResourceConfig {

	public App() {

		register(new AbstractBinder() {

			@Override
			protected void configure() {
				bindFactory(StaticDataProxyFactory.class).to(StaticDataProxy.class).proxy(true).proxyForSameScope(false).in(RequestScoped.class);
				bindFactory(UserDataProxyFactory.class).to(UserDataProxy.class).proxy(true).proxyForSameScope(false).in(RequestScoped.class);
				bindFactory(BookingProxyFactory.class).to(BookingProxy.class).proxy(true).proxyForSameScope(false).in(RequestScoped.class);
			}

		});

	}

}
