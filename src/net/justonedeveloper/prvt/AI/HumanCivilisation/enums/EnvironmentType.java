package net.justonedeveloper.prvt.AI.HumanCivilisation.enums;

import java.util.Random;

public enum EnvironmentType {
	EMPTY, CAMP, VILLAGE, TOWN, CITY, BIG_CITY, BOOMING_CITY, MEGA_CITY, GODTIER_CITY,
	HOSTILE, FRIENDLY, NEUTRAL, ROTTEN;
	
	public static EnvironmentType getStartEnvironment() {
		int env = new Random().nextInt(4);
		switch (env) {
			case 0:
				return FRIENDLY;
			case 1:
				return HOSTILE;
			case 2:
				return ROTTEN;
			default:
				return NEUTRAL;
		}
	}
	
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
}
