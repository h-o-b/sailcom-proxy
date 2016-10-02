package ch.sailcom.server.service.impl;

import java.io.Serializable;
import java.util.Arrays;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.sailcom.server.model.User;
import ch.sailcom.server.proxy.SessionProxy;
import ch.sailcom.server.service.SessionService;

@SessionScoped
public class SessionServiceImpl implements SessionService, Serializable {

	private static final long serialVersionUID = -3695176711275632182L;

	private static Logger LOGGER = LoggerFactory.getLogger(SessionServiceImpl.class);

	@Inject
	private BeanManager manager;

	@Inject
	private SessionProxy sessionProxy;

	public SessionServiceImpl() {
		LOGGER.debug("SessionServiceImpl(): {}\n{}", manager, Arrays.toString(new Throwable().getStackTrace()));
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
		return this.sessionProxy.login(user, pwd);
	}

	@Override
	public User getUser() {
		return this.sessionProxy.getUser();
	}

	@Override
	public void logout() {
		this.sessionProxy.logout();
	}

}
