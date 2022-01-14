package net.justonedeveloper.prvt.AI.HumanCivilization;

import net.justonedeveloper.prvt.AI.HumanCivilization.entity.HumanEntity;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.HumanProperty;
import net.justonedeveloper.prvt.AI.HumanCivilization.util.Log;

import java.io.IOException;
import java.util.HashMap;

public class Civilization {
	
	static World primaryWorld;
	
	//TODO: humans can build cities if architecht, humans split onto more fields if they can (camps or too many), Preferred Population Density
	
	public static void main1(String[] args) {
		primaryWorld = World.newWorld();
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("World: " + primaryWorld + "; Size: " + primaryWorld.gridSize + "; GridMap: " + primaryWorld.grid);
		while (primaryWorld.getGridMap().Status != 0) {} //Wait for Grid Generation
		
		HumanEntity h = HumanEntity.newHuman(primaryWorld, "500x500", 50, HumanProperty.generatePropertySet(null), 25);
		Log.log("UUID Print", h.getUUID());
		
		while (true) {
			System.out.println("Current Population: " + HumanEntity.totalPopulation());
			HumanEntity.birthCycle();
			try {
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) {
		System.out.println("Test main method active, nothing is gonna happen here.");
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
		del = null;
	}
}
