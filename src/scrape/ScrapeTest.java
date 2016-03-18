package scrape;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import config.Param;
import config.ScrapeParam;

public class ScrapeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testScrape() {
		Scrape scrape = new Scrape();
		
		Param p1 = createParam("http://aaa.co.jp", "price", 0);
		Param p2 = createParam("http://bbb.co.jp", "price", 1000);
		Param p3 = createParam("http://ccc.co.jp", "rate", 10);
		Param p4 = createParam("http://ddd.co.jp", "rate", 100);
		Param p5 = createParam("http://eee.co.jp", "rate", 0);
		
		
		ArrayList<Param> params = new ArrayList<Param>();
		params.add(p1);
		params.add(p2);
		params.add(p3);
		params.add(p4);
		params.add(p5);
		
		
		ScrapeParam sp = createScrapeParam("testId", "hogehoge@mail.com", params);
		
		scrape.scrape(sp);
	}
	
	@Test
	public void testParse(){
		Scrape scrape = new Scrape();
		String html = "";
		
		
		
		
	}
	
	
	
	@Test
	public ScrapeParam createScrapeParam(String userId,String address,List<Param> params){
		return new ScrapeParam(userId,address,params);
	}
	
	@Test
	public Param createParam(String url,String mode,int border){
		return new Param(url,mode,border);
		
	}

}
