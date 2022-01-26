package net.justonedeveloper.prvt.AI.HumanCivilization.enums;

import net.justonedeveloper.prvt.AI.HumanCivilization.Happiness;
import net.justonedeveloper.prvt.AI.HumanCivilization.entity.HumanEntity;

public enum Action {
	MOVE, REPRODUCE, STAY;
	
	public static void Act(String Field, HumanEntity e) {
		int happiness = Happiness.calculate(Field, e);
		long population = e.getPopulation(Field);
		//TODO Method for how near something is to the actual value in enum order
	}
}
