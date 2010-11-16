package mrl.action;

import mrl.actor.Actor;
import mrl.level.MLevel;
import mrl.player.MPlayer;

public class BallMorph extends Action{
	public String getID(){
		return "BALLMORPH";
	}
	
	public void execute(){
        MLevel aLevel = performer.getLevel();
        MPlayer aPlayer = (MPlayer) performer;
        if (!aPlayer.isBallMorphed()){
        	aLevel.addMessage("You turn into a ball!");
        	aPlayer.setBallMorphed(true);
        } else {
        	aLevel.addMessage("You recover your shape");
        	aPlayer.setBallMorphed(false);
        }
	}
	
	public int getCost(){
		MPlayer p = (MPlayer) performer;
		return p.getAttackCost();
	}
	
	public boolean canPerform(Actor a){
		MPlayer aPlayer = (MPlayer) a;
		if (!aPlayer.isHasMorphBall()){
			invalidationMessage = "[/suit.ballMorph {Suit Functionality Unavailable}]";
			return false;
		}
		return true;
		
	}
	
	public String getSFX(){
		return "wav/dropBomb.wav";
	}
}