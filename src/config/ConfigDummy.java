package config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigDummy implements Config {
	
	@Override
	public List<ScrapeParam> readParams() {
		List<ScrapeParam> sps = new ArrayList<ScrapeParam>();
		
		Param p = new Param("www.dummy.co.jp","price",100);
		ScrapeParam sp = new ScrapeParam(
				"dummy", 
				"dummy@email.com", 
				new ArrayList<Param>(Arrays.asList(p))
				);
		sps.add(sp);
		
		return sps;
	}
}
