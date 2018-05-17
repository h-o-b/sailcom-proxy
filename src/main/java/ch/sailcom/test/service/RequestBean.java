package ch.sailcom.test.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class RequestBean {

	private static Logger LOGGER = LoggerFactory.getLogger(RequestBean.class);

	public RequestBean() {
		LOGGER.info("RequestBean.create [" + this + "]");
	}

	@PostConstruct
	private void init() {
		LOGGER.info("RequestBean.init [" + this + "]");
	}

	public String hello() {
		String hello = "RequestBean.hello [" + this + "]";
		LOGGER.info(hello);
		return hello;
	}

	@PreDestroy
	private void exit() {
		LOGGER.info("RequestBean.exit [" + this + "]");
	}

}
