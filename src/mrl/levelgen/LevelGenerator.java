
package mrl.levelgen;

import mrl.feature.Feature;
import mrl.feature.FeatureFactory;
import mrl.game.*;
import mrl.level.*;
import mrl.monster.Monster;
import mrl.monster.MonsterFactory;

import sz.util.Debug;
import sz.util.Position;
import sz.util.Util;

public abstract class LevelGenerator {
	//public abstract Level generateLevel(String param, Dispatcher dispa);

	protected Cell[][] renderLevel(String[][] cellIds, MLevel lev) throws CRLException{
		Debug.enterMethod(this, "renderLevel");
		int z = 0; //One level for now
		Cell[][] ret = new Cell[cellIds.length][cellIds[0].length];
		for (int x = 0; x < cellIds.length; x++)
			for (int y = 0; y < cellIds[0].length; y++){
				//ret[x][y] = mcf.getMapCell(cellIds[x][y]);
				String iconic = cellIds[x][y];
				String[] cmds = iconic.split(" ");
				if (!cmds[0].equals("NOTHING"))
					try {
						ret[x][y] = MapCellFactory.getMapCellFactory().getMapCell(cmds[0]);
					} catch (CRLException crle){
						Debug.byebye("Exception creating the level "+crle);
					}
				if (cmds.length > 1){
					if (cmds[1].equals("FEATURE")){
						if (cmds.length < 4 || Util.chance(Integer.parseInt(cmds[3]))){
							Feature vFeature = FeatureFactory.getFactory().buildFeature(cmds[2]);
							vFeature.setPosition(x,y,z);
							if (cmds.length > 4){
								if (cmds[4].equals("COST")) {
									vFeature.setKeyCost(Integer.parseInt(cmds[5]));
								}
							}
							lev.addFeature(vFeature);
						}
					}else
					if (cmds[1].equals("MONSTER")){
						Monster toAdd = MonsterFactory.getFactory().buildMonster(cmds[2]);
						toAdd.setPosition(x,y,z);
						lev.addMonster(toAdd);
					}else
					if (cmds[1].equals("EXIT")){
						lev.addExit(new Position(x,y,z), cmds[2]);
					} else
					if (cmds[1].equals("EXIT_FEATURE")){
						lev.addExit(new Position(x,y,z), cmds[2]);
						Feature vFeature = FeatureFactory.getFactory().buildFeature(cmds[3]);
						vFeature.setPosition(x,y,z);
						if (cmds.length > 4){
							if (cmds[5].equals("COST")) {
								vFeature.setKeyCost(Integer.parseInt(cmds[6]));
							}
						}
						lev.addFeature(vFeature);
					} else
					if (cmds[1].equals("EOL")){
						lev.addExit(new Position(x,y,z), "_NEXT");
						Feature endFeature = FeatureFactory.getFactory().buildFeature(cmds[2]);
						endFeature.setPosition(x,y,z);
						if (cmds.length > 3){
							//Debug.say("Hi... i will set the cost");
							if (cmds[3].equals("COST")) {
								//Debug.say("Hi... i did it to "+vFeature);
								endFeature.setKeyCost(Integer.parseInt(cmds[4]));
							}
						}
						lev.addFeature(endFeature);
					}
				}
			}
		Debug.exitMethod(ret);
		return ret;
	}
	
	protected void renderLevelFeatures(MLevel level, String[][] cellIds) throws CRLException{
		for (int x = 0; x < cellIds.length; x++)
			for (int y = 0; y < cellIds[0].length; y++) {
				if (cellIds[x][y].startsWith("F_")){
					Feature f = FeatureFactory.getFactory().buildFeature(cellIds[x][y].substring(2).split(" ")[1]);
					f.setPosition(x,y,0);
					level.addFeature(f);
				}
			}
	}

	protected int placeKeys(MLevel ret){
		Debug.enterMethod(this, "placeKeys");
		//Place the magic Keys
		int keys = Util.rand(1,4);
		Position tempPosition = new Position(0,0);
		for (int i = 0; i < keys; i++){
			int keyx = Util.rand(1,ret.getWidth()-1);
			int keyy = Util.rand(1,ret.getHeight()-1);
			int keyz = Util.rand(0, ret.getDepth()-1);
			tempPosition.x = keyx;
			tempPosition.y = keyy;
			tempPosition.z = keyz;
			if (ret.isWalkable(tempPosition)){
				Feature keyf = FeatureFactory.getFactory().buildFeature("KEY");
				keyf.setPosition(tempPosition.x, tempPosition.y, tempPosition.z);
				ret.addFeature(keyf);
			} else {
				i--;
			}
		}
		Debug.exitMethod(keys);
		return keys;
		
	}
	
	

}