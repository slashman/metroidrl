package mrl.levelgen.featureCarve;

import java.util.ArrayList;

import mrl.action.Action;
import mrl.game.CRLException;
import mrl.game.Prize;
import mrl.item.ItemFactory;
import mrl.level.Cell;
import mrl.level.MLevel;
import mrl.level.MapCellFactory;
import mrl.levelgen.LevelGenerator;
import mrl.levelgen.LevelMetaData;
import sz.util.Position;
import sz.util.Util;

public class FeatureCarveGenerator extends LevelGenerator{
	private String[][] preLevel;
	private boolean[][] mask;
	private String[][] preLevelB;
	private boolean[][] maskB;
	private ArrayList hotspots = new ArrayList();
	private ArrayList roomHotspots = new ArrayList();
	private String solidCell;
	private String corridor;
	private ArrayList levelFeatures;
	private ArrayList prizeFeatures;
	private ArrayList exitFeatures;
	private ArrayList exits, exitDoors;
	private LevelMetaData metadata;
	
	public void initialize(ArrayList levelFeatures, 
			ArrayList prizeFeatures, 
			ArrayList exitFeatures,String solidCell, int xdim, int ydim, String corridor, 
			LevelMetaData metaData){
		this.metadata = metaData;
		preLevel = new String[xdim][ydim];
		mask = new boolean[xdim][ydim];
		preLevelB = new String[xdim][ydim];
		maskB = new boolean[xdim][ydim];
		this.solidCell = solidCell;
		this.corridor = corridor;
		this.levelFeatures = levelFeatures;
		this.prizeFeatures = prizeFeatures;
		this.exitFeatures = exitFeatures;
		this.exits = metaData.exits;
		this.exitDoors = metaData.exitDoors;
	}
	
	private void save(){
		for (int i = 0; i < mask.length; i++){
			System.arraycopy(mask[i], 0, maskB[i], 0, mask[i].length);
			System.arraycopy(preLevel[i], 0, preLevelB[i], 0, preLevel[i].length);
		}
	}
	
	private void rollBack(){
		for (int i = 0; i < mask.length; i++){
			System.arraycopy(maskB[i], 0, mask[i], 0, mask[i].length);
			System.arraycopy(preLevelB[i], 0, preLevel[i], 0, preLevel[i].length);
		}
	}
	
	public MLevel generateLevel() throws CRLException{
		boolean checked = false;
		boolean placed = false;
		int i = 0;
		go: while (!checked) {
			ArrayList pendingFeatures = new ArrayList(levelFeatures);
			boolean prizesOn = false;
			boolean exitsOn = false;
			hotspots.clear();
			roomHotspots.clear();
			//Fill the level with solid element
			for (int x = 0; x < getLevelWidth(); x++){
				for (int y = 0; y < getLevelHeight(); y++){
					preLevel[x][y] = solidCell;
					mask[x][y] = false;
				}
			}
			
			//Dig out a single room or a feature in the center of the map
			Position pos = new Position(getLevelWidth() / 2, getLevelHeight() / 2);
			Feature room = null;
			int direction = 0;
			boolean finished = false;
			
			while (!placed){
 				room = (Feature) Util.randomElementOf(pendingFeatures);
 				switch (Util.rand(1,4)){
					case 1:
						direction = Action.UP;
						break;
					case 2:
						direction = Action.DOWN;
						break;
					case 3:
						direction = Action.LEFT;
						break;
					case 4:
						direction = Action.RIGHT;
						break;
					}
				if (room.drawOverCanvas(preLevel, pos, direction, mask, hotspots)){
					pendingFeatures.remove(room);
					while (pendingFeatures.isEmpty() && !(finished && checked)){
						if (prizesOn){
							if (exitsOn){
								finished = true;
								checked = true;
							} else {
								pendingFeatures = new ArrayList(exitFeatures);
								exitsOn = true;
							}
						} else {
							pendingFeatures = new ArrayList(prizeFeatures);
							prizesOn = true;
						}
					}
					placed = true;
				} else {
					i++;
					if (i > 50000){
		 				i = 0;
		 				//System.exit(0);
		 				continue go;
		 			}
				}
			}
			
			placed = false;
			save();
			//boolean placeRoom = true;
			boolean letsRollBack = false;
			while (!finished){
				pos = (Position) Util.randomElementOf(hotspots);
				// Try to make a branch (corridor + room)
				int corridors = Util.rand(1,3);
				int j = 0;
				//corridors = 1; //TEST
				while (j<corridors && !letsRollBack){
					CorridotFeature corridorF = new CorridotFeature(Util.rand(4,5), corridor);
					switch (Util.rand(1,4)){
 					case 1:
 						direction = Action.UP;
 						break;
 					case 2:
 						direction = Action.DOWN;
 						break;
 					case 3:
 						direction = Action.LEFT;
 						break;
 					case 4:
 						direction = Action.RIGHT;
 						break;
 					}
					if (corridorF.drawOverCanvas(preLevel, pos, direction, mask, roomHotspots)){
						j++;
						pos = corridorF.getTip();
					} else {
						letsRollBack = true;
					}
				}
				if (letsRollBack){
					rollBack();
					letsRollBack = false;
					continue;
				}
				
 				room = (Feature) Util.randomElementOf(pendingFeatures);
 				// direction is kept from the last corridor
				if (room.drawOverCanvas(preLevel, pos, direction, mask, hotspots)){
					pendingFeatures.remove(room);
					save();
					while (pendingFeatures.isEmpty() && !(finished && checked)){
						if (prizesOn){
							if (exitsOn){
								finished = true;
								checked = true;
							} else {
								pendingFeatures = new ArrayList(exitFeatures);
								exitsOn = true;
							}
						} else {
							pendingFeatures = new ArrayList(prizeFeatures);
							prizesOn = true;
						}
					}
					placed = true;
				} else {
					rollBack();
				}
 				placed = false;
	 			
			}
		}
		
		
		
		MLevel ret = new MLevel();
		Cell[][][] cells = new Cell[1][][];
		cells [0] = renderLevel(preLevel, ret);
		ret.setCells(cells);
		renderLevelFeatures(ret, preLevel);
		//Scatter items
		int items = Util.rand(10,15);
		Position where = new Position(0,0);
		for (int ii = 0; ii < items; ii++){
			do {
				where.x = Util.rand(1,getLevelWidth()-2);
				where.y = Util.rand(1,getLevelHeight()-2);
			} while (!ret.isItemPlaceable(where));
			ret.addItem(ItemFactory.thus.createItem(metadata.itemDistribution.getAnItem()), where);
		}
		
		
		/*Position exit = new Position(0,0);
		for (int ii = 0; ii < exits.size(); ii++){
			String exitPoint = (String)exits.get(ii);
			String exitPointCell = (String)exitDoors.get(ii);
			while (true){
				exit.x = Util.rand(1,getLevelWidth()-2);
				exit.y = Util.rand(1,getLevelHeight()-2);
				if (ret.isExitPlaceable(exit) ){
					ret.addExit(exit, exitPoint);
					ret.getCells()[exit.z][exit.x][exit.y] = MapCellFactory.getMapCellFactory().getMapCell(exitPointCell);
					break;
				}
			}
		}*/
		return ret;
	}
	
	private int getLevelWidth(){
		return preLevel.length;
	}
	
	
	private int getLevelHeight(){
		return preLevel[0].length;
	}
	
	

}
