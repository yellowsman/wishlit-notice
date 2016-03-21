package scrape;

import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import config.Config;
import config.ConfigDummy;
import config.Param;
import config.ScrapeParam;

// TODO:
//  - cssQuery()を使って全件一致する方法を調べる(条件全てを使った検索)
//  - 割引率のみならず割引額の場合を検討する

// MEMO:
//	- wishlistの固定リンクの末尾にパラメータでpage={番号}を付与すると遷移できる
//  - 例) http://www.amazon.co.jp/registry/wishlist/3QBJIM8BYANLP/ref=cm_sw_r_tw_ws_r0h8wb0MXKGZD?page=2

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

	public List<Item> scrape(ScrapeParam sparam) {
		// dummy
		String name = "";
		int price = 100;
		int rate = 100;
		String item_url = "";
		String image_url = "";
		ArrayList<Item> item_list = new ArrayList<Item>();
		WishListParse wp = new WishListParse();

		for (Param param : sparam.getParams()) {
			try {
				String html = Jsoup.connect(param.getWishlistUrl()).get().html();
				Elements elements = wp.parseItemList(html);

				// jsoupでp.getUrl()で取得したURLに対してparse処理
				for (Element el : elements) {
					name = wp.parseName(el);
					price = wp.parsePrice(el);
					rate = wp.parseRate(el);
					item_url = wp.parseItemUrl(el);
					image_url = wp.parseImageUrl(el);
				}

				// modeによって判定を変える
				if (param.getMode().equals("price") && price <= param.getBorder()) {
					item_list.add(new Item(name, price, rate, item_url,
							image_url));
				} else if (param.getMode().equals("rate") && rate >= param.getBorder()) {
					item_list.add(new Item(name, price, rate, item_url,
							image_url));
				}
			} catch (IOException e) {
				e.printStackTrace();
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
}
