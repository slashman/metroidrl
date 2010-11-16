package mrl.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import mrl.game.CRLException;
import mrl.levelgen.mapData.CellMap;
import mrl.levelgen.mapData.MapData;
import mrl.levelgen.mapData.Room;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class RoomsLoader {
	private static ArrayList processRooms(Node node){
		ArrayList ret = new ArrayList();
		NodeList nodes = ((Element)node).getElementsByTagName("room");
		for (int i = 0; i < nodes.getLength(); i++){
			Node n = nodes.item(i);
			ret.add(processRoom(n));
		}
		return ret;
	}
	
	private static Room processRoom(Node node){
		Room ret = new Room();
		String id = at(node, "id");
		ret.setId(id);
		ret.setCellmapId(at(node, "cellmap"));
		ret.setFacet(at(node, "facet"));
		ret.setDescription(at(node, "description"));
		NodeList nodes = ((Element)node).getElementsByTagName("map");
		for (int i = 0; i < nodes.getLength(); i++){
			Node n = nodes.item(i);
			ret.addCharmap(processCharMap(n));
		}
		
		nodes = ((Element)node).getElementsByTagName("cellmap");
		for (int i = 0; i < nodes.getLength(); i++){
			Node n = nodes.item(i);
			ret.setPrivateCellMap(processCellMap(n));
		}
		
		return ret;
	}
	
	private static String[] processCharMap(Node node){
		ArrayList temp = new ArrayList(); 
		NodeList nodes = ((Element)node).getElementsByTagName("r");
		for (int i = 0; i < nodes.getLength(); i++){
			Element r = (Element)nodes.item(i);
			temp.add(r.getFirstChild().getNodeValue());
		}
		return (String[]) temp.toArray(new String[temp.size()]);
	}
		
	private static ArrayList processCellMaps(Node node){
		ArrayList ret = new ArrayList();
		NodeList nodes = ((Element)node).getElementsByTagName("cellmap");
		for (int i = 0; i < nodes.getLength(); i++){
			Node n = nodes.item(i);
			ret.add(processCellMap(n));
		}
		return ret;
	}
	
	private static String at(Node node, String attribute){
		Node attributeNode = node.getAttributes().getNamedItem(attribute);
		if (attributeNode == null)
			return null;
		else
			return attributeNode.getNodeValue();
	}
	private static CellMap processCellMap(Node node){
		CellMap ret = new CellMap();
		String id = at(node, "id");
		ret.setId(id);
		NodeList nodes = ((Element)node).getElementsByTagName("cellmapping");
		for (int i = 0; i < nodes.getLength(); i++){
			Node n = nodes.item(i);
			ret.addMapping(at(n, "char"), at(n, "cell"));
		}
		return ret;
	}
	
	public static MapData getMapData(String mapFile) throws CRLException{
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse (new File(mapFile));
			MapData mapData = new MapData();
			// normalize text representation
			doc.getDocumentElement().normalize();
			NodeList cellMapNodes = doc.getElementsByTagName("cellmaps");
			for (int i = 0; i < cellMapNodes.getLength(); i++){
				Node n = cellMapNodes.item(i);
				mapData.setCellMaps(processCellMaps(n));
			}
			NodeList roomsNodes = doc.getElementsByTagName("rooms");
			for (int i = 0; i < roomsNodes.getLength(); i++){
				Node n = roomsNodes.item(i);
				mapData.setRooms(processRooms(n));
			}
			return mapData;
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

