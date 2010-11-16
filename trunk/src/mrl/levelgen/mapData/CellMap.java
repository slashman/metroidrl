package mrl.levelgen.mapData;

import java.util.Hashtable;

public class CellMap {
	private Hashtable cellMappings = new Hashtable();
	private String id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public void addMapping(String character, String cell){
		cellMappings.put(character, cell);
	}
	
	public String getMapping(String character){
		return (String) cellMappings.get(character);
	}
	
}
