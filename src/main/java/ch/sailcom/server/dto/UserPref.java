package ch.sailcom.server.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class UserPref implements Serializable /* for MapDb */ {

	private static final long serialVersionUID = -5997213792851823945L;

	public List<Integer> favoriteShips;

	public Map<Integer, Integer> starredShips;

}
