package ch.sailcom.server.rest;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class SimpleTest extends JerseyTest {

	@Path("/sut")
	public static class HelloResource {
		@GET
		@Path("/hello")
		public String getHello(@QueryParam("name") String name) {
			return "Hello " + name + "!";
		}
	}

	@Override
	protected Application configure() {
		return new ResourceConfig(HelloResource.class);
	}

	@Test
	public void test() {
		final String hello = target("/sut/hello").queryParam("name", "Hannes").request().get(String.class);
		assertEquals("Hello Hannes!", hello);
	}

}
