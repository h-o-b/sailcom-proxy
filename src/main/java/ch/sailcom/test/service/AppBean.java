package ch.sailcom.test.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class AppBean {

	private static Logger LOGGER = LoggerFactory.getLogger(AppBean.class);

	public AppBean() {
		LOGGER.info("AppBean.create [" + this + "]");
	}

	@PostConstruct
	private void init() {
		LOGGER.info("AppBean.init [" + this + "]");
	}

	public String hello() {
		String hello = "AppBean.hello [" + this + "]";
		LOGGER.info(hello);
		return hello;
	}

	@PreDestroy
	private void exit() {
		LOGGER.info("AppBean.exit [" + this + "]");
	}

}
