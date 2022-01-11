package net.justonedeveloper.prvt.AI.HumanCivilisation;

import net.justonedeveloper.prvt.AI.HumanCivilisation.entity.HumanEntity;
import net.justonedeveloper.prvt.AI.HumanCivilisation.util.Log;

public class Civilisation {
	
	static World primaryWorld;
	
	public static void main(String[] args) {
		HumanEntity h = new HumanEntity();
		primaryWorld = new World();
		Log.log("UUID Print", h.getUUID());
	}
}
