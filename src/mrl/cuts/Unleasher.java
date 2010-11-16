package mrl.cuts;

import java.io.Serializable;

import mrl.game.Game;
import mrl.level.MLevel;


public abstract class Unleasher implements Serializable {
	protected boolean enabled = true;
	
	public boolean enabled(){
		return enabled;
	}
	
	public void disable(){
		enabled = false;
	}
	
	public abstract void unleash(MLevel level, Game game);
	/*This must check condition first*/
	
}
