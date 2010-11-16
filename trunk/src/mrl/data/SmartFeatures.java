package mrl.data;

import mrl.ai.SelectorFactory;
import mrl.feature.SmartFeature;
import mrl.ui.AppearanceFactory;

public class SmartFeatures {
	public static SmartFeature[] getSmartFeatures(SelectorFactory sf){
		AppearanceFactory apf = AppearanceFactory.getAppearanceFactory();
		SmartFeature [] ret = new SmartFeature [2];
		ret[0] = new SmartFeature("BOMB", "Energy Bomb", apf.getAppearance("BOMB")); ret[0].setSelector(sf.getSelector("BOMB_SELECTOR"));
		ret[1] = new SmartFeature("SUPERBOMB", "Power Bomb", apf.getAppearance("SUPERBOMB")); ret[1].setSelector(sf.getSelector("SUPERBOMB_SELECTOR"));
		return ret;
	}
}
