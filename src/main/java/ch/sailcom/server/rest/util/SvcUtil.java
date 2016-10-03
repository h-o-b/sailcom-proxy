package ch.sailcom.server.rest.util;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class SvcUtil {

	public static String getStackTrace() {
		List<StackTraceElement> stackTrace = Arrays.asList(new Throwable().getStackTrace());
		return stackTrace.stream().skip(1).map(s -> s.toString()).reduce(null, (a, b) -> (a != null ? a + "\n" : "") + b);
	}

	public static String getErrorEntity(String msg) {
		return "{ \"error\": \"" + msg + "\"}";
	}

	public static void check(boolean cond, String msg, int httpStatus) {
		if (cond) {
			return;
		}
		throw new WebApplicationException(Response.status(httpStatus).entity(getErrorEntity(msg)).build());
	}

	public static void check(boolean cond, String msg) {
		check(cond, msg, HttpURLConnection.HTTP_BAD_REQUEST);
	}

}
