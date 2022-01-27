package net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties;

public enum Profession implements HumanProperty {
	
	ARCHITECT, FARMER, TEACHER;
	
	//Builders can Build
	public static Profession[] ALL = {ARCHITECT, FARMER, TEACHER};

	public static PopulationPreference getMinComfortablePopulation(Profession prof) {
		switch (prof) {
			case ARCHITECT:
				return PopulationPreference.MEDIUM;
			default:	//FARMER
				return PopulationPreference.LOW;
		}
	}
	public static PopulationPreference getMaxComfortablePopulation(Profession prof) {
		switch (prof) {
			case ARCHITECT:
				return PopulationPreference.EXTREME;
			case TEACHER:
				return PopulationPreference.HIGH;
			default:	//FARMER
				return PopulationPreference.MEDIUM;
		}
	}
}
