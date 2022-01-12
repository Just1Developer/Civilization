package net.justonedeveloper.prvt.AI.HumanCivilization.entity;

import net.justonedeveloper.prvt.AI.HumanCivilization.GridMap;
import net.justonedeveloper.prvt.AI.HumanCivilization.World;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.HumanProperty;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.PopulationPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HumanEntity extends Entity {
	
	public static Map<HumanProperty[], HumanEntity> allHumans = new HashMap<HumanProperty[], HumanEntity>();
	
	public HumanEntity(String StartField, int StartAmount) {
		addHuman(StartField, StartAmount);
	}
	public HumanEntity(String[] StartFields, int[] StartAmounts) {
		for(int i = 0; i < StartFields.length; i++) {
			addHuman(StartFields[i], StartAmounts[i]);
		}
	}
	
	public static HumanEntity newHuman(String Field, int amount, HumanProperty[] props) {
		//if human exists, add to bodycount
		
		if(allHumans.containsKey(props)) {
			return allHumans.get(props).addHuman(Field, amount);
		} else {
			return new HumanEntity(Field, amount).setProps(props);
		}
	}
	
	public HumanEntity setProps(HumanProperty[] props) {
		return this;
	}
	
	private int totalBodycount = 0;
	private HashMap<String, Integer> bodycount = new HashMap<String, Integer>();
	
	//Begin Properties
	
	private PopulationPreference PrefPopulation;
	
	//End Properties
	
	public HumanEntity addHuman(String Field, int amount) {
		if(World.currentWorld.getGridMap().FieldExists(Field)) {
			int current = 0;
			if(bodycount.containsKey(Field)) current = bodycount.get(Field);
			
			totalBodycount += amount;
			bodycount.put(Field, current+amount);
		}
		return this;
	}
	
}
