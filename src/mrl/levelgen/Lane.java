package mrl.levelgen;

import java.util.ArrayList;

public class Lane {
	private String laneName;
	private ArrayList barriers = new ArrayList();
	
	public Lane(String laneName){
		this.laneName = laneName;
	}
	
	public void addBarrier(String barrierId){
		barriers.add(barrierId);
	}
	
	public boolean hasBarrier(String barrierId){
		return barriers.contains(barrierId);
	}
	
	public String getLaneName(){
		return laneName;
	}
}
