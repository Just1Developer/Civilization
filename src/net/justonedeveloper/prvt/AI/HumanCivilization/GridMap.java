package net.justonedeveloper.prvt.AI.HumanCivilization;

import net.justonedeveloper.prvt.AI.HumanCivilization.entity.HumanEntity;
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
	private HashMap<String, Long> populationPerField = new HashMap<>();
	
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
			PopulationType.evaluate();																					//TODO EVALUATE METHOD
		}
	}
	
	public void refreshPopulation(String[] Fields) {
		for(String f : Fields) {
			refreshPopulation(f);
		}
	}
	public void refreshPopulation(ArrayList<String> Fields) {
		for(String f : Fields) {
			refreshPopulation(f);
		}
	}
	public void refreshPopulation(String Field) {
		if(!Field.contains("x")) {
			Field = Field + "x" + Field;
		}
		if(FieldExists(Field)) {
			long pop = 0;
			for(HumanEntity h : HumanEntity.allHumans) {
				pop += h.getPopulation(Field);
			}
			populationPerField.put(Field, pop);
		}
	}
	
	public void alterPopulation(String Field, long amount) {
		if(populationPerField.containsKey(Field)) {
			amount += populationPerField.get(Field);
		} else if(FieldExists(Field)) {
			if(amount < 0) return;
		} else return;
		populationPerField.put(Field, amount);
		if(populationPerField.get(Field) < 0) populationPerField.put(Field, 0L);
	}
	
	public String getNearestField(String Field, PopulationType populationMin, PopulationType populationMax) {
		
		int x_offset = 1, y_offset = 0, coordX = Integer.parseInt(Field.split("x")[0]), coordY = Integer.parseInt(Field.split("x")[1]), count = 0, max = size*size;
		
		while(count < max) {
			
			for(int iX = -1; iX < 2; iX++) {
				for(int iY = -1; iY < 2; iY++) {
					final String field = (coordX + (x_offset * iX)) + "x" + (coordY + (y_offset * iY));
					if (FieldExists(field)) {
						if(!populationPerField.containsKey(field)) {
							if(PopulationType.isInBounds(0, populationMin, populationMax)) return field;
						} else if(PopulationType.isInBounds(populationPerField.get(field), populationMin, populationMax)) return field;
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
	public String[] getAdjacentFields(String[] Fields) { return getAdjacentFields((ArrayList<String>) Arrays.asList(Fields)); };
	public String[] getAdjacentFields(ArrayList<String> Fields) {
		ArrayList<String> f = new ArrayList<>(), fClone = (ArrayList<String>) Fields.clone();
		for(String field : Fields) {
			for(String ad : getAdjacentFields(field)) {
				if(!fClone.contains(ad) && !f.contains(ad)) f.add(ad);
			}
		}
		return ConvertToStringArray(f);
	}
	public String[] getAdjacentFields(String[] Fields, int range) {
		ArrayList<String> f = new ArrayList<>();
		for(int i = 0; i < range; i++) {
			String[] n = getAdjacentFields(f);
			for(String n1 : n) {
				if(!f.contains(n1)) f.add(n1);
			}
		}
		return ConvertToStringArray(f);
	}
	
	//Get a cluster of Fields that have similar population
	public String[] getFieldCluster(String Field, int tolerance) { return getFieldCluster(Field, tolerance, null); }
	public String[] getFieldCluster(String Field, int tolerance, ArrayList<String> exclude) {
		
		ArrayList<String> fields = new ArrayList<>();
		GridMap g = World.currentWorld.getGridMap();
		
		if(Field == null) return ConvertToStringArray(fields);
		if(g == null) return ConvertToStringArray(fields);
		if(exclude == null) exclude = new ArrayList<>();
		
		//variables
		ArrayList<String> newFields = new ArrayList<>();
		newFields.add(Field);
		PopulationType CityType = PopulationType.parseType(getFieldPopulation(Field));
		
		//Integers
		int x_offset = 1, y_offset = 0, coordX = Integer.parseInt(Field.split("x")[0]), coordY = Integer.parseInt(Field.split("x")[1]), count = 0, max = g.getSize()*g.getSize();
		int fails = 0;		//Formula for Radius: pi*r² -> 3.5*x_offset*x_offset
		
		while(count < max) {
			
			for(int iX = -1; iX < 2; iX++) {
				for(int iY = -1; iY < 2; iY++) {
					final String field = (coordX + (x_offset * iX)) + "x" + (coordY + (y_offset * iY));
					if (g.FieldExists(field)) {
						
						if(PopulationType.isInBounds(g.getFieldPopulation(field), CityType, new int[] {-tolerance, tolerance}) && !newFields.contains(field) &&
								!exclude.contains(PopulationType.parseType(g.getFieldPopulation(field)))) {
							
							newFields.add(field);
							fails = 0;
							
						} else fails++;
					}
				}
			}
			if(y_offset < x_offset) y_offset++;
			else x_offset++;
			
			if(3.5*x_offset*x_offset <= fails) break;
			
			count++;
			
		}
		
		return ConvertToStringArray(newFields);
	}
	
	public static String[] ConvertToStringArray(ArrayList<String> s) {
		String[] n = new String[s.size()];
		for(int i = 0; i < s.size(); i++) {
			n[i] = s.get(i);
		}
		return n;
	}

	public long getFieldPopulation(String Field) {
		if(!FieldExists(Field)) return 0;
		if(!populationPerField.containsKey(Field)) {
			populationPerField.put(Field, 0L);
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
