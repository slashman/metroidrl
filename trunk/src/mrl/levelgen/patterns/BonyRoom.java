package mrl.levelgen.patterns;

import mrl.cuts.BonyIntro;
import mrl.cuts.Unleasher;

public class BonyRoom extends StaticPattern
{
	public String getMapKey(){
		return "BASE1";
	}
	
	public BonyRoom (String returningLevel) {
		cellMap = new String [][]{
		{
			"SSSSSSSSSSSSSSSSS",
			"S...............S",
			"S.....R...R.....S",
			"S...............S",
			"S..R.........R..S",
			"S.......B.......S",
			"S..R.........R..S",
			"S...............S",
			"S.....R...R.....S",
			"S...............S",
			"S....R.....R....S",
			"S...............S",
			"S...............S",
			"S....R.....R....S",
			"S...............S",
			"S...............S",
			"S...............S",
			"S...R.......R...S",
			"S...............S",
			"S...............S",
			"S...R.......R...S",
			"S...............S",
			"SSSSSSSS.SSSSSSSS",
			"S...............S",
			"S...............S",
			"S.R...........R.S",
			"S...............S",
			"SSSSSSSSESSSSSSSS"
			}
		};

	
		charMap.put("S", "METALLIC_WALL");
		charMap.put(".", "METALLIC_FLOOR");
		charMap.put("R", "CHOZO_RUIN");
		charMap.put("E", "METALLIC_FLOOR EXIT "+returningLevel);
		charMap.put("B", "METALLIC_FLOOR MONSTER BONY");
		
		unleashers = new Unleasher[]{
			new BonyIntro()
		};
	}

	public String getDescription() {
		return "Rock'nRollie Room";
	}
	public int getPopulationAverage(){
		return 0;
	}
	public String getMusicKeyMorning() {
		return "TITLE";
	}

	public String getMusicKeyNoon() {
		return null;
	}
}
	
