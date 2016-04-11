package core;

import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import config.Config;
import config.ConfigDummy;
import config.Param;
import config.ScrapeParam;

public class Scraper {
	// TODO:
	// - cssQuery()を使って全件一致する方法を調べる(条件全てを使った検索)
	// - 割引率のみならず割引額の場合を検討する

	// MEMO:
	// - wishlistの固定リンクの末尾にパラメータでpage={番号}を付与すると遷移できる
	// - 例)
	// http://www.amazon.co.jp/registry/wishlist/3QBJIM8BYANLP/ref=cm_sw_r_tw_ws_r0h8wb0MXKGZD?page=2

	public static void main(String[] args) {
		Scraper s = new Scraper();
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
		WishListParser wp = new WishListParser();

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
					//item_list.add(new Item(name, price, rate, item_url, image_url));
				} else if (param.getMode().equals("rate") && rate >= param.getBorder()) {
					//item_list.add(new Item(name, price, rate, item_url, image_url));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return item_list;
	}

	public void scrape(ArrayList<WishList> wll) {
		for (WishList wl : wll) {
			scrape(wl);
		}
	}

	public void scrape(WishList wl) {
		WishListParser wlp = new WishListParser();
		String html;
		try {
			html = Jsoup.connect(wl.getWishListUrl()).get().html();
			Elements elements = wlp.parseItemList(html);

			HashMap<String, Boolean> namemap = new HashMap<String, Boolean>();
			for (Element e : elements) {
				namemap.put(wlp.parseName(e), false);
			}

			for (Item i : wl.getItems()) {
				namemap.replace(i.getName(), true);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (wl.hasUrl()) {

		}

	}

	// Wishlistを更新する関数
	// map処理のように、関数とリスト2つを渡して新しいリストを返す
	public ArrayList<Item> wishlistUpdate(ArrayList<Item> origin, ArrayList<Item> current) {
		ArrayList<Item> update = new ArrayList<Item>();

		// 2つのリストで別々の位置を管理
		Iterator<Item> oit = origin.iterator();
		Iterator<Item> cit = current.iterator();

		for (Item oitem = oit.next(); oit.hasNext() && cit.hasNext(); oitem = oit.next()) {
			
			// 今のアイテムリストに存在しなければスキップ
			if (current.contains(oitem) == false)
				continue;

			for (Item nitem = cit.next(); cit.hasNext(); nitem = cit.next()) {
				if (oitem.equals(nitem)) {
					// メモリ消費量が1つのWishlistで3倍に膨れ上がる(origin,curretn,update)
					// これでいいならこれでいいし、減らさないといけないなら減らす
					// if処理がインスタンス生成の時間よりも早いならifを噛ませてインスタンス数を減らす
					update.add(new Item(oitem.getName(), oitem.getOriginPrice(), nitem.getCurrentPrice(), oitem.getUpdateDate(), oitem.getImage_url(),
							oitem.getDetail_url()));
					break;

				} else {
					update.add(nitem);
				}
			}

		}
		
		return update;
	}
	
	

	// Wishlistからメールを送るべきアイテムを選び出す
	public ArrayList<Item> wishlistFilter(ArrayList<Item> list,int filter_price) {
		ArrayList<Item> pickup = new ArrayList<Item>();
		list.stream().filter(pri -> pri.getCurrentPrice() <= filter_price).map(i -> pickup.add(i));
		return pickup;
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
