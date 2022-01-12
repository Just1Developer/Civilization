package net.justonedeveloper.prvt.AI.HumanCivilization.entity;

public class Entity {
	
	private final String UUID = net.justonedeveloper.prvt.AI.HumanCivilization.enums.UUID.generate();
	
	public Entity() {
		
	}
	
	public String getUUID() {
		return UUID;
	}
	
}
