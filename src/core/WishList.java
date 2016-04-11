package core;

import java.util.ArrayList;

public class WishList {
	private String wishlist_url;
	private ArrayList<Item> items;
	
	public WishList(ArrayList<Item> items){
		this("",items);
		
	}
	public WishList(String wishlist_url, ArrayList<Item> items) {
		this.wishlist_url = wishlist_url;
		this.items = items;
	}

	public String getWishListUrl() {
		return wishlist_url;
	}

	public ArrayList<Item> getItems() {
		return items;
	}
	
	public boolean hasUrl(){
		return !wishlist_url.equals("");
	}
	
	public boolean addItem(Item item){
		return items.add(item);
	}
	
}
