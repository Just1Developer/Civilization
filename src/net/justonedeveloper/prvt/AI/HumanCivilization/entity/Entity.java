package net.justonedeveloper.prvt.AI.HumanCivilization.entity;

public class Entity {
	
	private String UUID = "";
	
	public Entity() {
		UUID = net.justonedeveloper.prvt.AI.HumanCivilization.enums.UUID.generate();
	}
	
	public String getUUID() {
		return UUID;
	}
	
}
