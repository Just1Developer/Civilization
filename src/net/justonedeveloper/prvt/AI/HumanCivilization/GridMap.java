package net.justonedeveloper.prvt.AI.HumanCivilization;

import net.justonedeveloper.prvt.AI.HumanCivilization.enums.EnvironmentType;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.FieldType;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.PopulationType;

import java.util.HashMap;

public class GridMap {
	
	private int size;		//SIZE IS EXCLUSIVE!
	private HashMap<String, FieldType> fields = new HashMap<String, FieldType>();
	private HashMap<String, FieldType> populationFields = new HashMap<String, FieldType>();
	private HashMap<String, Integer> populationPerField = new HashMap<String, Integer>();
	
	public int Status = 0;
	
	public GridMap(int size, EnvironmentType LandScape) {
		this.size = size;
		resetFieldsTo(fields, LandScape);
		resetFieldsTo(fields, PopulationType.EMPTY);
	}
	
	private void resetFieldsTo(HashMap<String, FieldType> fields, FieldType e) {
		fields.clear();
		Status = 1;
		new Thread(new Runnable() {
			public void run() {
				for(int x = 0; x < size; x++) {
					for(int y = 0; y < size; y++) {
						fields.put(x + "x" + y, e);
					}
				}
			}
		}).start();
		Status = 0;
	}
	
	private void updatePopulationProperty(String Field) {
		if(!Field.contains("x")) {
			Field = Field + "x" + Field;
		}
		if(populationFields.containsKey(Field)) {
			PopulationType.evaluate();
		}
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
