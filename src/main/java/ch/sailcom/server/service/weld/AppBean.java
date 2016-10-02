package ch.sailcom.server.service.weld;

import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class AppBean {

	private static Logger LOGGER = LoggerFactory.getLogger(AppBean.class);

	public AppBean() {
		LOGGER.info("AppBean " + this);
	}

}
