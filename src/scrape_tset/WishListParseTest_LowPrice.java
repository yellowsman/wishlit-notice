package scrape_tset;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import scrape.WishListParse;

@RunWith(JUnit4.class)
public class WishListParseTest_LowPrice {
	final static String testcase_lowprice_otaku = "http://www.amazon.co.jp/registry/wishlist/3PM929GC0JA7B/ref=cm_sw_r_tw_ws_5RX7wb1CKD0HN";
	static Elements elements;
	static WishListParse wp;
	static String testcase;
	WishListParseTest_NextPage wpt;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testcase = testcase_lowprice_otaku;
		wp = new WishListParse();
	}

	@Test
	public void testParseItemList() {
		try {
			elements = wp.parseItemList(Jsoup.connect(testcase).userAgent("Mozzila").get().html());
			assertThat(elements.size(), is(8));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testParseName() {
		String name = wp.parseName(elements.get(2));
		assertThat(name,is("Mad Max: Fury Road (Blu-ray 3D + Blu-ray + DVD +UltraViolet)"));
	}

	@Test
	public void testParsePrice() {
		int price = wp.parsePrice(elements.get(2));
		assertThat(price,is(6120));
	}

	public void testParseRate() {
		fail("Not yet implemented");
	}

	@Test
	public void testParseItemUrl() {
		String url = wp.parseItemUrl(elements.get(6));
		assertThat(url, is("http://www.amazon.co.jp"
		+ "/dp/B000KKYUCO/"));
	}

	@Test
	public void testParseImageUrl() {
		String url = wp.parseImageUrl(elements.get(2));
		assertThat(url, is("http://ecx.images-amazon.com/images/I/510JTAHIuFL._SL500_SL135_.jpg"));
		
	}

	@Test
	public void testIsContainSellText() {
		assertTrue(wp.isContainSellText(elements.get(6)));
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
