package net.justonedeveloper.prvt.AI.HumanCivilization;

import net.justonedeveloper.prvt.AI.HumanCivilization.entity.HumanEntity;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.EnvironmentType;

import java.util.Collection;

public class World {
	
	public static World currentWorld;
	
	private EnvironmentType env;
	private int gridSize;		//1000 ==> 1000x1000 different property grids
	GridMap grid;
	
	public World() {
		new World(1000);
	}
	public World(int size) {
		currentWorld = this;
		gridSize = size;
		generate();
		grid = new GridMap(gridSize, env);
	}
	
	public Collection<HumanEntity> getHumans() {
		return HumanEntity.allHumans.values();
	}
	
	private void generate() {
		env = EnvironmentType.getStartEnvironment();
	}
	public GridMap getGridMap() {
		return grid;
	}

}
