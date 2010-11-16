package sz.rpg;

import sz.util.Util;

public class Roll {
	private int dices;
	private int sides;
	
	public Roll (int dices, int sides){
		this.dices = dices;
		this.sides = sides;
	}
	
	public int roll(){
		int sum = 0;
		for (int i = 0; i < dices; i++){
			sum += Util.rand(1, sides);
		}
		return sum;
	}
	
	
	public String getDescription(){
		return dices+"d"+sides;
	}
}
