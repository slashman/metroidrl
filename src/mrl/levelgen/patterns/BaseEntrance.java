package mrl.levelgen.patterns;

import mrl.levelgen.MonsterSpawnInfo;

public class BaseEntrance extends StaticPattern
{
	public String getMapKey(){
		return "BASE1";
	}
	
	public int getPopulationAverage(){
		return 10;
	}
	
	public BaseEntrance () {
		cellMap = new String [][]{
		{
			"ttttttttttttttttttttttttttttttttttttttttttttt-ttttttttttttt-tttttttttttt",
			"tttttttttttttttttttxxxxxxxxxttttttttttttttttt-ttttttttttttt-tttttttttttt",
			"ttttttttttttttttttttttxxxxxxxxxxxxttttttttttt--tttttttttttt-tttttttttttt",
			"ttttttttttttttttttttttttttttttxxxxxxxxxxaxxxxt-tttttttttttt--ttttttttttt",
			"ttttttttttttttttttxxxxxxxxxxxxxaxxxxxxtttttttt-ttttttttttttt--t.tttttttt",
			"tttttttttttaxxxxxxxxxxxxxxxxxtttttttttttttttt--tttttttttttttt--....ttttt",
			"xxxxxtttttttxxxxxxxxxxxxxxxxxxxxxxxxxxx-------ttttttttttttx...--.......x",
			"txxxxtttxxxxxxxxxxxxxxxxxxxxxxxxxxxxx---xxxxxxxxxxxtttttt......--....xxx",
			"xtttttttttxxxxxxxxxxxxxxxxxxxxxxxxxx--xxxxxxxxxxxxxxxxtt........------xx",
			"xxxtttxxxxxxxxxxxxxxx..............--........xxxxxxttt.....ttt...tttt--x",
			"xxxxxxxxxxxxxx...................xx-xxxxxxxxxxxxxxxxxxxx.....ttt..tttSSS",
			"tttttttxxxxxxxxxxxxxxxx............-...xxxxxxxxxxxxxxx.....a....SSSSSS*S",
			"xxxxxxxxxxxxxxxxxx......a..........-xx.........................xS******S",
			"xxxxxxxxxxxxxx.....................-.....................tt.....S******S",
			"xxxaxx............................--............................S******S",
			"xxxxxxx...a..xxxxx................-............a..........SSSSSSS******S",
			"xxx.....xxxxxxx...................-...................xxxxS************S",
			"xxxxxxxxxxxxxxxxxx................-...x...................S************S",
			"xxxxxxxxxxxxxxx.................a.-...x................xxx*************S",
			"xxxxxxttttxxxxxx..................-..xx...................*************X",
			"xxxtttttttttx.....................-......................x*************S",
			"xxxxxxtt.x.xx.....................-....................xxxS************S",
			"...................................................xxxxxxxS************S",
			"Y......................................................xxxSSSSSSS******S",
			".............................................a.......xxxxxx.....S******S",
			"xx..xxxxxx...x.........a..........-...........a........x.......tS******S",
			"xxxxxxxxxx........................-...xx........................S******S",
			"xxxxxxxxxxxxx.....................-....xx..............xxxx.....SSSSSS*S",
			"xxxxxxxxxxx.......................-.....x...........xx...xxxxxxxxxxxxSSS",
			"xxxxxxxxxxxxxxxxx.................-........................xxxxxxxxxxxx-",
			"xxxxxxx....xxxx..................a--xxxx..xxxxxx...xxxxx....xx.....xxxx-",
			"xxxax........xxx...................--xxxxxx.........xxx.....xx..xttttxx-",
			"xxxxxxxxx...xxxxx............xxxxxxx--xxxxxxxx...........xxxxxxt...xxxx-",
			"xxxxxxxxxxxxxx...........xxxxxxxxxxxx--xxxxx..............xxxxxxx..xxxx-",
			"xxxxxxxxxxxxxxxxx.....xxxxxxxxxxxxxxxx--..................a..xxxxxxxx---",
			"xxxxxxxxxxxxxxxxx.xxxxxxxxxxxx...xxxxxx-xxxxxxxx.......xxxxxxxxxxtttx-xx",
			"xxxxxxxxxxxxxxxxxxxxxxxxxxxxx....xxxxxx-xxxxxxxxxxxxxxxxxxxxxxxxxxxx--xx",
			"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx-xxxxxxxxxaxxxxxxxxxxxxxxxxx--ttx",
			"ttttxxxxxxxxxxxttttttttxxxxxxxxxxxxxxtt---ttttttttttttttttttttttt---tttt",
			"ttttttttxxxxxxxxttttttttttttttttttttttttt--ttttttttttttttttttttt--tttttt",
			"ttttttttttttttattttttttttttttttttttttttttt--tttxxxxxxxxxxxxxxttt-ttttttt",
			"ttttttttttttttttttttttttttttttttttttttttttt--ttttttttttttttttttt-ttttttt"
			}
		};

	
		charMap.put(".", "ROCK");
		charMap.put("x", "ROCK1");
		charMap.put("t", "ROCK2");
		charMap.put("a", "TREE");
		charMap.put("-", "METALLIC_GRATE");
		charMap.put("S", "METALLIC_WALL");
		charMap.put("*", "METALLIC_FLOOR");
		charMap.put("Y", "ROCK EXIT SURFACE");
		charMap.put("X", "METALLIC_FLOOR EXIT DECKS");

		
		
	}

	public String getDescription() {
		return "Base Entrance";
	}
	
	public String[] getDwellers(){
		return new String[]{"GALACTIC_TROOPER", "GALACTIC_SOLDIER"};
	}

	public String getMusicKeyMorning() {
		return "SURFACE";
	}

	public String getMusicKeyNoon() {
		return null;
	}
	
}