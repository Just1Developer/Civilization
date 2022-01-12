package net.justonedeveloper.prvt.AI.HumanCivilization.entity;

import net.justonedeveloper.prvt.AI.HumanCivilization.GridMap;
import net.justonedeveloper.prvt.AI.HumanCivilization.World;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.HumanProperty;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.PopulationDensityPreference;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.PopulationPreference;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.Profession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HumanEntity extends Entity {
	
	public static List<HumanEntity> allHumans = new ArrayList<HumanEntity>();
	
	public HumanEntity(World w, String StartField, int StartAmount) {
		world = w;
		addHuman(StartField, StartAmount);
	}
	public HumanEntity(World w, String[] StartFields, int[] StartAmounts) {
		world = w;
		for(int i = 0; i < StartFields.length; i++) {
			addHuman(StartFields[i], StartAmounts[i]);
		}
	}
	
	public static HumanEntity newHuman(World w, String Field, int amount, HumanProperty[] props) {
		//if human exists, add to bodycount
		HumanEntity h = HumanAlreadyExists(props);
		if(h != null) {
			return h.addHuman(Field, amount);
		} else {
			return new HumanEntity(w, Field, amount).setProps(props);
		}
	}
	
	private static HumanEntity HumanAlreadyExists(HumanProperty[] props) {
		for(HumanEntity h : allHumans) {
			if(h.allProps.equals(props)) return h;
		}
		return null;
	}
	
	public World world;
	public HumanEntity setProps(HumanProperty[] props) {
		allProps = props;
		
		return this;
	}
	
	private int totalBodycount = 0;
	private HashMap<String, Integer> bodycount = new HashMap<String, Integer>();
	
	//Begin Properties
	
	private HumanProperty[] allProps;
	
	private PopulationPreference PrefPopulation;
	private PopulationDensityPreference PrefDensity;
	private int DensityMaxTolerance;			//In percent, ranges from 10% to 50%
	private Profession profession;
	private int age = 0;
	
	//End Properties
	
	//Start get Properties
	
	public PopulationPreference getPrefPopulation() {
		return PrefPopulation;
	}
	public Profession getProfession() {
		return profession;
	}
	public int getAge() {
		return age;
	}
	
	//End get Properties
	
	public HumanEntity addHuman(String Field, int amount) {
		if(World.currentWorld.getGridMap().FieldExists(Field)) {
			int current = 0;
			if(bodycount.containsKey(Field)) current = bodycount.get(Field);
			
			World.currentWorld.getGridMap().alterPopulation(Field, amount);
			totalBodycount += amount;
			bodycount.put(Field, current+amount);
			
		}
		return this;
	}
	
	public HumanEntity die(String Field, int amount) {
		if(World.currentWorld.getGridMap().FieldExists(Field)) {
			int current = 0;
			if(bodycount.containsKey(Field)) current = bodycount.get(Field);
			
			World.currentWorld.getGridMap().alterPopulation(Field, -amount);
			totalBodycount -= amount;
			bodycount.put(Field, current-amount);
			
			if(totalBodycount <= 0) {
			
			}
		}
		return this;
	}
	
}
