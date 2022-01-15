package net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties;

import net.justonedeveloper.prvt.AI.HumanCivilization.enums.PopulationType;

public enum PopulationPreference implements HumanProperty {
	
	LOW, MEDIUM, HIGH, EXTREME;
	
	public static PopulationPreference[] ALL = {LOW, MEDIUM, HIGH, EXTREME};

	public static PopulationPreference fromType(PopulationType type) {
		switch (type) {
			case EMPTY:
			case CAMP:
			case VILLAGE:
				return LOW;
			case TOWN:
			case CITY:
				return MEDIUM;
			case BIG_CITY:
			case BOOMING_CITY:
				return HIGH;
			default:
				return EXTREME;
		}
	}

	public static int offsetFrom(PopulationPreference pref, PopulationPreference Target) {
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
