package mrl.data;

import mrl.level.Cell;
import mrl.ui.AppearanceFactory;

public class Cells {
	public static Cell [] getCellDefinitions(AppearanceFactory apf){

		Cell [] ret = new Cell [43];
		ret [0] = new Cell("ROCK", "rock", "Rock", apf.getAppearance("ROCK"));
		ret [1] = new Cell("ROCK1", "rock", "Rock (2)", apf.getAppearance("ROCK1")); ret[1].setHeight(1);
		ret [2] = new Cell("ROCK2", "rock", "Rock (4)", apf.getAppearance("ROCK2")); ret[2].setHeight(2);
		
		ret [3] = new Cell("GUNSHIP1", "gunship", "TXF-2 Combat Gunship", apf.getAppearance("GUNSHIP1"),true,true);
		ret [4] = new Cell("GUNSHIP2", "gunship", "TXF-2 Combat Gunship", apf.getAppearance("GUNSHIP2"),true,true);
		ret [5] = new Cell("GUNSHIP3", "gunship", "TXF-2 Combat Gunship", apf.getAppearance("GUNSHIP3"));
		ret [6] = new Cell("METALLIC_GRATE", "grate", "Metallic Grate (6)", apf.getAppearance("METALLIC_GRATE")); ret[6].setHeight(6);
		ret [7] = new Cell("METALLIC_WALL", "metallic wall", "Metallic Wall", apf.getAppearance("METALLIC_WALL"),true,true);
		ret [8] = new Cell("METALLIC_FLOOR", "metallic floor", "Metallic Floor", apf.getAppearance("METALLIC_FLOOR"));
		ret [30] = new Cell("METALLIC_FLOOR2", "metallic floor", "Metallic Floor (2)", apf.getAppearance("METALLIC_FLOOR2")); ret [30].setHeight(2);
		ret [31] = new Cell("METALLIC_FLOOR3", "metallic floor", "Metallic Floor (4)", apf.getAppearance("METALLIC_FLOOR3"));ret [31].setHeight(4);
		ret [9] = new Cell("GLASS_WINDOW", "glass window", "Big glass window", apf.getAppearance("GLASS_WINDOW"), true, false);
		ret [10] = new Cell("TELEPOD", "elevator", "Elevator", apf.getAppearance("TELEPOD"));
		ret [11] = new Cell("A_SIGN", "'A' sign", "'A' sign", apf.getAppearance("A_SIGN"));
		ret [12] = new Cell("T_SIGN", "'T' sign", "'T' sign", apf.getAppearance("T_SIGN"));
		ret [13] = new Cell("O_SIGN", "'O' sign", "'O' sign", apf.getAppearance("O_SIGN"));
		ret [14] = new Cell("B_SIGN", "'B' sign", "'B' sign", apf.getAppearance("B_SIGN"));
		ret [15] = new Cell("C_SIGN", "'C' sign", "'C' sign", apf.getAppearance("C_SIGN"));
		ret [16] = new Cell("H_SIGN", "'H' sign", "'H' sign", apf.getAppearance("H_SIGN"));
		ret [17] = new Cell("E_SIGN", "'E' sign", "'E' sign", apf.getAppearance("E_SIGN"));
		ret [18] = new Cell("G_SIGN", "'G' sign", "'G' sign", apf.getAppearance("G_SIGN"));
		ret [19] = new Cell("N_SIGN", "'N' sign", "'N' sign", apf.getAppearance("N_SIGN"));
		
		ret [20] = new Cell("CHOZO_RUIN", "chozo relic", "Ruins of a Chozo statue", apf.getAppearance("CHOZO_RUIN"));
		ret [21] = new Cell("LAVA", "lava", "Lava", apf.getAppearance("LAVA")); ret[21].setDamageOnStep(60);
		ret [22] = new Cell("METALLIC_HOLE", "metallic wall", "Metallic Wall", apf.getAppearance("METALLIC_WALL"),false,true);  
		ret [23] = new Cell("HIGH_FLOOR", "metallic floor", "Metallic Floor (6)", apf.getAppearance("HIGH_FLOOR")); ret[23].setHeight(4);
		ret [24] = new Cell("BREAKABLE_WALL", "metallic wall", "Metallic Wall", apf.getAppearance("METALLIC_WALL"),true,true);
		ret [25] = new Cell("BLUE_DOOR", "blue door", "Blue Gate", apf.getAppearance("BLUE_DOOR"),false,false);
		ret [26] = new Cell("MISSILE_DOOR", "pink gate", "Pink Gate", apf.getAppearance("MISSILE_DOOR"), true,true);
		ret [27] = new Cell("SUPERMISSILE_DOOR", "green gate", "Green Gate", apf.getAppearance("SUPERMISSILE_DOOR"), true,true);
		ret [28] = new Cell("POWERBOMB_DOOR", "yellow gate", "Yellow Gate", apf.getAppearance("POWERBOMB_DOOR"), true, true);
		
		ret [29] = new Cell("TREE", "alien tree", "Tree", apf.getAppearance("TREE"), true, true);
		
		ret [32] = new Cell("YELLOWISH_FLOOR", "metallic floor", "Metallic Floor", apf.getAppearance("YELLOWISH_FLOOR"));
		ret [33] = new Cell("GREENISH_FLOOR", "metallic floor", "Metallic Floor", apf.getAppearance("GREENISH_FLOOR"));
		ret [34] = new Cell("BLUEISH_FLOOR", "metallic floor", "Metallic Floor", apf.getAppearance("BLUEISH_FLOOR"));
		ret [35] = new Cell("PURPLISH_FLOOR", "metallic floor", "Metallic Floor", apf.getAppearance("PURPLISH_FLOOR"));
				
		//ret [36] = new Cell("YELLOWISH_WALL", "metallic wall", "Metallic Wall", apf.getAppearance("YELLOWISH_WALL"),true,true);
		ret [36] = new Cell("YELLOWISH_WALL", "metallic wall", "Metallic Wall", apf.getAppearance("YELLOWISH_WALL"),true,true);
		ret [37] = new Cell("GREENISH_WALL", "metallic wall", "Metallic Wall", apf.getAppearance("GREENISH_WALL"),true,true);
		ret [38] = new Cell("BLUEISH_WALL", "metallic wall", "Metallic Wall", apf.getAppearance("BLUEISH_WALL"),true,true);
		ret [39] = new Cell("PURPLISH_WALL", "metallic wall", "Metallic Wall", apf.getAppearance("PURPLISH_WALL"),true,true);
		
		ret [40] = new Cell("CRYSTAL_WALL", "crystal wall", "Crystal Wall", apf.getAppearance("CRYSTAL_WALL"),true,false);
		ret [41] = new Cell("RADIOACTIVE_AREA", "radioactive area", "Radioactivity Exposed Area", apf.getAppearance("RADIOACTIVE_AREA"));
		
		ret [42] = new Cell("WORKSTATION", "computer", "Computer workstation", apf.getAppearance("WORKSTATION"));
		return ret;
	}

}
