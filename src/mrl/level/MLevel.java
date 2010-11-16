package mrl.level;


import java.io.Serializable;
import java.util.*;

import javax.swing.JOptionPane;

import mrl.actor.*;
import mrl.cuts.Unleasher;
import mrl.data.Cells;
import mrl.feature.*;
import mrl.game.Game;
import mrl.item.Item;
import mrl.levelgen.*;
import mrl.monster.*;
import mrl.player.*;
import mrl.ui.*;
import mrl.ui.effects.*;

import sz.fov.*;
import sz.util.*;



public class MLevel implements FOVMap, Serializable{
	private String ID;
	private Unleasher[] unleashers = new Unleasher[]{};
	private Cell[][][] map;
	private boolean[][][] visible;
	private boolean[][][] remembered;
	private VMonster monsters;
	private VFeatures features;
	private Hashtable smartFeatures = new Hashtable();
	private MPlayer player;
	private SZQueue messagesneffects;
	private Dispatcher dispatcher;
	private String description;
	private boolean isDay;
	private boolean isHostageSafe;
	private int candles;
	private boolean isCandled;
	private boolean isCold;
	private MonsterSpawnInfo[] inhabitants;
	private String[] dwellerIDs;
	
	private Hashtable bloods = new Hashtable();
	private Hashtable frosts = new Hashtable();
	private Hashtable<String, ArrayList<Item>> items = new Hashtable<String, ArrayList<Item>>();
	private Hashtable exits = new Hashtable();
	private Hashtable exitPositions = new Hashtable();
	
	private Hashtable hashCounters = new Hashtable();
	
	private String mapLocationKey;
	
	public void addExit(Position where, String levelID){
		exits.put(levelID, new Position(where));
		exitPositions.put(where.toString(), levelID);
	}
	
	public Position getExitFor(String levelID){
		return (Position)exits.get(levelID);
	}
	
	public void addItem(Item p, Position where){
		p.setPosition(where);
		ArrayList<Item> itemList = items.get(where.toString());
		if (itemList == null){
			itemList = new ArrayList<Item>();
			items.put(where.toString(), itemList);
		}
		itemList.add(p);
	}
	
	public ArrayList<Item> getItemsAt(Position where){
		return items.get(where.toString());
	}


	public void addFrost(Position where, int frostness){
		if (getFrostAt(where) != 0)
			frosts.remove(where);
		frosts.put(where.toString(), new Counter(frostness));
	}

	public void addBlood(Position where, int bloodness){
		if (getBloodAt(where) != null)
			bloods.remove(where);
		if (!isValidCoordinate(where))
			return;
		if (getMapCell(where) == null || getMapCell(where).isSolid())
			return;
		if (!getMapCell(where).isWater()) 
			bloods.put(where.toString(), bloodness+"");
	}

	public String getBloodAt(Position where){
		return (String) bloods.get(where.toString());
	}

	public int getFrostAt(Position where){
		Counter x = (Counter) frosts.get(where.toString());
		if (x != null)
			return 1;
		else
			return 0;
	}

	//private VEffect effects;

	public MLevel(){
		monsters = new VMonster(20);
		features = new VFeatures(20);
		//effects = new VEffect(10);
		messagesneffects = new SZQueue(50);
	}


	public void addMessage(Message what){
		/*what = null;
		what.getText();*/
		UserInterface.getUI().addMessage(what);
		//dispatcher.addActor(what, true, what);
	}

	public void addMessage(String what){
		addMessage(new Message(what, player.getPosition()));
	}

	public void addMessage(String what, Position where){
		addMessage(new Message(what, where));
	}


	public void addActor (Actor what){
		Debug.doAssert(what != null, "Tried to add a null actor to the world");
		dispatcher.addActor(what, true);
		if (what instanceof Monster)
			monsters.addMonster((Monster)what);
		what.setLevel(this);
	}
	
	public void removeActor (Actor what){
		Debug.doAssert(what != null, "Tried to remove a null actor to the world");
		dispatcher.removeActor(what);
	}

	public void addEffect (Effect what){
		UserInterface.getUI().drawEffect(what);
	}

	public SZQueue getMessagesAndEffects(){
		return messagesneffects;
	}

	public Cell getMapCell(int x, int y, int z){
		if (z<map.length && x<map[0].length && y < map[0][0].length && x >= 0 && y >= 0 && z >= 0)
			return map[z][x][y];
		else return null;
	}

	public Feature getFeatureAt(Position x){
		return features.getFeatureAt(x);
	}

	public Monster getMonsterAt(Position x){
		return monsters.getMonsterAt(x);
	}

	public Actor getActorAt(Position x){
		Vector actors = dispatcher.getActors();
		for (int i = 0; i < actors.size(); i++){
			Actor a = (Actor) actors.elementAt(i);
			/*Debug.say(a);
			Debug.say(a.getPosition());*/
			if (a.getPosition().equals(x))
				return (Actor) actors.elementAt(i);
		}
		return null;
	}

	public Monster getMonsterAt(int x, int y, int z){
		return monsters.getMonsterAt(new Position(x,y,z));
	}

	public void destroyFeature(Feature ooh){
		features.removeFeature(ooh);
	}

	public Cell getMapCell(Position where){
		return getMapCell(where.x, where.y, where.z);
	}

	public boolean isWalkable(Position where){
		return getMapCell(where) != null && !getMapCell(where).isSolid() &&
			(!getMapCell(where).isWater() || getFrostAt(where) != 0);
	}

	public void setCells(Cell[][][] what){
		map = what;
		visible= new boolean[what.length][what[0].length][what[0][0].length];
		remembered= new boolean[what.length][what[0].length][what[0][0].length];
	}

	public int getWidth(){
		return map[0].length;
	}

	public int getHeight(){
		return map[0][0].length;

	}
	
	public int getDepth(){
		return map.length;
	}

	private Respawner respawner;
	//private int keyCounter;
	/*public void keyInminent(){
		keyCounter = Util.rand(1,5);
	}*/

	public void setRespawner(Respawner what){
		Debug.enterMethod(this, "setRespawner", what);
		if (respawner != null)
			dispatcher.removeActor(respawner);
		respawner = what;
		if (respawner != null){
			dispatcher.addActor(what);
			what.setLevel(this);
		}
		Debug.exitMethod();
	}

	public void createMonster(String who, Position where/*, String feat*/){
	 	Monster x = MonsterFactory.getFactory().buildMonster(who);
	 	x.setPosition(where);
	 	/*if (!feat.equals(""))
	 		x.setFeaturePrize(feat);
	 	*/
	 	addMonster(x);
    }

    public void setBoss(Monster what){
    	boss = what;
    	addMonster(what);
    }

	public void addMonster(Monster what){
		monsters.addMonster(what);
		dispatcher.addActor(what);
		what.setLevel(this);
	}
	
	public void removeBoss(){
		monsters.remove(boss);
		dispatcher.removeActor(boss);
		boss = null;
	}

	public void removeMonster(Monster what){
        monsters.remove(what);
		dispatcher.removeActor(what);
		what.setLevel(this);
	}

	public void removeSmartFeature(SmartFeature what){
		smartFeatures.remove(what.getPosition().toString());
		dispatcher.removeActor(what);
	}


	public void addFeature(Feature what){
		features.addFeature(what);
		if (what.getID().equals("CANDLE"))
			candles++;
	}

	public void addSmartFeature(SmartFeature what){

		smartFeatures.put(what.getPosition().toString(), what);
		what.setLevel(this);
		dispatcher.addActor(what);
	}

	public void addSmartFeature (String featureID, Position location){
		SmartFeature x = SmartFeatureFactory.getFactory().buildFeature(featureID);
		x.setPosition(location.x, location.y, location.z);
		addSmartFeature(x);
	}

	public void addFeature(String featureID, Position location){
		//Debug.say("Add"+featureID);
		Feature x = FeatureFactory.getFactory().buildFeature(featureID);
		x.setPosition(location.x, location.y, location.z);
		addFeature(x);
	}

	public void setPlayer(MPlayer what){
		player = what;
		if (!dispatcher.contains(what))
			dispatcher.addActor(what, true);
		player.setLevel(this);
	}


	/*public java.util.Vector getMonsters(){
		return monsters;
	} */

	public Cell[][][] getCells(){
		return map;
	}

	public Cell[][] getVisibleCellsAround(int x, int y, int z, int xspan, int yspan){
		int xstart = x - xspan;
		int ystart = y - yspan;
		int xend = x + xspan;
		int yend = y + yspan;
		Cell [][] ret = new Cell [2 * xspan + 1][2 * yspan + 1];
		int px = 0;
		for (int ix = xstart; ix <=xend; ix++){
			int py = 0;
			for (int iy =  ystart ; iy <= yend; iy++){
				if (ix >= 0 && ix < map[0].length && iy >= 0 && iy<map[0][0].length && isVisible(ix, iy)){
					//darken(ix, iy);
					ret[px][py] = map[z][ix][iy];
					/*Las celdas de abajo*/
					if (isValidCoordinate(ix,iy,z) && map[z][ix][iy] == null){
						int pz = z;
						while (pz < getDepth()-1){
							if (map[pz+1][ix][iy] == null){
								pz++;
							} else {
								ret[px][py] = map[pz+1][ix][iy];
								//remembered[pz+1][ix][iy]= true;
								break;
							}
						}
					}
				}
				py++;
			}
			px++;
		}
		return ret;
	}
	
	public Cell[][] getMemoryCellsAround(int x, int y, int z, int xspan, int yspan){
		int xstart = x - xspan;
		int ystart = y - yspan;
		int xend = x + xspan;
		int yend = y + yspan;
		Cell [][] ret = new Cell [2 * xspan + 1][2 * yspan + 1];
		int px = 0;
		for (int ix = xstart; ix <=xend; ix++){
			int py = 0;
			for (int iy =  ystart ; iy <= yend; iy++){
				if (ix >= 0 && ix < map[0].length && iy >= 0 && iy<map[0][0].length && remembers(ix, iy)){
					ret[px][py] = map[z][ix][iy];
				}
				/*Las celdas de abajo*/
				if (isValidCoordinate(ix,iy,z) && map[z][ix][iy] == null){
					int pz = z;
					while (pz < getDepth()-1 && remembers(ix, iy, pz+1)){
						if (map[pz+1][ix][iy] == null){
							pz++;
						} else {
							ret[px][py] = map[pz+1][ix][iy];
							break;
						}
					}
				}
				py++;
			}
			px++;
		}
		return ret;
	}
/*
	public Cell[][] getCellsAround(int x, int y, int z, int xspan, int yspan){
		int xstart = x - xspan;
		int ystart = y - yspan;
		int xend = x + xspan;
		int yend = y + yspan;
		Cell [][] ret = new Cell [2 * xspan + 1][2 * yspan + 1];
		int px = 0;
		for (int ix = xstart; ix <=xend; ix++){
			int py = 0;
			for (int iy =  ystart ; iy <= yend; iy++){
				//Debug.say("px " + px+" py"+py);
				//Debug.say("ix " +ix+" iy"+iy+"z"+z);
				if (ix >= 0 && ix < map[0].length && iy >= 0 && iy<map[0][0].length)
					ret[px][py] = map[z][ix][iy];
				py++;
			}
			px++;
		}
		return ret;
	}*/

	public MPlayer getPlayer() {
		return player;
	}

	public void setDispatcher(Dispatcher value) {
		Debug.enterMethod(this, "setDispatcher", value);
		dispatcher = value;
		Debug.exitMethod();
	}

	public void stopTime(int turns){
		dispatcher.setFixed(player, turns);
	}


	public void populate(){
		if (getDwellerIDs() == null || getDwellerIDs().length == 0)
			return;
		//int enemies = getWidth()*getHeight()/100;
		int variation = Util.rand(5,10);
		int enemies = getPopulationAverage() - 5 + variation;
		enemies = Util.rand(enemies-3,enemies+3);
		for (int i = 0; i < enemies; i++){
			Monster monster = MonsterFactory.getFactory().buildMonster(Util.randomElementOf(getDwellerIDs()));
			Position spawnPosition = new Position(Util.rand(1,getWidth()-2), Util.rand(1,getHeight()-2), Util.rand(0, getDepth()-1));
			if (!isWalkable(spawnPosition))
				continue;
			monster.setPosition(spawnPosition);
			addMonster(monster);
		}
	}
	
	public void respawn(){
		Monster monster = MonsterFactory.getFactory().getMonsterForLevel(this);
		int spawnPosition = MonsterFactory.getFactory().getLastSpawnPosition();
		if (monster == null)
			return;
		Position nearPlayer = null;
		boolean ok = false;
		switch (spawnPosition){
		case MonsterSpawnInfo.UNDERGROUND: case MonsterSpawnInfo.BORDER:
			for (int i = 0; i < 30; i++){
				nearPlayer = Position.add(player.getPosition(), new Position(Util.rand(-10,10), Util.rand(-10,10), 0));
				validate (nearPlayer);
				if (getMapCell(nearPlayer) == null || (getMapCell(nearPlayer).isWater() && !monster.canSwim()))
					continue;
				if ((!monster.canSwim() && 
						getMapCell(nearPlayer).getHeight() == 0
						&& !getMapCell(nearPlayer).isSolid()) ||
					(monster.canSwim() &&
							getMapCell(nearPlayer).isWater())){
					ok = true;
					break;
				}
			}
			break;
		case MonsterSpawnInfo.WATER:
			for (int i = 0; i < 30; i++){
				nearPlayer = Position.add(player.getPosition(), new Position(Util.rand(-10,10), Util.rand(-10,10), 0));
				validate (nearPlayer);
				if (getMapCell(nearPlayer) == null || (!getMapCell(nearPlayer).isWater()))
					continue;
				ok = true;
				break;
			}
			break;
		}
			
		if (ok) {
			Emerger em = new Emerger(monster, nearPlayer, Util.rand(2,5));
			dispatcher.addActor(em);
			em.setSelector(new EmergerAI());
			em.setLevel(this);
		}
	}

	private void validate(Position what){
		if (what.x < 0) what.x = 0;
		if (what.y < 0) what.y = 0;
		if (what.x > getWidth() - 1) what.x = getWidth() - 1;
		if (what.y > getHeight() - 1) what.y = getHeight() - 1;
	}

	public boolean isValidCoordinate(Position what){
		return 	isValidCoordinate(what.x, what.y, what.z);
	}
	
	public boolean isValidCoordinate(int x, int y){
		return 	! (x < 0 ||
					y < 0  ||
					x > getWidth() - 1 ||
					y > getHeight() - 1);
	}
	
	public boolean isValidCoordinate(int x, int y, int z){
		return 	z >= 0 && z < getDepth() && isValidCoordinate(x,y);
	}

	private int levelNumber;
	
	public int getLevelNumber() {
		return levelNumber;
	}

	public void setLevelNumber(int value) {
		levelNumber = value;
	}

	public void updateLevelStatus(){
		/*if (boss != null && boss.isDead())
			player.informPlayerEvent(Player.EVT_FORWARD);*/
		if (hashCounters.size() > 0){
			for (int i = 0; i < hashCounters.size(); i++){
				((Counter)hashCounters.elements().nextElement()).reduce();
			}
		}
		reduceFrosts();
	}

	private void reduceFrosts(){
		Enumeration counters = frosts.elements();
		while (counters.hasMoreElements()){
			Counter counter = (Counter)counters.nextElement();
			counter.reduce();
		}

		Enumeration keys = frosts.keys();
		while (keys.hasMoreElements()){
			Object key = keys.nextElement();
			if  (((Counter)frosts.get(key)).isOver()){
				addMessage("The ice melts away!");
				player.land();
				frosts.remove(key);
			}
		}
	}

	private Monster boss;
	//private Position startPosition, endPosition;

	/*public void setPositions(Position start, Position end) {
		startPosition = start;
		endPosition = end;
	}*/

	public void setInhabitants(MonsterSpawnInfo[] value) {
		inhabitants = value;
	}

	public Dispatcher getDispatcher() {
		Debug.enterMethod(this, "getDispatcher");
		Debug.exitMethod(dispatcher);
		return dispatcher;

	}

	public Monster getBoss(){
		return boss;
	}

	public VMonster getMonsters() {
		return monsters;
	}

	public void spawnTreasure(){
		Position nearPlayer = null;
		while (true){
			nearPlayer = Position.add(player.getPosition(), new Position(Util.rand(-5,5), Util.rand(-5,5), 0));
			validate (nearPlayer);
			if (getMapCell(nearPlayer) == null)
				continue;
			if (getMapCell(nearPlayer).getHeight() == 0
				&& !getMapCell(nearPlayer).isWater()
				&& !getMapCell(nearPlayer).isSolid()){
				String treasure = "";
				switch (Util.rand(1,4)){
				case 1:
					treasure = "CROWN";
					break;
				case 2:
					treasure = "CHEST";
					break;
				case 3:
					treasure = "RAINBOW_MONEY_BAG";
					break;
				case 4:
					if (Util.chance(70))
						treasure = "MOAUI_HEAD";
					else
						treasure = "CHEST";
				}

				addFeature(treasure, nearPlayer);
				return;
			}
		}
	}

	public SmartFeature getSmartFeature(Position where) {
		return (SmartFeature) smartFeatures.get(where.toString());
	}
/*
	public Position getStartPosition() {
		return startPosition;
	}

	public Position getEndPosition() {
		return endPosition;
	}*/

	public void signal (Position center, int range, String message){
		Vector actors = dispatcher.getActors();
		for (int i = 0; i < actors.size(); i++){
			if (Position.flatDistance(center, ((Actor)actors.elementAt(i)).getPosition()) <= range)
				((Actor)actors.elementAt(i)).message(message);
		}
	}
	
	public void removeRespawner(){
		dispatcher.removeActor(respawner);
		respawner = null;
	}
	
	public void anihilate(){
		if (monsters.size() == 1)
			return;
		Monster bony = getMonsterByID("BONY");
		monsters.removeAll();
		dispatcher.removeAll();
		dispatcher.addActor(player);
		if (bony != null)
			addMonster(bony);
	}
	
	public boolean isDay(){
		return isDay;
	}
	
	public void setIsDay(boolean newDay){
		if (haunted){
			if (isDay){
				if (!newDay){
					savePop();
					anihilate();
					setRespawner(nightRespawner);
				}
			} else {
				if (newDay){
					anihilate();
					removeRespawner();
					loadPop();
				}
			}
		}
		isDay = newDay;
	}
	
	public void savePop(){
		tempActors = new Vector(dispatcher.getActors());
	}
	
	public void loadPop(){
		for (int i = 0; i < tempActors.size(); i++){
			dispatcher.addActor((Actor)tempActors.elementAt(i));
			if (tempActors.elementAt(i) instanceof Monster) {
				monsters.addMonster((Monster)tempActors.elementAt(i));
			}
		}
	}
	
	private Vector tempActors;
	
	private Respawner nightRespawner;
	private boolean haunted;

	public boolean isHaunted() {
		return haunted;
	}

	public void setHaunted(boolean haunted) {
		this.haunted = haunted;
	}
	
	public void setNightRespawner(Respawner ap){
		nightRespawner = ap;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isHostageSafe() {
		return isHostageSafe;
	}

	public void setHostageSafe(boolean isHostageSafe) {
		this.isHostageSafe = isHostageSafe;
	}

	public boolean blockLOS(int x, int y) {
		if (!isValidCoordinate(x,y))
			return true;
		if (map[player.getPosition().z][x][y] == null)
			return false;
		else
			return map[player.getPosition().z][x][y].isOpaque();
			//return map[player.getPosition().z][x][y] == null || map[player.getPosition().z][x][y].isSolid();
	}
	
	public void setSeen(int x, int y) {
		if (!isValidCoordinate(x,y))
			return;
		visible[player.getPosition().z][x][y]= true;
		remembered[player.getPosition().z][x][y]= true;
		Monster m = getMonsterAt(x,y, player.getPosition().z);
		if (m != null){
			m.setWasSeen(true);
		}
	}
	
	public void darken(){
		for (int x = 0; x < getWidth(); x++)
			for (int y = 0; y < getHeight(); y++)
				darken(x,y);
	}
	
	public void darken(int x, int y){
		if (!isValidCoordinate(x,y))
			return;
		visible[player.getPosition().z][x][y]= false;
	}

	public boolean remembers(int x, int y){
		if (!isValidCoordinate(x,y))
			return false;
		return remembered[player.getPosition().z][x][y];
	}
	
	public boolean remembers(int x, int y, int z){
		if (!isValidCoordinate(x,y,z))
			return false;
		return remembered[z][x][y];
	}
	
	public boolean isVisible(int x, int y){
		if (!isValidCoordinate(x,y))
			return false;
		return visible[player.getPosition().z][x][y];
	}
	
	public Position getDeepPosition(Position where){
		Position ret = new Position(where);
		//System.out.println(where);
		if (!isValidCoordinate(where))
			return null;
		if (map[ret.z][ret.x][ret.y] != null)
			return where;
		while(map[ret.z][ret.x][ret.y] == null  && ret.z < getDepth()-1){
			if (map[ret.z+1][ret.x][ret.y] != null){
				ret.z++;
				return ret;
			}
			ret.z++;
		}
		return null;
	}
	
	private Hashtable hashFlags = new Hashtable();
	public void setFlag(String flagID, boolean value){
		hashFlags.remove(flagID);
		hashFlags.put(flagID, new Boolean(value));
	}
	
	public boolean getFlag(String flagID){
		Boolean flag = (Boolean) hashFlags.get(flagID); 
		if (flag == null || !flag.booleanValue())
			return false;
		else
			return true;
	}
	
	public Monster getMonsterByID(String monsterID){
		VMonster monsters = getMonsters();
		for (int i = 0; i < monsters.size(); i++)
			if (monsters.elementAt(i).getID().equals(monsterID))
				return monsters.elementAt(i);
		return null;
	}
	
	public void setUnleashers(Unleasher[] pUnleashers){
		unleashers = pUnleashers;
	}
	
	public void checkUnleashers(Game game){
		for (int i = 0; i < unleashers.length; i++){
			if (unleashers[i].enabled())
				unleashers[i].unleash(this, game);
		}
	}
	
	public void disableTriggers(){
		for (int i = 0; i < unleashers.length; i++){
			unleashers[i].disable();
		}
	}
	
	private String musicKeyMorning;
	private String musicKeyNoon;

	public String getMusicKeyMorning() {
		return musicKeyMorning;
	}

	public void setMusicKeyMorning(String musicKeyMorning) {
		this.musicKeyMorning = musicKeyMorning;
	}

	public String getMusicKeyNoon() {
		return musicKeyNoon;
	}

	public void setMusicKeyNoon(String musicKeyNoon) {
		this.musicKeyNoon = musicKeyNoon;
	}
	
	public boolean hasNoonMusic(){
		return musicKeyNoon != null && !musicKeyNoon.equals("");
	}

	public String getID() {
		return ID;
	}

	public void setID(String id) {
		ID = id;
	}

	public boolean isExit(Position pos){
		return getExitOn(pos) != null;
	}
	
	public String getExitOn(Position pos){
		return (String)exitPositions.get(pos.toString());
	}
	
	public MonsterSpawnInfo[] getSpawnInfo (){
		return inhabitants;
	}

	public String[] getDwellerIDs() {
		return dwellerIDs;
	}

	public void setDwellerIDs(String[] dwellerIDs) {
		this.dwellerIDs = dwellerIDs;
	}

	public int getDepthFromPlayer(int x, int y){
		int ret = 0;
		int zrunner = player.getPosition().z; 
		while (map[zrunner][x][y] == null){
			ret++;
			zrunner--;
			if (zrunner == -1)
				break;
		}
		return ret;
	}
	
	public Counter getCounter(String id){
		return (Counter) hashCounters.get(id);
	}
	
	public void addCounter(String id, int count){
		hashCounters.put(id, new Counter(count));
	}
	
	public void removeCounter(String id){
		hashCounters.remove(id);
	}
	
	
	
	public void removeExit(String exitID){
		Position where = (Position) exits.get(exitID);
		exitPositions.remove(where.toString());
		exits.remove(exitID);
	}
	
	public String getMapLocationKey(){
		return mapLocationKey;
	}

	public void setMapLocationKey(String mapLocationKey) {
		this.mapLocationKey = mapLocationKey;
	}
	
	public void addCandle(){
		candles++;
	}
	
	public int getCandles(){
		return candles;
	}
	
	public boolean isCandled(){
		return isCandled;
	}
	
	public void setIsCandled(boolean value){
		isCandled = value;
	}

	public boolean isCold() {
		return isCold;
	}

	public void setCold(boolean isCold) {
		this.isCold = isCold;
	}
	
	private int populationAverage;

	public int getPopulationAverage() {
		return populationAverage;
	}

	public void setPopulationAverage(int populationAverage) {
		this.populationAverage = populationAverage;
	}
	
	public boolean isExitPlaceable(Position where){
		return !getMapCell(where).isSolid() && getExitOn(where) == null;
	}
	
	public boolean isItemPlaceable(Position where){
		return !getMapCell(where).isSolid() && getExitOn(where) == null;
	}
	
	public void removeItemFrom(Item what, Position where){
		ArrayList stack = items.get(where.toString());
		if (stack != null){
			stack.remove(what);
			if (stack.size() == 0)
				items.values().remove(stack);
		}
	}
	
}