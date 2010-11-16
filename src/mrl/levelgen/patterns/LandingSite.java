package mrl.levelgen.patterns;

import mrl.cuts.Start;
import mrl.cuts.Unleasher;
import mrl.levelgen.MonsterSpawnInfo;

public class LandingSite extends StaticPattern
{
	public String getMapKey(){
		return "LANDING";
	}
	
	public LandingSite () {
		cellMap = new String [][]{
		{
			"tttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt",
			"ttttttttttttttt....xxxxxxxxxttttttttttttttttttttttttt.....tttttttttttttt",
			"ttttttttt............txxxxxxxxxxxxttttttttttttttttttttt...tttttttttttttt",
			"ttttttttttt..t.....tttttttttttxxxxxxxxxxxxxxxtttttttt....ttttttttttttttt",
			"ttt...ttttttttttttxxxxxxxxxxxxxxxxxxxxtttttttttttttttt.....ttttttttttttt",
			"ttttt...tttxxxxxxxxxxxxxxxxxxtttttttttttttttttttttttt........ttttttttttt",
			"xxxx..ttttttxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxttttttttttttxxxxxxxxxxxxxx",
			"txxxxtttxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxtttttttttxxxxxxxxxxxx",
			"xtttttttttxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxtttttttttttttttxxx",
			"xxxtttxxxxxxxxxxxxxxx........................xxxxxxttttttttttttttttttxxx",
			"xxxxxxxxxxxxxx............*......xxxxxxxxxxxxxxxxxxxxxxxttttttttt*tttxxx",
			"tttttttxxxxxxxxxxxxxxxx................xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"xxxxxxx*xxxxxxxxxx..................xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"xxxxxxxxxxxxxxxxxxxxxx...<.>................xxxxxxxxxxxtttttttttttxxxxxx",
			"xxxxxx.................<<<=>>>.................xxxxxxxxxxxxxxxxxxxxxxxxx",
			"xxxxxxx......xxxxx.....<1===2>...........*.xxxxxxxxxxxxxxxxxxxxttttxxxxx",
			"xxx.....xxxxxxx.......<<<=S=><<....xxxxxxxxxxxxxxxxxxx*xxxxxxxtttttxxxxx",
			"xxxxxxxxxxxxxxxxxx......<...>................xxxxxxxxxxxxxxxxxxxxxx*xxxx",
			"xxxxxxxxxxxxxxxxxxxx.......*.......xxx.................xxxxxxxxxxxxxxxxx",
			"xxxxxxttttxxxxxxxx..............xxxxxxxxxxxx.....xxxxxxxxxxxxxtttttxxxxx",
			"xxxtttttttttxxxxxxx...........xxxxxxxxxxxxxxxx...........xxxxxxxxxttxxxx",
			"xxxxxxtttxxxx*xxxxxxxxx.................xxxxxxxx.......xxxxxxxxxxxtttxxx",
			"xxxxxxxxxxxxxxxx......................*............xxxxxxxxxxxttttt*xxxx",
			"xxxxxxxxxxxx...............xxxxx...xxxx................xxxxxxxxxttttxxxx",
			"xxxx.*......................xxxxxxxxxxxxxxx..........xxxxxxxxxxxxtxxxxxx",
			"xxxxxxxxxxxxxx......................xxxxxxxxx..........x.......ttt......",
			"xxxxxxxxxx........xxxxxxxxxxxxxxxxxxxxxxxx.............................N",
			"xxx..xxxxxxxx.............*xxxxxxxxxxxxxx..............xxxx.............",
			"xxx...xxxxx..................................xxxxxxxxx...xxxxxxxxxxxxxxx",
			"xxxxxxxxx...............xxxxxxxxxxxxxxxx...................xxxxxxxxxxxxx",
			"xxxxxxx...........................xxxxxxxxxxxxxx...xxxxx....xxxxxxxxxxxx",
			"xxxxx................................xxxxxxxxxx.....xxx.....xxxxxttttxxx",
			"xxxxxxxxx....................xxxxxxxxxxxxxxxxx...........xx*xxxtttxxxxxx",
			"xxxxxxxxxxx*xx...........xxxxxxxxxxxxxxxxxxx..............xxxxxxxxxxxxxx",
			"xxxxx...xxxxxxxxx.....xxxx*xxxxxxxxxxxxx.....................xxxxxxxxxxx",
			"xxx...xxxxxxxxxxx.xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx.......xxxxxxxxxxtttxxxx",
			"xxxx....xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
			"xxx.......xxxxxxxxxxxxxxxxxxxxxx....xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxtttx",
			"tt...xxxxxxxxxxttttttttxxxxxxx...xxxxtttttttttttt...tttttttttttttttttttt",
			"ttttttttxxxxxxxxttttttttttttttttt...tttttttttttt....tttttttttttttttttttt",
			"tttttttttttttttttttttttttttttttttttttttttttttttxxxxxxxxxxxxxxttttttttttt",
			"tttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt",
			}
		};

	
		charMap.put(".", "ROCK");
		charMap.put("x", "ROCK1");
		charMap.put("t", "ROCK2");
		charMap.put("<", "GUNSHIP1");
		charMap.put(">", "GUNSHIP2");
		charMap.put("=", "GUNSHIP3");
		charMap.put("1", "GUNSHIP3 FEATURE RECOVER_ENERGY");
		charMap.put("2", "GUNSHIP3 FEATURE RECOVER_MISSILE");
		charMap.put("*", "TREE");
		charMap.put("S", "ROCK EXIT _BACK");
		charMap.put("N", "ROCK EXIT SURFACE");
		
		 unleashers = new Unleasher[]{new Start()};

		
	}
	
	public String[] getDwellers(){
		//return new String[] {"GALACTIC_TROOPER", "ZOOMER", "ZEELA", "CACATAC"};
		return new String[] {"ZOOMER", "ZEELA", "CACATAC"};
	}

	public String getDescription() {
		return "Landing Site";
	}

	public String getMusicKeyMorning() {
		return "SURFACE";
	}

	public String getMusicKeyNoon() {
		return null;
	}
	
	public int getPopulationAverage(){
		return 5;
	}
}