package net.justonedeveloper.prvt.AI.HumanCivilization.object;

import net.justonedeveloper.prvt.AI.HumanCivilization.GridMap;
import net.justonedeveloper.prvt.AI.HumanCivilization.World;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.PopulationType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CityObject {
	
	public static HashMap<String, String> FieldBelonging = new HashMap<>();
	public static ArrayList<CityObject> cityObjects = new ArrayList<>();
	
	public static ArrayList<String> diff(ArrayList<String> sample, ArrayList<String> control) {	//returns everything thats in the first arraylist but not the second
		ArrayList<String> ret = new ArrayList<>();
		for(String s : sample) {
			if(!control.contains(s)) ret.add(s);
		}
		return ret;
	}

	public static void updateCityObjects() {
		ArrayList<CityObject> newObj = new ArrayList<>();
		for(CityObject o : cityObjects) {
			if(o.updateFields().size() > 0) newObj.add(o);
		}
		cityObjects = newObj;
	}
	
	public static CityObject getCityObjectFromField(String Field) {
		for(CityObject o : cityObjects) {
			if(o.updateFields().contains(Field)) return o;
		}
		return null;
	}
	
	public CityObject getClosestCityFromField(String Field, PopulationType CityType, int tolerance) {
		
		CityObject thisObj = getCityObjectFromField(Field);
		
		GridMap g = World.currentWorld.getGridMap();
		
		if(g == null) return null;
		
		//variables
		ArrayList<String> checkedFields = new ArrayList<>();
		checkedFields.add(Field);
		
		//Integers
		int x_offset = 1, y_offset = 0, coordX = Integer.parseInt(Field.split("x")[0]), coordY = Integer.parseInt(Field.split("x")[1]), count = 0, max = g.getSize()*g.getSize();
		int fails = 0;		//Formula for Radius: pi*r² -> 3.5*x_offset*x_offset
		
		while(count < max) {
			
			for(int iX = -1; iX < 2; iX++) {
				for(int iY = -1; iY < 2; iY++) {
					final String field = (coordX + (x_offset * iX)) + "x" + (coordY + (y_offset * iY));
					if (g.FieldExists(field)) {
						
						if(PopulationType.isInBounds(g.getFieldPopulation(field), CityType, new int[] {-tolerance, tolerance}) && !checkedFields.contains(field) &&
								!exclude.contains(PopulationType.parseType(g.getFieldPopulation(field)))) {
							
							if(!getCityObjectFromField(field).equals(thisObj)) return getCityObjectFromField(field);
							
						}
						checkedFields.addAll(getCityObjectFromField(field).updateFields());
					} else fails++;
				}
			}
			if(y_offset < x_offset) y_offset++;
			else x_offset++;
			
			if(3.5*x_offset*x_offset <= fails) break;
			
			count++;
			
		}
		return null;
	}
	
	/*public static CityObject[] sortedBySize() {
		CityObject[] obj = new CityObject[1];
		for(CityObject o : cityObjects) {
			o.updateFields();
			
			if(obj.length == 1 && obj[0] == null) {
				obj[0] = o;
				continue;
			}
			
			for(CityObject o2 : obj) {
			
			}
		}
	}*/
	
	
	//------------NON-STATIC CITY OBJECT------------


	private String UUID;
	private PopulationType CityType;
	private ArrayList<String> fields = new ArrayList<>();
	private int totalPopulation = 0;
	private int[] bounds = new int[2];
	private String CityName = "";

	public CityObject(String startingField) {
		create(startingField, -1, 1);
	}
	public CityObject(String startingField, int lowerBound, int upperBound) {
		create(startingField, lowerBound, upperBound);
	}
	private void create(String field, int lowerBound, int upperBound) {
		UUID = net.justonedeveloper.prvt.AI.HumanCivilization.util.UUID.generate();
		fields.add(field);
		CityType = PopulationType.parseType(updatePopulation());
		updateFields();
		CityName = CityNames.genNewName();
		setBounds(lowerBound, upperBound);
		setExclude(true, true);
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
	
	//Exclude Stuff
	
	private ArrayList<PopulationType> exclude = new ArrayList<>();
	
	public ArrayList<PopulationType> setExclude(boolean excludeEmpty, boolean overrideCurrent) {
		if(excludeEmpty) setExclude(null, (ArrayList<PopulationType>) Arrays.asList(PopulationType.EMPTY), overrideCurrent);
		else setExclude((ArrayList<PopulationType>) Arrays.asList(PopulationType.EMPTY), null, overrideCurrent);
		return exclude;
	}
	public ArrayList<PopulationType> setExclude(PopulationType add, PopulationType remove, boolean overrideCurrent) {
		setExclude((ArrayList<PopulationType>) Arrays.asList(add), (ArrayList<PopulationType>) Arrays.asList(remove), overrideCurrent);
		return exclude;
	}
	public ArrayList<PopulationType> setExclude(PopulationType[] add, PopulationType[] remove, boolean overrideCurrent) {
		setExclude((ArrayList<PopulationType>) Arrays.asList(add), (ArrayList<PopulationType>) Arrays.asList(remove), overrideCurrent);
		return exclude;
	}
	public ArrayList<PopulationType> setExclude(ArrayList<PopulationType> add, ArrayList<PopulationType> remove, boolean overrideCurrent) {
		if(overrideCurrent) exclude.clear();
		if(add != null) {
			for(PopulationType p : add) {
				if(!exclude.contains(p)) exclude.add(p);
			}
		}
		if(remove != null) {
			for(PopulationType p : remove) {
				if(exclude.contains(p)) exclude.remove(p);
			}
		}
		return exclude;
	}
	
	
	
	public ArrayList<String> updateFields() {
		
		String Field = fields.get(0);
		GridMap g = World.currentWorld.getGridMap();
		
		ArrayList<String> fields_old = (ArrayList<String>) fields.clone();
		
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
						
						if(PopulationType.isInBounds(g.getFieldPopulation(field), CityType, bounds) && !newFields.contains(field) &&
								!exclude.contains(PopulationType.parseType(g.getFieldPopulation(field)))) {
							
							//Check UUID
							if(FieldBelonging.containsKey(field) && !FieldBelonging.get(field).equals(UUID)) fails++;
							else {
								newFields.add(field);
								FieldBelonging.put(field, UUID);
								fails = 0;
							}
							
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
		
		freeFields(diff(fields_old, fields));
		
		return newFields;
	}
	
	private void freeFields(ArrayList<String> fields) {
		for(String f : fields) {
			if(this.fields.contains(f)) {
				this.fields.remove(f);
				FieldBelonging.remove(f);
			}
		}
	}
}
