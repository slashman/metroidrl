package mrl.ui.consoleUI;

import mrl.game.PlayerGenerator;
import mrl.player.MPlayer;
import mrl.ui.AppearanceFactory;
import mrl.ui.Display;
import sz.csi.CharKey;
import sz.csi.ConsoleSystemInterface;
import sz.csi.textcomponents.TextBox;

public class CharPlayerGenerator extends PlayerGenerator{
	public CharPlayerGenerator(ConsoleSystemInterface si){
		this.si = si;
	}
	private ConsoleSystemInterface si;
	
	public MPlayer generatePlayer(){
		si.cls();
		si.print(2,3, "Log name:", ConsoleSystemInterface.WHITE);
		si.refresh();
		si.locateCaret(3 +"Log name:".length(), 3);
		String name = si.input(10);
    	return getPlayer(name);
	}
}