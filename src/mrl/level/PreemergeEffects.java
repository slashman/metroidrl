package mrl.level;

import mrl.action.*;
import mrl.ui.*;
import mrl.ui.effects.*;


public class PreemergeEffects extends Action{
	private static PreemergeEffects singleton = new PreemergeEffects();
	
	public String getID(){
		return "Preemerge";
	}

	public void execute(){
		MLevel aLevel = performer.getLevel();
		Emerger em = (Emerger) performer;
		//aLevel.addMessage("You see something crawling out of the soil!", em.getPoint());
        //aLevel.addEffect(new StaticAnimEffect(em.getPoint(), "^", Appearance.BROWN));
		
        drawEffect(EffectFactory.getSingleton().createLocatedEffect(em.getPoint(), "SFX_MONSTER_CRAWLING"));
	}

	public static PreemergeEffects getAction(){
		return singleton;
	}
}