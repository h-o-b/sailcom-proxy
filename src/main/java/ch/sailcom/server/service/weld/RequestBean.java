package ch.sailcom.server.service.weld;

import javax.enterprise.context.RequestScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class RequestBean {

	private static Logger LOGGER = LoggerFactory.getLogger(RequestBean.class);

	public RequestBean() {
		LOGGER.info("RequestBean " + this);
	}

}
