package mrl.ui.consoleUI.effects;


import mrl.ui.consoleUI.ConsoleUserInterface;
import sz.csi.ConsoleSystemInterface;
import sz.util.*;

public class CharFlashEffect extends CharEffect{
	private int color;

    public CharFlashEffect(String ID, int color){
    	super (ID);
    	this.color = color;
    }

	public void drawEffect(ConsoleUserInterface ui, ConsoleSystemInterface si){
		si.flash(color);
		//animationPause();
	}

}