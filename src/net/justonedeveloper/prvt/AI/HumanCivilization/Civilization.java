package net.justonedeveloper.prvt.AI.HumanCivilization;

import net.justonedeveloper.prvt.AI.HumanCivilization.entity.HumanEntity;
import net.justonedeveloper.prvt.AI.HumanCivilization.util.Log;

public class Civilization {
	
	static World primaryWorld;
	
	public static void main(String[] args) {
		HumanEntity h = new HumanEntity();
		primaryWorld = new World();
		Log.log("UUID Print", h.getUUID());
	}
}
