package ch.sailcom.server.rest.util;

import static org.mockito.Mockito.mock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.sailcom.server.rest.App;

public class TestAppBase extends App {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestAppBase.class);

	private final HttpServletRequest request = mock(HttpServletRequest.class);
	private final HttpSession httpSession = mock(HttpSession.class);
	private final Map<String, Object> httpSessionAttributes = new ConcurrentHashMap<String, Object>();

	public TestAppBase() {

		LOGGER.debug("init servlet mocks");
		initMock();

		LOGGER.debug("register session classes");
		register(new AbstractBinder() {
			@Override
			protected void configure() {
				bind(request).to(HttpServletRequest.class);
			}
		});

	}

	private void initMock() {

		// Mock HttpServletRequest.getSession(boolean)
		Mockito.doAnswer(new Answer<HttpSession>() {
			@Override
			public HttpSession answer(InvocationOnMock invocation) throws Throwable {
				return httpSession;
			}
		}).when(request).getSession(Mockito.anyBoolean());

		// Mock HttpServletRequest.getSession()
		Mockito.doAnswer(new Answer<HttpSession>() {
			@Override
			public HttpSession answer(InvocationOnMock invocation) throws Throwable {
				return httpSession;
			}
		}).when(request).getSession();

		// Mock HttpSession.setAttribute
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				String key = invocation.getArgumentAt(0, String.class);
				Object value = invocation.getArgumentAt(1, Object.class);
				httpSessionAttributes.put(key, value);
				return null;
			}
		}).when(httpSession).setAttribute(Mockito.anyString(), Mockito.anyObject());

		// Mock HttpSession.getAttribute
		Mockito.doAnswer(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				String key = invocation.getArgumentAt(0, String.class);
				Object value = httpSessionAttributes.get(key);
				return value;
			}
		}).when(httpSession).getAttribute(Mockito.anyString());

	}

}
