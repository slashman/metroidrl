package mrl.levelgen;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

import mrl.feature.Feature;
import mrl.feature.FeatureFactory;
import mrl.game.CRLException;
import mrl.level.Cell;
import mrl.level.MLevel;
import mrl.level.MapCellFactory;
import sz.util.Debug;
import sz.util.Position;
import sz.util.Util;

public class BaseGenerator extends LevelGenerator {
	private String [][] preLevel;
	private ArrayList /*String*/ exits ;
	private ArrayList /*String*/ exitDoors ;
	private String baseWall, baseFloor, baseDoor, baseFloor2, baseFloor3;
	
	
	public void init(String pWall, String pFloor, String pFloor2, String pFloor3, String pDoor, ArrayList exits, ArrayList pExitDoors){
		this.exits = exits;
		exitDoors = pExitDoors;
		baseWall = pWall;
		baseFloor = pFloor;
		baseFloor2 = pFloor2;
		baseFloor3 = pFloor3;
		baseDoor = pDoor;
	}
	
	private void divideHorizontal(Room r, int height){
		int doorx = r.xpos+Util.rand(1, r.width-2);
		//int doory = r.ypos+height-1;
		for (int i = 1; i < r.width-1; i++){
			if (r.xpos+i != doorx)
				preLevel[r.xpos+i][r.ypos+height-1] = baseWall;
		}
		//System.out.print("Door>");
		//preLevel[][] = baseDoor;
	}
	
	private void divideVertical(Room r, int width){
		int doory = r.ypos+Util.rand(1, r.height-2);
		for (int i = 1; i < r.height-1; i++){
			if (r.ypos+i != doory)
				preLevel[r.xpos+width-1][r.ypos+i] = baseWall;
		}
		//System.out.print("Door>");
		//preLevel[r.xpos+width-1][r.ypos+Util.rand(1, r.height-2)] = baseDoor;
	}
	
	private void ellevateLevel(){
		int blocks = Util.rand(5,10);
		for (int i = 0; i < blocks; i++){
			int xpos = Util.rand(3,getWidth()-3);
			int ypos = Util.rand(3,getHeight()-3);
			int xdim = Util.rand(2,getWidth()-xpos-3);
			int ydim = Util.rand(2,getHeight()-ypos-3);
			for (int xrun = xpos; xrun <= xpos+xdim; xrun++){
				for (int yrun = ypos; yrun <= ypos+ydim; yrun++){
					if (preLevel[xrun][yrun] == baseFloor){
						preLevel[xrun][yrun] = baseFloor2;
					} else if (preLevel[xrun][yrun] == baseFloor2){
						preLevel[xrun][yrun] = baseFloor3;
					} 
				}
			}
		}
		
	}
	
	public MLevel generateLevel(int xdim, int ydim) throws CRLException{
		preLevel = new String[xdim][ydim];
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
		
		ellevateLevel();
		/**/
		int divisions = Util.rand(10,20);
		//divisions = 50;
		int i = 0;
		Vector rooms = new Vector();
		Room room = new Room(0,0,getWidth(),getHeight());
		rooms.add(room);
		while (i < divisions){
			//System.out.print("Room>");
			room = (Room) Util.randomElementOf(rooms);
			boolean horizontal = Util.chance(50);
			if (horizontal){
				if (room.height < 5){
					i++;
					continue;
				}
				//System.out.print("DivideH>");
				int height = Util.rand(3, room.height-2);
				divideHorizontal(room, height);
				rooms.add(new Room(room.xpos,room.ypos,room.width,height));
				rooms.add(new Room(room.xpos,room.ypos+height-1,room.width,room.height-height+1));
			} else {
				if (room.width < 5){
					i++;
					continue;
				}
				//System.out.print("DivideV>");
				int width = Util.rand(3, room.width-2);
				divideVertical(room, width);
				rooms.add(new Room(room.xpos,room.ypos,width,room.height));
				rooms.add(new Room(room.xpos+width-1,room.ypos,room.width-width+1,room.height));
			}
			rooms.remove(room);
			i++;
		}
		
		//printIt();
		/**/
		MLevel ret = new MLevel();
		Cell[][] floor = renderLevel(preLevel, ret);
		Cell[][][] cells = new Cell[1][][];
		cells[0] = floor;
		ret.setCells(cells);
		
		//Place the exits
		for (i= 0; i < exits.size(); i++){
			int yExit = 0;
			int xExit = 0;
			switch (Util.rand(0,3)){
			case 0:
				yExit = Util.rand(5, getHeight() - 5);
				xExit = 0;
				break;
			case 1:
				yExit = Util.rand(5, getHeight() - 5);
				xExit = getWidth()-1;
				break;
			case 2:
				xExit = Util.rand(5, getWidth() - 5);
				yExit = getHeight()-1;
				break;
			case 3:
				xExit = Util.rand(5, getWidth() - 5);
				yExit = 0;
				break;
			}
			Position exit = new Position(xExit,yExit);
			if (ret.getExitOn(exit) != null){
				i--;
				continue;
			}
			ret.addExit(exit, (String)exits.get(i));
			cells[0][xExit][yExit] = MapCellFactory.getMapCellFactory().getMapCell((String)exitDoors.get(i));
			//Feature door = FeatureFactory.getFactory().buildFeature((String)exitDoors.get(i));
			//door.setPosition(xExit, yExit,0);
			//ret.addFeature(door);
		}
		return ret;
	}
	
	private int getWidth(){
		return preLevel.length;
	}
	
	private int getHeight(){
		return preLevel[0].length;
	}

	private void printIt(){
		for (int y = 0; y < getHeight(); y++){
			for (int x = 0; x < getWidth(); x++){
				System.out.print(preLevel[x][y]);
			}
			System.out.println();
		}
	}
	
	class Room{
		public int xpos,ypos,width,height;
		Room (int xpos, int ypos, int width, int height){
			this.xpos = xpos;
			this.ypos= ypos;
			this.width = width;
			this.height = height;
		}
	}
	
	public static void main(String args[]){
		BaseGenerator base = new BaseGenerator();
		base.init("#", ".", ":",";","/", new ArrayList(), new ArrayList());
		try {
			base.generateLevel(80,25);
		} catch (CRLException celre){
			
		}
	}

}