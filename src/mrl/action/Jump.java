package mrl.action;

import mrl.feature.Feature;
import mrl.level.Cell;
import mrl.level.MLevel;
import mrl.monster.Monster;
import mrl.player.MPlayer;
import sz.util.Debug;
import sz.util.Position;
import sz.util.Util;

public class Jump extends Action{
	private MPlayer aPlayer; 
	public String getID(){
		return "Jump";
	}
	
   	public String getSFX(){
   		if (Util.chance(50))
   			return "wav/spinjump1.wav";
   		else
   			return "wav/spinjump2.wav";
	}

	public int getCost(){
		return aPlayer.getJumpCost();
	}

	public void execute(){
		Debug.doAssert(performer instanceof MPlayer, "Jump action, tried for not player");
		aPlayer = (MPlayer) performer;
        MLevel aLevel = performer.getLevel();
        
        if (aPlayer.isBallMorphed()){
        	if (aPlayer.isSpringBallActivated()){
        		aLevel.addMessage("You jump as a ball");
        		aPlayer.jump();
        	} else {
        		aLevel.addMessage("You cannot jump as a ball");
        	}
        } else {
	        if (aPlayer.isJumping())
	        	if (aPlayer.isSpaceJumpActivated()){
	        		aLevel.addMessage("You bounce over the air!");
	        		aPlayer.jump();
	        	} else {
	        		aLevel.addMessage("You cannot jump on mid-air");
	        	}
	        else
	        	aPlayer.jump();
        }
        
	}
}
