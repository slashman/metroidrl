package mrl.monster;

import mrl.ai.*;
import mrl.ui.*;
import mrl.ui.consoleUI.CharImage;

public class MonsterDefinition {
	private String ID;
	private String description;
	private String longDescription;
	private /*transient*/ Appearance appearance;
	private String wavOnHit;
	private ActionSelector defaultSelector;
	private CharImage portrait;
	
	private int maxHits;
	private int attack = 1;
	private int sightRange = 10;
	private int leaping; //Capability of moving to an higher cell
	private boolean ethereal; // Walks thru solid
	private boolean canSwim;
	
	
	private int minLevel, maxLevel;
	private int attackCost = 50, walkCost = 50;
	private int evadeChance;
	private String evadeMessage;
	private int autorespawnCount;
	/* Debe ser calculado en la UI
	private ListItem sightListItem;
	*/

	public void setWavOnHit(String value){
		wavOnHit = value;
	}
	
	public String getWavOnHit(){
		return wavOnHit;
	}
	
	public int getEvadeChance() {
		return evadeChance;
	}

	public void setEvadeChance(int evadeChance) {
		this.evadeChance = evadeChance;
	}

	public String getEvadeMessage() {
		return evadeMessage;
	}

	public void setEvadeMessage(String evadeMessage) {
		this.evadeMessage = evadeMessage;
	}

	public MonsterDefinition (String pID){
		//sightListItem = new BasicListItem(' ',0, "");
		ID = pID;
    }

	public String getID() {
		return ID;
	}

	public int getAttack() {
		return attack;
	}

	public String getDescription() {
		return description;
	}

	public int getSightRange() {
		return sightRange;
	}

	public int getLeaping() {
		return leaping;
	}

	

	public boolean isEthereal() {
		return ethereal;
	}

	public boolean isCanSwim() {
		return canSwim;
	}

	

	
	

	public int getMaxHits() {
		return maxHits;
	}

	public Appearance getAppearance() {
		return appearance;
	}

	public ActionSelector getDefaultSelector() {
		return defaultSelector;
	}

	public void setAppearance(Appearance appearance) {
		this.appearance = appearance;
		/*if (appearance != null){
			((BasicListItem)sightListItem).setIndex(appearance.getChar());
			((BasicListItem)sightListItem).setIndexColor(appearance.getColor());
		}*/
	}

	

	public void setCanSwim(boolean canSwim) {
		this.canSwim = canSwim;
	}

	public void setDefaultSelector(ActionSelector defaultSelector) {
		this.defaultSelector = defaultSelector;
	}

	public void setDescription(String description) {
		this.description = description;
		/*if (appearance != null)
			((BasicListItem)sightListItem).setRow(getDescription());*/
	}

	public void setEthereal(boolean ethereal) {
		this.ethereal = ethereal;
	}

	public void setLeaping(int leaping) {
		this.leaping = leaping;
	}

	public void setMaxHits(int maxHits) {
		this.maxHits = maxHits;
	}

	

	public void setSightRange(int sightRange) {
		this.sightRange = sightRange;
	}

	public void setAttack(int touchDamage) {
		this.attack = touchDamage;
	}

	

	/*public ListItem getSightListItem() {
		return sightListItem;
	}

	public void setSightListItem(ListItem sightListItem) {
		this.sightListItem = sightListItem;
	}*/

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}

	public int getAttackCost() {
		return attackCost;
	}

	public void setAttackCost(int attackCost) {
		this.attackCost = attackCost;
	}

	public int getWalkCost() {
		return walkCost;
	}

	public void setWalkCost(int walkCost) {
		this.walkCost = walkCost;
	}

	public int getAutorespawnCount() {
		return autorespawnCount;
	}

	public void setAutorespawnCount(int autorespawnCount) {
		this.autorespawnCount = autorespawnCount;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public CharImage getPortrait() {
		return portrait;
	}

	public void setPortrait(CharImage portrait) {
		this.portrait = portrait;
	}


}