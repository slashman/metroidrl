package mrl.ui.consoleUI.effects;

import mrl.action.*;
import mrl.ui.*;
import mrl.ui.consoleUI.ConsoleUserInterface;
import sz.csi.ConsoleSystemInterface;
import sz.util.*;

public class CharIconMissileEffect extends CharDirectedEffect {
	private char missile;
	private int misColor;
	
	public void drawEffect(ConsoleUserInterface ui, ConsoleSystemInterface si){
		UserInterface.getUI().getPlayer().see();
		UserInterface.getUI().refresh();
		
		Position oldPoint = effectLine.next();
		int oldColor = -1;
		char oldChar = ' ';
		int too = 0;
		
		for (int i = 0; i < depth; i++){
			Position next = effectLine.next();
			if (i != 0){
				Position relative = Position.subs(oldPoint, ui.getPlayer().getPosition());
				Position toPrint = Position.add(ui.PC_POS, relative);
				si.print(toPrint.x(), toPrint.y(), ""+oldChar, oldColor);
			}
		
			oldPoint = new Position(next);
			char icon = missile;
			
			Position relative = Position.subs(next, ui.getPlayer().getPosition());
			Position toPrint = Position.add(ui.PC_POS, relative);
			if (!ui.insideViewPort(toPrint))
				break;
			oldChar = si.peekChar(toPrint.x(), toPrint.y());
			oldColor = si.peekColor(toPrint.x(), toPrint.y());
			si.safeprint(toPrint.x(), toPrint.y(), icon, misColor);
			animationPause();
		}
	}

	public CharIconMissileEffect(String ID, char missile, int misColor,  int delay){
		super(ID);
		setMissile(missile);
		setMisColor(misColor);
		setAnimationDelay(delay);
	}

	public void setMissile(char value) {
		missile = value;
	}

	public void setMisColor(int value) {
		misColor = value;
	}


}