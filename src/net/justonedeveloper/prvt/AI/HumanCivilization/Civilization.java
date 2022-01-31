package net.justonedeveloper.prvt.AI.HumanCivilization;

import net.justonedeveloper.prvt.AI.HumanCivilization.object.CityNames;
import net.justonedeveloper.prvt.AI.HumanCivilization.entity.HumanEntity;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.HumanProperty;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.PopulationDensityPreference;
import net.justonedeveloper.prvt.AI.HumanCivilization.util.Log;

import java.io.IOException;

public class Civilization {
	
	static World primaryWorld;

	/*
	* TODO: humans can build cities if architecht, humans split onto more fields if they can (camps or too many), Preferred Population Density
	*/

	public static void main(String[] args) {
		
		//Initialization
		CityNames.ini();
		
		primaryWorld = World.newWorld();
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("World: " + primaryWorld + "; Size: " + primaryWorld.gridSize + "; GridMap: " + primaryWorld.grid);
		while (primaryWorld.getGridMap().Status != 0) {} //Wait for Grid Generation
		
		HumanEntity h = HumanEntity.newHuman(primaryWorld, "500x500", 5000, HumanProperty.generatePropertySet(null), 25);
		Log.log("UUID Print", h.getUUID());
		
		//primaryWorld.getGridMap().getAdjacentFields("500x500");		//To test if it works
		
		int generation = 1;
		
		while (true) {
			HumanEntity.birthCycle();
			HumanEntity.advanceAge();
			HumanEntity.spreadHumans();
			System.out.println("Generation: " + generation + "; Current Population: " + HumanEntity.totalPopulation() + "; Total HumanEntites: " + HumanEntity.allHumans.size());
			generation++;
			try {
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main1(String[] args) {
		
		//Initialization
		CityNames.ini();
		
		System.out.println("Test main method active, nothing is gonna happen here.");
		System.out.println("PopulationDesPref of 4128: " + PopulationDensityPreference.parsePref(4128));
	}
	
	private boolean sim;
	
	private void simulate() {
		sim = true;
		while (sim) {
			//SIMULATION TICK
		}
	}
	private void stop() { sim = false; }
	
	public static void deleteHumanEntity(HumanEntity del) {
		if(HumanEntity.allHumans.contains(del)) HumanEntity.allHumans.remove(del);
	}
}
