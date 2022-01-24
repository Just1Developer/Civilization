package net.justonedeveloper.prvt.AI.HumanCivilization;

import net.justonedeveloper.prvt.AI.HumanCivilization.enums.EnvironmentType;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.FieldType;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.PopulationType;
import net.justonedeveloper.prvt.AI.HumanCivilization.object.CityObject;

import java.util.ArrayList;
import java.util.Arrays;
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
	
	public int getSize() { return size; }
	
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
	
	//Get Adjacent Fields
	
	public String[] getAdjacentFields(String Field) {
		if(!FieldExists(Field)) return new String[0];
		
		ArrayList<String> f = new ArrayList<>();
		for(int x = -1; x <= 1; x+=2) {
			for(int y = -1; y <= 1; y+=2) {
				if(FieldExists(x + "x" + y)) f.add(x + "x" + y);
			}
		}
		return ConvertToStringArray(f);
	}
	public String[] getAdjacentFields(String[] Fields) {
		ArrayList<String> f = new ArrayList<>(), fClone = (ArrayList<String>) Arrays.asList(Fields);
		for(String field : Fields) {
			for(String ad : getAdjacentFields(field)) {
				if(!fClone.contains(ad) && !f.contains(ad)) f.add(ad);
			}
		}
		return ConvertToStringArray(f);
	}
	
	public static String[] ConvertToStringArray(ArrayList<String> s) {
		String[] n = new String[s.size()];
		for(int i = 0; i < s.size(); i++) {
			n[i] = s.get(i);
		}
		return n;
	}

	public int getFieldPopulation(String Field) {
		if(!FieldExists(Field)) return 0;
		if(!populationPerField.containsKey(Field)) {
			populationPerField.put(Field, 0);
			return 0;
		} else return populationPerField.get(Field);
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
