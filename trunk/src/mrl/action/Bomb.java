package mrl.action;

import mrl.actor.Actor;
import mrl.level.MLevel;
import mrl.player.MPlayer;

public class Bomb extends Action{
	public String getID(){
		return "BOMB";
	}
	
	public void execute(){
        MLevel aLevel = performer.getLevel();
        MPlayer aPlayer = (MPlayer) performer;
        if (aPlayer.isEnergyBombActivated()){
	        aLevel.addMessage("You drop an energy bomb.");
			aLevel.addSmartFeature("BOMB", performer.getPosition());
        }
	}
	
	public int getCost(){
		MPlayer p = (MPlayer) performer;
		return p.getAttackCost();
	}
	
	public String getSFX(){
		return "wav/dropBomb.wav";
	}
	
	public boolean canPerform(Actor a){
		MPlayer player = (MPlayer) a;
		if (!player.isEnergyBombActivated()){
			invalidationMessage = "[/suit.dropBomb {Suit Functionality Unavailable}]";
			return false;
		} 
		if (!player.isBallMorphed()){
			invalidationMessage = "You can only drop energy bombs as a ball.";
			return false;
		}
		return true;
	}
}