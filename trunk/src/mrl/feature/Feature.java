package mrl.feature;

import mrl.player.*;
import mrl.ui.*;
import sz.csi.textcomponents.BasicListItem;
import sz.csi.textcomponents.ListItem;
import sz.util.*;
import sz.csi.textcomponents.*;


public class Feature implements Cloneable, java.io.Serializable {
	/** A feature is something that stays inside the level but may be moved,
	 * destroyed or otherwise affected. */
	private Feature prize;
	private boolean destroyable, isSolid;
	private int 
			energyPrize,
			missilePrize,
			supermissilePrize,
			powerBombPrize,
			energyTankPrize,
			reserveTankPrize,
			missileCapacityPrize,
			superMissileCapacityPrize,
			powerBombCapacityPrize;
	
	private boolean
		variaSuitPrize,
		chargeBeamPrize,
		iceBeamPrize,
		waveBeamPrize,
		plasmaBeamPrize,
		hiJumpPrize,
		screwAttackPrize,
		spaceJumpPrize,
		springBallPrize,
		spiderBallPrize,
		xRayVisorPrize,
		thermoVisorPrize,
		gravitySuitPrize,
		morphBallPrize,
		bombBlastPrize
		;
	
	private Position position;
	private transient Appearance appearance;
	private String ID, description,appearanceID;
	private String trigger;
	private int heightMod;
	private int keyCost;
	private String effect;
	private int scorePrize;
	private int healPrize;
	private boolean relevant = true;

	public String getID(){
		return ID;
	}

	public Object clone(){
		try {
			Feature x = (Feature) super.clone();

			if (position != null)
				x.setPosition(position.x, position.y, position.z);
			if (prize != null)
				x.setPrize((Feature)prize.clone());
			return x;
		} catch (CloneNotSupportedException cnse){
			Debug.doAssert(false, "failed class cast, Feature.clone()");
		}
		return null;
	}

	public void setPrize(Feature what){
		prize = what;
	}

	public Feature (String pID, Appearance pApp, String pDescription){
		ID = pID;
		appearance = pApp;
		appearanceID = pApp.getID();
		description = pDescription;
		Debug.doAssert(pApp != null, "No appearance specified for Feature");
	}

	public void setPosition(int x, int y, int z){
		position = new Position (x,y, z);
	}

	public Appearance getAppearance(){
		if (appearance == null){
			if (appearanceID != null)
				appearance = AppearanceFactory.getAppearanceFactory().getAppearance(appearanceID);
		}
		return appearance;
	}

	public String getDescription(){
		return description;
	}

	public Position getPosition(){
		return position;
	}

	
	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String value) {
		trigger = value;
	}

	public int getHeightMod() {
		return heightMod;
	}

	public void setHeightMod(int value) {
		heightMod = value;
	}

	public int getKeyCost() {
		return keyCost;
	}

	public void setKeyCost(int value) {
		keyCost = value;
	}

	public boolean isSolid() {
		return isSolid;
	}

	public void setSolid(boolean value) {
		isSolid = value;
	}

	public boolean isDestroyable() {
		return destroyable;
	}

	public void setDestroyable(boolean value) {
		destroyable = value;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String value) {
		effect = value;
	}

	public int getScorePrize() {
		return scorePrize;
	}

	public void setScorePrize(int value) {
		scorePrize = value;
	}

	public int getHealPrize() {
		return healPrize;
	}

	public void setHealPrize(int value) {
		healPrize = value;
	}

	public boolean isRelevant() {
		return relevant;
	}

	public void setRelevant(boolean relevant) {
		this.relevant = relevant;
	}

	public boolean isVisible(){
		return !getAppearance().getID().equals("VOID");
	}

	public int getEnergyPrize() {
		return energyPrize;
	}

	public void setEnergyPrize(int energyPrize) {
		this.energyPrize = energyPrize;
	}

	public int getMissilePrize() {
		return missilePrize;
	}

	public void setMissilePrize(int missilePrize) {
		this.missilePrize = missilePrize;
	}

	public int getPowerBombPrize() {
		return powerBombPrize;
	}

	public void setPowerBombPrize(int powerBombPrize) {
		this.powerBombPrize = powerBombPrize;
	}

	public int getSupermissilePrize() {
		return supermissilePrize;
	}

	public void setSupermissilePrize(int supermissilePrize) {
		this.supermissilePrize = supermissilePrize;
	}

	public boolean isChargeBeamPrize() {
		return chargeBeamPrize;
	}

	public void setChargeBeamPrize(boolean chargeBeamPrize) {
		this.chargeBeamPrize = chargeBeamPrize;
	}

	public int getEnergyTankPrize() {
		return energyTankPrize;
	}

	public void setEnergyTankPrize(int energyTankPrize) {
		this.energyTankPrize = energyTankPrize;
	}

	public boolean isGravitySuitPrize() {
		return gravitySuitPrize;
	}

	public void setGravitySuitPrize(boolean gravitySuitPrize) {
		this.gravitySuitPrize = gravitySuitPrize;
	}

	public boolean isHiJumpPrize() {
		return hiJumpPrize;
	}

	public void setHiJumpPrize(boolean hiJumpPrize) {
		this.hiJumpPrize = hiJumpPrize;
	}

	public boolean isIceBeamPrize() {
		return iceBeamPrize;
	}

	public void setIceBeamPrize(boolean iceBeamPrize) {
		this.iceBeamPrize = iceBeamPrize;
	}

	public int getMissileCapacityPrize() {
		return missileCapacityPrize;
	}

	public void setMissileCapacityPrize(int missileCapacityPrize) {
		this.missileCapacityPrize = missileCapacityPrize;
	}

	public boolean isPlasmaBeamPrize() {
		return plasmaBeamPrize;
	}

	public void setPlasmaBeamPrize(boolean plasmaBeamPrize) {
		this.plasmaBeamPrize = plasmaBeamPrize;
	}

	public int getPowerBombCapacityPrize() {
		return powerBombCapacityPrize;
	}

	public void setPowerBombCapacityPrize(int powerBombCapacityPrize) {
		this.powerBombCapacityPrize = powerBombCapacityPrize;
	}

	public int getReserveTankPrize() {
		return reserveTankPrize;
	}

	public void setReserveTankPrize(int reserveTankPrize) {
		this.reserveTankPrize = reserveTankPrize;
	}

	public boolean isScrewAttackPrize() {
		return screwAttackPrize;
	}

	public void setScrewAttackPrize(boolean screwAttackPrize) {
		this.screwAttackPrize = screwAttackPrize;
	}

	public boolean isSpaceJumpPrize() {
		return spaceJumpPrize;
	}

	public void setSpaceJumpPrize(boolean spaceJumpPrize) {
		this.spaceJumpPrize = spaceJumpPrize;
	}

	public boolean isSpiderBallPrize() {
		return spiderBallPrize;
	}

	public void setSpiderBallPrize(boolean spiderBallPrize) {
		this.spiderBallPrize = spiderBallPrize;
	}

	public boolean isSpringBallPrize() {
		return springBallPrize;
	}

	public void setSpringBallPrize(boolean springBallPrize) {
		this.springBallPrize = springBallPrize;
	}

	public int getSuperMissileCapacityPrize() {
		return superMissileCapacityPrize;
	}

	public void setSuperMissileCapacityPrize(int superMissileCapacityPrize) {
		this.superMissileCapacityPrize = superMissileCapacityPrize;
	}

	public boolean isThermoVisorPrize() {
		return thermoVisorPrize;
	}

	public void setThermoVisorPrize(boolean thermoVisorPrize) {
		this.thermoVisorPrize = thermoVisorPrize;
	}

	public boolean isVariaSuitPrize() {
		return variaSuitPrize;
	}

	public void setVariaSuitPrize(boolean variaSuitPrize) {
		this.variaSuitPrize = variaSuitPrize;
	}

	public boolean isWaveBeamPrize() {
		return waveBeamPrize;
	}

	public void setWaveBeamPrize(boolean waveBeamPrize) {
		this.waveBeamPrize = waveBeamPrize;
	}

	public boolean isXRayVisorPrize() {
		return xRayVisorPrize;
	}

	public void setXRayVisorPrize(boolean rayVisorPrize) {
		xRayVisorPrize = rayVisorPrize;
	}

	public boolean isMorphBallPrize() {
		return morphBallPrize;
	}

	public void setMorphBallPrize(boolean morphBallPrize) {
		this.morphBallPrize = morphBallPrize;
	}

	public boolean isBombBlastPrize() {
		return bombBlastPrize;
	}

	public void setBombBlastPrize(boolean bombBlastPrize) {
		this.bombBlastPrize = bombBlastPrize;
	}
}