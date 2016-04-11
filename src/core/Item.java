package core;

import java.util.Calendar;

public class Item {
	private String name;
	private int origin_price;
	private int current_price;
	private String detail_url;
	private String image_url;
	private Calendar update_date;
	
	public String getName() {
		return name;
	}
	public int getOriginPrice() {
		return origin_price;
	}
	public int getCurrentPrice(){
		return current_price;
	}
	public String getDetail_url() {
		return detail_url;
	}
	public String getImage_url() {
		return image_url;
	}
	public Calendar getUpdateDate() {
		return update_date;
	}
	
	public Item(String name, int origin_price, int current_price, Calendar update_date,String detail_url,
			String image_url) {
		this.name = name;
		this.origin_price = origin_price;
		this.current_price = current_price;
		this.update_date = update_date;
		this.detail_url = detail_url;
		this.image_url = image_url;
	}	
	
}
