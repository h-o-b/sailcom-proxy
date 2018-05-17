package ch.sailcom.test.service;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SessionScoped
public class SessionBean implements Serializable {

	private static final long serialVersionUID = 3618439009596702454L;

	private static Logger LOGGER = LoggerFactory.getLogger(SessionBean.class);

	public SessionBean() {
		LOGGER.info("SessionBean.create [" + this + "]");
	}

	@PostConstruct
	private void init() {
		LOGGER.info("SessionBean.init [" + this + "]");
	}

	public String hello() {
		String hello = "SessionBean.hello [" + this + "]";
		LOGGER.info(hello);
		return hello;
	}

	@PreDestroy
	private void exit() {
		LOGGER.info("SessionBean.exit [" + this + "]");
	}

}
