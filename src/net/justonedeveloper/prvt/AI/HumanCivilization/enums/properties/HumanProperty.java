package net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties;

import net.justonedeveloper.prvt.AI.HumanCivilization.entity.Entity;
import net.justonedeveloper.prvt.AI.HumanCivilization.util.Log;
import net.justonedeveloper.prvt.AI.HumanCivilization.util.constants;
import net.justonedeveloper.prvt.AI.HumanCivilization.entity.HumanEntity;

import java.util.ArrayList;
import java.util.Random;

public interface HumanProperty {
	
	static Random r = new Random();
	
	static HumanProperty getRandomHumanProperty() {
		HumanProperty prop = null;
		
		return prop;
	}
	
	static PopulationDensityPreference getRandomPopulationDensityPreference() {
		return PopulationDensityPreference.ALL[r.nextInt(PopulationDensityPreference.ALL.length)];
	}
	static PopulationPreference getRandomPopulationPreference() {
		return PopulationPreference.ALL[r.nextInt(PopulationPreference.ALL.length)];
	}
	static Profession getRandomProfession() {
		return Profession.ALL[r.nextInt(Profession.ALL.length)];
	}
	
	static HumanProperty[] generatePropertySet(HumanEntity parent) {
		HumanProperty[] props = new HumanProperty[3];
		
		//Profession
		if(probability(constants.percInherit) && parent != null
				&& parent.getProfession() != null) props[0] = parent.getProfession();
		else props[0] = getRandomProfession();

		//Population Preference
		if(probability(constants.percInherit) && parent != null
				&& parent.getPrefPopulation() != null) props[1] = parent.getPrefPopulation();
		else props[1] = getRandomPopulationPreference();

		//Population Density Preference
		if(probability(constants.percInherit) && parent != null
				&& parent.getPrefPopulationDensity() != null) props[2] = parent.getPrefPopulationDensity();
		else props[2] = getRandomPopulationDensityPreference();

		//return properties
		return props;
	}
	
	static boolean probability(float d) {
		d *= 10;
		if(Math.round(d) < r.nextInt(1000)) return true;
		return false;
	}

}
