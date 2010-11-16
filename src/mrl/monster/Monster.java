package mrl.monster;

import mrl.action.*;
import mrl.actor.*;
import mrl.level.Emerger;
import mrl.level.EmergerAI;
import mrl.player.MPlayer;
import mrl.ui.Appearance;
import mrl.ui.consoleUI.CharImage;
import sz.util.*;


public class Monster extends Actor implements Cloneable{
	//Attributes
	private String defID;
	private transient MonsterDefinition definition;
	private String featurePrize;
	private int frozenCounter;
	protected int hits;
	private boolean visible = true;
	private boolean wasSeen = false;
	
	public Monster (MonsterDefinition md){
 		definition = md;
 		defID = md.getID();
 		//selector = md.getDefaultSelector();
 		selector = md.getDefaultSelector().derive();
 		hits = md.getMaxHits();
	}
	
	public String getLongDescription(){
		return definition.getLongDescription();
		
	}
	
	public void act(){
		if (isFrozen()){
			updateStatus();
			return;
		}
		super.act();
		wasSeen = false;
	}
	
	public boolean canSwim(){
		return getDefinition().isCanSwim();
	}
	public Object clone(){
		try {
        	return super.clone();
		} catch (Exception x)
		{
			return null;
		}

	}

	public void damage(int dam){
		if (Util.chance(getEvadeChance())){
			if (wasSeen())
				level.addMessage("The "+getDescription()+" "+getEvadeMessage());
			return;
		}
		if (isFrozen())
			dam *= 2;
		
		if (this == level.getBoss()){
			if (dam != 0){
				dam /= 3;
				if (dam == 0)
					dam = 1;
			}
		}
		
		hits -= dam;

		if (isDead()){
			if (this == level.getBoss()){
				level.removeBoss();
				level.getPlayer().addHistoricEvent("vanquished "+this.getDescription()+" on the "+level.getDescription());
				level.anihilate();
				level.removeRespawner();
				//level.getPlayer().addSoulPower(Util.rand(10,20)*level.getLevelNumber());
			} else {
				setPrize();
			}
			if (featurePrize != null && !level.getMapCell(getPosition()).isSolid())
				level.addFeature(featurePrize, getPosition());
			
			die();
			level.getPlayer().getGameSessionInfo().addDeath(getDefinition());
		}
	}

	public void damageWithWeapon(int dam){
		damage(dam);
	}

	public void die(){
		super.die();
		level.removeMonster(this);
		if (getAutorespawncount() > 0){
			Emerger em = new Emerger(MonsterFactory.getFactory().buildMonster(getDefinition().getID()), getPosition(), getAutorespawncount());
			level.addActor(em);
			em.setSelector(new EmergerAI());
			em.setLevel(getLevel());
		}
	}

	public void freeze(int cont){
		frozenCounter = cont;
	}

	public Appearance getAppearance(){
		return getDefinition().getAppearance();
	}

	public int getAttack(){
		return getDefinition().getAttack();
	}

	public int getAttackCost() {
		return getDefinition().getAttackCost();
	}

	

	public int getAutorespawncount(){
		return getDefinition().getAutorespawnCount();
	}

	private MonsterDefinition getDefinition(){
		if (definition == null){
			definition = MonsterFactory.getFactory().getDefinition(defID);
		}
		return definition;
	}

	public String getDescription(){
	//This may be flavored with specific monster daya
		return getDefinition().getDescription();
	}
	
	public int getEvadeChance(){
		return getDefinition().getEvadeChance();
	}

	public String getEvadeMessage(){
		return getDefinition().getEvadeMessage();
	}
	public String getFeaturePrize() {
		return featurePrize;
	}

	public int getFreezeResistance(){
		return 0; //placeholder
	}

	public int getHits(){
		return hits;
	}
	
	public String getID(){
		return getDefinition().getID();
	}

	public int getLeaping(){
		return getDefinition().getLeaping();
	}

	

	public int getWalkCost() {
		return getDefinition().getWalkCost();
	}

 	public String getWavOnHit(){
		return getDefinition().getWavOnHit();
	}

	/*public ActionSelector getSelector(){
		return selector;
		//return definition.getDefaultSelector();
	}*/

	public boolean isDead(){
		return hits <= 0;
	}

	public boolean isEthereal(){
		return getDefinition().isEthereal();
	}

	public boolean isFrozen(){
		return frozenCounter > 0;
	}

	public boolean isInWater(){
		if (level.getMapCell(getPosition())!= null)
			return level.getMapCell(getPosition()).isWater();
		else
			return false;
	}
	
	

	/*public ListItem getSightListItem(){
		return definition.getSightListItem();
	}*/

	public boolean isVisible(){
		return visible;
	}
	
	public boolean playerInRow(){
		Position pp = level.getPlayer().getPosition();
		/*if (!playerInRange())
			return false;*/
		//Debug.say("pp"+pp);
		//Debug.say(getPosition());
		if (pp.x == getPosition().x || pp.y == getPosition().y)
			return true;
		if (pp.x - getPosition().x == pp.y - getPosition().y)
			return true;
		return false;
	}
	
	public void setFeaturePrize(String value) {
		featurePrize = value;
	}
	
	private void setPrize(){
		MPlayer p = level.getPlayer();
		String [] prizeList = null;
		if (p.getAbsoluteEnergy() < p.getMaxEnergy() && Util.chance(20)){
			setFeaturePrize("SMALL_ENERGY");
			return;
		}
		if (p.getAbsoluteEnergy() < p.getMaxEnergy() && Util.chance(10)){
			setFeaturePrize("BIG_ENERGY");
			return;
		}
		if (p.getMissiles() < p.getMissilesCapacity() && Util.chance(20)){
			setFeaturePrize("MISSILES");
			return;
		}
		if (p.getSuperMissiles() < p.getSuperMissilesCapacity() && Util.chance(10)){
			setFeaturePrize("SUPERMISSILES");
			return;
		}
		if (p.getPowerBombs() < p.getPowerBombsCapacity() && Util.chance(10)){
			setFeaturePrize("POWER_BOMBS");
			return;
		}
	}
	
	public void setVisible(boolean value){
		visible = value;
	}

	public void setWasSeen(boolean value){
		wasSeen = true;
	}
	
	public int starePlayer(){
		/** returns the direction in which the player is seen */
		if (level.getPlayer() == null || level.getPlayer().getPosition().z != getPosition().z || level.getPlayer().getHeight() != level.getMapCell(getPosition()).getHeight())
			return -1;
		if (Position.flatDistance(level.getPlayer().getPosition(), getPosition()) <= getDefinition().getSightRange()){
			Position pp = level.getPlayer().getPosition();
			if (pp.x == getPosition().x){
				if (pp.y > getPosition().y){
					return Action.DOWN;
				} else {
                     return Action.UP;
				}
			} else
			if (pp.y == getPosition().y){
				if (pp.x > getPosition().x){
					return Action.RIGHT;
				} else {
					return Action.LEFT;
				}
			} else
			if (pp.x < getPosition().x){
				if (pp.y > getPosition().y)
					return Action.DOWNLEFT;
				else
					return Action.UPLEFT;
			} else {
                if (pp.y > getPosition().y)
					return Action.DOWNRIGHT;
				else
					return Action.UPRIGHT;
			}
		}
		return -1;
	}
	
	public void updateStatus(){
		if (isFrozen()){
			frozenCounter--;
		}
	}
	
	public boolean waitsPlayer(){
		return false;
	}
	
	public boolean wasSeen(){
		return wasSeen;
	}
	
	public int getHeight(){
		//Will be replaced to something more sophisticated with flying enemies and stuff
		return level.getMapCell(getPosition()).getHeight();
	}
	
	public CharImage getPortrait(){
		return getDefinition().getPortrait();
	}
}