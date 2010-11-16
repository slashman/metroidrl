package mrl.ui.consoleUI;

import sz.csi.ConsoleSystemInterface;

public class CharImage {
	private char[][] imageData;
	private int[][] colorData;
	
	public int[][] getColorData() {
		return colorData;
	}

	public char[][] getImageData() {
		return imageData;
	}

	public void setColorData(int[][] colorData) {
		this.colorData = colorData;
	}

	public void setImageData(char[][] imageData) {
		this.imageData = imageData;
	}
	
	public void drawOver(ConsoleSystemInterface si, int px, int py){
		int oy = 0;
		for (int y = py; y < py + imageData.length-1; y++, oy++){
			int ox = 0;
			for (int x = px; x < px + imageData[oy].length-1; x++, ox++){
				si.print(x,y,imageData[oy][ox], colorData[oy][ox]);
			}
		}
	}
}
