
package mrl.player;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import mrl.action.Action;
import mrl.action.ActionFactory;
import mrl.actor.Actor;
import mrl.feature.Feature;
import mrl.game.SFXManager;
import mrl.item.Item;
import mrl.item.Weapon;
import mrl.level.Cell;
import mrl.monster.Monster;
import mrl.monster.VMonster;
import mrl.ui.Appearance;
import mrl.ui.AppearanceFactory;
import sz.fov.FOV;
import sz.util.Debug;
import sz.util.Position;
import sz.util.Util;


public class MPlayer extends Actor {
	private boolean doNotRecordScore = false;
	
	// Attributes
	private String name;
	private int sex;
	
	private Weapon currentWeapon;
	private Hashtable<String, Equipment> inventory = new Hashtable<String, Equipment>();
    private int carryMax;
    
	//Status
	private int tankEnergy;
	private int energyTanks;
	private int energyTanksCapacity;
	private int reserveEnergy;
	private int reserveEnergyMax;
	private int missiles;
	private int missilesCapacity;
	private int superMissiles;
	private int superMissilesCapacity;
	private int powerBombs;
	private int powerBombsCapacity;
	
	/*Suit upgrades*/
	private boolean hasChargeBeam;
	private boolean hasIceBeam;
	private boolean hasWaveBeam;
	private boolean hasPlasmaBeam;
	private boolean hasVariaSuit;
	private boolean hasGravitySuit;
	private boolean hasHighJumpBooster;
	private boolean hasScrewAttack;
	private boolean hasSpaceJump;
	private boolean hasSpringBall;
	private boolean hasSpiderBall;
	private boolean hasXRayVisor;
	private boolean hasThermoVisor;
	private boolean hasEnergyBomb;
	private boolean hasMorphBall;
	
	private boolean isChargeBeamActivated;
	private boolean isIceBeamActivated;
	private boolean isWaveBeamActivated;
	private boolean isPlasmaBeamActivated;
	private boolean isVariaSuitActivated;
	private boolean isGravitySuitActivated;
	
	private boolean isHighJumpBoosterActivated;
	private boolean isScrewAttackActivated;
	private boolean isSpaceJumpActivated;
	private boolean isSpringBallActivated;
	private boolean isSpiderBallActivated;
	private boolean isEnergyBombActivated;
	private boolean isMorphBallActivated;
	
	private boolean isXRayVisorActivated;
	private boolean isThermoVisorActivated;
	
	private boolean isBallMorphed;
	private boolean autoFire;
	
	private int walkCost = 50;
	private int jumpCost = 50;
	private int attackCost = 50;
	
	private int jumpingPower = 4;
	
	public int getJumpingPower(){
		return jumpingPower * (isHighJumpBoosterActivated ? 2 : 1);
	}
	private int evadeChance;
	
	private GameSessionInfo gameSessionInfo;
	
	//Relationships
	private transient PlayerEventListener playerEventListener;
	
	public void addHistoricEvent(String description){
		gameSessionInfo.addHistoryItem(description);
	}
	
	public void informPlayerEvent(int code){
		if (playerEventListener != null)
			playerEventListener.informEvent(code);
	}

	public void informPlayerEvent(int code, Object param){
		playerEventListener.informEvent(code, param);
	}

	private void damage(int dam){
		if (isGravitySuitActivated){
			dam = (int)(dam * 0.5d);
		}
		
		if (isVariaSuitActivated()){
			dam = (int)(dam * 0.75d);
		}

		if (dam <= 0 ){
			level.addMessage("The attack is ineffective.");
			return;
		}
		reduceEnergy(dam);
	}
	
	private void reduceEnergy(int energy){
		int tanks = (int)(energy / 100.0d);
		int remnant = energy % 100;
		energyTanks -= tanks;
		if (remnant > getTankEnergy()){
			energyTanks --;
			tankEnergy = 100- (remnant - getTankEnergy());
		} else {
			tankEnergy -= remnant;
		}
	}
	
	public int getAbsoluteEnergy(){
		return energyTanks * 100 + tankEnergy;
	}
	
	public int getMaxEnergy(){
		return 100+energyTanksCapacity*100;
	}

	public void selfDamage(int damageType, int dam){
		damage(dam);
		if (getAbsoluteEnergy() < 0){
			switch (damageType){
				case MPlayer.DAMAGE_WALKED_ON_LAVA:
					gameSessionInfo.setDeathCause(GameSessionInfo.BURNED_BY_LAVA);
					break;
			}
		}

	}

	public void damage (Monster who, int dam){
		if (Util.chance(evadeChance)) {
			level.addMessage("You jump and avoid the "+who.getDescription()+" attack!");
		}
		damage(dam);
		if (getAbsoluteEnergy() < 0){
			SFXManager.play("wav/rich_di2.wav");
			gameSessionInfo.setDeathCause(GameSessionInfo.KILLED);
			gameSessionInfo.setKillerMonster(who);
			gameSessionInfo.setDeathLevelDescription(level.getDescription());
		}
	}

	public final static int DEATH = 99, EVT_NEXT_LEVEL = 98, EVT_BACK_LEVEL = 97, WON = 96;
	
	public void useReserveEnergy(){
		level.addMessage("WARNING: Suit heavily damaged. Using reserve energy tanks.");
		int reserve = getReserveEnergy();
		while (reserve > 0){
			if (reserve>50){
				addEnergy(50);
				reserve-=50;
			} else {
				addEnergy(reserve);
				reserve = 0;
			}
		}
		setReserveEnergy(0);
	}
	
	public void checkDeath(){
		if (getAbsoluteEnergy() < 0) {
			if (getReserveEnergy() > 0){
				useReserveEnergy();
			}else {
				informPlayerEvent (DEATH);
			}
		}
	}

	public String getName() {
		return name;
	}

	
	public void setName(String value) {
		name = value;
	}

	public PlayerEventListener getPlayerEventListener() {
		return playerEventListener;
	}

	public void setPlayerEventListener(PlayerEventListener value) {
		playerEventListener = value;
	}

	public GameSessionInfo getGameSessionInfo() {
		return gameSessionInfo;
	}

	public void setGameSessionInfo(GameSessionInfo value) {
		gameSessionInfo = value;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int value) {
		sex = value;
	}

	public void updateStatus(){
		if (level.isCold()){
			level.addMessage("It is too cold!");
			selfDamage(MPlayer.DAMAGE_COLD, 20);
		}
		if (isChargingBeam()){
			beamChargeCount++;
			if (isChargedBeam()){
				level.addMessage("Your beam is charged!");
			}
		}
		
		if (isJumping){
			jumpingCounter--;
			if (jumpingCounter >= (int)(getJumpingPower() / 2.0d)){
				height++;
			} else {
				height--;
				if (height <= level.getMapCell(getPosition()).getHeight()){
					height = level.getMapCell(getPosition()).getHeight();
					jumpingCounter = 0;
					isJumping = false;
					land();
				} 
			}
			/*if (jumpingCounter == 0)
				isJumping = false;*/
		}
    	/*if (isPoisoned()){
    		if (Util.chance(40)){
    			level.addMessage("You feel the poison coursing through your veins!");
    			selfDamage(Player.DAMAGE_POISON, 3);
    		}
    	}
        regen();*/
	}

	public void land(){
		landOn (getPosition());
	}

	public void landOn (Position destinationPoint){
		Debug.enterMethod(this, "landOn", destinationPoint);
		Cell destinationCell = level.getMapCell(destinationPoint);
		
        if (destinationCell == null){
        	destinationPoint = level.getDeepPosition(destinationPoint);
        	if (destinationPoint == null) {
        		level.addMessage("You fall into a endless pit!");
				gameSessionInfo.setDeathCause(GameSessionInfo.ENDLESS_PIT);
				tankEnergy = -1; energyTanks = 0;
				informPlayerEvent(MPlayer.DEATH);
				Debug.exitMethod();
				return;
        	}else {
        		destinationCell = level.getMapCell(destinationPoint);
        	}
        }
        
        setPosition(destinationPoint);
        
		if (destinationCell.isSolid()){
			// Tries to land on a freesquare around
			Position tryP = Position.add(destinationPoint, Action.directionToVariation(Action.UP));
			if (level.getMapCell(tryP) != null && !level.getMapCell(tryP).isSolid()){
				landOn(tryP);
				Debug.exitMethod();
				return;
			} else {
				tryP = Position.add(destinationPoint, Action.directionToVariation(Action.DOWN));
				if (level.getMapCell(tryP) != null && !level.getMapCell(tryP).isSolid()){
					landOn(tryP);
					Debug.exitMethod();
					return;
				}
				else {
					tryP = Position.add(destinationPoint, Action.directionToVariation(Action.LEFT));
					if (level.getMapCell(tryP) != null && !level.getMapCell(tryP).isSolid()){
						landOn(tryP);
						Debug.exitMethod();
						return;
					}
					else {
						tryP = Position.add(destinationPoint, Action.directionToVariation(Action.RIGHT));
						if (level.getMapCell(tryP) != null && !level.getMapCell(tryP).isSolid()){
							landOn(tryP);
							Debug.exitMethod();
							return;
						}
						else {
							level.addMessage("You are smashed inside the "+destinationCell.getShortDescription()+"!");
							gameSessionInfo.setDeathCause(GameSessionInfo.SMASHED);
							tankEnergy = -1; energyTanks = 0;
							informPlayerEvent(MPlayer.DEATH);
							Debug.exitMethod();
							return;
						}
					}
				}
			}
		}
		
		if (destinationCell.getDamageOnStep() > 0 && !isGravitySuitActivated()){
			level.addMessage("You are injured by the "+destinationCell.getShortDescription()+"!");
			selfDamage(MPlayer.DAMAGE_WALKED_ON_LAVA, destinationCell.getDamageOnStep());
		}

		if (destinationCell.getHeightMod() != 0){
			setPosition(Position.add(destinationPoint, new Position(0,0, destinationCell.getHeightMod())));
		}
		if (!isJumping())
			height = destinationCell.getHeight();
		if (!level.isWalkable(destinationPoint)){
			if (destinationCell.isWater()){
				level.addMessage("You fall in the "+destinationCell.getShortDescription()+"!");
			}
		}

		
		Feature destinationFeature = level.getFeatureAt(destinationPoint);
		while (destinationFeature != null){
			if (destinationFeature.getHeightMod() != 0){
				setPosition(Position.add(destinationPoint, new Position(0,0, destinationFeature.getHeightMod())));
			}
			if (destinationFeature.getEnergyPrize() > 0){
				level.addMessage("Energy Tank absorbed "+destinationFeature.getEnergyPrize()+" energy.");
				addEnergy(destinationFeature.getEnergyPrize());
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.getEnergyTankPrize() > 0){
				level.addMessage("Energy Tank extended by "+destinationFeature.getEnergyTankPrize()+" energy.");
				energyTanksCapacity++;
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.getReserveTankPrize() > 0){
				level.addMessage("Reserve Tank extended by "+destinationFeature.getReserveTankPrize()+" energy.");
				reserveEnergyMax += 100;
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.getMissilePrize() > 0){
				level.addMessage("Missile Tank absorbed "+destinationFeature.getMissilePrize()+" missiles.");
				addMissiles(destinationFeature.getMissilePrize());
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.getSupermissilePrize() > 0){
				level.addMessage("Missile Tank absorbed "+destinationFeature.getSupermissilePrize()+" super missiles.");
				addSuperMissiles(destinationFeature.getSupermissilePrize());
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.getPowerBombPrize() > 0){
				level.addMessage("Power Tank absorbed "+destinationFeature.getPowerBombPrize()+" bombs.");
				addPowerBombs(destinationFeature.getPowerBombPrize());
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.isBombBlastPrize()){
				level.addMessage("** Energy Bomb upgrade added to power suit **");
				setHasEnergyBomb(true);
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.isChargeBeamPrize()){
				level.addMessage("** Charge Beam upgrade added to power suit **");
				setHasChargeBeam(true);
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.isGravitySuitPrize()){
				level.addMessage("** Gravity Suit upgrade added to power suit **");
				setHasGravitySuit(true);
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.isHiJumpPrize()){
				level.addMessage("** Hi-jump booster added to power suit **");
				setHasHighJumpBooster( true);
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.isIceBeamPrize()){
				level.addMessage("** Ice Beam added to power suit **");
				setHasIceBeam(true);
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.isMorphBallPrize()){
				level.addMessage("** Morph Ball upgrade added to power suit **");
				setHasMorphBall (true);
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.isPlasmaBeamPrize()){
				level.addMessage("** Plasma Beam added to power suit **");
				setHasPlasmaBeam (true);
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.isScrewAttackPrize()){
				level.addMessage("** Screw Attack upgrade added to power suit **");
				setHasScrewAttack (true);
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.isSpaceJumpPrize()){
				level.addMessage("** Space jump upgrade added to power suit **");
				setHasSpaceJump (true);
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.isSpiderBallPrize()){
				level.addMessage("** Spider Ball upgrade added to power suit **");
				setHasSpiderBall (true);
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.isSpringBallPrize()){
				level.addMessage("** Spring ball upgrade added to power suit **");
				setHasSpringBall (true);
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.isThermoVisorPrize()){
				level.addMessage("** Thermo-visor upgrade added to suit visor **");
				setHasThermoVisor (true);
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.isVariaSuitPrize()){
				level.addMessage("** Varia suit upgrade added to power suit **");
				setHasVariaSuit(true);
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.isWaveBeamPrize()){
				level.addMessage("** Wave Beam upgrade added to power suit **");
				setHasWaveBeam(true);
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.isXRayVisorPrize()){
				level.addMessage("** X-Ray upgrade added to suit visor **");
				setHasXRayVisor(true);
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.getMissileCapacityPrize()>0){
				level.addMessage("** Missile tank capacity increased **");
				missilesCapacity += destinationFeature.getMissileCapacityPrize();
				missiles += destinationFeature.getMissileCapacityPrize();
				level.destroyFeature(destinationFeature);
			}
			if (destinationFeature.getSuperMissileCapacityPrize()>0){
				level.addMessage("** SuperMissile tank capacity increased **");
				superMissilesCapacity += destinationFeature.getSuperMissileCapacityPrize();
				superMissiles += destinationFeature.getSuperMissileCapacityPrize();
				level.destroyFeature(destinationFeature);
			}
			if (destinationFeature.getPowerBombCapacityPrize()>0){
				level.addMessage("** Power Bomb tank capacity increased **");
				powerBombsCapacity += destinationFeature.getPowerBombCapacityPrize();
				powerBombs += destinationFeature.getPowerBombCapacityPrize();
				level.destroyFeature(destinationFeature);
			}
			
			if (destinationFeature.getID().equals("A")){
				level.destroyFeature(destinationFeature);
				weaponA = true;
				checkWeapons();
			}
			if (destinationFeature.getID().equals("B")){
				level.destroyFeature(destinationFeature);
				weaponB = true;
				checkWeapons();
			}
			if (destinationFeature.getID().equals("C")){
				level.destroyFeature(destinationFeature);
				weaponC = true;
				checkWeapons();
			}
			if (destinationFeature.getID().equals("G")){
				level.destroyFeature(destinationFeature);
				weaponG = true;
				checkWeapons();
			}
			if (destinationFeature.getID().equals("RECOVER_ENERGY")){
				recoverEnergy();
			}
			
			if (destinationFeature.getID().equals("RECOVER_MISSILE")){
				recoverWeapons();
			}
			Feature pred = destinationFeature;
			destinationFeature = level.getFeatureAt(destinationPoint);
			if (destinationFeature == pred)
				destinationFeature = null;
		}
		if (level.isExit(getPosition())){
			String exit = level.getExitOn(getPosition());
			if (exit.equals("_START")){
				//Do nothing. This must be changed with startsWith("_");
			} else if (exit.equals("_NEXT")){
				informPlayerEvent(MPlayer.EVT_NEXT_LEVEL);
			} else if (exit.equals("_BACK")){
				informPlayerEvent(MPlayer.EVT_BACK_LEVEL);
			} else {
				informPlayerEvent(MPlayer.EVT_GOTO_LEVEL, exit);
			}
			
		}
		Debug.exitMethod();
	}

	private boolean weaponA, weaponB, weaponC, weaponG;
	
	private void recoverEnergy(){
		setTankEnergy(99);
		setEnergyTanks(getEnergyTanksCapacity());
	}
	
	private void recoverWeapons(){
		setMissiles(getMissilesCapacity());
		setSuperMissiles(getSuperMissilesCapacity());
		setPowerBombs(getPowerBombsCapacity());
	}
	
	private void checkWeapons(){
		if (weaponA && weaponB && weaponC && weaponG)
			informPlayerEvent(MPlayer.WON);
	}
	
	public void act() {
		if (isAutoFire()){
			setFlag("autofire_act", !getFlag("autofire_act"));
		}
		
		if (isAutoFire() && getFlag("autofire_act")){
			if (getLockedMonster() != null && getLockedMonster().isDead())
				setLockedMonster(getNearestMonsterOnSight());
			if (getLockedMonster() == null){
				setAutoFire(false);
			} else {
				if (sees(getLockedMonster())){
					Action x = ActionFactory.getActionFactory().getAction("AUTOFIRE");
					execute(x);
					return;
				}
			}
		} 
		super.act();
		
	}
	
	public void setLockedMonster(Monster m){
		lockedMonster= m;
	}
	
	private Monster lockedMonster;
	
	public Monster getLockedMonster(){
		return lockedMonster;
	}
	private void addEnergy(int val){
		if (tankEnergy+val>99){
			int remnant = (tankEnergy+val)-100;
			if (energyTanks < energyTanksCapacity){
				energyTanks++;
				tankEnergy = remnant;
			} else {
				tankEnergy = 99;
				reserveEnergy += remnant / 2;
				if (reserveEnergy > reserveEnergyMax)
					reserveEnergy = reserveEnergyMax;
			}
		} else {
			tankEnergy += val;
		}
	}
	
	private void addMissiles(int val){
		missiles += val;
		if (missiles > missilesCapacity)
			missiles = missilesCapacity;
	}
	
	private void addSuperMissiles(int val){
		superMissiles += val;
		if (superMissiles > superMissilesCapacity)
			superMissiles = superMissilesCapacity;
	}
	
	private void addPowerBombs(int val){
		powerBombs += val;
		if (powerBombs > powerBombsCapacity)
			powerBombs = powerBombsCapacity;
	}
	
	public void ballMorph(boolean val){
		isBallMorphed = val;
	}
	
	public boolean isBallMorphed(){
		return isBallMorphed;
	}
	
	public Appearance getAppearance(){
		String app = "";
		if (isBallMorphed()){
			app+="BALL";
		}
		if (isGravitySuitActivated()){
			app = "GRAVITY_"+app;
		} else
    	if (isVariaSuitActivated()) {
    		app = "VARIA_"+app;
    	} else {
    		app = "POWER_"+app;
    	}

		return AppearanceFactory.getAppearanceFactory().getAppearance(app);
		
    }

	private Vector availableSkills = new Vector(10);

	public Vector getAvailableSkills(){
		availableSkills.removeAllElements();
		if (hasChargeBeam)
			availableSkills.add(skills.get("CHARGE_BEAM"));
		return availableSkills;
	}

	private final static Hashtable skills = new Hashtable();
	static{
		skills.put("CHARGE_BEAM", new Skill("Charge Beam", null, 1));
	}

	public final static int
		EVT_CHAT = 11, 
		EVT_GOTO_LEVEL = 15;
	
	public final static int
		
		DAMAGE_WALKED_ON_LAVA = 1,
		DAMAGE_USING_ITEM = 2,
		DAMAGE_POISON = 3,
		DAMAGE_COLD = 4;

	private int baseSightRange = 7;
	
	public int getSightRange(){
		return baseSightRange  + (isXRayVisorActivated?3:0)+level.getMapCell(getPosition()).getHeight();
	}

	public void setFOV(FOV fov){
		this.fov = fov;
	}
	
	private transient FOV fov;
	
	public void see(){
		fov.startCircle(getLevel(), getPosition().x, getPosition().y, getSightRange());
	}
	
	public void darken(){
		level.darken();
	}
	/*
	public Position getNearestMonsterPosition(){
		VMonster monsters = level.getMonsters();
		Monster nearMonster = null;
		int minDist = 150;
		for (int i = 0; i < monsters.size(); i++){
			Monster monster = (Monster) monsters.elementAt(i);
			int distance = Position.flatDistance(level.getPlayer().getPosition(), monster.getPosition());
			if (distance < minDist){
				minDist = distance;
				nearMonster = monster;
			}
		}
		if (nearMonster != null)
			return nearMonster.getPosition();
		else
			return null;
	}
*/
	 public boolean sees(Position p){
		 return level.isVisible(p.x, p.y);
	 }
	 
	 public boolean sees(Monster m){
		 return sees(m.getPosition());
	 }
	 
	
	public boolean isDoNotRecordScore() {
		return doNotRecordScore;
	}

	public void setDoNotRecordScore(boolean doNotRecordScore) {
		this.doNotRecordScore = doNotRecordScore;
	}

	public int getMissiles() {
		return missiles;
	}

	public void setMissiles(int missiles) {
		this.missiles = missiles;
	}

	public int getMissilesCapacity() {
		return missilesCapacity;
	}

	public void setMissilesCapacity(int missilesCapacity) {
		this.missilesCapacity = missilesCapacity;
	}

	public int getPowerBombs() {
		return powerBombs;
	}

	public void setPowerBombs(int powerBombs) {
		this.powerBombs = powerBombs;
	}

	public int getPowerBombsCapacity() {
		return powerBombsCapacity;
	}

	public void setPowerBombsCapacity(int powerBombsCapacity) {
		this.powerBombsCapacity = powerBombsCapacity;
	}

	public int getReserveEnergy() {
		return reserveEnergy;
	}

	public void setReserveEnergy(int reserveEnergy) {
		this.reserveEnergy = reserveEnergy;
	}

	public int getReserveEnergyMax() {
		return reserveEnergyMax;
	}

	public void setReserveEnergyMax(int reserveEnergyMax) {
		this.reserveEnergyMax = reserveEnergyMax;
	}

	public int getSuperMissiles() {
		return superMissiles;
	}

	public void setSuperMissiles(int superMissiles) {
		this.superMissiles = superMissiles;
	}

	public int getSuperMissilesCapacity() {
		return superMissilesCapacity;
	}

	public void setSuperMissilesCapacity(int superMissilesCapacity) {
		this.superMissilesCapacity = superMissilesCapacity;
	}

	public int getTankEnergy() {
		return tankEnergy;
	}

	public void setTankEnergy(int tankEnergy) {
		this.tankEnergy = tankEnergy;
	}
	
	public String getBeamType(){
		if (isIceBeamActivated)
			if (isWaveBeamActivated)
				if (isChargedBeam())
					return "ICEWAVECHARGED";
				else
					return "ICEWAVE";
			else
				if (isChargedBeam())
					return "ICECHARGED";
				else
					return "ICE";
		else if (isPlasmaBeamActivated)
			if (isWaveBeamActivated)
				if (isChargedBeam())
					return "PLASMAWAVECHARGED";
				else
					return "PLASMAWAVE";
			else
				if (isChargedBeam())
					return "PLASMACHARGED";
				else
					return "PLASMA";
		else
			if (isWaveBeamActivated)
				if (isChargedBeam())
					return "WAVECHARGED";
				else
					return "WAVE";
			else
				if (isChargedBeam())
					return "CHARGED";
				else
					return "NORMAL";
	}
	
	public boolean isChargedBeam(){
		return beamChargeCount == 5;
	}
	
	public boolean isChargingBeam(){
		return beamChargeCount > 0 && beamChargeCount < 5;
	}

	public boolean isChargeBeamActivated() {
		return isHasChargeBeam();
	}

	public void setChargeBeamActivated(boolean isChargeBeamActivated) {
		this.isChargeBeamActivated = isChargeBeamActivated;
	}

	public boolean isHighJumpBoosterActivated() {
		return isHasHighJumpBooster();
	}

	public void setHighJumpBoosterActivated(boolean isHighJumpBoosterActivated) {
		this.isHighJumpBoosterActivated = isHighJumpBoosterActivated;
	}

	public boolean isIceBeamActivated() {
		return isHasIceBeam();
	}

	public void setIceBeamActivated(boolean isIceBeamActivated) {
		this.isIceBeamActivated = isIceBeamActivated;
		
	}

	public boolean isPlasmaBeamActivated() {
		return isHasPlasmaBeam();
	}

	public void setPlasmaBeamActivated(boolean isPlasmaBeamActivated) {
		this.isPlasmaBeamActivated = isPlasmaBeamActivated;
	}

	public boolean isScrewAttackActivated() {
		return isHasScrewAttack();
	}

	public void setScrewAttackActivated(boolean isScrewAttackActivated) {
		this.isScrewAttackActivated = isScrewAttackActivated;
	}

	public boolean isSpaceJumpActivated() {
		return isHasSpaceJump();
	}

	public void setSpaceJumpActivated(boolean isSpaceJumpActivated) {
		this.isSpaceJumpActivated = isSpaceJumpActivated;
	}

	public boolean isSpiderBallActivated() {
		return isHasSpiderBall();
	}

	public void setSpiderBallActivated(boolean isSpiderBallActivated) {
		this.isSpiderBallActivated = isSpiderBallActivated;
	}

	public boolean isSpringBallActivated() {
		return isHasSpringBall();
	}

	public void setSpringBallActivated(boolean isSpringBallActivated) {
		this.isSpringBallActivated = isSpringBallActivated;
	}

	public boolean isThermoVisorActivated() {
		return isHasThermoVisor();
	}

	public void setThermoVisorActivated(boolean isThermoVisorActivated) {
		this.isThermoVisorActivated = isThermoVisorActivated;
	}

	public boolean isVariaSuitActivated() {
		if (isGravitySuitActivated())
			return false;
		return isHasVariaSuit();
	}

	public void setVariaSuitActivated(boolean isVariaSuitActivated) {
		this.isVariaSuitActivated = isVariaSuitActivated;
	}

	public boolean isWaveBeamActivated() {
		return isHasWaveBeam();
	}

	public void setWaveBeamActivated(boolean isWaveBeamActivated) {
		this.isWaveBeamActivated = isWaveBeamActivated;
	}

	public boolean isXRayVisorActivated() {
		return isHasXRayVisor();
	}

	public void setXRayVisorActivated(boolean isXRayVisorActivated) {
		this.isXRayVisorActivated = isXRayVisorActivated;
	}

	public void setBallMorphed(boolean isBallMorphed) {
		this.isBallMorphed = isBallMorphed;
	}
	
	public int getBeamAttack(){
		int ret = 1;
		if (isIceBeamActivated() || isPlasmaBeamActivated())
			ret += 3;
		if (isChargedBeam())
			ret *= 2;
		return ret;
	}
	
	public void dischargeBeam(){
		beamChargeCount = 0;
	}
	
	public void initBeamCharge(){
		beamChargeCount = 1;
	}
	
	private int beamChargeCount;

	public int getAttackCost() {
		return attackCost;
	}

	public void setAttackCost(int attackCost) {
		this.attackCost = attackCost;
	}
	
	public int getMissileAttack(){
		return 3;
	}
	
	public int getSuperMissileAttack(){
		return 6;
	}
	
	public int getScrewAttackDamage(){
		return 8;
	}
	

	public boolean isHasChargeBeam() {
		return hasChargeBeam;
	}

	public void setHasChargeBeam(boolean hasChargeBeam) {
		this.hasChargeBeam = hasChargeBeam;
		isChargeBeamActivated = hasChargeBeam;
	}

	public boolean isHasHighJumpBooster() {
		return hasHighJumpBooster;
	}

	public void setHasHighJumpBooster(boolean hasHighJumpBooster) {
		this.hasHighJumpBooster = hasHighJumpBooster;
		isHighJumpBoosterActivated = hasHighJumpBooster; 
	}

	public boolean isHasIceBeam() {
		return hasIceBeam;
	}

	public void setHasIceBeam(boolean hasIceBeam) {
		this.hasIceBeam = hasIceBeam;
		isIceBeamActivated = hasIceBeam;
	}

	public boolean isHasPlasmaBeam() {
		return hasPlasmaBeam;
	}

	public void setHasPlasmaBeam(boolean hasPlasmaBeam) {
		this.hasPlasmaBeam = hasPlasmaBeam;
		isPlasmaBeamActivated = hasPlasmaBeam;
	}

	public boolean isHasScrewAttack() {
		return hasScrewAttack;
	}

	public void setHasScrewAttack(boolean hasScrewAttack) {
		this.hasScrewAttack = hasScrewAttack;
		isScrewAttackActivated = hasScrewAttack;
	}

	public boolean isHasSpaceJump() {
		return hasSpaceJump;
	}

	public void setHasSpaceJump(boolean hasSpaceJump) {
		this.hasSpaceJump = hasSpaceJump;
		isSpaceJumpActivated = hasSpaceJump;
	}

	public boolean isHasSpiderBall() {
		return hasSpiderBall;
	}

	public void setHasSpiderBall(boolean hasSpiderBall) {
		this.hasSpiderBall = hasSpiderBall;
		isSpiderBallActivated = hasSpiderBall;
	}

	public boolean isHasSpringBall() {
		return hasSpringBall;
	}

	public void setHasSpringBall(boolean hasSpringBall) {
		this.hasSpringBall = hasSpringBall;
		isSpringBallActivated = hasSpringBall;
	}

	public boolean isHasThermoVisor() {
		return hasThermoVisor;
	}

	public void setHasThermoVisor(boolean hasThermoVisor) {
		this.hasThermoVisor = hasThermoVisor;
		isThermoVisorActivated = hasThermoVisor;
	}

	public boolean isHasVariaSuit() {
		return hasVariaSuit;
	}

	public void setHasVariaSuit(boolean hasVariaSuit) {
		this.hasVariaSuit = hasVariaSuit;
		if (!isGravitySuitActivated)
			isVariaSuitActivated = hasVariaSuit;
	}

	public boolean isHasWaveBeam() {
		return hasWaveBeam;
	}

	public void setHasWaveBeam(boolean hasWaveBeam) {
		this.hasWaveBeam = hasWaveBeam;
		isWaveBeamActivated = hasWaveBeam;
	}

	public boolean isHasXRayVisor() {
		return hasXRayVisor;
	}

	public void setHasXRayVisor(boolean hasXRayVisor) {
		this.hasXRayVisor = hasXRayVisor;
		isXRayVisorActivated = hasXRayVisor;
	}

	public int getJumpCost() {
		return jumpCost;
	}

	public void setJumpCost(int jumpCost) {
		this.jumpCost = jumpCost;
	}
	
	public boolean isScewAttacking(){
		return isJumping() && isHasScrewAttack();
	}
	
	public void jump (){
		isJumping = true;
		if (!isJumping())
			height = level.getMapCell(getPosition()).getHeight();
		jumpingCounter = getJumpingPower();
	}
	
	public boolean isJumping(){
		return isJumping;
	}
	private boolean isJumping;
	private int height;
	private int jumpingCounter;
	
	public int getHeight(){
		return height;
	}
	
	public int getWalkCost(){
		return walkCost;
	}
	
	public int getRating(){
		return 
			getEnergyTanksCapacity() + 
			(getReserveEnergyMax() / 100) * 2 +
			(isHasChargeBeam() ? 1 :0)+
			(isHasEnergyBomb() ? 1 :0)+
			(isHasGravitySuit() ? 4 :0)+
			(isHasHighJumpBooster() ? 1 :0)+
			(isHasIceBeam() ? 2 :0)+
			(isHasMorphBall() ? 1 :0)+
			(isHasPlasmaBeam() ? 3 :0)+
			(isHasScrewAttack() ? 5 :0)+
			(isHasSpaceJump() ? 1 :0)+
			(isHasSpiderBall() ? 3 :0)+
			(isHasSpringBall() ? 1 :0)+
			(isHasThermoVisor() ? 1 :0)+
			(isHasVariaSuit() ? 2 :0)+
			(isHasWaveBeam() ? 2 :0)+
			(isHasXRayVisor() ? 2 :0)+
			(int)(getMissilesCapacity()/40.0d) +
			(int)(getSuperMissilesCapacity()/20.0d) +
			(int)(getSuperMissilesCapacity()/10.0d) 
			;
	}
	
	public String getArmorDescription(){
		if (isGravitySuitActivated)
			return "Gravity Suit";
		if (isVariaSuitActivated())
			return "Varia Suit";
		return "Power Suit";
	}
	
	public String getBeamDescription(){
		String beam = "";
		if (isIceBeamActivated())
			if (isWaveBeamActivated())
				beam = "Ice Wave";
			else
				beam = "Ice Shoot";
		if (isPlasmaBeamActivated())
			if (isWaveBeamActivated())
				beam = "Plasma Wave";
			else
				beam = "Plasma Shoot";
		if (isWaveBeamActivated())
			beam = "Wave Beam";
		else
			beam = "Power Shoot";
		if (isChargedBeam()) {
			return "*"+beam + "*";
		}else
			return beam;
	}
	
	public int getEnergyTanks(){
		return energyTanks;
	}
	
	public void setEnergyTanks(int val){
		energyTanks = val;
	}

	public int getEnergyTanksCapacity() {
		return energyTanksCapacity;
	}

	public void setEnergyTanksCapacity(int energyTanksCapacity) {
		this.energyTanksCapacity = energyTanksCapacity;
	}

	public boolean isHasEnergyBomb() {
		return hasEnergyBomb;
	}

	public void setHasEnergyBomb(boolean hasEnergyBomb) {
		this.hasEnergyBomb = hasEnergyBomb;
		isEnergyBombActivated = true;
	}

	
	
	public boolean isHasGravitySuit() {
		return hasGravitySuit;
	}

	public void setHasGravitySuit(boolean hasGravitySuit) {
		this.hasGravitySuit = hasGravitySuit;
		isGravitySuitActivated = hasGravitySuit;
		isVariaSuitActivated = !hasGravitySuit;
	}

	public boolean isHasMorphBall() {
		return hasMorphBall;
	}

	public void setHasMorphBall(boolean hasMorphBall) {
		this.hasMorphBall = hasMorphBall;
		isMorphBallActivated = hasMorphBall;
	}

	public boolean isGravitySuitActivated() {
		return isHasGravitySuit();
	}

	public void setGravitySuitActivated(boolean isGravitySuitActivated) {
		this.isGravitySuitActivated = isGravitySuitActivated;
	}

	public boolean isEnergyBombActivated() {
		return isHasEnergyBomb();
	}

	public boolean isMorphBallActivated() {
		return isHasMorphBall();
	}

	public boolean isAutoFire() {
		return autoFire;
	}

	public void setAutoFire(boolean autoFire) {
		this.autoFire = autoFire;
	}
	
	public Monster getNearestMonsterOnSight(){
		int minDistance = 9999999;
		Monster nearest = null;
		for (int i = 0; i < monstersOnSight.size(); i++){
			Monster m = (Monster)monstersOnSight.get(i);
			int distance = Position.flatDistance(getPosition(), m.getPosition());
			if (distance < minDistance){
				nearest = m;
			}
		}
		return nearest;
	}
	
	private ArrayList monstersOnSight = new ArrayList();
	public void addMonsterOnSight(Monster m){
		monstersOnSight.add(m);
	}
	
	public void resetMonstersOnSight(){
		monstersOnSight.clear();		
	}
	
	public ArrayList getMonstersOnSight(){
		return monstersOnSight;
	}
	
	public void addItem(Item toAdd){
		if (!canCarry()){
			if (level != null)
				level.addMessage("You can't carry anything more");
			return;
		}
		String toAddID = toAdd.getFullID();
		Equipment equipmentx = (Equipment) inventory.get(toAddID);
		if (equipmentx == null)
			inventory.put(toAddID, new Equipment(toAdd, 1));
		else
			equipmentx.increaseQuantity();
	}
	
	public boolean canCarry(){
		return getItemCount() < carryMax;
	}
	
	public int getItemCount(){
		int eqCount = 0;
		Enumeration en = inventory.elements();
		while (en.hasMoreElements())
			eqCount += ((Equipment)en.nextElement()).getQuantity();
		return eqCount;
	}
	
    public void setCarryMax(int x){
    	carryMax = x;
    }
}