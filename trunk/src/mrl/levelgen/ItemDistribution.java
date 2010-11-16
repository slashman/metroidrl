package mrl.levelgen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import sz.util.Util;

public class ItemDistribution implements Serializable{
	private ArrayList<ItemDistributionDef> distribution = new ArrayList<ItemDistributionDef>();
	
	/**
	 * Adds certain kind of item to the distribution
	 * 
	 * @param itemId
	 * @param weight Weights of all items in the distribution must sum 100
	 */
	public void sum(String itemId, int weight){
		ItemDistributionDef idd = new ItemDistributionDef();
		idd.itemId = itemId;
		idd.weight = weight;
		distribution.add(idd);
	}
	
	public void fixWeights(){
		int acum = 0;
		for (ItemDistributionDef def : distribution) {
			acum += def.weight;
		}
		double rate = 100.0d / (double)acum;
		if (rate == 1)
			return;
		for (ItemDistributionDef def : distribution) {
			def.weight *= rate;
		}		
	}
	
	@SuppressWarnings("unchecked")
	public String getAnItem(){
		int pin = Util.rand(0,100);
		Collections.sort(distribution);
		fixWeights();
		int acum = 0;
		for (ItemDistributionDef def : distribution) {
			acum += def.weight;
			if (pin <= acum){
				return def.itemId;
			}
		}
		return null;
	}
	
	class ItemDistributionDef implements Comparable, Serializable{
		public String itemId;
		public int weight;
		
		public int compareTo(Object arg0) {
			ItemDistributionDef x = (ItemDistributionDef)arg0;
			return weight - x.weight;
		}
	}
}

