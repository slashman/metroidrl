package mrl.levelgen.featureCarve;

public abstract class BarrierFeature extends Feature {
	protected String exit;
	public void setExit(String val){
		exit = val;
	}
	
	String [][] variaBarrier = new String [][]{{
				"SSSSSSSSSSSSSSSSS",
				"S........S......S",
				"S........SS.....S",
				"2.........S.....S",
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
				"S.....SSSSSSSSSSS",
				"S...............S",
				"S...............S",
				"S......SSSSSS...S",
				"S......S........S",
				"S......S........S",
				"SSSSSSSS1SSSSSSSS"
			}
		};
		
		String [][] gravityBarrier = new String [][]{
				{
					"SSSSSSS2SSSSSSSSS",
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
					"SSSSSSSSSSSlllllS",
					"SlllllllllllllllS",
					"SlllllllllllllllS",
					"SlllllllllllllllS",
					"SlllllSSSSSSSSSSS",
					"SlllllllllllllllS",
					"SlllllllllllllllS",
					"SllllllSSSSSSlllS",
					"SllllllSllllllllS",
					"SllllllSllllllllS",
					"SSSSSSSS1SSSSSSSS"
				}
			};

		String [][] morphBarrier = new String [][]{
		{
			"SSSSSSSS2SSSSSSSS",
			"S...............S",
			"S...............S",
			"S...............S",
			"SSSxSSSSSSSSSSSSS",
			"SSSxxxxxxxxxxxSSS",
			"SSSSSSSSSSSSSxSSS",
			"S...............S",
			"S...............S",
			"S...............S",
			"SSSSSSSS1SSSSSSSS"
			}
		};

		String [][] missileBarrier = new String [][]{
		{
			"SSSSSSSS2SSSSSSSS",
			"S...............S",
			"S...............S",
			"S...............S",
			"SSSSSSSS.SSSSSSSS",
			"SSSSSSSSMSSSSSSSS",
			"SSSSSSS...SSSSSSS",
			"S...............S",
			"S...............S",
			"S...............S",
			"SSSSSSSS1SSSSSSSS"
			}
		};

		String [][] supermissileBarrier = new String [][]{
		{
			"SSSSSSSS2SSSSSSSS",
			"S...............S",
			"S...............S",
			"S...............S",
			"SSSSSSSS.SSSSSSSS",
			"SSSSSSSSISSSSSSSS",
			"SSSSSSS...SSSSSSS",
			"S...............S",
			"S...............S",
			"S...............S",
			"SSSSSSSS1SSSSSSSS"
			}
		};
		
		String [][] hijumpBarrier = new String [][]{
		{
			"SSSSSSSS2SSSSSSSS",
			"S...............S",
			"S...............S",
			"S...............S",
			"SSSSSSSHHHSSSSSSS",
			"SSSSSSSHHHSSSSSSS",
			"SSSSSSSHHHSSSSSSS",
			"S...............S",
			"S...............S",
			"S...............S",
			"SSSSSSSS1SSSSSSSS"
			}
		};	

		String [][] bombBarrier = new String [][]{
		{
			"SSSSSSSS2SSSSSSSS",
			"S...............S",
			"S...............S",
			"S...............S",
			"SSSSRSSSSSSSSRSSS",
			"SSSRRRSSSSSSRRRSS",
			"SSSSRSSSSSSSSRSSS",
			"S...............S",
			"S...............S",
			"S...............S",
			"SSSSSSSS1SSSSSSSS"
			}
		};

		String [][] powerBombBarrier = new String [][]{
		{
			"SSSSSSSS2SSSSSSSS",
			"S...............S",
			"S...............S",
			"S...............S",
			"SSSSSSSS.SSSSSSSS",
			"SSSSSSSSPSSSSSSSS",
			"SSSSSSS...SSSSSSS",
			"S...............S",
			"S...............S",
			"S...............S",
			"SSSSSSSS1SSSSSSSS"
			}
		};


}
