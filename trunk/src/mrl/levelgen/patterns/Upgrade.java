package mrl.levelgen.patterns;

import mrl.levelgen.MonsterSpawnInfo;

public class Upgrade extends StaticPattern
{
	public String getMapKey(){
		return "BASE1";
	}
	public int getPopulationAverage(){
		return 0;
	}
	public Upgrade (String returningLevel, String upgrade) {
		cellMap = new String [][]{
		{
			"SSSSSSSSSSSSSSSSS",
			"S...............S",
			"S...............S",
			"S.......U.......S",
			"S..R.........R..S",
			"S...............S",
			"S...............S",
			"S...............S",
			"S...R.........R.S",
			"S...............S",
			"SSSSSSSSESSSSSSSS"
			}
		};

		if (upgrade.startsWith("ENERGY_TANK"))
			upgrade = "ENERGY_TANK";
		if (upgrade.startsWith("RESERVE_TANK"))
			upgrade = "RESERVE_TANK";
		charMap.put("S", "METALLIC_WALL");
		charMap.put(".", "METALLIC_FLOOR");
		charMap.put("R", "CHOZO_RUIN");
		charMap.put("E", "METALLIC_FLOOR EXIT "+returningLevel);
		charMap.put("U", "METALLIC_FLOOR FEATURE "+upgrade);
	}

	public String getDescription() {
		return "Artifact Storage";
	}

	public String getMusicKeyMorning() {
		return "SECRETS";
	}

	public String getMusicKeyNoon() {
		return null;
	}
}
	
