package net.justonedeveloper.prvt.AI.HumanCivilization.enums;

import java.util.HashMap;

public class HumanValue {

	private long totalPopulation;
	private HashMap<String, Long> fieldPopulation = new HashMap<>();

	public HumanValue(String[] fields, long[] population) {
		long c = fields.length;
		if(population.length < fields.length) c = population.length;
		totalPopulation = 0;
		for(int i = 0; i < c; i++) {
			fieldPopulation.put(fields[i], population[i]);
			totalPopulation += population[i];
		}
	}
	public HumanValue(String[] fields, long population) {
		for(String f : fields) {
			fieldPopulation.put(f, population);
			totalPopulation += population;
		}
	}

	public String[] getFields() {
		return fieldPopulation.keySet().toArray(new String[0]);
	}
	public long getFieldPopulation(String field) {
		if(fieldPopulation.containsKey(field)) return fieldPopulation.get(field);
		return 0;
	}
	public long getFieldPopulation(String[] fields) {
		long pop = 0;
		for(String field : fields) {
			pop += getFieldPopulation(field);
		}
		return pop;
	}
	public long getTotalPopulation() {
		return totalPopulation;
	}
}
