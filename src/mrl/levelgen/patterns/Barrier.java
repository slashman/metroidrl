package mrl.levelgen.patterns;

public class Barrier extends StaticPattern
{
	String [][] variaBarrier = new String [][]{
		{
			"SSSSSSSSSSSSSSSSS",
			"S.....S...S.....S",
			"S.....S.2.S.....S",
			"2.....S...S.....S",
			"S.....SS.SS.....S",
			"SSSSSSSS.SSSSSSSS",
			"S........S......S",
			"S........SS.....S",
			"S.........S.....S",
			"S.........SS....S",
			"S..........S....S",
			"S...............S",
			"S...............S",
			"SSSSSSSSSSSSS...S",
			"S..........S....S",
			"S.........S.....S",
			"S........S......S",
			"S.......S.......S",
			"S......SSSSSSSSSS",
			"S...............S",
			"S...............S",
			"S...............S",
			"SSSSSSSSSSS.....S",
			"S...............S",
			"S...............S",
			"S...............S",
			"SSSSSSSS.SSSSSSSS",
			"S......S.S......S",
			"S.....SS.SS.....S",
			"S.....S...SSS...S",
			"S.....S.1.S.....S",
			"S.....S...S.....S",
			"SSSSSSSSSSSSSSSSS"
		}
	};
	
	String [][] gravityBarrier = new String [][]{
			{
				"SSSSSSSSSSSSSSSSS",
				"SllllSS...SSllllS",
				"SllllS.....SllllS",
				"SllllS..2..SllllS",
				"SSSSSS.....SSSSSS",
				"SllllSS...SSllllS",
				"SlllllSS.SSlllllS",
				"SSSSSSSS.SSSSSSSS",
				"SlllllllllllllllS",
				"SlllllllllllllllS",
				"SlllllllllllllllS",
				"SlllllllllllllllS",
				"SlllllllllllllllS",
				"SlllllllllllllllS",
				"SlllllllllllllllS",
				"SllllSSSSSSSSlllS",
				"SlllllllllllllllS",
				"SlllllllllllllllS",
				"SlllllllllllllllS",
				"SlllllllllllllllS",
				"SllllllSSSSlllllS",
				"SlllllllllllllllS",
				"SlllllllllllllllS",
				"SlllllllllllllllS",
				"SlllllllllllllllS",
				"SSSSSSSSlSSSSSSSS",
				"SllllllSlSllllllS",
				"SllllllSlSllllllS",
				"SlllllSS.SSSSSSSS",
				"SllllSS...SSllllS",
				"SllllS.....SllllS",
				"SllllS..1..SSlllS",
				"SllllS.....SllllS",
				"SllllSS...SSllllS",
				"SSSSSSSSSSSSSSSSS"
			}
		};

	String [][] morphBarrier = new String [][]{
	{
		"SSSSSSSSSSSSSSSSS",
		"S....SS...SS....S",
		"S....S.....S....S",
		"S....S..2..S....S",
		"SSSSSS.....SSSSSS",
		"S....SS...SS....S",
		"S.....SS.SS.....S",
		"S......S.S......S",
		"SSSSSSSS.SSSSSSSS",
		"S...............S",
		"S...............S",
		"S...............S",
		"SSSxSSSSSSSSSSSSS",
		"SSSxxxxxxxxxxxSSS",
		"SSSSSSSSSSSSSxSSS",
		"S...............S",
		"S...............S",
		"S...............S",
		"SSSSSSSS.SSSSSSSS",
		"S......S.S......S",
		"S.....SS.SS.....S",
		"S....SS...SS....S",
		"S....S.....S....S",
		"S....S..1..S....S",
		"SSSSSS.....SSSSSS",
		"S....SS...SS....S",
		"SSSSSSSSSSSSSSSSS",
		
		
		
		}
	};

	String [][] missileBarrier = new String [][]{
	{
		"SSSSSSSSSSSSSSSSS",
		"S....SS...SS....S",
		"S....S.....S....S",
		"S....S..2..S....S",
		"SSSSSS.....SSSSSS",
		"S....SS...SS....S",
		"S.....SS.SS.....S",
		"S......S.S......S",
		"SSSSSSSS.SSSSSSSS",
		"S...............S",
		"S...............S",
		"S...............S",
		"SSSSSSSS.SSSSSSSS",
		"SSSSSSSSMSSSSSSSS",
		"SSSSSSS...SSSSSSS",
		"S...............S",
		"S...............S",
		"S...............S",
		"SSSSSSSS.SSSSSSSS",
		"S......S.S......S",
		"S.....SS.SS.....S",
		"S....SS...SS....S",
		"S....S.....S....S",
		"S....S..1..S....S",
		"SSSSSS.....SSSSSS",
		"S....SS...SS....S",
		"SSSSSSSSSSSSSSSSS",
		}
	};

	String [][] supermissileBarrier = new String [][]{
	{
		"SSSSSSSSSSSSSSSSS",
		"S....SS...SS....S",
		"S....S.....S....S",
		"S....S..2..S....S",
		"SSSSSS.....SSSSSS",
		"S....SS...SS....S",
		"S.....SS.SS.....S",
		"S......S.S......S",
		"SSSSSSSS.SSSSSSSS",
		"S...............S",
		"S...............S",
		"S...............S",
		"SSSSSSSS.SSSSSSSS",
		"SSSSSSSSISSSSSSSS",
		"SSSSSSS...SSSSSSS",
		"S...............S",
		"S...............S",
		"S...............S",
		"SSSSSSSS.SSSSSSSS",
		"S......S.S......S",
		"S.....SS.SS.....S",
		"S....SS...SS....S",
		"S....S.....S....S",
		"S....S..1..S....S",
		"SSSSSS.....SSSSSS",
		"S....SS...SS....S",
		"SSSSSSSSSSSSSSSSS",
		}
	};
	
	String [][] hijumpBarrier = new String [][]{
	{
		"SSSSSSSSSSSSSSSSS",
		"S....SS...SS....S",
		"S....S.....S....S",
		"S....S..2..S....S",
		"SSSSSS.....SSSSSS",
		"S....SS...SS....S",
		"S.....SS.SS.....S",
		"S......S.S......S",
		"SSSSSSSS.SSSSSSSS",
		"S...............S",
		"S...............S",
		"S...............S",
		"SSSSSSSHHHSSSSSSS",
		"SSSSSSSHHHSSSSSSS",
		"SSSSSSSHHHSSSSSSS",
		"S...............S",
		"S...............S",
		"S...............S",
		"SSSSSSSS.SSSSSSSS",
		"S......S.S......S",
		"S.....SS.SS.....S",
		"S....SS...SS....S",
		"S....S.....S....S",
		"S....S..1..S....S",
		"SSSSSS.....SSSSSS",
		"S....SS...SS....S",
		"SSSSSSSSSSSSSSSSS",
		}
	};	

	String [][] bombBarrier = new String [][]{
	{
		"SSSSSSSSSSSSSSSSS",
		"S....SS...SS....S",
		"S....S.....S....S",
		"S....S..2..S....S",
		"SSSSSS.....SSSSSS",
		"S....SS...SS....S",
		"S.....SS.SS.....S",
		"S......S.S......S",
		"SSSSSSSS.SSSSSSSS",
		"S...............S",
		"S...............S",
		"S...............S",
		"SSSSRSSSSSSSSRSSS",
		"SSSRRRSSSSSSRRRSS",
		"SSSSRSSSSSSSSRSSS",
		"S...............S",
		"S...............S",
		"S...............S",
		"SSSSSSSS.SSSSSSSS",
		"S......S.S......S",
		"S.....SS.SS.....S",
		"S....SS...SS....S",
		"S....S.....S....S",
		"S....S..1..S....S",
		"SSSSSS.....SSSSSS",
		"S....SS...SS....S",
		"SSSSSSSSSSSSSSSSS",
		}
	};

	String [][] powerBombBarrier = new String [][]{
	{
		"SSSSSSSSSSSSSSSSS",
		"S....SS...SS....S",
		"S....S.....S....S",
		"S....S..2..S....S",
		"SSSSSS.....SSSSSS",
		"S....SS...SS....S",
		"S.....SS.SS.....S",
		"S......S.S......S",
		"SSSSSSSS.SSSSSSSS",
		"S...............S",
		"S...............S",
		"S...............S",
		"SSSSSSSS.SSSSSSSS",
		"SSSSSSSSPSSSSSSSS",
		"SSSSSSS...SSSSSSS",
		"S...............S",
		"S...............S",
		"S...............S",
		"SSSSSSSS.SSSSSSSS",
		"S......S.S......S",
		"S.....SS.SS.....S",
		"S....SS...SS....S",
		"S....S.....S....S",
		"S....S..1..S....S",
		"SSSSSS.....SSSSSS",
		"S....SS...SS....S",
		"SSSSSSSSSSSSSSSSS",
		}
	};
	
	public int getPopulationAverage(){
		return 0;
	}
	
	public String getMapKey(){
		return "BASE1";
	}
	
	public Barrier (String fromLevel, String toLevel, String upgrade) {
		
		if (upgrade.equals("VARIA_SUIT"))
			cellMap = variaBarrier;
		else if (upgrade.equals("GRAVITY_SUIT"))
			cellMap = gravityBarrier;
		else if(upgrade.equals("MORPH_ENERGY"))
			cellMap = morphBarrier;
		else if(upgrade.equals("MISSILE_TANK"))
			cellMap = missileBarrier;
		else if (upgrade.equals("SUPERMISSILE_TANK"))
			cellMap = supermissileBarrier;
		else if (upgrade.equals("HI_JUMP"))
			cellMap = hijumpBarrier;
		else if (upgrade.equals("BOMB_ENERGY"))
			cellMap = bombBarrier;
		else if (upgrade.equals("POWER_BOMB_TANK"))
			cellMap = powerBombBarrier;
		
		charMap.put("S", "METALLIC_WALL");
		charMap.put(".", "METALLIC_FLOOR");
		charMap.put("B", "METALLIC_FLOOR FEATURE "+upgrade);
		charMap.put("1", "TELEPOD EXIT "+fromLevel);
		charMap.put("2", "TELEPOD EXIT "+toLevel);
		charMap.put("l", "LAVA");
		charMap.put("x", "METALLIC_HOLE");
		charMap.put("M", "MISSILE_DOOR");
		charMap.put("I", "SUPERMISSILE_DOOR");
		charMap.put("H", "HIGH_FLOOR");
		charMap.put("R", "BREAKABLE_WALL");
		charMap.put("P", "POWERBOMB_DOOR");
		
	}

	public String getDescription() {
		return "???";
	}

	public String getMusicKeyMorning() {
		return "SECRETS";
	}

	public String getMusicKeyNoon() {
		return null;
	}
}
	