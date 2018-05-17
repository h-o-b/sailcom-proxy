package ch.sailcom.session.domain;

import java.io.Serializable;

public class User implements Serializable /* CDI */ {

	private static final long serialVersionUID = 6017294354632164167L;

	public String id;
	public String name;
	public String role;

	public String ip;

}
