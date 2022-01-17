package net.justonedeveloper.prvt.AI.HumanCivilization.CityObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class CityNames {
	
	private static Random r = new Random();
	private static ArrayList<String> taken = new ArrayList<>(), all = new ArrayList<>();
	
	public static void ini() {
		all.addAll(List.of("York", "Tokyo", "Berlin", "Manhattan", "Freiburg", "Magdeburg", "Hannover", "San Francisco", "Detroit", "Birmingham", "Birkshire",
				"Plymth", "Gallifrey", "Nexus", "Nymphia", "London", "Makao", "Syntia", "Überlangen", "Neureut", "Wuppertal", "Siegen", "Amsterdam", "Sanku", "Rippea"));
	}
	
	public static ArrayList<String> getTakenNames() {
		return taken;
	}
	
	public static String genNewName() {
		
		if(all.isEmpty()) ini();
		
		int max = 5;
		String name = "", newS = "New ";
		ArrayList<String> names = new ArrayList<>();
		for(int i = 0; i < max; i++) {
			names.add(" ");
		}
		
		for(int i = 0; i < max; i++) {
			name = all.get(r.nextInt(all.size()));
			
			//Check if Name is taken
			
			while(taken.contains(name)) {
				name = newS + name;
			}
			names.set(i, name);
			
			//Check if name was taken
			if(!name.startsWith(newS)) break;
		}
		
		//Now that we have at least 1 name, we get the shortest one
		for(String n : names) {
			if(n.equals(name)) continue;
			if(n.split(" ").length < name.split(" ").length && !name.equals(" ")) name = n;
		}
		
		//Return new Name
		taken.add(name);
		return name;
	}

}
