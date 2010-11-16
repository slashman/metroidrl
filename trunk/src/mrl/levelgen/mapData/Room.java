package mrl.levelgen.mapData;

import java.util.ArrayList;

public class Room {
	private String id;
	private String cellmapId;
	private String facet;
	private ArrayList /*String[]*/ charmap = new ArrayList();
	private String description;
	private CellMap privateCellMap;
	private String doors;
	
	public String getCellmapId() {
		return cellmapId;
	}
	public void setCellmapId(String cellmapId) {
		this.cellmapId = cellmapId;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFacet() {
		return facet;
	}
	public void setFacet(String facets) {
		this.facet = facets;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public CellMap getPrivateCellMap() {
		return privateCellMap;
	}
	public void setPrivateCellMap(CellMap privateCellMap) {
		this.privateCellMap = privateCellMap;
	}
	public ArrayList getCharmap() {
		return charmap;
	}
	
	public void addCharmap(String[] charmap) {
		this.charmap.add(charmap);
	}
	public String getDoors() {
		return doors;
	}
	public void setDoors(String doors) {
		this.doors = doors;
	}
}
