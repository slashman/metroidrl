package mrl.player;

import mrl.*;
import mrl.action.*;
import mrl.ui.*;
import mrl.ui.consoleUI.CharAppearance;
import sz.csi.textcomponents.MenuItem;

public class Skill implements MenuItem{
	private Action action;
	private int heartCost;
	private String actionDescription;
	private boolean symbolic;

	public Skill(String pActionDescription, Action pAction, int pHeartCost){
		actionDescription = pActionDescription;
		action = pAction;
		heartCost = pHeartCost;
	}
	
	public Skill(String pActionDescription){
		actionDescription = pActionDescription;
		symbolic = true;
	}

	public Action getAction() {
		return action;
	}


	public String getMenuDescription(){
		if (isSymbolic())
			return actionDescription;
		else
			return actionDescription + " ("+ heartCost+")";
	}

	/*Unsafe, Coupled*/
	public char getMenuChar() {
		return ' ';
	}
	
	/*Unsafe, Coupled*/
	public int getMenuColor() {
		return 0;
	}

	public boolean isSymbolic() {
		return symbolic;
	}
}
