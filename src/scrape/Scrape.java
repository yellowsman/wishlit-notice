package scrape;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import config.Config;
import config.ConfigDummy;
import config.Param;
import config.ScrapeParam;

// TODO:
//  - cssQuery()を使って全件一致する方法を調べる(条件全てを使った検索)
//  - 割引率のみならず割引額の場合を検討する


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

	
	
	// --------------------------------------
	// メール送信処理
	// --------------------------------------
	boolean sendMail(List<Item> items, String address) {
		return false;
	}
	
	
	// --------------------------------------
	// パラメータ読み込み処理
	// --------------------------------------
	List<ScrapeParam> readConfig() {
		Config cd = new ConfigDummy();
		return cd.readParams();
	}
	
	// --------------------------------------
	// パース処理
	// --------------------------------------
	Elements parseItems(){
		// wish-listのアイテムを全取得
		String selector_css = "a-fixed-left-grid   a-spacing-large";
		return null;
	}
	
	String parseName(String html){
		Document doc = Jsoup.parse(html);
		return doc.select(".a-link-normal a-declarative").text();
	}
	
	int parsePrice(String html){
		Document doc = Jsoup.parse(html);
		return Integer.parseInt(doc.select(".a-size-base a-color-price a-text-bold").text()); // 金額まで取り出せていない
	}
	
	// 割合か金額か判断
	// 金額だった場合は元の値から割合を算出してから返す
	int parseRate(String html){
		Document doc = Jsoup.parse(html);
		return Integer.parseInt(doc.select(".a-row itemPriceDrop").text());
	}
	
	String parseItemUrl(String html){
		Document doc = Jsoup.parse(html);
		return null;
	}
	
	String parseImageUrl(String html){
		Document doc = Jsoup.parse(html);
		return null;
	}
	
	boolean isContainSellText(String str){
		return str.contains("値下がりしました:");
	}
	
	// お気に入りリストに次にページがあればtrue
	boolean isNextPage(Document doc){
		return (doc.getElementById("wishlistPagination") != null);		
	}
}
