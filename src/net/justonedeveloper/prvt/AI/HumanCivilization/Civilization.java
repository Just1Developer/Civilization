package net.justonedeveloper.prvt.AI.HumanCivilization;

import net.justonedeveloper.prvt.AI.HumanCivilization.entity.HumanEntity;
import net.justonedeveloper.prvt.AI.HumanCivilization.util.Log;

public class Civilization {
	
	static World primaryWorld;
	
	public static void main(String[] args) {
		primaryWorld = new World();
		HumanEntity h = new HumanEntity("500x500", 50);
		Log.log("UUID Print", h.getUUID());
	}
	
	private boolean sim;
	
	private void simulate() {
		sim = true;
		while (sim) {
			//SIMULATION TICK
		}
	}
	private void stop() { sim = false; }
}
