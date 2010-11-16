package mrl.levelgen.mapData;

import java.util.ArrayList;

public class MapData {
	private ArrayList cellMaps;
	private ArrayList rooms;
	
	public void setCellMaps(ArrayList cellMaps) {
		this.cellMaps = cellMaps;
	}
	public ArrayList getCellMaps() {
		return cellMaps;
	}
	public void setRooms(ArrayList rooms) {
		this.rooms = rooms;
	}
	public ArrayList getRooms() {
		return rooms;
	}
	
	public CellMap getCellMap(String cellMapId){
		for (int i = 0; i < getCellMaps().size(); i++){
			CellMap c = (CellMap)getCellMaps().get(i);
			if (c.getId().equals(cellMapId)){
				return c;
			}
		}
		return null;
	}
}
