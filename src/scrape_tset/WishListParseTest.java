package scrape_tset;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import scrape.WishListParse;

@RunWith(JUnit4.class)
public class WishListParseTest {
	final static String testcase_nextpage = "http://www.amazon.co.jp/registry/wishlist/3QBJIM8BYANLP/ref=cm_sw_r_tw_ws_jRX7wb0N6CX8K";
	final static String testcase_lowprice = "http://www.amazon.co.jp/registry/wishlist/3PM929GC0JA7B/ref=cm_sw_r_tw_ws_5RX7wb1CKD0HN";
	static Elements elements;
	static WishListParse wp;
	static String testcase;
	WishListParseTest wpt;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testcase = testcase_nextpage;
		wp = new WishListParse();
	}

	@Test
	public void testParseItemList() {
		try {
			elements = wp.parseItemList(Jsoup.connect(testcase).get().html());
			assertThat(elements.size(), is(25));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testParseName() {
		// 使えない文字やエスケープ文字の検査
		String name = wp.parseName(elements.first());
		assertThat(name,is("カンバン仕事術 ―チームではじめる見える化と​改善"));
		
	}

	@Test
	public void testParsePrice() {
		int price = wp.parsePrice(elements.first());
		assertThat(price,is(3888));
		
	}

	public void testParseRate() {
		fail("Not yet implemented");
	}

	@Test
	public void testParseItemUrl() {
		String url = wp.parseItemUrl(elements.first());
		assertThat(url, is("http://www.amazon.co.jp/" + "/dp/487311764X/ref=wl_it_dp_o_pC_nS_ttl?_encoding=UTF8&colid=3QBJIM8BYANLP&coliid=I3HVA8MVUC8IT2"));
	}

	@Test
	public void testParseImageUrl() {
		String url = wp.parseImageUrl(elements.first());
		assertThat(url, is("http://ecx.images-amazon.com/images/I/51CvAjXFHEL._SL500_SL135_.jpg"));
	}

	@Test
	public void testIsContainSellText() {
		assertFalse(wp.isContainSellText(elements.first()));
	}

	@Test
	public void testHasNextPage() {
		try {
			assertFalse(wp.hasNextPage(Jsoup.connect(testcase).get(),1));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
