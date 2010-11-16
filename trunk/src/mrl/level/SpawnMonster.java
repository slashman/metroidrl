package mrl.level;

import mrl.*;
import mrl.action.*;
import sz.util.*;

public class SpawnMonster extends Action{
	private static SpawnMonster singleton = new SpawnMonster();
	public String getID(){
		return "SpawnMonster";
	}

	public void execute(){
		MLevel level = performer.getLevel();
		Respawner perf = (Respawner) performer;
		if (Util.chance(perf.getProb()))
			level.respawn();
	}

	public static SpawnMonster getAction(){
		return singleton;
	}
}