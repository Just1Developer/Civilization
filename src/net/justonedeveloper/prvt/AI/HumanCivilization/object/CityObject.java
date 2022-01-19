package net.justonedeveloper.prvt.AI.HumanCivilization.object;

import net.justonedeveloper.prvt.AI.HumanCivilization.GridMap;
import net.justonedeveloper.prvt.AI.HumanCivilization.World;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.PopulationType;

import java.util.ArrayList;

public class CityObject {

	public static ArrayList<CityObject> cityObjects = new ArrayList<>();


	//------------NON-STATIC CITY OBJECT------------


	private PopulationType CityType;
	private ArrayList<String> fields = new ArrayList<>();
	private int totalPopulation = 0;
	private int[] bounds = {-1, 1};
	private String CityName = "";

	public CityObject(String startingField) {
		create(startingField, -1, 1);
	}
	public CityObject(String startingField, int lowerBound, int upperBound) {
		create(startingField, lowerBound, upperBound);
	}
	private void create(String field, int lowerBound, int upperBound) {
		fields.add(field);
		CityType = PopulationType.parseType(updatePopulation());
		updateFields();
		CityName = CityNames.genNewName();
		setBounds(lowerBound, upperBound);
	}
	
	//Start getAttributes
	
	public PopulationType getCityType() { return CityType; }
	public int[] getTypeBounds() { return bounds; }
	public String getCityName() { return CityName; }
	
	//End getAttributes

	public int[] setBounds(int lower, int upper) {
		bounds[0] = lower; bounds[1] = upper;
		return bounds;
	}

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
						
						//2 Möglichkeiten: offsetFrom oder isInBounds -> inBounds
						
						if(PopulationType.isInBounds(g.getFieldPopulation(field), CityType, bounds) && !newFields.contains(field)) {
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
