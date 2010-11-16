package mrl.ai.monster;

import mrl.action.Action;
import mrl.action.ActionFactory;
import mrl.action.monster.MonsterWalk;
import mrl.actor.Actor;
import mrl.ai.ActionSelector;
import mrl.ai.MonsterAI;
import mrl.ai.RangedAttack;
import mrl.level.Cell;
import mrl.monster.Monster;
import sz.util.Debug;
import sz.util.Position;
import sz.util.Util;


public class WanderToPlayerAI extends MonsterAI{
	/** This AI is used by those enemies that just walk until they find
	 * the player, optionally performing a ranged attack 
	 * him when he is in range */
	
	
	public Action selectAction(Actor who){
		Debug.doAssert(who instanceof Monster, "WanderToPlayerAI selectAction");
		Monster aMonster = (Monster) who;
		int directionToPlayer = aMonster.starePlayer();
		if (directionToPlayer == -1){
			//Wander aimlessly 
        	int direction = Util.rand(0,7);
	     	Action ret = new MonsterWalk();
	     	ret.setDirection(direction);
	     	return ret;
		} else {
			int distanceToPlayer = Position.flatDistance(aMonster.getPosition(), aMonster.getLevel().getPlayer().getPosition());
			//Decide if will attack the player or walk to him
			if (Util.chance(50)){
				//Will try to attack the player
				if (rangedAttacks != null && aMonster.playerInRow()){
					//Try
					for (int i = 0; i < rangedAttacks.size(); i++){
						RangedAttack ra = (RangedAttack)rangedAttacks.elementAt(i);
						if (distanceToPlayer <= ra.getRange())
							if (Util.chance(ra.getFrequency())){
								Action ret = ActionFactory.getActionFactory().getAction(ra.getAttackId());
								ret.setDirection(directionToPlayer);
								return ret;
							}
					}
				}
			}
			// Couldnt attack the player, move to him
        	Action ret = new MonsterWalk();
            ret.setDirection(directionToPlayer);
            Cell currentCell = aMonster.getLevel().getMapCell(aMonster.getPosition());
            Cell destinationCell = aMonster.getLevel().getMapCell(
				Position.add(
					aMonster.getPosition(),
					Action.directionToVariation(directionToPlayer)
				));

			if (destinationCell == null || currentCell == null){
				ret.setDirection(Util.rand(0,7));
				return ret;
			}
            if ((destinationCell.isSolid() && !aMonster.isEthereal()) ||
				destinationCell.getHeight() > currentCell.getHeight()+aMonster.getLeaping() + 1 )
				ret.setDirection(Util.rand(0,7));
	     	return ret;
		}

	 }

	 public String getID(){
		 return "WANDER";
	 }

     public ActionSelector derive(){
    	 
 		try {
	 		return (ActionSelector) clone();
	 	} catch (CloneNotSupportedException cnse){
			return null;
	 	}
 	}

     
}