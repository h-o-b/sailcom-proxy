package ch.sailcom.mobile;

import java.io.Serializable;

public class Ship implements Serializable {

	private static final long serialVersionUID = 1L;

	public int id;
	public String name;
	public boolean isMine;

	public int harborId;
	public int lakeId;

	public boolean hasImg;
	public String nrPlate;
	public String type;
	public String location;
	public int pax;
	public String sailSize;
	public String length;

}
