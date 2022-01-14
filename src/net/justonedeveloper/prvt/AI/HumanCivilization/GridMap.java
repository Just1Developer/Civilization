package net.justonedeveloper.prvt.AI.HumanCivilization;

import net.justonedeveloper.prvt.AI.HumanCivilization.enums.EnvironmentType;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.FieldType;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.PopulationType;

import java.util.HashMap;

public class GridMap {
	
	private int size;		//SIZE IS EXCLUSIVE!
	private HashMap<String, FieldType> fields = new HashMap<>();
	private HashMap<String, FieldType> populationFields = new HashMap<>();
	private HashMap<String, Integer> populationPerField = new HashMap<>();
	
	public int Status = 0;
	
	public GridMap(int size, EnvironmentType LandScape) {
		System.out.println("Generating GridMap...");
		this.size = size;
		Status = 1;
		resetFieldsTo(fields, LandScape);
		resetFieldsTo(populationFields, PopulationType.EMPTY);
		Status = 0;
		System.out.println("GridMap generation complete.");
	}
	
	private void resetFieldsTo(HashMap<String, FieldType> fields, FieldType e) {
		fields.clear();
		System.out.println("Resetting Fields...");
		if(Status == 0) Status = 2;
		new Thread(new Runnable() {
			public void run() {
				for(int x = 0; x < size; x++) {
					for(int y = 0; y < size; y++) {
						fields.put(x + "x" + y, e);
					}
				}
			}
		}).start();
		if(Status == 2) Status = 0;
	}
	
	private void updatePopulationProperty(String Field) {
		if(!Field.contains("x")) {
			Field = Field + "x" + Field;
		}
		if(populationFields.containsKey(Field)) {
			PopulationType.evaluate();
		}
	}
	
	public void alterPopulation(String Field, int amount) {
		if(populationPerField.containsKey(Field)) {
			amount += populationPerField.get(Field);
		} else if(FieldExists(Field)) {
			if(amount < 0) return;
		} else return;
		populationPerField.put(Field, amount);
		if(populationPerField.get(Field) < 0) populationPerField.put(Field, 0);
	}
	
	public String getNearestField(String Field, PopulationType populationMin, PopulationType populationMax) {
		
		int x_offset = 1, y_offset = 0, coordX = Integer.parseInt(Field.split("x")[0]), coordY = Integer.parseInt(Field.split("x")[1]), count = 0, max = size*size;
		
		while(count < max) {
			
			for(int iX = -1; iX < 2; iX++) {
				for(int iY = -1; iY < 2; iY++) {
					final String field = (coordX + (x_offset * iX)) + "x" + (coordY + (y_offset * iY));
					if (FieldExists(field)) {
						if(PopulationType.isInBounds(populationPerField.get(field), populationMin, populationMax)) return field;
					}
				}
			}
			if(y_offset < x_offset) y_offset++;
			else x_offset++;
			count++;
			
		}
		return Field;
	}
	
	public boolean FieldExists(String Field) {
		return (populationFields.containsKey(Field) || fields.containsKey(Field));
	}
	
	public EnvironmentType getField(int x, int y) {
		if(x < size && x >= 0 && y < size && y >= 0) {
			String coords = x + "x" + y;
			if(fields.containsKey(coords)) return (EnvironmentType) fields.get(coords);
		}
		return null;
	}

}
