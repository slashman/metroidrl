package mrl.levelgen.featureCarve;

import java.util.ArrayList;

import sz.util.Position;

public abstract class Feature {
	private String id;
	
	public void setId(String val){
		id = val;
	}
	
	public String getId(){
		return id;
	}
	
	public abstract int getWidth();
	public abstract int getHeight();
	
	public abstract boolean drawOverCanvas(String[][] canvas, Position where, int direction, boolean [][] mask, ArrayList hotspots);
	
	protected boolean isValid(int x,int y,String[][]canvas){
		return x>=0 && x < canvas.length -1 && y >=0 && y <canvas[0].length-1;
	}
}
