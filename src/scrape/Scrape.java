package scrape;

import java.util.ArrayList;
import java.util.List;

import config.Param;
import config.ScrapeParam;

public class Scrape {

	public static void main(String[] args) {
		Scrape s = new Scrape();
		ArrayList<ScrapeParam> splist = (ArrayList<ScrapeParam>) s.readConfig();
		for (ScrapeParam sp : splist) {
			ArrayList<Item> items = (ArrayList<Item>) s.scrape(sp);
			if (s.sendMail(items, sp.getAddress()) == false) {
				// 送信失敗処理
				// 再試行、再度失敗するならログに書き出すなど
			}
		}
	}

	List<Item> scrape(ScrapeParam sp) {
		// dummy
		String name = "";
		int price = 100;
		int rate = 100;
		String item_url = "";
		String image_url = "";
		

		ArrayList<Item> item_list = new ArrayList<Item>();

		for (Param p : sp.getParams()) {
			String html = p.getWishlistUrl();
			
			// jsoupでp.getUrl()で取得したURLに対してparse処理
			if(html.equals("") == false){
				name = parseName(html);
				price = parsePrice(html);
				rate = parseRate(html);
				item_url = parseItemUrl(html);
				image_url = parseImageUrl(html);
			}

			
			// modeによって判定を変える
			if (p.getMode().equals("price") && price <= p.getBorder()) {
				item_list.add(new Item(
						name, 
						price, 
						rate,
						item_url,
						image_url
				));
			} else if (p.getMode().equals("rate") && rate >= p.getBorder()) {
				item_list.add(new Item(
						name, 
						price, 
						rate,
						item_url, 
						image_url
				));
			}
		}

		return item_list;
	}

	boolean sendMail(List<Item> items, String address) {
		return false;

	}

	List<ScrapeParam> readConfig() {
		// Configクラスから取る
		ArrayList<ScrapeParam> sps = null; 
		
		return sps;
	}
	
	String parseName(String html){
		return null;
	}
	
	int parsePrice(String html){
		return 0;
	}
	
	int parseRate(String html){
		return 0;
	}
	
	String parseItemUrl(String html){
		return null;
	}
	
	String parseImageUrl(String html){
		return null;
	}

}
