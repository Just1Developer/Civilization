package net.justonedeveloper.prvt.AI.HumanCivilization.enums;

public enum HappinessValue {

	POGCHAMP, VERY_HAPPY, HAPPY, MEDIUM_HAPPY, UNHAPPY, VERY_UNHAPPY, DEPRESSED, SUICIDAL;

	public static HappinessValue valueOf(int happiness) {
		if(happiness >= 95) return POGCHAMP;
		if(happiness >= 85) return VERY_HAPPY;
		if(happiness >= 70) return HAPPY;
		if(happiness >= 55) return MEDIUM_HAPPY;
		if(happiness >= 40) return UNHAPPY;
		if(happiness >= 25) return VERY_UNHAPPY;
		if(happiness >= 10) return DEPRESSED;
		return SUICIDAL;
	}

}
