package net.justonedeveloper.prvt.AI.HumanCivilization.CityObject;

import net.justonedeveloper.prvt.AI.HumanCivilization.Civilization;
import net.justonedeveloper.prvt.AI.HumanCivilization.World;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.PopulationType;

import java.util.ArrayList;

public class CityObject {

	public static ArrayList<CityObject> camps = new ArrayList<CityObject>();


	//------------NON-STATIC CITY OBJECT------------

	private PopulationType CityType;
	private ArrayList<String> fields = new ArrayList<String>();
	private int totalPopulation = 0;

	public CityObject(String startingField) {
		fields.add(startingField);
		CityType = PopulationType.parseType(updatePopulation());
	}

	public PopulationType getCityType() { return CityType; }

	public int updatePopulation() {
		totalPopulation = 0;
		for(String f : fields) {
			totalPopulation += World.currentWorld.getGridMap().getFieldPopulation(f);
		}
		return totalPopulation;
	}

	public void upgrade() {

	}
}
