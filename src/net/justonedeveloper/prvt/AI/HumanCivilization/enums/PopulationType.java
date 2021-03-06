package net.justonedeveloper.prvt.AI.HumanCivilization.enums;

import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.PopulationPreference;

public enum PopulationType implements FieldType {
	
	EMPTY, CAMP, VILLAGE, TOWN, CITY, BIG_CITY, BOOMING_CITY, MEGA_CITY, GODTIER_CITY;
	
	private static int[] bounds = {9,50,5000,500000,250000,1000000,9999999,49999999};
	
	/*
	 * Empty: 0 - 9
	 * Camp: 10 - 50
	 * Village: 51-5000
	 * Town: 5001 - 50000
	 * City: 50001 - 250.000
	 * Big City: 250.001 - 1.000.000
	 * Booming_City: 1.000.001 - 9.999.999
	 * Mega_City: 10.000.000 - 49.999.999
	 * Godtier_City: 50.000.000+
	 * */
	
	public static PopulationType[] ALL = {EMPTY, CAMP, VILLAGE, TOWN, CITY, BIG_CITY, BOOMING_CITY, MEGA_CITY, GODTIER_CITY};
	
	public static PopulationType evaluate() {
		
		return EMPTY;
	}
	
	public static boolean isInBounds(long population, PopulationType min, PopulationType max) { return isInBounds(parseType(population), min, max); }
	public static boolean isInBounds(PopulationType test, PopulationType min, PopulationType max) {
		boolean bmin = (min == null), bmax = true, maxReached = false;
		for(PopulationType p : ALL) {
			//switch statement idk why not possible
			if(p == test) break;
			else if(p == min) bmin = true;
			else if(p == max) {
				if(maxReached) bmax = false;
				else maxReached = true;
			}
		}
		return (bmin && bmax);
	}
	public static boolean isInBounds(long population, PopulationType comparison, int[] bounds) {
		return isInBounds(parseType(population), downgrade(comparison, bounds[0]), upgrade(comparison, bounds[1]));
	}
	
	public static PopulationType parseType(long population) {
		for(int i = 0; i < bounds.length; i++) {
			if(population <= bounds[i]) return ALL[i];
		}
		return PopulationType.GODTIER_CITY;
	}

	public static PopulationType upgrade(PopulationType upgrade, int level) {
		for(int i = 0; i < level; i++) {
			upgrade = upgrade(upgrade);
		}
		return upgrade;
	}
	public static PopulationType upgrade(PopulationType upgrade) {
		switch (upgrade) {
			case EMPTY:
				return CAMP;
			case CAMP:
				return VILLAGE;
			case VILLAGE:
				return TOWN;
			case TOWN:
				return CITY;
			case CITY:
				return BIG_CITY;
			case BIG_CITY:
				return BOOMING_CITY;
			case BOOMING_CITY:
				return MEGA_CITY;
			case MEGA_CITY:
				return GODTIER_CITY;
			default:
				return upgrade;
		}
	}

	public static PopulationType downgrade(PopulationType downgrade, int level) {
		for(int i = 0; i < level; i++) {
			downgrade = downgrade(downgrade);
		}
		return downgrade;
	}
	public static PopulationType downgrade(PopulationType downgrade) {
		switch (downgrade) {
			case CAMP:
				return EMPTY;
			case VILLAGE:
				return CAMP;
			case TOWN:
				return VILLAGE;
			case CITY:
				return TOWN;
			case BIG_CITY:
				return CITY;
			case BOOMING_CITY:
				return BIG_CITY;
			case MEGA_CITY:
				return BOOMING_CITY;
			case GODTIER_CITY:
				return MEGA_CITY;
			default:
				return downgrade;
		}
	}

	public static int offsetFrom(PopulationType pref, PopulationType Target) {
		int res = trueOffsetFrom(pref, Target);
		if(res < 0) return res*(-1);
		return res;
	}
	public static int trueOffsetFrom(PopulationType pref, PopulationType Target) {
		int indexPref = -1, indexTarget = -1;
		for(int i = 0; i < ALL.length; i++) {
			if(ALL[i] == pref) indexPref = i;
			if(ALL[i] == Target) indexTarget = i;
			if(indexPref >= 0 && indexTarget >= 0) break;
		}
		if(indexPref > indexTarget) return indexPref-indexTarget;
		return (indexTarget-indexPref)*(-1);
	}

	/*
	* Switch OHNE break;:
	* switch (upgrade) {
			case EMPTY -> {
				return CAMP;
			}
		}
	* */
	
}
