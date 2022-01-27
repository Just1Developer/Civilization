package net.justonedeveloper.prvt.AI.HumanCivilization;

import net.justonedeveloper.prvt.AI.HumanCivilization.entity.HumanEntity;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.PopulationType;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.HumanProperty;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.PopulationDensityPreference;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.PopulationPreference;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.Profession;

import java.util.ArrayList;

public abstract class Happiness {

	public static int calculate(String Field, HumanEntity human) { return calculate(Field, human.getAllProps());}
	public static int calculate(String Field, HumanProperty[] props) {

		Profession prof;
		PopulationPreference PrefPop;
		PopulationDensityPreference PrefPopDens;

		for(HumanProperty p : props) {
			if(p instanceof Profession) {
				prof = (Profession) p;
			} else if(p instanceof PopulationPreference) {
				PrefPop = (PopulationPreference) p;
			} else if(p instanceof PopulationDensityPreference) {
				PrefPopDens = (PopulationDensityPreference) p;
			}
		}

		long FieldPopulation = Civilization.primaryWorld.getGridMap().getFieldPopulation(Field);
		PopulationPreference FieldPopPref = PopulationPreference.fromType(PopulationType.parseType(FieldPopulation));

		/**
		 * Plan: Happiness starts at value 100
		 * Categories
		 */
		
		return 0;
	}

	public static boolean isInBounds(PopulationPreference current, String Field, int tolerance) { return isInBounds(current, World.currentWorld.getGridMap().getFieldPopulation(Field), tolerance); }
	public static boolean isInBounds(PopulationPreference current, long FieldPopulation, int tolerance) {



		return false;
	}

}
