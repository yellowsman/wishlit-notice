package config;

public class Param {
	private String url;
	private String mode; // price or rate
	private int border;
	
	public Param(String url, String mode, int border) {
		this.url = url;
		this.mode = mode;
		this.border = border;
	}
	public String getUrl() {
		return url;
	}
	public String getMode() {
		return mode;
	}
	public int getBorder() {
		return border;
	}
	
}
