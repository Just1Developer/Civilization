package net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties;

import java.util.HashMap;
import java.util.Map;

public enum PopulationDensityPreference {
	//Values are per grid and upper bound
	TOLERANCE(10) /*in percent (+10% is ok)*/,
	VERY_LOW_DENS(500), LOW_DENS(5000),
	MEDIUM_DENS(15000), UP_MEDIUM_DENS(35000), HIGH_DENS(100000),
	EXTREME_DENS(500000), ABYSMAL_DENS(1500000), UNLIMITED_DENS(2000000000);
	
	private int value;
	private static Map map = new HashMap<>();
	
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
}
