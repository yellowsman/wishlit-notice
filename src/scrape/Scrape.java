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

public class Scrape {
	final String url_domain = "http://www.amazon.co.jp/";

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
			try {
				String html = Jsoup.connect(p.getWishlistUrl()).get().html();
				Elements elements = parseItemList(html);

				// jsoupでp.getUrl()で取得したURLに対してparse処理
				for (Element el : elements) {
					name = parseName(el);
					price = parsePrice(el);
					rate = parseRate(el);
					item_url = parseItemUrl(el);
					image_url = parseImageUrl(el);
				}

				// modeによって判定を変える
				if (p.getMode().equals("price") && price <= p.getBorder()) {
					item_list.add(new Item(name, price, rate, item_url,
							image_url));
				} else if (p.getMode().equals("rate") && rate >= p.getBorder()) {
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

	// --------------------------------------
	// パース処理
	// --------------------------------------
	Elements parseItemList(String html) {
		// wish-listのアイテムを全取得
		String selector_css = "a-fixed-left-grid   a-spacing-large";
		Document doc = Jsoup.parse(html);
		return doc.select(selector_css);
	}

	String parseName(Element element) {
		return element.select(".a-link-normal a-declarative").text();
	}

	int parsePrice(Element element) {
		return Integer.parseInt(element.select(
				".a-size-base a-color-price a-text-bold").text()); // 金額まで取り出せていない
	}

	// 割合か金額か判断
	// 金額だった場合は元の値から割合を算出してから返す
	int parseRate(Element element) {
		String rate_word = element.select(".a-row itemPriceDrop").text();
		if (rate_word.contains("%")) {
			// sample => "値下がりしました: 9%"
			int p1 = rate_word.indexOf(":");
			int p2 = rate_word.indexOf("%");
			return Integer.parseInt(rate_word.substring(p1, p2 - 1).trim());
		} else {
			// sample => "値下がりしました: ￥ 1 ほしい物リストに追加した時の価格は、￥724 でした"
			int p1 = rate_word.indexOf("￥");
			int p2 = rate_word.indexOf("ほ");
			int low_price = Integer.parseInt(rate_word.substring(p1, p2 - 1)
					.trim());

			int p3 = rate_word.lastIndexOf("￥");
			int p4 = rate_word.indexOf("で");
			int origin_price = Integer.parseInt(rate_word.substring(p3, p4 - 1)
					.trim());

			return (low_price * 100) / origin_price;
		}
	}

	String parseItemUrl(Element element) {
		return element.select(".a-link-normal a-declarative").attr("href");
	}

	String parseImageUrl(Element element) {
		return element.select(".a-link-normal a-declarative").first()
				.attr("src");
	}

	boolean isContainSellText(String str) {
		return str.contains("値下がりしました:");
	}

	// お気に入りリストに次にページがあればtrue
	boolean isNextPage(Document doc) {
		return (doc.getElementById("wishlistPagination") != null);
	}
}
