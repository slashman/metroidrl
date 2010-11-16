package mrl.cuts;

import sz.util.Util;
import mrl.game.Game;
import mrl.level.MLevel;
import mrl.ui.Display;

public class BonyIntro extends Unleasher {

	public void unleash(MLevel level, Game game) {
		if (Util.chance(90))
			return;
		switch (Util.rand(0,4)){
		case 0:
			level.addMessage("HAHAHA! Girl in shiny suit ya, want ya soul!");
			break;
		case 1:
			level.addMessage("My spell is about to fade away! need YA soul!");
			break;
		case 2:
			level.addMessage("BOOONN TO BE WAAILLLD!");
			break;
		case 3:
			level.addMessage("Hullo! 'mon! use my sweet engine will ya!");
			break;
		case 4:
			level.addMessage("Soulbag! Soulbag! Open Soulbag!");
			break;
		}
		
		if (level.getMonsterByID("BONY") == null)
			enabled = false;
	}

}
