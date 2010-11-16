package mrl.ui;

import java.io.File;
import java.util.Vector;

import mrl.game.Game;
import mrl.player.HiScore;
import mrl.player.MPlayer;


public abstract class Display {
	public static Display thus;
	public abstract int showTitleScreen();	
	public abstract void showIntro(MPlayer player);
	public abstract boolean showResumeScreen(MPlayer player);
	public abstract void showEndgame(MPlayer player);	
	public abstract void showHiscores(HiScore[] scores);
	public abstract void showHelp();
	public abstract int showSavedGames(File[] saveFiles);
	public abstract void showChat(String chatID, Game game);
	public abstract void showScreen(Object screenID);
	public abstract void showNav(String message);
	public abstract void showMap(String locationKey, String locationDescription);
	public abstract void showScan(mrl.monster.Monster who, MPlayer player);
}
