package mrl.ai.monster;

import java.util.Iterator;

import mrl.action.Action;
import mrl.action.ActionFactory;
import mrl.action.monster.MonsterWalk;
import mrl.actor.Actor;
import mrl.ai.ActionSelector;
import mrl.ai.MonsterAI;
import mrl.ai.RangedAttack;
import mrl.monster.Monster;

import sz.util.Position;
import sz.util.Util;

public class RangedAI extends MonsterAI{
	
	private int approachLimit = 5;

	public Action selectAction(Actor who){
		Monster aMonster = (Monster) who;
		int directionToPlayer = aMonster.starePlayer();
		int playerDistance = Position.flatDistance(aMonster.getPosition(), aMonster.getLevel().getPlayer().getPosition());
		if (directionToPlayer == -1){
			//A stationary here would do nothing
			int direction = Util.rand(0,7);
	     	Action ret = new MonsterWalk();
	     	ret.setDirection(direction);
	     	return ret;
		}
		if (playerDistance < approachLimit){
			//get away from player
			Action ret = new MonsterWalk();
            int direction = Action.toIntDirection(Position.mul(Action.directionToVariation(directionToPlayer), -1));
            ret.setDirection(direction);
            return ret;
		} else {
			//Randomly decide if will approach the player or attack
			if (aMonster.playerInRow() && rangedAttacks != null && Util.chance(80)){
				//Try to attack the player
				for (Iterator iter = rangedAttacks.iterator(); iter.hasNext();) {
					RangedAttack element = (RangedAttack) iter.next();
					if (element.getRange() >= playerDistance && Util.chance(element.getFrequency())){
						//Perform the attack
						Action ret = ActionFactory.getActionFactory().getAction(element.getAttackId());
						ret.setDirection(directionToPlayer);
						ret.setPosition(aMonster.getLevel().getPlayer().getPosition());
						return ret;
					}
				}
			}
			// Couldnt attack the player, so walk to him
			Action ret = new MonsterWalk();
            ret.setDirection(directionToPlayer);
            return ret;
			
		}
	 }

	 public String getID(){
		 return "RANGED_AI";
	 }

	 public ActionSelector derive(){
 		try {
	 		return (ActionSelector) clone();
	 	} catch (CloneNotSupportedException cnse){
			return null;
	 	}
 	}

	 public void setApproachLimit(int limit){
		 approachLimit = limit;
	 }

	 
}