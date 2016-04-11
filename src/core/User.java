package core;

import java.util.ArrayList;

public class User {
	private int userid;
	private String name;
	private String address;
	private int mode;
	private int condition;
	private ArrayList<WishList> wl;


	public User(int userid, String name, String address, int mode,
			int condition, ArrayList<WishList> wl) {
		super();
		this.userid = userid;
		this.name = name;
		this.address = address;
		this.mode = mode;
		this.condition = condition;
		this.wl = wl;
	}


	public int getUserid() {
		return userid;
	}


	public String getName() {
		return name;
	}


	public String getAddress() {
		return address;
	}


	public int getMode() {
		return mode;
	}


	public int getCondition() {
		return condition;
	}


	public ArrayList<WishList> getWl() {
		return wl;
	}
	
	

}
