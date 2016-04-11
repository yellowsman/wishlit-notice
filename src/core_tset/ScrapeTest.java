package core_tset;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import config.Param;
import config.ScrapeParam;
import core.Scraper;

public class ScrapeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testScrape() {
		Scraper scrape = new Scraper();
		
		Param p1 = createParam("http://aaa.co.jp", "price", 0);
		Param p2 = createParam("http://bbb.co.jp", "price", 1000);
		Param p3 = createParam("http://ccc.co.jp", "rate", 10);
		Param p5 = createParam("http://eee.co.jp", "rate", 0);
		
		
		ArrayList<Param> params = new ArrayList<Param>();
		params.add(p1);
		params.add(p2);
		params.add(p3);
		params.add(p5);
		
		
		ScrapeParam sp = createScrapeParam("testId", "hogehoge@mail.com", params);
		
		scrape.scrape(sp);
	}
	
	
	
	public ScrapeParam createScrapeParam(String userId,String address,List<Param> params){
		return new ScrapeParam(userId,address,params);
	}
	

	public Param createParam(String url,String mode,int border){
		return new Param(url,mode,border);
		
	}

}
