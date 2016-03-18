package scrape;

public class Item {
	private String name;
	private int price;
	private int rate; //Š„ˆø—¦
	private String item_url;
	private String image_url;
	
	public String getName() {
		return name;
	}
	public int getPrice() {
		return price;
	}
	public int getRate() {
		return rate;
	}
	public String getItem_url() {
		return item_url;
	}
	public String getImage_url() {
		return image_url;
	}
	public Item(String name, int price, int rate, String item_url,
			String image_url) {
		super();
		this.name = name;
		this.price = price;
		this.rate = rate;
		this.item_url = item_url;
		this.image_url = image_url;
	}
	
	
	
	
}
