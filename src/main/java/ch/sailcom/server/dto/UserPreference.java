package ch.sailcom.server.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class UserPreference implements Serializable {

	private static final long serialVersionUID = 1918628163085421696L;

	public List<Integer> favoriteShips;

	public Map<Integer, Integer> starredShips;

}
