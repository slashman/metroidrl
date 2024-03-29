package mrl.ai.monster;

import java.util.Iterator;

import mrl.action.Action;
import mrl.action.ActionFactory;
import mrl.actor.Actor;
import mrl.ai.ActionSelector;
import mrl.ai.MonsterAI;
import mrl.ai.RangedAttack;
import mrl.monster.Monster;

import sz.util.Position;
import sz.util.Util;

public class StationaryAI extends MonsterAI{
	
	public Action selectAction(Actor who){
		Monster aMonster = (Monster) who;
		int directionToPlayer = aMonster.starePlayer();
		int playerDistance = Position.flatDistance(aMonster.getPosition(), aMonster.getLevel().getPlayer().getPosition());
		if (directionToPlayer == -1){
	     	return null;
		}
		else {
			//Randomly decide if will approach the player or attack
			if (aMonster.playerInRow() && rangedAttacks != null && Util.chance(80)){
				//Try to attack the player
				for (Iterator iter = rangedAttacks.iterator(); iter.hasNext();) {
					RangedAttack element = (RangedAttack) iter.next();
					if (element.getRange() >= playerDistance && Util.chance(element.getFrequency())){
						//Perform the attack
						Action ret = ActionFactory.getActionFactory().getAction(element.getAttackId());
						ret.setDirection(directionToPlayer);
						return ret;
					}
				}
			}
			// Couldnt attack the player, so do nothing
            return null;
		}
	 }

	 public String getID(){
		 return "STATIONARY_AI";
	 }

	 public ActionSelector derive(){
 		try {
	 		return (ActionSelector) clone();
	 	} catch (CloneNotSupportedException cnse){
			return null;
	 	}
 	}
}