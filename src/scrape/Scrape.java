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

public class Scrape {
	final String url_domain = "http://www.amazon.co.jp/";

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

				// jsoup��p.getUrl()�Ŏ擾����URL�ɑ΂���parse����
				for (Element el : elements) {
					name = parseName(el);
					price = parsePrice(el);
					rate = parseRate(el);
					item_url = parseItemUrl(el);
					image_url = parseImageUrl(el);
				}

				// mode�ɂ���Ĕ����ς���
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

	// --------------------------------------
	// �p�[�X����
	// --------------------------------------
	Elements parseItemList(String html) {
		// wish-list�̃A�C�e����S�擾
		String selector_css = "a-fixed-left-grid   a-spacing-large";
		Document doc = Jsoup.parse(html);
		return doc.select(selector_css);
	}

	String parseName(Element element) {
		return element.select(".a-link-normal a-declarative").text();
	}

	int parsePrice(Element element) {
		return Integer.parseInt(element.select(
				".a-size-base a-color-price a-text-bold").text()); // ���z�܂Ŏ��o���Ă��Ȃ�
	}

	// ���������z�����f
	// ���z�������ꍇ�͌��̒l���犄�����Z�o���Ă���Ԃ�
	int parseRate(Element element) {
		String rate_word = element.select(".a-row itemPriceDrop").text();
		if (rate_word.contains("%")) {
			// sample => "�l�����肵�܂���: 9%"
			int p1 = rate_word.indexOf(":");
			int p2 = rate_word.indexOf("%");
			return Integer.parseInt(rate_word.substring(p1, p2 - 1).trim());
		} else {
			// sample => "�l�����肵�܂���: �� 1 �ق��������X�g�ɒǉ��������̉��i�́A��724 �ł���"
			int p1 = rate_word.indexOf("��");
			int p2 = rate_word.indexOf("��");
			int low_price = Integer.parseInt(rate_word.substring(p1, p2 - 1)
					.trim());

			int p3 = rate_word.lastIndexOf("��");
			int p4 = rate_word.indexOf("��");
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
		return str.contains("�l�����肵�܂���:");
	}

	// ���C�ɓ��胊�X�g�Ɏ��Ƀy�[�W�������true
	boolean isNextPage(Document doc) {
		return (doc.getElementById("wishlistPagination") != null);
	}
}
