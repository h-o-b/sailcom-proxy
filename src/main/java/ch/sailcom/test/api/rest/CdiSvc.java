package ch.sailcom.test.api.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.Conversation;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ch.sailcom.session.api.rest.Authenticated;
import ch.sailcom.test.service.AppBean;
import ch.sailcom.test.service.ConversationBean;
import ch.sailcom.test.service.RequestBean;
import ch.sailcom.test.service.SessionBean;

/**
 * Ship Service
 */
@Path("/test/cdi")
@Authenticated
public class CdiSvc {

	@Inject
	AppBean appBean;

	@Inject
	SessionBean sessionBean;

	@Inject
	Conversation conversation;

	@Inject
	ConversationBean conversationBean;

	@Inject
	RequestBean requestBean;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, String> getBeans() throws IOException {
		Map<String, String> beans = new HashMap<String, String>();
		beans.put("app", appBean.hello());
		beans.put("session", sessionBean.hello());
		beans.put("conversation", conversationBean.hello());
		if (!conversation.isTransient()) {
			beans.put("cid", conversationBean.getCid());
		}
		beans.put("request", requestBean.hello());
		return beans;
	}

	@GET
	@Path("/beginConversation")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, String> startConversation() throws IOException {
		conversationBean.beginConversation();
		return getBeans();
	}

	@GET
	@Path("/endConversation")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, String> stopConversation() throws IOException {
		conversationBean.endConversation();
		return getBeans();
	}

}
