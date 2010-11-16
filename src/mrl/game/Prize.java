package mrl.game;

import java.io.Serializable;

public class Prize implements Serializable{
	private String priceId;
	
	public String getPriceId() {
		return priceId;
	}
	
	public Prize(String priceId) {
		this.priceId = priceId;
	}
}
