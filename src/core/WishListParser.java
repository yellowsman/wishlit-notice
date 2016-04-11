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
		// wish-list�̃A�C�e����S�擾
		String selector_css = ".a-fixed-left-grid.a-spacing-large";
		Document doc = Jsoup.parse(html);
		return doc.select(selector_css);
	}

	public String parseName(Element element) {
		return element.select(".a-link-normal.a-declarative").attr("title");
	}

	public int parsePrice(Element element) {
		// "��"��","�Ɨ]���ȋ󔒂�����
		return Integer.parseInt(element
				.select(".a-size-base.a-color-price.a-text-bold").text()
				.replaceAll("��|,", "").trim());
	}

	// ���������z�����f
	// ���z�������ꍇ�͌��̒l���犄�����Z�o���Ă���Ԃ�
	public int parseRate(Element element) {
		String rate_word = element.select(".a-row.itemPriceDrop").text();
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

	public String parseItemUrl(Element element) {
		return url_domain
				+ element.select(".a-link-normal.a-declarative").first()
						.attr("href").substring(0, 15);
	}

	public String parseImageUrl(Element element) {
		return element.select("img").first().attr("src");
	}

	// �������ǂ̒l�������Ă������s��
	// ����Ȃ�Element�̕����}�V
	public boolean isContainSellText(Element element) {
		System.out.println(element.html());
		String str = element.select(".a-row.itemPriceDrop").text();
		int siz = element.select(".a-row.itemPriceDrop").size();
		System.out.println(siz);
		System.out.println(str);

		return str.contains("�l�����肵�܂���:");
	}

	// ���C�ɓ��胊�X�g�Ɏ��Ƀy�[�W�������true
	// action="pag-trigger"�̐��𐔂���
	// �����ɔԍ���n���āA���̒l+1�̈ʒu��"pag-trigger"�������true
	// �Ȃ����(�v�f�����I�[�o�[����)�Ȃ�false
	public boolean hasNextPage(Document doc, int num) {
		if (num <= 0)
			return false;
		Elements els = doc.getElementsByAttributeValue("data-action",
				"pag-trigger");
		return (els.size() > 0 && els.size() <= num);
	}
}
