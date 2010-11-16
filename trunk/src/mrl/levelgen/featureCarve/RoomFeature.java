package mrl.levelgen.featureCarve;

import java.util.ArrayList;

import mrl.action.Action;
import sz.util.Position;
import sz.util.Util;

public class RoomFeature extends Feature {
	protected int width, height;
	private String floor;
	protected Position start;
	public RoomFeature(int width, int height, String floor) {
		start = new Position(0,0);
		this.width = width;
		this.height = height;
		this.floor = floor;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public boolean drawOverCanvas(String[][] canvas, Position where, int direction, boolean [][] mask, ArrayList hotspots){
		int rndPin = 0;
		switch (direction){
		case Action.UP:
			rndPin = Util.rand(1,width-2);
			start.x = where.x - rndPin;
			start.y = where.y - height + 1;
			break;
		case Action.DOWN:
			rndPin = Util.rand(1,width-2);
			start.x = where.x - rndPin;
			start.y = where.y;
			break;
		case Action.LEFT:
			rndPin = Util.rand(1,height-2);
			start.x = where.x - width + 1;
			start.y = where.y - rndPin;
			break;
		case Action.RIGHT:
			rndPin = Util.rand(1,height-2);
			start.x = where.x;
			//start.x = where.x+1;
			start.y = where.y - rndPin;
			break;
		case Action.SELF:
			start.x = where.x;
			start.y = where.y;
			break;
		}
		
		//Check the mask if available
		if (mask != null){
			for (int x = start.x; x < start.x + width; x++){
				for (int y = start.y; y < start.y + height; y++){
					if (!isValid(x,y,canvas) || mask[x][y]){
						return false;
					}
				}
			}
		}
		
		//Carve
		for (int x = start.x; x < start.x + width; x++){
			for (int y = start.y; y < start.y + height; y++){
				if (x==start.x || x == start.x + width-1 ||
					y==start.y || y == start.y + height-1 
				){
					if (mask != null)
						mask[x][y] = true;
					if (!(x==start.x && y == start.y) &&
						!(x==start.x+width-1 && y == start.y) &&
						!(x==start.x && y == start.y+height-1) &&
						!(x==start.x+width-1 && y == start.y+height-1))
						if (hotspots != null)
							hotspots.add(new Position(x,y));
				} else {
					canvas[x][y]=floor;
					if (mask != null)
						mask[x][y] = true;
				}
				
				
			}
		}
		
		return true;
	}
}
