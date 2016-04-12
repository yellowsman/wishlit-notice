package core;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WishListParser {
	private final String url_domain = "http://www.amazon.co.jp";
	private String html;

	public Elements parseItemList(String html) {
		// wish-listのアイテムを全取得
		String selector_css = ".a-fixed-left-grid.a-spacing-large";
		Document doc = Jsoup.parse(html);
		return doc.select(selector_css);
	}

	public String parseName(Element element) {
		return element.select(".a-link-normal.a-declarative").attr("title");
	}

	public int parsePrice(Element element) {
		// "￥"と","と余分な空白を除く
		return Integer.parseInt(element
				.select(".a-size-base.a-color-price.a-text-bold").text()
				.replaceAll("￥|,", "").trim());
	}

	// 割合か金額か判断
	// 金額だった場合は元の値から割合を算出してから返す
	public int parseRate(Element element) {
		String rate_word = element.select(".a-row.itemPriceDrop").text();
		if (rate_word.contains("%")) {
			// sample => "値下がりしました: 9%"
			int p1 = rate_word.indexOf(":");
			int p2 = rate_word.indexOf("%");
			return Integer.parseInt(rate_word.substring(p1, p2 - 1).trim());
		} else {
			// sample => "値下がりしました: ￥ 1 ほしい物リストに追加した時の価格は、￥724 でした"
			int p1 = rate_word.indexOf("￥");
			int p2 = rate_word.indexOf("ほ"); // 値段の次の文字の位置を抜き出す
			int low_price = Integer.parseInt(rate_word.substring(p1, p2 - 1)
					.trim());

			int p3 = rate_word.lastIndexOf("￥");
			int p4 = rate_word.indexOf("で"); // 値段の次の位置を抜き出す
			int origin_price = Integer.parseInt(rate_word.substring(p3, p4 - 1)
					.trim());

			return (low_price * 100) / origin_price;
		}
	}

	public String parseItemUrl(Element element) {
		return url_domain
				+ element.select(".a-link-normal.a-declarative").first()
						.attr("href").substring(0, 15);
	}

	public String parseImageUrl(Element element) {
		return element.select("img").first().attr("src");
	}

	// 引数がどの値をもっていくか不明
	// これならElementの方がマシ
	public boolean isContainSellText(Element element) {
		System.out.println(element.html());
		String str = element.select(".a-row.itemPriceDrop").text();
		int siz = element.select(".a-row.itemPriceDrop").size();
		System.out.println(siz);
		System.out.println(str);

		return str.contains("値下がりしました:");
	}

	// お気に入りリストに次にページがあればtrue
	// action="pag-trigger"の数を数える
	// 引数に番号を渡して、その値+1の位置の"pag-trigger"があればtrue
	// なければ(要素数をオーバーする)ならfalse
	public boolean hasNextPage(Document doc, int num) {
		if (num <= 0)
			return false;
		Elements els = doc.getElementsByAttributeValue("data-action",
				"pag-trigger");
		return (els.size() > 0 && els.size() <= num);
	}
}
