package net.justonedeveloper.prvt.AI.HumanCivilization.CityObject;

import net.justonedeveloper.prvt.AI.HumanCivilization.Civilization;
import net.justonedeveloper.prvt.AI.HumanCivilization.GridMap;
import net.justonedeveloper.prvt.AI.HumanCivilization.World;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.PopulationType;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CityObject {

	public static ArrayList<CityObject> camps = new ArrayList<>();


	//------------NON-STATIC CITY OBJECT------------

	private PopulationType CityType;
	private ArrayList<String> fields = new ArrayList<>();
	private int totalPopulation = 0;
	private String CityName = "";

	public CityObject(String startingField) {
		fields.add(startingField);
		CityType = PopulationType.parseType(updatePopulation());
		updateFields();
		CityName = CityNames.genNewName();
	}
	
	//Start getAttributes
	
	public PopulationType getCityType() { return CityType; }
	public String getCityName() { return CityName; }
	
	//End getAttributes
	
	public int updatePopulation() {
		totalPopulation = 0;
		for(String f : fields) {
			totalPopulation += World.currentWorld.getGridMap().getFieldPopulation(f);
		}
		return totalPopulation;
	}
	
	public ArrayList<String> updateFields() {
		
		String Field = fields.get(0);
		GridMap g = World.currentWorld.getGridMap();
		
		if(Field == null) return fields;
		if(g == null) return fields;
		
		//new Field list
		ArrayList<String> newFields = new ArrayList<>();
		newFields.add(Field);
		
		//Integers
		int x_offset = 1, y_offset = 0, coordX = Integer.parseInt(Field.split("x")[0]), coordY = Integer.parseInt(Field.split("x")[1]), count = 0, max = g.getSize()*g.getSize();
		int fails = 0;		//Formula for Radius: pi*r² -> 3.5*x_offset*x_offset
		
		while(count < max) {
			
			for(int iX = -1; iX < 2; iX++) {
				for(int iY = -1; iY < 2; iY++) {
					final String field = (coordX + (x_offset * iX)) + "x" + (coordY + (y_offset * iY));
					if (g.FieldExists(field)) {
						
						
						
						if(PopulationType.parseType(g.getFieldPopulation(field)) == CityType && !newFields.contains(field)) {
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
		
		fields = newFields;
		return newFields;
	}
}
