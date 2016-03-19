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
//  - cssQuery()���g���đS����v������@�𒲂ׂ�(�����S�Ă��g��������)
//  - �������݂̂Ȃ炸�����z�̏ꍇ����������


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
			
			// jsoup��p.getUrl()�Ŏ擾����URL�ɑ΂���parse����
			if(html.equals("") == false){
				name = parseName(html);
				price = parsePrice(html);
				rate = parseRate(html);
				item_url = parseItemUrl(html);
				image_url = parseImageUrl(html);
			}

			
			// mode�ɂ���Ĕ����ς���
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
	Elements parseItems(){
		// wish-list�̃A�C�e����S�擾
		String selector_css = "a-fixed-left-grid   a-spacing-large";
		return null;
	}
	
	String parseName(String html){
		Document doc = Jsoup.parse(html);
		return doc.select(".a-link-normal a-declarative").text();
	}
	
	int parsePrice(String html){
		Document doc = Jsoup.parse(html);
		return Integer.parseInt(doc.select(".a-size-base a-color-price a-text-bold").text()); // ���z�܂Ŏ��o���Ă��Ȃ�
	}
	
	// ���������z�����f
	// ���z�������ꍇ�͌��̒l���犄�����Z�o���Ă���Ԃ�
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
		return str.contains("�l�����肵�܂���:");
	}
	
	// ���C�ɓ��胊�X�g�Ɏ��Ƀy�[�W�������true
	boolean isNextPage(Document doc){
		return (doc.getElementById("wishlistPagination") != null);		
	}
}
