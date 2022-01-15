package net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties;

import net.justonedeveloper.prvt.AI.HumanCivilization.enums.PopulationType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum PopulationDensityPreference implements HumanProperty {
	//Values are per grid and upper bound
	TOLERANCE(10) /*in percent (+10% is ok)*/,
	VERY_LOW_DENS(500), LOW_DENS(5000),
	MEDIUM_DENS(15000), UP_MEDIUM_DENS(35000), HIGH_DENS(100000),
	EXTREME_DENS(500000), ABYSMAL_DENS(1500000), UNLIMITED_DENS(2000000000);
	
	public static PopulationDensityPreference[] ALL = {VERY_LOW_DENS, LOW_DENS, MEDIUM_DENS, UP_MEDIUM_DENS, HIGH_DENS, EXTREME_DENS, ABYSMAL_DENS, UNLIMITED_DENS};
	
	private int value;
	private static Map map = new HashMap<Integer, PopulationDensityPreference>();
	
	private PopulationDensityPreference(int value) {
		this.value = value;
	}
	
	static {
		for (PopulationDensityPreference pref : PopulationDensityPreference.values()) {
			map.put(pref.value, pref);
		}
	}
	
	public static PopulationDensityPreference valueOf(int pref) {
		return (PopulationDensityPreference) map.get(pref);
	}
	
	public int getValue() {
		return value;
	}

	/*@Deprecated
	public static PopulationDensityPreference fromType(PopulationType type) {
		switch (type) {
			case EMPTY:
			case CAMP:
				return VERY_LOW_DENS;
			case VILLAGE:
				return LOW_DENS;
			case TOWN:
				return UP_MEDIUM_DENS;
			case CITY:
				return HIGH_DENS;
			case BIG_CITY:
				return EXTREME_DENS;
			case BOOMING_CITY:
				return ABYSMAL_DENS;
			default:
				return UNLIMITED_DENS;
		}
	}*/

	//Parse from Int
	public static PopulationDensityPreference parsePref(int population) {
		Object[] ob = map.keySet().toArray();
		Arrays.sort(ob);

		for(Object key : ob) {
			if(population < (Integer) key) return (PopulationDensityPreference) map.get(key);
		}
		return PopulationDensityPreference.VERY_LOW_DENS;
	}

	public static int offsetFrom(PopulationDensityPreference pref, PopulationDensityPreference Target) {
		int indexPref = -1, indexTarget = -1;
		for(int i = 0; i < ALL.length; i++) {
			if(ALL[i] == pref) indexPref = i;
			if(ALL[i] == Target) indexTarget = i;
			if(indexPref >= 0 && indexTarget >= 0) break;
		}
		if(indexPref > indexTarget) return indexPref-indexTarget;
		return indexTarget-indexPref;
	}

}
