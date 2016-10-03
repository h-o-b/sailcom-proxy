package ch.sailcom.server.service.cdi;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ConversationScoped
public class ConversationBean implements Serializable {

	private static final long serialVersionUID = 2653881470621390866L;

	private static Logger LOGGER = LoggerFactory.getLogger(ConversationBean.class);

	@Inject
	Conversation conversation;

	public ConversationBean() {
		LOGGER.info("ConversationBean.create [" + this + "]");
	}

	@PostConstruct
	private void init() {
		LOGGER.info("ConversationBean.init [" + this + "]");
	}

	public void beginConversation() {
		conversation.begin();
		LOGGER.info("ConversationBean.begin [" + this + "]: " + conversation.getId());
	}

	public String getCid() {
		return conversation.getId();
	}

	public String hello() {
		String hello = "ConversationBean.hello [" + this + "]";
		LOGGER.info(hello);
		return hello;
	}

	public void endConversation() {
		LOGGER.info("ConversationBean.end [" + this + "]: " + conversation.getId());
		conversation.end();
	}

	@PreDestroy
	private void exit() {
		LOGGER.info("ConversationBean.exit [" + this + "]");
	}

}
