package ch.sailcom.server.model;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class UserPref implements Serializable /* for MapDb */ {

	private static final long serialVersionUID = -5997213792851823945L;

	public Set<Integer> favoriteShips;

	public Map<Integer, Integer> ratedShips;

}
