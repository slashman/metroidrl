package mrl.level;

import mrl.action.*;
import mrl.ui.*;
import mrl.ui.effects.*;
import sz.util.*;

public class SFX extends Action{
	private static SFX singleton = new SFX();
	public String getID(){
		return "SFX";
	}

	public void execute(){
		MLevel level = performer.getLevel();
		//level.addEffect(new FlashEffect(performer.getLevel().getPlayer().getPosition(), Appearance.WHITE));
		drawEffect(EffectFactory.getSingleton().createLocatedEffect(performer.getLevel().getPlayer().getPosition(), "SFX_THUNDER_FLASH"));
	}

	private final static int THUNDER = 1;
	private int effect;

	public void setEffect(int pEffect){
		effect = pEffect;
	}

	public static SFX getThunder(){
		singleton.setEffect(THUNDER);
		return singleton;
	}
}