package mrl.data;

import mrl.feature.Feature;
import mrl.ui.AppearanceFactory;

public class Features {
	public static Feature[] getFeatureDefinitions(AppearanceFactory apf){
        Feature [] ret = new Feature [32];
		ret[0] = new Feature("SMALL_ENERGY", apf.getAppearance("SMALL_ENERGY"), "Energy globe"); ret[0].setEnergyPrize(5);
		ret[1] = new Feature("BIG_ENERGY", apf.getAppearance("BIG_ENERGY"), "Big energy globe"); ret[1].setEnergyPrize(20);
		ret[2] = new Feature("MISSILES", apf.getAppearance("MISSILES"), "Missile Energy"); ret[2].setMissilePrize(2);
		ret[3] = new Feature("SUPERMISSILES", apf.getAppearance("SUPERMISSILES"), "Super Missile Energy"); ret[3].setSupermissilePrize(1);
		ret[4] = new Feature("POWER_BOMBS", apf.getAppearance("POWER_BOMBS"), "Bomb Energy"); ret[4].setPowerBombPrize(1);
		
		ret[5] = new Feature("ENERGY_TANK", apf.getAppearance("ENERGY_TANK"), "Energy Tank"); ret[5].setEnergyTankPrize(1);
		ret[6] = new Feature("RESERVE_TANK", apf.getAppearance("RESERVE_TANK"), "Reserve Tank"); ret[6].setReserveTankPrize(1);
		ret[7] = new Feature("MISSILE_TANK", apf.getAppearance("MISSILE_TANK"), "Missile Tank"); ret[7].setMissileCapacityPrize(5);
		ret[8] = new Feature("SUPERMISSILE_TANK",apf.getAppearance("SUPERMISSILE_TANK"), "Super Missile Tank"); ret[8].setSuperMissileCapacityPrize(5);
		ret[9] = new Feature("POWER_BOMB_TANK", apf.getAppearance("POWER_BOMB_TANK"), "Power Bomb Tank"); ret[9].setPowerBombCapacityPrize(5);
		
		ret[10] = new Feature("VARIA_SUIT", apf.getAppearance("VARIA_ENERGY"), "Varia Energy"); ret[10].setVariaSuitPrize(true);
		ret[11] = new Feature("CHARGE_BEAM", apf.getAppearance("CHARGE_BEAM"), "Charge Beam"); ret[11].setChargeBeamPrize(true);
		ret[12] = new Feature("ICE_BEAM", apf.getAppearance("ICE_BEAM"), "Ice Beam"); ret[12].setIceBeamPrize(true);
		ret[13] = new Feature("WAVE_BEAM", apf.getAppearance("WAVE_BEAM"), "Wave Beam"); ret[13].setWaveBeamPrize(true);
		ret[14] = new Feature("PLASMA_BEAM", apf.getAppearance("PLASMA_BEAM"), "Plasma Beam"); ret[14].setPlasmaBeamPrize(true);
		ret[15] = new Feature("HI_JUMP", apf.getAppearance("HI_JUMP"), "HiJump Booster"); ret[15].setHiJumpPrize(true);
		ret[16] = new Feature("SCREW_ATTACK", apf.getAppearance("SCREW_ATTACK"), "Screw Upgrade"); ret[16].setScrewAttackPrize(true);
		ret[17] = new Feature("SPACE_JUMP", apf.getAppearance("SPACE_JUMP"), "Space Jump"); ret[17].setSpaceJumpPrize(true);
		ret[18] = new Feature("SPRINGBALL", apf.getAppearance("SPRINGBALL"), "Spring Ball"); ret[18].setSpringBallPrize(true);
		ret[19] = new Feature("SPIDERBALL", apf.getAppearance("SPIDERBALL"), "Spider Ball"); ret[19].setSpiderBallPrize(true);
		ret[20] = new Feature("XRAYVISOR", apf.getAppearance("XRAYVISOR"), "X-Ray Visor"); ret[20].setXRayVisorPrize(true);
		ret[21] = new Feature("THERMOVISOR", apf.getAppearance("THERMOVISOR"), "Thermo Visor"); ret[21].setThermoVisorPrize(true);
		ret[22] = new Feature("GRAVITY_ENERGY", apf.getAppearance("GRAVITY_ENERGY"), "Gravity Energy"); ret[22].setGravitySuitPrize(true);
		
		ret[23] = new Feature("GRAVITY_SUIT", apf.getAppearance("GRAVITY_SUIT"), "Gravity Energy"); ret[23].setGravitySuitPrize(true);
		ret[24] = new Feature("MORPH_ENERGY", apf.getAppearance("MORPH_ENERGY"), "Ball Morpher"); ret[24].setMorphBallPrize(true);
		ret[25] = new Feature("BOMB_ENERGY", apf.getAppearance("BOMB_ENERGY"), "Bomb Blast"); ret[25].setBombBlastPrize(true);
		
		ret[26] = new Feature("A", apf.getAppearance("A"), "Atomic Weapon");
		ret[27] = new Feature("B", apf.getAppearance("B"), "Biological Weapon");
		ret[28] = new Feature("C", apf.getAppearance("C"), "Chemical Weapon");
		ret[29] = new Feature("G", apf.getAppearance("G"), "Genetical Weapon");
		
		ret[30] = new Feature("RECOVER_ENERGY", apf.getAppearance("RECOVER_ENERGY"), "Energy Pod");
		ret[31] = new Feature("RECOVER_MISSILE", apf.getAppearance("RECOVER_MISSILE"), "Missile Pod");
		
		return ret;
	}
}
