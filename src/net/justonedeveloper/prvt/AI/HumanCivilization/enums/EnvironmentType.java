package net.justonedeveloper.prvt.AI.HumanCivilization.enums;

import java.util.Random;

public enum EnvironmentType implements FieldType {
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
}
