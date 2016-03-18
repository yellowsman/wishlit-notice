package config;

public class Param {
	private String url;
	private String mode; // price or rate
	private int border;
	
	public Param(String wishlistUrl, String mode, int border) {
		this.url = wishlistUrl;
		this.mode = mode;
		this.border = border;
	}
	public String getWishlistUrl() {
		return url;
	}
	public String getMode() {
		return mode;
	}
	public int getBorder() {
		return border;
	}
	
}
