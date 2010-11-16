package mrl.feature;

import java.io.Serializable;
import java.util.*;

import sz.util.*;

public class VFeatures implements Serializable{
   	Vector features;
	Hashtable mLocs;

	public void addFeature(Feature what){
		features.add(what);
		//mLocs.put(what, what.getPosition());
		mLocs.put(what.getPosition(), what);
	}

	public Feature getFeatureAt(Position p){
		//return (Feature) mLocs.get(p);
		for (int i=0; i<features.size(); i++){
			if (((Feature)features.elementAt(i)).getPosition().equals(p)){
				return (Feature)features.elementAt(i);
			}
		}
		//Debug.byebye("Feature not found! "+p);
		return null;
	}

	public VFeatures(int size){
		features = new Vector(size);
		mLocs = new Hashtable(size);
	}

	public void removeFeature(Feature o){
		features.remove(o);
		if (mLocs.containsValue(o))
			mLocs.remove(o);
	}

}