package ch.sailcom.mobile.svc;

import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ch.sailcom.mobile.server.ServerSession;
import ch.sailcom.mobile.server.impl.ServerSessionImpl;

public class SvcUtil {

	private static final String SAILCOM_SESSION = "sailcomSession";
	private static Gson gs = null;

	public static ServerSession getServerSession(HttpSession clientSession) {  
		if (clientSession.getAttribute(SAILCOM_SESSION) == null) {
			clientSession.setAttribute(SAILCOM_SESSION, new ServerSessionImpl(clientSession));
		}
		return (ServerSession) clientSession.getAttribute(SAILCOM_SESSION);
	}

	public static boolean isId(String s) {  
		try {  
			@SuppressWarnings("unused")
			Integer d = Integer.parseInt(s);  
		} catch(NumberFormatException nfe) {  
			return false;  
		}
		return true;  
	}

	public static String toJson(Object o) {  
		if (gs == null) {
			gs = new GsonBuilder().setPrettyPrinting().create();
		}
		return gs.toJson(o);
	}

}
