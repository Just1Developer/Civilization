package net.justonedeveloper.prvt.AI.HumanCivilization.CityObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class CityNames {
	
	private static Random r = new Random();
	private static ArrayList<String> taken = new ArrayList<>(), all = new ArrayList<>();
	
	public static void ini() {
		all.addAll(List.of("York", "Tokyo", "Berlin", "Manhattan", "Freiburg", "Magdeburg", "Hannover", "San Francisco", "Detroit", "Birmingham", "Birkshire",
				"Plymth", "Gallifrey", "Nexus", "Nymphia", "", "", "", "", "", "", "", "", "", ""));
	}
	
	public static ArrayList<String> getTakenNames() {
		return taken;
	}
	
	public static String genNewName() {
		
		if(all.isEmpty()) ini();
		
		int i = 0, max = 3;
		String name = "", newS = "New ";
		String[] names = {"", "", ""};
		
		while (name.equals("") && i < max) {
			names[i] = all.get(r.nextInt(all.size()));
			//Will be done today
			i++;
		}
		
		return name;
	}

}
