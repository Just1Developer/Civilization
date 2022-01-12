package net.justonedeveloper.prvt.AI.HumanCivilization.enums;

public enum PopulationType implements FieldType {
	
	EMPTY, CAMP, VILLAGE, TOWN, CITY, BIG_CITY, BOOMING_CITY, MEGA_CITY, GODTIER_CITY;
	
	private int[] bounds = {0,0};
	
	public static PopulationType evaluate() {
		
		return EMPTY;
	}
	
}
