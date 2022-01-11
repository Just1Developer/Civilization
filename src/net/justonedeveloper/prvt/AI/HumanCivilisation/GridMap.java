package net.justonedeveloper.prvt.AI.HumanCivilisation;

import net.justonedeveloper.prvt.AI.HumanCivilisation.enums.EnvironmentType;

import java.util.HashMap;

public class GridMap {
	
	private int size;		//SIZE IS EXCLUSIVE!
	private EnvironmentType startingEnvironment;
	private HashMap<String, EnvironmentType> fields = new HashMap<String, EnvironmentType>();
	
	public int Status = 0;
	
	public GridMap(int size, EnvironmentType starter) {
		startingEnvironment = starter;
		this.size = size;
		resetTo(startingEnvironment);
	}
	
	private void resetTo(EnvironmentType e) {
		fields.clear();
		Status = 1;
		new Thread(new Runnable() {
			public void run() {
				for(int x = 0; x < size; x++) {
					for(int y = 0; y < size; y++) {
						fields.put(x + "x" + y, e);
					}
				}
			}
		}).start();
		Status = 0;
	}
	
	public EnvironmentType getField(int x, int y) {
		if(x < size && x >= 0 && y < size && y >= 0) {
			String coords = x + "x" + y;
			if(fields.containsKey(coords)) return fields.get(coords);
		}
		return null;
	}

}
