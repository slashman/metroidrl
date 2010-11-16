package mrl.levelgen.mapData;

import java.util.ArrayList;
import java.util.Hashtable;

import mrl.action.Action;
import mrl.levelgen.featureCarve.Feature;

import sz.util.Circle;
import sz.util.Position;
import sz.util.Util;


public class MapDataFeature extends Feature{
	private Room room;
	private CellMap baseCellmap;
	protected Position start = new Position(0,0);
	String[] template;
	
	public MapDataFeature (Room room, CellMap baseCellmap){
		this.room = room;
		this.baseCellmap = baseCellmap;
		template = (String[])Util.randomElementOf(room.getCharmap());
	}
	
	public int getHeight() {
		return template.length;
	}
	
	public int getWidth() {
		return template[0].length();
	}
	
	public boolean drawOverCanvas(String[][] canvas, Position where, int direction, boolean[][] mask, ArrayList hotspots) {
		int width = template[0].length(); 
		int height = template.length;
		int rndPin = 0;
		int midWidth = (int)(Math.floor(width / 2.0));
		int midHeight = (int)(Math.floor(height / 2.0));
		switch (direction){
		case Action.UP:
			rndPin = midWidth;
			start.x = where.x - rndPin;
			start.y = where.y - height + 1;
			break;
		case Action.DOWN:
			rndPin = midWidth;
			start.x = where.x - rndPin;
			start.y = where.y;
			break;
		case Action.LEFT:
			rndPin = midHeight;
			start.x = where.x - width + 1;
			start.y = where.y - rndPin;
			break;
		case Action.RIGHT:
			rndPin = midHeight;
			start.x = where.x;
			start.y = where.y - rndPin;
			break;
		case Action.SELF:
			start.x = where.x;
			start.y = where.y;
			break;
		}
		
		//Check the mask
		for (int x = start.x; x < start.x + width; x++){
			for (int y = start.y; y < start.y + height; y++){
				if (!isValid(x,y,canvas) || mask[x][y]){
					return false;
				}
			}
		}
		
		hotspots.add(new Position(start.x,start.y+midHeight));
		hotspots.add(new Position(start.x+width-1,start.y+midHeight));
		hotspots.add(new Position(start.x+midWidth,start.y));
		hotspots.add(new Position(start.x+midWidth,start.y+height-1));
		
		//Carve
		Position cursor = new Position(0,0);
		for (int y = 0; y < template.length; y++){
			for (int x = 0; x < template[0].length(); x++){
				cursor.x = start.x + x;
				cursor.y = start.y + y;
				mask[cursor.x][cursor.y] = true;
				String key = template[y].charAt(x)+"";
				String mapping = room.getPrivateCellMap().getMapping(key);
				if (mapping == null){
					mapping = baseCellmap.getMapping(key);
				}
				if (mapping == null){
					mapping = SpecialRoomFactory.thus.getGenericMapping().getMapping(key);
				}
				if (mapping == null){
					System.out.println("This is a debug breakpoint.");
				}
				if (!cursor.equals(where))
					canvas[cursor.x][cursor.y] = mapping;
			}
		}
		
		
		mask[start.x][start.y+midHeight] = false;
		mask[start.x+width-1][start.y+midHeight] = false;
		
		
		mask[start.x+midWidth][start.y] = false;
		mask[start.x+midWidth][start.y+height-1] = false;
		
		
		template = (String[])Util.randomElementOf(room.getCharmap()); 
		return true;
	}
	
}
