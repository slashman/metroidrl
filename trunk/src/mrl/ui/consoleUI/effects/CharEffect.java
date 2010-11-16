package mrl.ui.consoleUI.effects;

import mrl.ui.consoleUI.ConsoleUserInterface;
import mrl.ui.effects.Effect;
import sz.csi.ConsoleSystemInterface;
import sz.util.Position;

public abstract class CharEffect extends Effect{
	public CharEffect(String id){
		super(id);
	}

	public CharEffect(String id, int delay){
		super(id, delay);
	}

	public abstract void drawEffect(ConsoleUserInterface ui, ConsoleSystemInterface si);
	

}
