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
				// ���M���s����
				// �Ď��s�A�ēx���s����Ȃ烍�O�ɏ����o���Ȃ�
			}
		}
	}

	List<Item> scrape(ScrapeParam sp) {
		String parse_mode = "";
		String parse_name = "";
		String parse_price = "";
		String parse_rate = "";
		String parse_item_url = "";
		String parse_image_url = "";
		
		ArrayList<Item> item_list = new ArrayList<Item>();
		
		for(Param p:sp.getParams()){
			// jsoup��p.getUrl()�Ŏ擾����URL�ɑ΂���parse����
			
			int price = 100;
			int rate = 100;
			// mode�ɂ���Ĕ����ς���
			if(p.getMode() == "price"){
				if(price <= p.getBorder()) {
					item_list.add(new Item());
				}
			}else{
				if(rate >= p.getBorder()){
					item_list.addAll(new Item());
				}
			}
		}
		
		return item_list;
	}

	boolean sendMail(List<Item> items, String address) {
		return false;

	}

	List<ScrapeParam> readConfig() {
		return null;
	}

}
