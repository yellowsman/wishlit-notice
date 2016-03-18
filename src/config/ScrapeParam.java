package config;

import java.util.List;

public class ScrapeParam {
	private String userId;
	private String address;
	private List<Param> params;
	
	public ScrapeParam(String userId,String address,List<Param> params){
		this.userId = userId;
		this.address = address;
		this.params = params;
	}
	
	public String getUserId() {
		return userId;
	}
	public String getAddress() {
		return address;
	}
	public List<Param> getParams() {
		return params;
	}

}