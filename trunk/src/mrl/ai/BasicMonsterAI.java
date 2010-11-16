package mrl.ai;

import java.util.Iterator;

import mrl.action.Action;
import mrl.action.ActionFactory;
import mrl.action.monster.MonsterCharge;
import mrl.action.monster.MonsterMissile;
import mrl.action.monster.MonsterWalk;
import mrl.action.monster.SummonMonster;
import mrl.actor.Actor;
import mrl.level.Cell;
import mrl.monster.Monster;

import sz.util.Position;
import sz.util.Util;

public class BasicMonsterAI extends MonsterAI{
	private boolean isStationary;
	private int waitPlayerRange;
	private int approachLimit = 0;
	private int patrolRange = 0;
	private int chargeCounter = 99;
	private int lastDirection = -1;
	private boolean changeDirection;
	private Position lastPlayerPosition;
	
	public Action selectAction(Actor who){
		Monster aMonster = (Monster) who;
		if (lastPlayerPosition == null){
			lastPlayerPosition = who.getLevel().getPlayer().getPosition();
		}
		int directionToPlayer = aMonster.starePlayer();
		int playerDistance = Position.flatDistance(aMonster.getPosition(), aMonster.getLevel().getPlayer().getPosition());
		if (patrolRange >0 && playerDistance > patrolRange){
			if (lastDirection == -1 || changeDirection){
				lastDirection = Util.rand(0,7);
				changeDirection = false;
			}
			Action ret = new MonsterWalk();
	     	ret.setDirection(lastDirection);
	     	lastPlayerPosition = null;
	     	return ret;
		}
		
		if (directionToPlayer == -1) {
			if (isStationary || waitPlayerRange > 0) {
				lastPlayerPosition = null;
				return null;
			} else {
				int direction = Util.rand(0,7);
				Cell destination = who.getLevel().getMapCell(Position.add(who.getPosition(), Action.directionToVariation(direction)));
				Cell current = who.getLevel().getMapCell(who.getPosition());
				if (destination != null && current != null && destination.getHeight() < current.getHeight())
					if (Util.chance(95)){
						lastPlayerPosition = null;
						return null;
					}
				Action ret = new MonsterWalk();
	            ret.setDirection(direction);
	            lastPlayerPosition = null;
	            return ret;
			}
		} else {
			if (waitPlayerRange > 0 && playerDistance > waitPlayerRange){
				lastPlayerPosition = null;
				return null;
			}
			
			
			if (playerDistance < approachLimit){
				//get away from player
				int direction = Action.toIntDirection(Position.mul(Action.directionToVariation(directionToPlayer), -1));
				Cell destination = who.getLevel().getMapCell(Position.add(who.getPosition(), Action.directionToVariation(direction)));
				Cell current = who.getLevel().getMapCell(who.getPosition());
				if (destination != null && current != null && destination.getHeight() < current.getHeight())
					if (who.getLevel().getPlayer().getHeight() >= current.getHeight()){
						lastPlayerPosition = null;
						return null;
					}
				Action ret = new MonsterWalk();
	            ret.setDirection(direction);
	            lastPlayerPosition = who.getLevel().getPlayer().getPosition();
	            return ret;
			} else {
				//Randomly decide if will approach the player or attack
				if (aMonster.wasSeen() && rangedAttacks != null && Util.chance(80)){
					//Try to attack the player
					for (Iterator iter = rangedAttacks.iterator(); iter.hasNext();) {
						RangedAttack element = (RangedAttack) iter.next();
						if (element.getRange() >= playerDistance && Util.chance(element.getFrequency())){
							//Perform the attack
							Action ret = ActionFactory.getActionFactory().getAction(element.getAttackId());
							if (element.getChargeCounter() > 0){
								if (chargeCounter == 0){
									chargeCounter = element.getChargeCounter();
								}else{
									chargeCounter --;
									continue;
								}
							}
							
							if (ret instanceof MonsterMissile){
								((MonsterMissile)ret).set(
										element.getAttackType(),
										element.getStatusEffect(),
										element.getRange(),
										element.getAttackMessage(),
										element.getEffectType(),
										element.getEffectID(),
										element.getDamage(),
										element.getEffectWav()
										);
							}else if (ret instanceof MonsterCharge){
								((MonsterCharge)ret).set(element.getRange(), element.getAttackMessage(), element.getDamage(),element.getEffectWav());
							}else if (ret instanceof SummonMonster){
								((SummonMonster)ret).set(element.getSummonMonsterId(), element.getAttackMessage());
							}
							//ret.setPosition(who.getLevel().getPlayer().getPosition());
							if (Util.chance(30)) //Aims correctly, must replace for a monstruous parameter
								lastPlayerPosition = who.getLevel().getPlayer().getPosition();
							ret.setPosition(lastPlayerPosition);
							lastPlayerPosition = who.getLevel().getPlayer().getPosition();
							return ret;
						}
					}
				}
				// Couldnt attack the player, so walk to him
				if (isStationary){
					return null;
				} else {
					Action ret = new MonsterWalk();
					if (!aMonster.isEthereal() && !who.getLevel().isWalkable(Position.add(who.getPosition(), Action.directionToVariation(directionToPlayer))))
						directionToPlayer = Util.rand(0,7);
					
					Cell destination = who.getLevel().getMapCell(Position.add(who.getPosition(), Action.directionToVariation(directionToPlayer)));
					Cell current = who.getLevel().getMapCell(who.getPosition());
					if (destination != null && current != null && destination.getHeight() < current.getHeight())
						if (who.getLevel().getPlayer().getHeight() >= current.getHeight()) {
							lastPlayerPosition = null;
							return null;
						}
					ret.setDirection(directionToPlayer);
					
					lastPlayerPosition = who.getLevel().getPlayer().getPosition();
		            return ret;
				}
			}
		}
	 }

	 public String getID(){
		 return "BASIC_MONSTER_AI";
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
	
	public void setWaitPlayerRange(int limit){
		 waitPlayerRange = limit;
	}
	
	public void setPatrolRange(int limit){
		 patrolRange = limit;
	}
	
	public int getPatrolRange(){
		return patrolRange;
	}

	public void setStationary(boolean isStationary) {
		this.isStationary = isStationary;
	}

	public void setChangeDirection(boolean value) {
		changeDirection = value;
	}
	 
}