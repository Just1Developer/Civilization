package net.justonedeveloper.prvt.AI.HumanCivilization;

import net.justonedeveloper.prvt.AI.HumanCivilization.entity.HumanEntity;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.EnvironmentType;

import java.util.Collection;
import java.util.List;

public class World {
	
	public static World currentWorld;
	
	private EnvironmentType env;
	public int gridSize;		//1000 ==> 1000x1000 different property grids
	public GridMap grid;
	
	public static World newWorld() {
		return new World(1000);
	}
	public World(int size) {
		currentWorld = this;
		gridSize = size;
		generate();
		grid = new GridMap(gridSize, env);
		System.out.println("GridMap set.");
	}
	
	public List<HumanEntity> getHumans() {
		return HumanEntity.allHumans;
	}
	
	private void generate() {
		env = EnvironmentType.getStartEnvironment();
	}
	public GridMap getGridMap() {
		return grid;
	}

}
