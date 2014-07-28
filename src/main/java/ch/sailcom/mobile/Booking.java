package ch.sailcom.mobile;

import java.io.Serializable;

public class Booking implements Serializable {

	private static final long serialVersionUID = 1L;

	public int shipId;
	public int harborId;
	public int lakeId;

	public String date;
	public String timeFrom;
	public String timeTo;
	public String dateFrom;
	public String dateTo;
	public int tripId;

	public String userName;
	public String userAdress;
	public String userPhone;
	public String userMobile;
	public String userMail;
	public String userComment;
	public String userHasImg;
	public String userImgUrl;

}
