package mrl.ui.consoleUI;

import mrl.ui.*;
import sz.csi.*;


public class CharAppearance extends Appearance {
	private char character;
	private int color;

	public final static CharAppearance VOID = new CharAppearance("VOID", ' ', ConsoleSystemInterface.BLACK);

	public static CharAppearance getVoidAppearance(){
		return VOID;
	}
	
	public CharAppearance(String ID, char character, int color){
		super(ID);
		this.character = character;
		this.color = color;
	}

	public char getChar(){
		return character;
	}

	public int getColor(){
		return color;
	}
	
	public static final int
    BLACK = 0,
	DARK_BLUE = 1,
	GREEN = 2,
	TEAL = 3,
	DARK_RED = 4,
	PURPLE = 5,
	BROWN = 6,
	LIGHT_GRAY = 7,
    GRAY = 8,
	BLUE = 9,
	LEMON = 10,
	CYAN = 11,
	RED = 12,
	MAGENTA = 13,
	YELLOW = 14,
	WHITE = 15;
	
	public static  int getColor(String colorName){
		if (colorName == null) return -1;
		if (colorName.equals("BLACK")) return BLACK;
		if (colorName.equals("DARK_BLUE")) return DARK_BLUE;
		if (colorName.equals("GREEN")) return GREEN;
		if (colorName.equals("TEAL")) return TEAL;
		if (colorName.equals("DARK_RED")) return DARK_RED;
		if (colorName.equals("PURPLE")) return PURPLE;
		if (colorName.equals("BROWN")) return BROWN;
		if (colorName.equals("LIGHT_GRAY")) return LIGHT_GRAY;
		if (colorName.equals("GRAY")) return GRAY;
		if (colorName.equals("BLUE")) return BLUE;
		if (colorName.equals("LEMON")) return LEMON;
		if (colorName.equals("CYAN")) return CYAN;
		if (colorName.equals("RED")) return RED ;
		if (colorName.equals("MAGENTA")) return MAGENTA; 
		if (colorName.equals("YELLOW")) return YELLOW;
		if (colorName.equals("WHITE")) return WHITE;
		return -1;
	}
}