package mrl.player;

public class HiScore {
	private String name;
	private int score;
	private String date;
	private String turns;
	private String deathString;
	private String deathLevel;
	
	public String getName() {
		return name;
	}

	public void setName(String value) {
		name = value;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int value) {
		score = value;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String value) {
		date = value;
	}

	public String getTurns() {
		return turns;
	}

	public void setTurns(String value) {
		turns = value;
	}

	public String getDeathString() {
		return deathString;
	}

	public void setDeathString(String value) {
		deathString = value;
	}

	public String getDeathLevel() {
		return deathLevel;
	}

	public void setDeathLevel(String deathLevel) {
		this.deathLevel = deathLevel;
	}

}
