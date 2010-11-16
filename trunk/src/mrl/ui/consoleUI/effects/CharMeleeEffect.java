package mrl.ui.consoleUI.effects;

import mrl.action.*;
import mrl.ui.*;
import mrl.ui.consoleUI.ConsoleUserInterface;
import sz.csi.ConsoleSystemInterface;
import sz.util.*;

public class CharMeleeEffect extends CharDirectionalEffect{
	
	private String directionMissileChars;
	private int color;

	public CharMeleeEffect(String ID, String pDirectionMissileChars, int pColor){
		super(ID);
		directionMissileChars = pDirectionMissileChars;
		color = pColor;
	}
	
	public void drawEffect(ConsoleUserInterface ui, ConsoleSystemInterface si){
		char whippy = directionMissileChars.charAt(direction);
		Position var = Action.directionToVariation(direction);
		Position abs = ui.getAbsolutePosition(getPosition());
		for (int j = 0; j<depth; j++){
			Position toPrint = Position.add(abs,
			Position.mul(var, j+1));
			si.safeprint(toPrint.x, toPrint.y, whippy, color);
			animationPause();
		}
	}

}