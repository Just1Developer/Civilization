package net.justonedeveloper.prvt.AI.HumanCivilisation;

import net.justonedeveloper.prvt.AI.HumanCivilisation.enums.EnvironmentType;

public class World {
	
	private EnvironmentType env;
	private int gridSize;		//1000 ==> 1000x1000 different property grids
	GridMap grid;
	
	public World() {
		new World(1000);
	}
	public World(int size) {
		gridSize = size;
		generate();
		grid = new GridMap(gridSize, env);
	}
	
	private void generate() {
		env = EnvironmentType.getStartEnvironment();
	}

}
