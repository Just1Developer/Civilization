package net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties;

import net.justonedeveloper.prvt.AI.HumanCivilization.constants;
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
		ArrayList<HumanProperty> list = new ArrayList<HumanProperty>();
		
		//Profession
		if(probability(constants.percInherit) && parent != null) list.add(parent.getProfession());
		else list.add(getRandomProfession());
		//Population Preference
		if(probability(constants.percInherit) && parent != null) list.add(parent.getPrefPopulation());
		else list.add(getRandomPopulationPreference());
		//Population Density Preference
		if(probability(constants.percInherit) && parent != null) list.add(parent.getPrefPopulationDensity());
		else list.add(getRandomPopulationDensityPreference());
		
		HumanProperty[] props = new HumanProperty[list.size()];
		for(int i = 0; i < list.size(); i++) {
			props[i] = list.get(i);
		}
		return props;
	}
	
	static boolean probability(float d) {
		d *= 10;
		if(Math.round(d) < r.nextInt(1000)) return true;
		return false;
	}

}
