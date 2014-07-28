package ch.sailcom.mobile;

import java.io.Serializable;

public class Trip implements Serializable {

	private static final long serialVersionUID = 1L;

	public String tripId;

	public int shipId;
	public int harborId;
	public int lakeId;

	public String dateFrom;
	public String timeFrom;
	public String dateTo;
	public String timeTo;

}
