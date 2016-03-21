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
//  - cssQuery()���g���đS����v������@�𒲂ׂ�(�����S�Ă��g��������)
//  - �������݂̂Ȃ炸�����z�̏ꍇ����������

// MEMO:
//	- wishlist�̌Œ胊���N�̖����Ƀp�����[�^��page={�ԍ�}��t�^����ƑJ�ڂł���
//  - ��) http://www.amazon.co.jp/registry/wishlist/3QBJIM8BYANLP/ref=cm_sw_r_tw_ws_r0h8wb0MXKGZD?page=2

public class Scrape {
	

	public static void main(String[] args) {
		Scrape s = new Scrape();
		ArrayList<ScrapeParam> splist = (ArrayList<ScrapeParam>) s.readConfig();
		for (ScrapeParam sp : splist) {
			ArrayList<Item> items = (ArrayList<Item>) s.scrape(sp);
			if (s.sendMail(items, sp.getAddress()) == false) {
				// ���M���s����
				// �Ď��s�A�ēx���s����Ȃ烍�O�ɏ����o���Ȃ�
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

				// jsoup��p.getUrl()�Ŏ擾����URL�ɑ΂���parse����
				for (Element el : elements) {
					name = wp.parseName(el);
					price = wp.parsePrice(el);
					rate = wp.parseRate(el);
					item_url = wp.parseItemUrl(el);
					image_url = wp.parseImageUrl(el);
				}

				// mode�ɂ���Ĕ����ς���
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
	// ���[�����M����
	// --------------------------------------
	boolean sendMail(List<Item> items, String address) {
		return false;
	}

	// --------------------------------------
	// �p�����[�^�ǂݍ��ݏ���
	// --------------------------------------
	List<ScrapeParam> readConfig() {
		Config cd = new ConfigDummy();
		return cd.readParams();
	}
}
