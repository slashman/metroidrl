package mrl.player;

public interface PlayerEventListener {
	public void informEvent(int code, Object param);
	public void informEvent(int code);
}