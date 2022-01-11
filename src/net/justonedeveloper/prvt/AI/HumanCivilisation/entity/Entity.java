package net.justonedeveloper.prvt.AI.HumanCivilisation.entity;

import net.justonedeveloper.prvt.AI.HumanCivilisation.enums.UUID;

public class Entity {
	
	private String UUID = "";
	
	public Entity() {
		UUID = net.justonedeveloper.prvt.AI.HumanCivilisation.enums.UUID.generate();
	}
	
	public String getUUID() {
		return UUID;
	}
	
}
