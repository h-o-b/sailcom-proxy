package ch.sailcom.server.service.weld;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SessionScoped
public class SessionBean implements Serializable {

	private static final long serialVersionUID = 3618439009596702454L;

	private static Logger LOGGER = LoggerFactory.getLogger(SessionBean.class);

	public SessionBean() {
		LOGGER.info("SessionBean " + this);
	}

}
