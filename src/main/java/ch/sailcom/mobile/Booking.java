package ch.sailcom.mobile;

import java.io.Serializable;

public class Booking implements Serializable {

	private static final long serialVersionUID = 1L;

	public int shipId;
	public int harborId;
	public int lakeId;

	public String bookDate;
	public String bookTimeFrom;
	public String bookTimeTo;

	public int tripId;
	public boolean isMine;
	public String tripDateFrom;
	public String tripTimeFrom;
	public String tripDateTo;
	public String tripTimeTo;

	public String userName;
	public String userAdress;
	public String userPhone;
	public String userMobile;
	public String userMail;
	public String userComment;
	public String userHasImg;
	public String userImgUrl;

}
