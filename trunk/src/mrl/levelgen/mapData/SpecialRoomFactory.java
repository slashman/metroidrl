package mrl.levelgen.mapData;

import java.util.ArrayList;

import mrl.levelgen.featureCarve.Feature;
import sz.util.Util;


public class SpecialRoomFactory {
	public static SpecialRoomFactory thus = new SpecialRoomFactory();
	private MapData mapData;
	public void initializeMapData(MapData mapData){
		this.mapData = mapData;
	}
	
	public CellMap getGenericMapping(){
		return mapData.getCellMap("GENERIC");
	}
	
	public Feature buildFeatureByFacet(String facet){
		ArrayList rooms = mapData.getRooms();
		ArrayList candidates = new ArrayList();
		for (int i = 0; i < rooms.size(); i++){
			Room r = (Room)rooms.get(i);
			if (r.getFacet().equals(facet)){
				candidates.add(r);
			}
		}
		return buildFeatureByRoom((Room) Util.randomElementOf(candidates));
	}
	
	public Feature buildFeatureByRoom(Room room){
		return new MapDataFeature(room, mapData.getCellMap(room.getCellmapId()));
	}
}
