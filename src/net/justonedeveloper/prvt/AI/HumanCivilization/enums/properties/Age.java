package net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties;

import java.util.HashMap;
import java.util.Map;

public enum Age {
	//Values are per grid and upper bound
	BORN(0), TEEN(15), ADULT(25), MIDDLE_AGED(40), ADVANCED_AGED(60), ELDERLY(80), ANCIENT(100), DEATH_HERSELF(125);
	
	public static Age[] ALL = {BORN, TEEN, ADULT, MIDDLE_AGED, ADVANCED_AGED, ELDERLY, ANCIENT, DEATH_HERSELF};
	
	private int value;
	private static Map map = new HashMap<>();
	
	private Age(int value) {
		this.value = value;
	}
	
	static {
		for (Age age : Age.values()) {
			map.put(age.value, age);
		}
	}
	
	public static Age valueOf(int age) {
		return (Age) map.get(age);
	}
	
	public int getValue() {
		return value;
	}
}
