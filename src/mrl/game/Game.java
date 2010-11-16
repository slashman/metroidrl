package mrl.game;


import java.util.*;

import mrl.Main;
import mrl.actor.*;
import mrl.item.Item;
import mrl.level.*;
import mrl.levelgen.*;
import mrl.player.*;
import mrl.ui.*;

//import crl.action.vkiller.Whip;
import sz.fov.FOV;
import sz.util.*;


public class Game implements CommandListener, PlayerEventListener, java.io.Serializable{
	//Configuration
	//private transient ConsoleSystemInterface si;
	private transient UserInterface ui;
	
	private Dispatcher dispatcher;
	private MPlayer player;
	private MLevel currentLevel;
	
	private Hashtable /*Level*/  storedLevels = new Hashtable();
	private boolean endGame;
	private long turns;
	private boolean isDay = true;
	private long timeSwitch;
	private String[] levelPath;
	private Hashtable /*LevelMetaData*/ hashLevelMap;

	public void commandSelected (int commandCode){
		if (commandCode == CommandListener.QUIT){
			finishGame();
		} else if (commandCode == CommandListener.SAVE) {
			GameFiles.saveGame(this, player);
			exitGame();
		}
	}

	private void run(){
		//si.cls();
		player.setFOV(new FOV());
		UserInterface.getUI().showMessage("{"+player.getName()+".log}, Samus has landed on the dry lands on Juran... Press '?' for Help");
		ui.refresh();
		while (!endGame){
			Actor actor = dispatcher.getNextActor();
            if (actor == player){
            	player.darken();
            	player.see();
            	ui.refresh();
				player.getGameSessionInfo().increaseTurns();
				player.checkDeath();
				player.getLevel().checkUnleashers(this);
			}
            if (endGame)
            	break;
			actor.act();
			if (endGame)
            	break;
			actor.getLevel().getDispatcher().returnActor(actor);
			 
			if (actor == player){
				if (currentLevel != null)
					currentLevel.updateLevelStatus();
				//ui.refresh();
				turns++;
			}
		}
	}
	 	
	public void resume(){
		player.setSelector(ui.getSelector());
		ui.setPlayer(player);
		ui.addCommandListener(this);
		ui.setGameOver(false);
		player.getLevel().addActor(player);
		player.setPlayerEventListener(this);
		endGame = false;
		turns = player.getGameSessionInfo().getTurns();
		run();
	}
	
	public void setPlayer(MPlayer p){
		player = p;
		player.setLevel(currentLevel);
		player.setFOV(new FOV());
		currentLevel.setPlayer(player);
		if (player.getGameSessionInfo() == null)
			player.setGameSessionInfo(new GameSessionInfo());
		player.setSelector(ui.getSelector());
		ui.setPlayer(player);
		player.setPlayerEventListener(this);
	}
	
	public void newGame(){
		player = PlayerGenerator.thus.generatePlayer();
		player.setGameSessionInfo(new GameSessionInfo());
		player.setSelector(ui.getSelector());
		ui.setPlayer(player);
		ui.addCommandListener(this);
		ui.setGameOver(false);
		player.setPlayerEventListener(this);
		//generateLevelPath();
		generateLevelMap();
		Display.thus.showIntro(player);
		
		loadLevel("LANDING", 0);
		//loadLevel("DECKS", 0);
		
		turns = 0;
		timeSwitch = 600;
		run();
	}
	
	public void prologue(){
		player = PlayerGenerator.thus.createSpecialPlayer("CHRIS");
		player.setGameSessionInfo(new GameSessionInfo());
		player.setSelector(ui.getSelector());
		player.setDoNotRecordScore(true);
		ui.setPlayer(player);
		ui.addCommandListener(this);
		ui.setGameOver(false);
		player.setPlayerEventListener(this);
		//generatePrologueLevelPath();
		//Display.thus.showIntro(player);
		loadLevel("PROLOGUE_KEEP");
		turns = 0;
		timeSwitch = 900;
		run();
	}
	
	/*private void generateLevelPath(){
		Vector levels = new Vector(20);
		String [][] order = new String [][]{
			{"SURFACE", "*"},
		};
		levels.add("LANDING");
		
		for (int i = 0; i < order.length; i++){
			int n = Util.rand(2,4);
			if (order[i][1].startsWith("*"))
				n = 1;
			for (int j = 0; j < n;j++)
				levels.add(order[i][0]+j);
		}
		levelPath = (String[]) levels.toArray(new String[levels.size()]);
		storedLevels = new Hashtable();
	}*/
	
	private void showMetaData(String id, String parentId, int depth){
		for (int i = 0; i < depth; i++){
			System.out.print(" ");
		}
		System.out.println("get "+id);
		LevelMetaData lmd = (LevelMetaData) hashLevelMap.get(id);
		System.out.println(lmd.levelName);
		if (lmd.prizes.size() != 0)
			for (int i = 0; i < depth; i++){
				System.out.print(" ");
			}
		for (int i = 0; i < lmd.prizes.size(); i++){
			Prize p = (Prize)lmd.prizes.get(i);
			System.out.print(p.getPriceId()+"-");
		}
		if (lmd.prizes.size() != 0)
			System.out.println();
		for (int i = 0; i < lmd.exits.size(); i++){
			for (int x = 0; x < depth; x++){
				System.out.print(" ");
			}
			System.out.println("Exit to "+lmd.exits.get(i)+" with "+lmd.exitDoors.get(i));
			if (!((String)lmd.exits.get(i)).equals(parentId)){
				showMetaData((String)lmd.exits.get(i), lmd.levelName, depth+1);
			}
		}
	}
	
	private String getStandardRoomType() {
		if (Util.chance(50))
			return "_BASE";
		else
			return "_SQUARES";
	}
	
	private LevelMetaData addMetaData(String id, int number){
		String roomType = id;
		LevelMetaData md = new LevelMetaData(roomType+number);
		hashLevelMap.put(roomType+number, md);
		return md;
	}
	
	private void generateLevelMap(){
		hashLevelMap = new Hashtable();
		//hashLevelMap.put("LANDING", new LevelMetaData("LANDING"));
		//hashLevelMap.put("SURFACE",new LevelMetaData("SURFACE"));
		//hashLevelMap.put("BASE1",new LevelMetaData("BASE1"));
		
		LevelMetaData ATO = addMetaData("_SQUARES", 1);
		LevelMetaData BAC = addMetaData("_BASE", 2);
		LevelMetaData CHE = addMetaData("_SQUARES", 3);
		LevelMetaData GEN = addMetaData("_BASE", 4);
		
		LevelMetaData[] lastRoom = new LevelMetaData[]{
			ATO, 
			BAC,
			CHE,
			GEN
		};
		ATO.levelDescription = getDescription(0);
		BAC.levelDescription = getDescription(1);
		CHE.levelDescription = getDescription(2);
		GEN.levelDescription = getDescription(3);
		ATO.addNextRoom("DECKS");
		BAC.addNextRoom("DECKS");
		CHE.addNextRoom("DECKS");
		GEN.addNextRoom("DECKS");
		ATO.musicKey = "ATO";
		BAC.musicKey = "BAC";
		CHE.musicKey = "CHE";
		GEN.musicKey = "GEN";
		ATO.dwellers = getDwellers(0, 1);
		BAC.dwellers = getDwellers(1, 2);
		CHE.dwellers = getDwellers(2, 3);
		GEN.dwellers = getDwellers(3, 4);
		ATO.itemDistribution = getItemDistribution(0, 1);
		BAC.itemDistribution = getItemDistribution(1, 2);
		CHE.itemDistribution = getItemDistribution(2, 3);
		GEN.itemDistribution = getItemDistribution(3, 4);
		
		ATO.defaultFloorCell = getDefaultFloorCell(0);
		ATO.defaultWallCell = getDefaultWallCell(0);
		ATO.facets = getFacets(0);
		BAC.defaultFloorCell = getDefaultFloorCell(1);
		BAC.defaultWallCell = getDefaultWallCell(1);
		BAC.facets = getFacets(1);
		CHE.defaultFloorCell = getDefaultFloorCell(2);
		CHE.defaultWallCell = getDefaultWallCell(2);
		CHE.facets = getFacets(2);
		GEN.defaultFloorCell = getDefaultFloorCell(3);
		GEN.defaultWallCell = getDefaultWallCell(3);
		GEN.facets = getFacets(3);
		String[] lanesStr = new String[] {
				"ATO", 
				"BAC",
				"CHE",
				"GEN"
			};
		
		Lane[] lanesInfo = new Lane[] {
				new Lane("ATO"), 
				new Lane("BAC"),
				new Lane("CHE"),
				new Lane("GEN")
			};
		/*
		 * lane[0] = ATO
		 * lane[1] = BAC
		 * lane[2] = CHE
		 * lane[3] = GEN
		 * */
		Vector barriers = new Vector();
		barriers.add("VARIA_SUIT");
		barriers.add("GRAVITY_SUIT");
		barriers.add("MORPH_ENERGY");
		barriers.add("MISSILE_TANK");
		barriers.add("SUPERMISSILE_TANK");
		barriers.add("HI_JUMP");
		barriers.add("BOMB_ENERGY");
		barriers.add("POWER_BOMB_TANK");
		
		/*Vector rooms = new Vector();
		rooms.add("BONY");*/
		
		Vector freeUpgrades = new Vector();
		freeUpgrades.add("ENERGY_TANK");
		freeUpgrades.add("ENERGY_TANK");
		freeUpgrades.add("ENERGY_TANK");
		freeUpgrades.add("ENERGY_TANK");
		freeUpgrades.add("ENERGY_TANK");
		freeUpgrades.add("ENERGY_TANK");
		freeUpgrades.add("ENERGY_TANK");
		freeUpgrades.add("ENERGY_TANK");
		freeUpgrades.add("ENERGY_TANK");
		freeUpgrades.add("ENERGY_TANK");
		freeUpgrades.add("ENERGY_TANK");
		freeUpgrades.add("ENERGY_TANK");
		freeUpgrades.add("ENERGY_TANK");
		
		freeUpgrades.add("RESERVE_TANK");
		freeUpgrades.add("RESERVE_TANK");
		freeUpgrades.add("SPIDERBALL");
		freeUpgrades.add("XRAYVISOR");
		freeUpgrades.add("SPRINGBALL");
		freeUpgrades.add("SPACE_JUMP");
		freeUpgrades.add("WAVE_BEAM");
		
		
		Vector freeUpgrades2 = new Vector();
		freeUpgrades2.add("ENERGY_TANK");
		freeUpgrades2.add("ENERGY_TANK");
		freeUpgrades2.add("RESERVE_TANK");
		freeUpgrades2.add("RESERVE_TANK");
		freeUpgrades2.add("SCREW_ATTACK");
		freeUpgrades2.add("PLASMA_BEAM");
		freeUpgrades2.add("ICE_BEAM");
		
		/*hashLevelMap.put("UPGRADE_VARIA_SUIT", new LevelMetaData("UPGRADE_VARIA_SUIT"));
		hashLevelMap.put("UPGRADE_GRAVITY_SUIT", new LevelMetaData("UPGRADE_GRAVITY_SUIT"));
		hashLevelMap.put("UPGRADE_MORPH", new LevelMetaData("UPGRADE_MORPH"));
		hashLevelMap.put("UPGRADE_MISSILE", new LevelMetaData("UPGRADE_MISSILE"));
		hashLevelMap.put("UPGRADE_SUPERMISSILE", new LevelMetaData("UPGRADE_SUPERMISSILE"));
		hashLevelMap.put("UPGRADE_HIJUMP", new LevelMetaData("UPGRADE_HIJUMP"));
		hashLevelMap.put("UPGRADE_BOMB", new LevelMetaData("UPGRADE_BOMB"));
		hashLevelMap.put("UPGRADE_POWER_BOMB", new LevelMetaData("UPGRADE_POWER_BOMB"));
		*/
		
		int baseID = 5;
		for (int lane = 0; lane < 4; lane++){
			int meat = Util.rand(4,6);
			for (int i = 0; i < meat; i++){
				//Place a normal room
				LevelMetaData next = addMetaData(getStandardRoomType(), baseID);
				baseID++;
				next.musicKey = getMusicKey(lane);
				next.defaultFloorCell = getDefaultFloorCell(lane);
				next.defaultWallCell = getDefaultWallCell(lane);
				next.facets = getFacets(lane);
				next.dwellers = getDwellers(lane, baseID);
				next.itemDistribution = getItemDistribution(lane, baseID);
				lastRoom[lane].addNextRoom(next.levelName);
				next.addNextRoom(lastRoom[lane].levelName);
				//lastRoom[lane] = next; Forks!
				if (Util.chance(40)){
					lastRoom[lane] = next;
				}
				next.levelDescription = getDescription(lane);
			}
		}
		
		while (barriers.size() != 0 || freeUpgrades.size() != 0){
			//Place some meat
			int meat = Util.rand(2,3);
			for (int i = 0; i < meat; i++){
				int lane = Util.rand(0,3);
				//Place a normal room
				LevelMetaData next = addMetaData(getStandardRoomType(), baseID);
				baseID++;
				next.musicKey = getMusicKey(lane);
				next.defaultFloorCell = getDefaultFloorCell(lane);
				next.defaultWallCell = getDefaultWallCell(lane);
				next.facets = getFacets(lane);
				next.dwellers = getDwellers(lane, baseID);
				next.itemDistribution = getItemDistribution(lane, baseID);
				lastRoom[lane].addNextRoom(next.levelName);
				next.addNextRoom(lastRoom[lane].levelName);
				//lastRoom[lane] = next; Forks!
				if (Util.chance(40)){
					lastRoom[lane] = next;
				}
				next.levelDescription = getDescription(lane);
			}
			
			int obstacles = Util.rand(2,4);
			// Place a random number of obstacles
			for (int i = 0; i< obstacles;){
				int lane = Util.rand(0,3);
				if (barriers.size() == 0){
					break;
				}
				//Pick a random barrier
				String barrier = (String)Util.randomElementOf(barriers);
				//Check for not placing a barrier for a superior upgrade before laying down that upgrade
				if (barrier.equals("POWER_BOMB_TANK") && barriers.contains("BOMB_ENERGY"))
					continue;
				if (barrier.equals("BOMB_ENERGY") && barriers.contains("MORPH_ENERGY"))
					continue;
				if (barrier.equals("GRAVITY_SUIT") && barriers.contains("VARIA_SUIT"))
					continue;
				if (barrier.equals("SUPERMISSILE_TANK") && barriers.contains("MISSILE_TANK"))
					continue;
				//Check for not placing a superior upgrade before a lower upgrade barrier
				if (barrier.equals("POWER_BOMB_TANK") && !lanesInfo[lane].hasBarrier("BOMB_ENERGY"))
					continue;
				if (barrier.equals("BOMB_ENERGY") && !lanesInfo[lane].hasBarrier("MORPH_ENERGY"))
					continue;
				if (barrier.equals("GRAVITY_SUIT") && !lanesInfo[lane].hasBarrier("VARIA_SUIT"))
					continue;
				if (barrier.equals("SUPERMISSILE_TANK") && !lanesInfo[lane].hasBarrier("MISSILE_TANK"))
					continue;

				//Checks sucessful
				i++;
				
				//Place the upgrade for the barrier at the random lane
				Prize upgrade = new Prize(barrier);
				lastRoom[lane].addPrize(upgrade);
				lanesStr[lane] = lanesStr[lane].concat("["+barrier+"]");
				
				//Place the barrier on a different lane
				int lane2 = lane;
				while (lane2 == lane){
					lane2 = Util.rand(0,3);
				}
				LevelMetaData next = new LevelMetaData("BARRIER_"+barrier);
				hashLevelMap.put(next.levelName, next);
				lastRoom[lane2].addNextRoom(next.levelName);
				lanesInfo[lane2].addBarrier(barrier);
				next.addNextRoom(lastRoom[lane2].levelName);
				lastRoom[lane2] = next;
				// Place a standard level after the barrier ? Unneeded
				/*next = new LevelMetaData(getStandardRoomType()+""+baseID);
				hashLevelMap.put(next.levelName, next);
				baseID++;
				lastRoom[lane2].addNextRoom(next.levelName);
				next.levelDescription = getDescription(lane2);
				next.musicKey = getMusicKey(lane2);
				next.dwellers = getDwellers(lane2, baseID);
				next.addNextRoom(lastRoom[lane2].levelName);
				lastRoom[lane2] = next;*/
				barriers.remove(barrier);

			}
			
			//Place a normal room after the upgrades? (?)
			/*
			int lane = Util.rand(0,3);
			LevelMetaData next = new LevelMetaData(getStandardRoomType()+""+baseID);
			hashLevelMap.put(next.levelName, next);
			baseID++;
			next.musicKey = getMusicKey(lane);
			next.dwellers = getDwellers(lane, baseID);
			lastRoom[lane].addNextRoom(next.levelName);
			next.addNextRoom(lastRoom[lane].levelName);
			lastRoom[lane] = next;
			next.levelDescription = getDescription(lane);*/

			//Place a random number of free upgrades (no barriers for these)
			int upgrades = Util.rand(1,2);
			for (int i = 0; i < upgrades; i++){
				if (freeUpgrades.size() > 0){
					int lane = Util.rand(0,3);
					String prize = (String)Util.randomElementOf(freeUpgrades);
					Prize upgrade = new Prize(prize);
					lastRoom[lane].addPrize(upgrade);
					freeUpgrades.remove(prize);
					if (freeUpgrades.size() == 0 && freeUpgrades2 != null){
						freeUpgrades = freeUpgrades2;
						freeUpgrades2 = null;
					}
				}
			}
		}
		LevelMetaData last = new LevelMetaData("LAST_ATO");
		last.levelDescription = "Atomic Weapon";
		hashLevelMap.put(last.levelName, last);
		last.musicKey = getMusicKey(0);
		lastRoom[0].addNextRoom(last.levelName);
		last.addNextRoom(lastRoom[0].levelName);
		
		last = new LevelMetaData("LAST_BAC");
		last.levelDescription = "Bacterial Weapon";
		hashLevelMap.put(last.levelName, last);
		last.musicKey = getMusicKey(1);
		lastRoom[1].addNextRoom(last.levelName);
		last.addNextRoom(lastRoom[1].levelName);
		
		last = new LevelMetaData("LAST_CHE");
		last.levelDescription = "Chemical Weapon";
		hashLevelMap.put(last.levelName, last);
		last.musicKey = getMusicKey(2);
		lastRoom[2].addNextRoom(last.levelName);
		last.addNextRoom(lastRoom[2].levelName);
		
		last = new LevelMetaData("LAST_GEN");
		last.levelDescription = "Genetic Weapon";
		hashLevelMap.put(last.levelName, last);
		last.musicKey = getMusicKey(3);
		lastRoom[3].addNextRoom(last.levelName);
		last.addNextRoom(lastRoom[3].levelName);
		
		/*
		showMetaData(ATO.levelName,"DECKS", 0 );
		showMetaData(BAC.levelName,"DECKS", 0);
		showMetaData(CHE.levelName,"DECKS", 0);
		showMetaData(GEN.levelName,"DECKS", 0);
		*/
		
		/*hashLevelMap.put(getStandardRoomType()+"1",new LevelMetaData(getStandardRoomType()+"1", new String[]{getStandardRoomType()+"2", getStandardRoomType()+"3"}, new String[]{"BLUE_DOOR", "BLUE_DOOR"}));
		hashLevelMap.put(getStandardRoomType()+"2",new LevelMetaData(getStandardRoomType()+"2", new String[]{getStandardRoomType()+"1"}, new String[]{"BLUE_DOOR"}));
		hashLevelMap.put(getStandardRoomType()+"3",new LevelMetaData(getStandardRoomType()+"3", new String[]{getStandardRoomType()+"4", getStandardRoomType()+"1"}, new String[]{"BLUE_DOOR", "BLUE_DOOR"}));
		hashLevelMap.put(getStandardRoomType()+"4",new LevelMetaData(getStandardRoomType()+"4", new String[]{getStandardRoomType()+"3"}, new String[]{"BLUE_DOOR"}));*/
		storedLevels = new Hashtable();
	}
	
	private String getDescription(int lane){
		return LANE_DESCRIPTIONS[lane];
	}
	
	private String getDefaultFloorCell(int lane){
		return LANE_FLOORS[lane];
	}
	
	private String getFacets(int lane){
		return LANE_FACETS[lane];
	}
	
	private String getDefaultWallCell(int lane){
		return LANE_WALLS[lane];
	}
	
	private String[] getDwellers(int lane, int depth){
		String dwell = "";
		switch (lane){
		case 0:
			if (depth > 25)
				dwell = "RADIATED_BEING:PULSAR_AUTOTURRET";
			else if (depth > 15)
				dwell = "RADIATED_BEING:DEFENSE_BOT:GEEMER";
			else if (depth > 6)
				dwell = "DEFENSE_BOT:GALACTIC_SOLDIER";
			else
				dwell = "GALACTIC_TROOPER:GALACTIC_SOLDIER";
			break;
		case 1:
			if (depth > 25)
				dwell = "BACTROID:GUARDIAN";
			else if (depth > 15)
				dwell = "GUARDIAN:BIO_TROOPER:GEEMER";
			else if (depth > 6)
				dwell = "BIO_TROOPER:GALACTIC_SOLDIER";
			else
				dwell = "GALACTIC_TROOPER:GALACTIC_SOLDIER";
			break;
		case 2:
			if (depth > 25)
				dwell = "POWDER_CANNON:TOXIC_DRAGONFLY";
			else if (depth > 15)
				dwell = "TOXIC_DRAGONFLY:TOXIC_TROOPER:GEEMER";
			else if (depth > 6)
				dwell = "TOXIC_TROOPER:GALACTIC_TROOPER";
			else
				dwell = "GALACTIC_TROOPER:GALACTIC_SOLDIER";
			break;
		case 3:
			if (depth > 25)
				dwell = "GENETROID:MUTATION";
			else if (depth > 15)
				dwell = "MUTATION:MUTANT_TROOPER:GEEMER";
			else if (depth > 6)
				dwell = "GUARDIAN_ROBOFROG:MUTANT_TROOPER";
			else
				dwell = "MUTANT_TROOPER:GALACTIC_TROOPER";
			break;
		}
		/*System.out.println(dwell);
		System.out.println(dwell.split(":")[0]);
	*/	return dwell.split(":");
	}
	
	ItemDistribution test = new ItemDistribution();
	{
		test.sum("MAGNALITH", 80);
		test.sum("ENERGY_PELLET", 20);
	}
	
	private ItemDistribution getItemDistribution(int lane, int depth){
		return test;
	}
	
	private String[] MUSIC_KEYS= new String[]{"ATO", "BAC", "CHE", "GEN" };
	private String[] LANE_DESCRIPTIONS= new String[]{"ATO", "BAC", "CHE", "GEN" };
	private String[] LANE_FLOORS= new String[]{"YELLOWISH_FLOOR", "GREENISH_FLOOR", "BLUEISH_FLOOR", "PURPLISH_FLOOR" };
	private String[] LANE_FACETS= new String[]{"ATO", "BAC", "CHE", "GEN" };
	private String[] LANE_WALLS= new String[]{"YELLOWISH_WALL", "GREENISH_WALL", "BLUEISH_WALL", "PURPLISH_WALL" };
	
	private String getMusicKey(int lane){
		return MUSIC_KEYS[lane];
	}
	public static void main(String args[]){
		new Game().generateLevelMap();
	}
	
	/*
	private void generatePrologueLevelPath(){
		Vector levels = new Vector(20);
		levels.add("PROLOGUE_KEEP");
		int levelTotal = 1;
		MonsterFactory.getFactory().setLevelTotal(levelTotal);
		ItemFactory.getItemFactory().setLevelTotal(levelTotal);
		levelPath = (String[]) levels.toArray(new String[levels.size()]);
		storedLevels = new Hashtable();
	}*/

	
	private void resumeScreen(){
		STMusicManagerNew.thus.playKey("GAME_OVER");
		if (Display.thus.showResumeScreen(player)){
			GameFiles.saveMemorialFile(player);
		}
    }
	
    public void informEvent(int code){
    	informEvent(code, null);
    }

	public void informEvent(int code, Object param){
		switch (code){
			case MPlayer.DEATH:
				ui.refresh();
				ui.showSystemMessage("You have been exterminated. [Press Space to continue]");
				finishGame();
				break;
			case MPlayer.EVT_GOTO_LEVEL:
				loadLevel((String)param);
				break;
			case MPlayer.EVT_NEXT_LEVEL:
				loadNextLevel();
				break;
			case MPlayer.EVT_BACK_LEVEL:
				loadBackLevel();
				break;
			case MPlayer.WON:
				wonGame();
		}
	}

	/*private void endGame(){
		Display.thus.showEndgame(player);
	}*/

	private void finishGame(){
		if (!player.isDoNotRecordScore()){
			GameFiles.saveHiScore(player);
			resumeScreen();
			Display.thus.showHiscores(GameFiles.loadScores());
		}
		GameFiles.permadeath(player);
		exitGame();
	}
	
	public void exitGame(){
		//levelNumber = -1;
		currentLevel.disableTriggers();
		currentLevel = null;
		ui.removeCommandListener(this);
		ui.setGameOver(true);
		player.setPlayerEventListener(null);
		
		
		endGame = true;
	}

	private void loadNextLevel(){
		if (currentLevel.getLevelNumber() == -1)
			Game.crash("A level outside the mainstream path tried to continue it", new Exception("A level outside the mainstream path tried to continue it"));
		loadLevel(levelPath[currentLevel.getLevelNumber()+1], currentLevel.getLevelNumber()+1);
		player.setPosition(currentLevel.getExitFor("_BACK"));
	}
	
	private void loadBackLevel(){
		if (currentLevel.getLevelNumber() == -1)
			Game.crash("A level outside the mainstream path tried to continue it", new Exception("A level outside the mainstream path tried to continue it"));
		loadLevel(levelPath[currentLevel.getLevelNumber()-1], currentLevel.getLevelNumber()-1);
		player.setPosition(currentLevel.getExitFor("_NEXT"));
	}
	
	private void loadLevel(String levelID) {
		loadLevel(levelID, -1);
	}
	
	public void wonGame(){
		Display.thus.showEndgame(player);
		player.getGameSessionInfo().setDeathCause(GameSessionInfo.ASCENDED);
		finishGame();
		return;
	}
	
	private void loadLevel(String levelID, int targetLevelNumber) {
		//JOptionPane.showMessageDialog(null, "loadLevel "+levelID+" with "+targetLevelNumber);
		Debug.say("Load new level");
		String formerLevelID = null; 
		if (currentLevel != null){
			formerLevelID = currentLevel.getID();
			MLevel storedLevel = (MLevel) storedLevels.get(formerLevelID);
			if (storedLevel == null){
				storedLevels.put(formerLevelID, currentLevel);
			}
		} else {
			formerLevelID = "_BACK";
		}
		MLevel storedLevel = (MLevel)storedLevels.get(levelID);
		if (storedLevel != null) {
			currentLevel = storedLevel;
			player.setPosition(currentLevel.getExitFor(formerLevelID));
			currentLevel.setIsDay(isDay);
		} else {
			try {
				LevelMetaData lmd = (LevelMetaData) hashLevelMap.get(levelID);
				//System.out.println(levelID);
				currentLevel = LevelMaster.createLevel(levelID, formerLevelID, targetLevelNumber, lmd);
				currentLevel.setPlayer(player);
				ui.setPlayer(player);
				currentLevel.setIsDay(isDay);
				if (currentLevel.getPlayer() != null)
					currentLevel.getPlayer().addHistoricEvent("got to the "+currentLevel.getDescription());
			} catch (CRLException crle){
				crash("Error while creating level "+levelID, crle);
			}
		}
		//currentLevel.setLevelNumber(targetLevelNumber);
		player.setLevel(currentLevel);
		if(currentLevel.getExitFor(formerLevelID) != null){
			player.setPosition(currentLevel.getExitFor(formerLevelID));
		} else if(currentLevel.getExitFor("_START") != null) {
			player.setPosition(currentLevel.getExitFor("_START"));
		}
		
		dispatcher = currentLevel.getDispatcher();
		if (currentLevel.hasNoonMusic() && !currentLevel.isDay()){
			STMusicManagerNew.thus.playKey(currentLevel.getMusicKeyNoon());
		} else {
			STMusicManagerNew.thus.playKey(currentLevel.getMusicKeyMorning());
		}
		currentLevel.anihilate();
		currentLevel.populate();
		ui.levelChange();
	}
	
	public void setLevel(MLevel level){
		currentLevel = level;
		player.setLevel(level);
		dispatcher = currentLevel.getDispatcher();
		if (currentLevel.hasNoonMusic() && !currentLevel.isDay()){
			STMusicManagerNew.thus.playKey(currentLevel.getMusicKeyNoon());
		} else {
			STMusicManagerNew.thus.playKey(currentLevel.getMusicKeyMorning());
		}
		
		//STMusicManagerNew.thus.playForLevel(levelNumber, levelPath[levelNumber], currentLevel.isDay());
		ui.levelChange();
		
		
	}
	public MPlayer getPlayer(){
		return player;
	}
	
	public static String getVersion(){
		return "0.72";
	}
	
	public void setInterfaces(UserInterface pui){
		ui = pui;
	}
	
	public static void crash(String message, Throwable exception){
		Main.crash(message, exception);
    }
	
	private static Vector reports = new Vector(20);
	public static void addReport(String report){
		reports.add(report);
	}
	
	public static Vector getReports(){
		return reports;
	}
}