
package mrl.game;

import java.util.Hashtable;

import mrl.player.MPlayer;


public abstract class PlayerGenerator {
	public static PlayerGenerator thus;
	public abstract MPlayer generatePlayer();
	
	private Hashtable SPECIAL_PLAYERS = new Hashtable();
	
	public MPlayer createSpecialPlayer(String playerID){
		return (MPlayer) SPECIAL_PLAYERS.get(playerID);
	}
	
	public MPlayer getPlayer(String name){
		MPlayer player = new MPlayer();
		player.setName(name);
		player.setEnergyTanksCapacity(1);
		player.setEnergyTanks(1);
		player.setTankEnergy(40);
		player.setReserveEnergyMax(100);
		player.setHasChargeBeam(true);
		player.setCarryMax(20);
		
		/* Full Power */
		/*player.setEnergyTanks(20);
		player.setTankEnergy(99);
		player.setMissilesCapacity(99);
		player.setMissiles(99);
		player.setSuperMissilesCapacity(99);
		player.setSuperMissiles(99);
		player.setHasGravitySuit(true);
		player.setGravitySuitActivated(true);
		player.setHasMorphBall(true);
		player.setHasEnergyBomb(true);
		player.setPowerBombsCapacity(99);
		player.setPowerBombs(99);
		/**/
		
		return player;
	}
}
