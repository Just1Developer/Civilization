package net.justonedeveloper.prvt.AI.HumanCivilization;

import net.justonedeveloper.prvt.AI.HumanCivilization.entity.HumanEntity;
import net.justonedeveloper.prvt.AI.HumanCivilization.util.Log;

public class Civilization {
	
	static World primaryWorld;
	
	//TODO: humans can build cities if architecht, humans split onto more fields if they can (camps or too many), Preferred Population Density
	
	public static void main(String[] args) {
		primaryWorld = new World();
		HumanEntity h = new HumanEntity(primaryWorld, "500x500", 50);
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
	
	public static void deleteHumanEntity(HumanEntity del) {
		if(HumanEntity.allHumans.contains(del)) HumanEntity.allHumans.remove(del);
		del = null;
	}
}
