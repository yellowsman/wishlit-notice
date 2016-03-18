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
	public int getCut_per() {
		return rate;
	}
	public String getItem_url() {
		return item_url;
	}
	public String getImage_url() {
		return image_url;
	}
	public Item(String name, int price, int cut_per, String item_url,
			String image_url) {
		super();
		this.name = name;
		this.price = price;
		this.rate = cut_per;
		this.item_url = item_url;
		this.image_url = image_url;
	}
	
	
	
	
}
