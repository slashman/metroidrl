package mrl.levelgen.patterns;

public class Last extends StaticPattern
{
	String [][] ATO = new String [][]{
		{
			"SSSSSSSSSSSSSSSSS",
			"S...............S",
			"S...............S",
			"1......A........S",
			"S...............S",
			"S...............S",
			"S...............S",
			"S...............S",
			"SSSSSSSSSSSSSSSSS",
		}
	};
	
	String [][] BAC = new String [][]{
			{
				"SSSSSSSSSSSSSSSSS",
				"S...............S",
				"S...............S",
				"1...............S",
				"S.......B.......S",
				"S...............S",
				"S...............S",
				"S...............S",
				"SSSSSSSSSSSSSSSSS",
			}
		};
	String [][] CHE = new String [][]{
			{
				"SSSSSSSSSSSSSSSSS",
				"S...............S",
				"S...............S",
				"1..........C....S",
				"S...............S",
				"S...............S",
				"S...............S",
				"S...............S",
				"SSSSSSSSSSSSSSSSS",
			}
		};
	
	String [][] GEN = new String [][]{
			{
				"SSSSSSSSSSSSSSSSS",
				"S...............S",
				"S...............S",
				"1...............S",
				"S..........G....S",
				"S...............S",
				"S...............S",
				"S...............S",
				"SSSSSSSSSSSSSSSSS",
			}
		};
		
	
	public String getMapKey(){
		return "BASE1";
	}
	
	public Last (String fromLevel, String lane) {
		if (lane.equals("ATO"))
			cellMap = ATO;
		else if (lane.equals("BAC"))
			cellMap = BAC;
		else if(lane.equals("CHE"))
			cellMap = CHE;
		else if(lane.equals("GEN"))
			cellMap = GEN;
		
		charMap.put("S", "METALLIC_WALL");
		charMap.put(".", "METALLIC_FLOOR");
		charMap.put("1", "METALLIC_FLOOR EXIT "+fromLevel);
		
		charMap.put("A", "METALLIC_FLOOR FEATURE A");
		charMap.put("B", "METALLIC_FLOOR FEATURE B");
		charMap.put("C", "METALLIC_FLOOR FEATURE C");
		charMap.put("G", "METALLIC_FLOOR FEATURE G");
				
	}

	public String getDescription() {
		return "???";
	}

	public String getMusicKeyMorning() {
		return "SECRETS";
	}
	public int getPopulationAverage(){
		return 0;
	}
	public String getMusicKeyNoon() {
		return null;
	}
}
