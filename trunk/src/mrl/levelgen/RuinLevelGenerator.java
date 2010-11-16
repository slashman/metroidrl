package mrl.levelgen;

import java.util.ArrayList;

import mrl.action.Action;
import mrl.feature.Feature;
import mrl.feature.FeatureFactory;
import mrl.game.*;
import mrl.item.ItemFactory;
import mrl.level.*;

import sz.util.Debug;
import sz.util.Position;
import sz.util.Util;

/**
 * Generates a level with building ruins
 * 
 * The ruins are defined as a big space with empty
 * 'houses' inside.
 * 
 * @author Slash
 *
 */
public class RuinLevelGenerator extends LevelGenerator {
	private String [][] preLevel;
	private boolean  [][] mask;
	private String baseWall, baseFloor, baseDoor, baseExit;
	private ArrayList rooms, hotspots;
	
	public void init(String pWall, String pFloor, String pDoor, String pExit, ArrayList rooms){
		baseWall = pWall;
		baseFloor = pFloor;
		baseDoor = pDoor;
		baseExit = pExit;
		this.rooms = rooms;
	}
	
	public MLevel generateLevel(int xdim, int ydim, LevelMetaData md) throws CRLException{
		preLevel = new String[xdim][ydim];
		mask = new boolean[xdim][ydim];
		hotspots = new ArrayList();
		for(int x = 0; x < xdim; x++)
			for (int y = 0; y < ydim; y++)
				preLevel[x][y] = baseFloor;
		for(int x = 0; x < xdim; x++){
			preLevel[x][0] = baseWall;
			preLevel[x][ydim-1] = baseWall;
		}
		for (int y = 0; y < ydim; y++){
			preLevel[0][y] = baseWall;
			preLevel[xdim-1][y] = baseWall;
		}
		
		MLevel ret = new MLevel();
		
		/*ruins += Util.rand(0,(int)(ruins*0.1));
		for (int i = 0; i < ruins; i++){
			doRuin();
		}*/
		
		for (int i = 0; i < rooms.size(); i++){
			mrl.levelgen.featureCarve.Feature f = (mrl.levelgen.featureCarve.Feature) rooms.get(i);
			doRoom(f);
		}
		
//		Place exits
		ArrayList exits = md.exits;
		for (int i = 0; i < exits.size(); i++){
			placeExit((String)exits.get(i), ret);
		}
		
		Cell[][] floor = renderLevel(preLevel, ret);
		Cell[][][] cells = new Cell[1][][];
		cells[0] = floor;
		ret.setCells(cells);
		
		//Scatter items
		int items = Util.rand(5,10);
		Position where = new Position(0,0);
		for (int ii = 0; ii < items; ii++){
			do {
				where.x = Util.rand(1,ret.getWidth()-2);
				where.y = Util.rand(1,ret.getHeight()-2);
			} while (!ret.isItemPlaceable(where));
			ret.addItem(ItemFactory.thus.createItem(md.itemDistribution.getAnItem()), where);
		}
		return ret;
	}
	
	private int getWidth(){
		return preLevel.length;
	}
	
	private int getHeight(){
		return preLevel[0].length;
	}
	
	private void placeExit(String exitId, MLevel ret){
		boolean openExit = false;
		
		int exitType = Util.rand(0,1);
		switch (exitType){
		case 0:
			//Open
			openExit = true;
			break;
		case 1:
			//Boxed
			openExit = false;
			break;
		}
		
		
		int xpos = 0;
		int ypos = 0;
		if (!openExit) {
			int width = 0;
			int height = 0;
			
			do {
				xpos = Util.rand(5, getWidth()-20);
				ypos = Util.rand(1, getHeight()-16);
				width = Util.rand(4,15);
				height = Util.rand(4,15);
		
			} while (hasConflicts(xpos, ypos, width, height));
	
			int halfWidth = (int)Math.round(width/2.0d);
			int halfHeight = (int)Math.round(height/2.0d);
			for(int x = xpos; x < xpos + width; x++){
				preLevel[x][ypos] = baseWall;
				preLevel[x][ypos+height-1] = baseWall;
				
			}
			for (int y = ypos; y < ypos + height; y++){
				preLevel[xpos][y] = baseWall;
				preLevel[xpos+width-1][y] = baseWall;
			}
	
			if (Util.chance(50))
				if (Util.chance(50))
					preLevel[xpos][ypos+halfHeight] = baseDoor;
				else
					preLevel[xpos+width-1][ypos+halfHeight] = baseDoor;
			else
				if (Util.chance(50))
					preLevel[xpos+halfWidth][ypos] = baseDoor;
				else
					preLevel[xpos+halfWidth][ypos+height-1] = baseDoor;
			preLevel[xpos+halfWidth][ypos+halfHeight] = baseExit;
			ret.addExit(new Position(xpos+halfWidth, ypos+halfHeight),exitId);  
		} else {
			do {
				xpos = Util.rand(5, getWidth()-5);
				ypos = Util.rand(5, getHeight()-5);
			} while (hasConflicts(xpos, ypos));
			preLevel[xpos][ypos] = baseExit;
			ret.addExit(new Position(xpos, ypos),exitId);
		}
		Debug.exitMethod();
		
		
	}
	
	private void doRoom(mrl.levelgen.featureCarve.Feature f){
		Debug.enterMethod(this, "doRuin");
		int xpos = 0;
		int ypos = 0;
		int width = 0;
		int height = 0;
		
		do {
			xpos = Util.rand(5, getWidth()-f.getWidth()-3);
			ypos = Util.rand(1, getHeight()-f.getHeight()-3);
			width = f.getWidth();
			height = f.getHeight();
		} while (hasConflicts(xpos, ypos, width, height));
		f.drawOverCanvas(preLevel, new Position(xpos, ypos), Action.SELF , mask, hotspots);
		for(int x = xpos; x < xpos + width; x++){
			preLevel[x][ypos] = baseWall;
			preLevel[x][ypos+height-1] = baseWall;
			
		}
		for (int y = ypos; y < ypos + height; y++){
			preLevel[xpos][y] = baseWall;
			preLevel[xpos+width-1][y] = baseWall;
		}

		if (Util.chance(50))
			if (Util.chance(50))
				preLevel[xpos][Util.rand(ypos+1, ypos+height-1)] = baseDoor;
			else
				preLevel[xpos+width-1][Util.rand(ypos+1, ypos+height-1)] = baseDoor;
		else
			if (Util.chance(50))
				preLevel[Util.rand(xpos+1, xpos+width-1)][ypos] = baseDoor;
			else
				preLevel[Util.rand(xpos+1, xpos+width-1)][ypos+height-1] = baseDoor;
		Debug.exitMethod();
	}

	private boolean hasConflicts(int xpos, int ypos, int width, int height){
		for(int x = xpos-1; x < xpos + width+1; x++){
			for (int y = ypos-1; y < ypos + height+1; y++){
				if (preLevel[x][y] == baseWall)
					return true;
			}
		}
		return false;
	}
	
	private boolean hasConflicts(int xpos, int ypos){
		if (preLevel[xpos][ypos] == baseWall)
			return true;
		return false;
	}
}
