package mrl.cuts;

import mrl.game.Game;
import mrl.level.MLevel;
import mrl.ui.Display;
import mrl.ui.UserInterface;
import sz.util.Util;

public class Start extends Unleasher {

	public void unleash(MLevel level, Game game) {
		level.removeExit("_BACK");
		Display.thus.showNav("Landing operation completed... Ship status: 45% functional... Initializing autorepair procedures... ");
		Display.thus.showNav("Janus Weaponry Facility detected to the east... Inminent hostile activity detected. Proceed with caution...");
		Display.thus.showNav("Energy Pods Activated... Good luck in your Mission Lady!");
		UserInterface.getUI().refresh();
		enabled = false;
	}

}
