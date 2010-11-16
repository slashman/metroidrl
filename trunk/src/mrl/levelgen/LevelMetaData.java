package mrl.levelgen;

import java.io.Serializable;
import java.util.ArrayList;
import mrl.game.Prize;

public class LevelMetaData implements Serializable{
	public String levelName;
	public String levelDescription;
	public String musicKey;
	public String defaultFloorCell;
	public String defaultWallCell;
	public ArrayList<String> exits = new ArrayList<String>();
	public ArrayList<String> exitDoors = new ArrayList<String>();
	public String[] dwellers;
	public ArrayList <Prize> prizes = new ArrayList<Prize>();
	public String facets;
	public ItemDistribution itemDistribution;

	public LevelMetaData(String pLevelName){
		levelName = pLevelName;
	}
	
	public void addNextRoom(String id){
		exits.add(id);
		exitDoors.add("TELEPOD");
	}
	
	public void addNextRoom(String id, String door){
		exits.add(id);
		exitDoors.add(door);
	}
	
	public void addPrize(Prize prize){
		prizes.add(prize);
	}
	
	
}
