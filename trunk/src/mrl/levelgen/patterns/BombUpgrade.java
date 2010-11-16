package mrl.levelgen.patterns;

import mrl.levelgen.MonsterSpawnInfo;

public class BombUpgrade extends StaticPattern
{
	public String getMapKey(){
		return "BASE1";
	}
	public int getPopulationAverage(){
		return 0;
	}
	public BombUpgrade (String returningLevel) {
		cellMap = new String [][]{
		{
			"tt----------------------------------------------------------------------",
			"t--*********************************************************************",
			"t-**********************************************************************",
			"--***********SSSSSSSWSSSSSSSSSSSSWWSSSSSSSSSSSSSSWSSSSSSS***************",
			"-***********SS*****ATO*******S********S*********BAC*****SS**************",
			"-***********S****************S********S******************S**************",
			"-**********SS****************S********S******************SS*************",
			"-*********SS*****************S********S*******************SS************",
			"-*********S******************S********S********************S************",
			"-*********S*********1********S********S**********2*********S************",
			"-*********S******************S********S********************S*********SSS",
			"-*********W************************************************W****SSSSSS*S",
			"-*********S******************S********S********************S****S******S",
			"-*********S******************S********S********************S****S******S",
			"-*********S******************S********S********************S****S******S",
			"-*********S******************S********S*******************SSSSSSS******S",
			"SSSSSSSSSSSSSSWSSSSSSSSSSSSSSSSS****SSSSSSSSSSSSSSSSSSSSSSS*****SSSSSSSS",
			"S**********************************************************************S",
			"S**********************************************************************S",
			"X**********************************************************************S",
			"S**********************************************************************S",
			"S**********************************************************************S",
			"SSSSSSSSSSSSSSWSSSSSSSSSSSSSSSSS****SSSSSSSSSSSSSSSSSSSSSSS*****SSSSSSSS",
			"-*********S******************S*********S******************SSSSSSS******S",
			"-*********S******************S*********S*******************S****S******S",
			"-*********S******************S*****************************S****S******S",
			"-*********S****************************S*******************S****S******S",
			"-*********S******************S*********S*******************W****SSSSSS*S",
			"-*********S******************S*********S*******************S*********SSS",
			"-*********S*********3********S*********S*********4*********S************",
			"-*********S******************S*********S*******************S************",
			"-*********SS*****************S*********S******************SS************",
			"-**********SS****************S*********S*****************SS*************",
			"-***********S****************S*********S*****************S**************",
			"-***********SS*****CHE*******S*********S********GEN*****SS**************",
			"-************SSSSSSSWSSSSSSSSSSSSSSSSSSSSSSSSSSSSWSSSSSSS***************",
			"-***********************************************************************",
			"-***********************************************************************",
			"-***********************************************************************",
			"-***********************************************************************",
			"--**********************************************************************",
			"*-----------------------------------------------------------------------"
			}
		};

	
		charMap.put(".", "ROCK");
		charMap.put("x", "ROCK1");
		charMap.put("t", "ROCK2");
		charMap.put("-", "METALLIC_GRATE");
		charMap.put("S", "METALLIC_WALL");
		charMap.put("*", "METALLIC_FLOOR");
		charMap.put("W", "GLASS_WINDOW");
		charMap.put("1", "TELEPOD EXIT _BASE1");
		charMap.put("2", "TELEPOD EXIT _BASE2");
		charMap.put("3", "TELEPOD EXIT _BASE3");
		charMap.put("4", "TELEPOD EXIT _BASE4");
		charMap.put("X", "TELEPOD EXIT LANDING");
		charMap.put("A", "A_SIGN");
		charMap.put("T", "T_SIGN");
		charMap.put("O", "O_SIGN");
		charMap.put("B", "B_SIGN");
		charMap.put("C", "C_SIGN");
		charMap.put("H", "H_SIGN");
		charMap.put("E", "E_SIGN");
		charMap.put("G", "G_SIGN");
		charMap.put("N", "N_SIGN");
		
		spawnInfo = new MonsterSpawnInfo []{
			new MonsterSpawnInfo("GALACTIC_TROOPER", MonsterSpawnInfo.UNDERGROUND, 1),
		};
	}

	public String getDescription() {
		return "Base Decks";
	}

	public String getMusicKeyMorning() {
		return "SURFACE";
	}

	public String getMusicKeyNoon() {
		return null;
	}
}
	
