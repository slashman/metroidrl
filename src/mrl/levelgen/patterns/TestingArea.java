package mrl.levelgen.patterns;

public class TestingArea  extends StaticPattern
{
	public String getMapKey(){
		return "BASE1";
	}
	
	public TestingArea () {
		cellMap = new String [][]{
		{
			"ttttttttttttttttttttttttttttttttttttttttttttt-ttttttttttttt-tttttttttttt",
			"tttttttttttttttttttxxxxxxxxxttttttttttttttttt-ttttttttttttt-tttttttttttt",
			"ttttttttttttttttttttttxxxxxxxxxxxxttttttttttt--tttttttttttt-tttttttttttt",
			"ttttttttttttttttttttttttttttttxxxxxxxxxxxxxxxt-tttttttttttt--ttttttttttt",
			"ttttttttttttttttttxxxxxxxxxxxxxxxxxxxxtttttttt-ttttttttttttt--t.tttttttt",
			"tttttttttttxxxxxxxxxxxxxxxxxxtttttttttttttttt--tttttttttttttt--....ttttt",
			"xxxxxtttttttxxxxxxxxxxxxxxxxxxxxxxxxxxx-------ttttttttttttx.S.--.......x",
			"txxxxtttxxxxxxxxxxxxxxxxxxxxxxxxxxxxx---xxxxxxxxxxxtttttt...S..--....xxx",
			"xtttttttttxxxxxxxxxxxxxxxxxxxxxxxxxx--xxxxxxxxxxxxxxxxtt....P...------xx",
			"xxxtttxxxxxxxxxxxxxxx..............--........xxxxxxttt.....tSt...tttt--x",
			"xxxxxxxxxxxxxx...................xx-xxxxxxxxxxxxxxxxxxxx.....ttt..tttSSS",
			"tttttttxxxxxxxxxxxxxxxx............-...xxxxxxxxxxxxxxx..........SSSSSS*S",
			"xxxxxxxxxxxxxxxxxx.................-xx.........................xS******S",
			"xxxxxxxxxxxxxx.....................-.....................tt.....S******S",
			"xxxxxx............................--............................S******S",
			"xxxxxxx......xxxxx.....ShSSSSSIS..-.......................SSSSSSS******S",
			"xxx.....xxxxxxx........ShSSS......-...................xxxxS**gggggggg**S",
			"xxxxxxxxxxxxxxxxxx.....ShhSS......-...x.....HH............S**gggggggg**S",
			"xxxxxxxxxxxxxxx........SShSS......-...x.....HH.........xxx*************S",
			"xxxxxxttttxxxxxx.......SShSSSSMS..-..xx.....HH............*************S",
			"xxxtttttttttx..........thhtt......-......................x*************S",
			"xxxxxxtt.x.xx..........thttt......-....................xxxS**gggggggg**S",
			"........................................H..........xxxxxxxS**gggggggg**S",
			"Y.......................................H..............xxxSSSSSSS******S",
			"...................................m....H............xxxxxx.....S******S",
			"xx..xxxxxx...x.........................................x.......tS******S",
			"xxxxxxxxxx............................xx........................S******S",
			"xxxxxxxxxxxxx..........................xx..............xxxx.....SSSSSS*S",
			"xxxxxxxxxxx...........lll....ABCG.......x.....RRR...xx...xxxxxxxxxxxxSSS",
			"xxxxxxxxxxxxxxxxx....lllll.....................RRSR........xxxxxxxxxxxx-",
			"xxxxxxx....xxxx.......lll.................xxxxxxRSRxxxxx....xx.....xxxx-",
			"xxxxx........xxx.....lll........................RRR.xxx.....xx..xttttxx-",
			"xxxxxxxxx...xxxxx....ll.....12345678.........x...........xxxxxxt...xxxx-",
			"xxxxxxxxxxxxxx.......ll...........................gggg....xxxxxxx..xxxx-",
			"xxxxxxxxxxxxxxxxx.....xxxxxxxxxxxxxxxx--..........gggg.......xxxxxxxx---",
			"xxxxxxxxxxxxxxxxx.xxxxxxxxxxxx...xxxxxx-xxxxxxxx.......xxxxxxxxxxtttx-xx",
			"xxxxxxxxxxxxxxxxxxxxxxxxxxxxx....xxxxxx-xxxxxxxxxxxxxxxxxxxxxxxxxxxx--xx",
			"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx-xxxxxxxxxxxxxxxxxxxxxxxxxxx--ttx",
			"ttttxxxxxxxxxxxttttttttxxxxxxxxxxxxxxtt---ttttttttttttttttttttttt---tttt",
			"ttttttttxxxxxxxxttttttttttttttttttttttttt--ttttttttttttttttttttt--tttttt",
			"tttttttttttttttttttttttttttttttttttttttttt--tttxxxxxxxxxxxxxxttt-ttttttt",
			"ttttttttttttttttttttttttttttttttttttttttttt--ttttttttttttttttttt-ttttttt"
			}
		};

	
		charMap.put(".", "ROCK");
		charMap.put("m", "ROCK MONSTER RED_ANT");
		charMap.put("x", "ROCK1");
		charMap.put("t", "ROCK2");
		charMap.put("-", "METALLIC_GRATE");
		charMap.put("S", "METALLIC_WALL");
		charMap.put("*", "METALLIC_FLOOR");
		charMap.put("Y", "ROCK EXIT _BACK");
		charMap.put("l", "LAVA");
		charMap.put("h", "METALLIC_HOLE");
		charMap.put("M", "MISSILE_DOOR");
		charMap.put("I", "SUPERMISSILE_DOOR");
		charMap.put("H", "HIGH_FLOOR");
		charMap.put("R", "BREAKABLE_WALL");
		charMap.put("P", "POWERBOMB_DOOR");
		charMap.put("g", "METALLIC_FLOOR FEATURE BIG_ENERGY");
		charMap.put("1", "METALLIC_FLOOR FEATURE THERMOVISOR");
		charMap.put("2", "METALLIC_FLOOR FEATURE GRAVITY_SUIT");
		charMap.put("3", "METALLIC_FLOOR FEATURE MORPH_ENERGY");
		charMap.put("4", "METALLIC_FLOOR FEATURE MISSILE_TANK");
		charMap.put("5", "METALLIC_FLOOR FEATURE SUPERMISSILE_TANK");
		charMap.put("6", "METALLIC_FLOOR FEATURE HI_JUMP");
		charMap.put("7", "METALLIC_FLOOR FEATURE BOMB_ENERGY");
		charMap.put("8", "METALLIC_FLOOR FEATURE POWER_BOMB_TANK");
		charMap.put("9", "METALLIC_FLOOR FEATURE XRAYVISOR");
		charMap.put("0", "METALLIC_FLOOR FEATURE POWER_BOMB_TANK");
		
		charMap.put("A", "METALLIC_FLOOR FEATURE SCREW_ATTACK");
		charMap.put("B", "METALLIC_FLOOR FEATURE SPACE_JUMP");
		charMap.put("C", "METALLIC_FLOOR FEATURE SPRINGBALL");
		charMap.put("G", "METALLIC_FLOOR FEATURE SPIDERBALL");
	}
	public int getPopulationAverage(){
		return 0;
	}
	public String getDescription() {
		return "Testing Testing";
	}

	public String getMusicKeyMorning() {
		return "";
	}

	public String getMusicKeyNoon() {
		return null;
	}
	
}