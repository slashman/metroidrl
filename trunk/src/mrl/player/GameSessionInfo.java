package mrl.player;

import java.io.Serializable;
import java.util.*;

import mrl.monster.*;


public class GameSessionInfo implements Serializable{
	private MPlayer player;
	private Monster killerMonster;
	private int deathCause = -1;
	private int turns;
	private long goldCount;
	private int deathLevel;
	private String deathLevelDescription;
	
	private Vector history = new Vector();
	
	public void addHistoryItem(String desc){
		history.add(desc);
	}

	public int getTurns(){
		return turns;

    }

    public void increaseTurns(){
    	turns ++;
    }

	public final static int
		KILLED = 0,
		DROWNED = 1,
		QUIT = 2,
		SMASHED = 3,
		STRANGLED_BY_ARMOR = 4,
		BURNED_BY_LAVA = 5,
		ASCENDED = 6,
		ENDLESS_PIT = 7,
		POISONED_TO_DEATH = 8;

	private Hashtable deathCount = new Hashtable();


	public MPlayer getPlayer() {
		return player;
	}

	public void setPlayer(MPlayer value) {
		player = value;
	}

/*	public Monster getKillerMonster() {
		return killerMonster;
	}*/

	public void setKillerMonster(Monster value) {
		killerMonster = value;
	}

	public void addDeath(MonsterDefinition who){
		MonsterDeath md = (MonsterDeath) deathCount.get(who.getID());
		if (md == null)
			deathCount.put(who.getID(), new MonsterDeath(who.getDescription()));
		else {
			md.increaseDeaths();
		}
    }

	public void addGold (int val){
		goldCount += val;
	}
	
	public long getGoldCount(){
		return goldCount;
	}
	public Hashtable getDeathCount() {
		return deathCount;
	}

	public String getShortDeathString(){
		switch (deathCause){
			case KILLED:
				return "Killed by a "+killerMonster.getDescription();
			case DROWNED:
				return "Drowned";
			case SMASHED:
				return "Smashed";
			case QUIT:
				return "Ran away crying";
			case STRANGLED_BY_ARMOR:
				return "Strangled inside an armor";
			case BURNED_BY_LAVA:
				return "Burned to the death by hot lava";
			case ASCENDED:
				return "Completed the mission";
			case ENDLESS_PIT:
				return "Fell into an endless pit";
			case POISONED_TO_DEATH:
				return "Poisoned to the death";
		}
		return "Perished...";
	}

	public String getDeathString(){
		switch (deathCause){
			case KILLED:
				return "Killed by a "+killerMonster.getDescription();
			case DROWNED:
				return "drowned to the death";
			case QUIT:
				return "Mission Aborted";
			case STRANGLED_BY_ARMOR:
				return "Was stranged inside an armor";
			case ASCENDED:
				return "Completed the mission... probably...";
			case ENDLESS_PIT:
				return "Fell into an endless pit";
			case POISONED_TO_DEATH:
				return "Poisoned to the death";
		}
		return "perished...";
	}


	public void setDeathCause(int value) {
		deathCause = value;
	}

	public int getTotalDeathCount(){
		Enumeration x = deathCount.elements();
		int acum = 0;
		while (x.hasMoreElements())
			acum+= ((MonsterDeath)x.nextElement()).getTimes();
		return acum;
	}

	

	public String getDeathLevelDescription() {
		return deathLevelDescription;
	}

	public void setDeathLevelDescription(String deathLevelDescription) {
		this.deathLevelDescription = deathLevelDescription;
	}
	
	public Vector getHistory(){
		return history;
	}
}