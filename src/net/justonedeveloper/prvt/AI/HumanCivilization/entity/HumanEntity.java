package net.justonedeveloper.prvt.AI.HumanCivilization.entity;

import net.justonedeveloper.prvt.AI.HumanCivilization.Civilization;
import net.justonedeveloper.prvt.AI.HumanCivilization.GridMap;
import net.justonedeveloper.prvt.AI.HumanCivilization.World;
import net.justonedeveloper.prvt.AI.HumanCivilization.constants;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.HumanProperty;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.PopulationDensityPreference;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.PopulationPreference;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.Profession;

import java.util.*;

public class HumanEntity extends Entity {
	
	public static List<HumanEntity> allHumans = new ArrayList<HumanEntity>();
	
	public static int totalPopulation() {
		int humans = 0;
		for(HumanEntity h : allHumans) {
			humans += h.getPopulation();
		}
		return humans;
	}
	
	public HumanEntity(World w, String StartField, int StartAmount, int StartingAge) {
		world = w;
		allHumans.add(this);
		addHuman(StartField, StartAmount, StartingAge);
	}
	public HumanEntity(World w, String[] StartFields, int[] StartAmounts, int StartingAge) {
		world = w;
		for(int i = 0; i < StartFields.length; i++) {
			addHuman(StartFields[i], StartAmounts[i], StartingAge);
		}
	}
	
	public static HumanEntity newHuman(World w, String Field, int amount, HumanProperty[] props, int age) {
		//if human exists, add to bodycount
		HumanEntity h = HumanAlreadyExists(props);
		if(h != null) {
			return h.addHuman(Field, amount, age);
		} else {
			return new HumanEntity(w, Field, amount, age).setProps(props);
		}
	}
	private static HumanEntity HumanAlreadyExists(HumanProperty[] props) {
		for(HumanEntity h : allHumans) {
			if(h.allProps.equals(props)) return h;
		}
		return null;
	}
	
	public static void birthCycle() {
		System.out.println("Starting Birth Cycle");
		Thread birth = new Thread() {
			public void run() {
				List<HumanEntity> humans = allHumans.subList(0, allHumans.size());
				for(HumanEntity h : humans) {
					h.birth();
				}
				allHumans = humans;
			}
		};
		birth.run();
		while (birth.isAlive()) {}
		System.out.println("Birth Cycle Finished.");
	}
	
	
	
	//----------NON-STATIC HUMANENTITY------------
	
	
	
	public World world;
	public HumanEntity setProps(HumanProperty[] props) {
		allProps = props;
		return this;
	}
	
	private int totalBodycount = 0;
	private HashMap<String, Integer> bodycount = new HashMap<String, Integer>();
	private HashMap<Integer, Integer> age = new HashMap<Integer, Integer>();		//<Age, Number of Population that age>
	
	//Begin Properties
	
	private HumanProperty[] allProps;
	
	private PopulationPreference PrefPopulation;
	private PopulationDensityPreference PrefDensity;
	private int DensityMaxTolerance;			//In percent, ranges from 10% to 50%
	private Profession profession;
	
	//End Properties
	
	//Start get Properties
	
	public PopulationPreference getPrefPopulation() {
		return PrefPopulation;
	}
	public PopulationDensityPreference getPrefPopulationDensity() {
		return PrefDensity;
	}
	public Profession getProfession() {
		return profession;
	}
	
	public int getPopulation() {
		return totalBodycount;
	}
	
	//End get Properties
	
	public HumanEntity addHuman(String Field, int amount, int age) {
		if(World.currentWorld.getGridMap().FieldExists(Field)) {
			int current = 0;
			if(bodycount.containsKey(Field)) current = bodycount.get(Field);
			
			//
			World.currentWorld.getGridMap().alterPopulation(Field, amount);
			totalBodycount += amount;
			bodycount.put(Field, current+amount);
			
			//Update Age List
			int AgePpl = 0;
			if(this.age.containsKey(age)) AgePpl = this.age.get(age);
			AgePpl += amount;
			this.age.put(age, AgePpl);
		} else System.out.println("Human Spawn Failed, Field does not exist!");
		return this;
	}
	
	public HumanEntity die(String Field, int amount) {
		if(World.currentWorld.getGridMap().FieldExists(Field)) {
			int current = 0;
			if(bodycount.containsKey(Field)) current = bodycount.get(Field);
			//TODO Alter amount by ppl alive on the Field
			World.currentWorld.getGridMap().alterPopulation(Field, -amount);
			totalBodycount -= amount;
			bodycount.put(Field, current-amount);
			
			if(totalBodycount <= 0) {
				Civilization.deleteHumanEntity(this);
				//or: totalBodycount = 0;
			}
		}
		return this;
	}
	
	public void birth() {
		for(int cur : age.keySet()) {
			int humans = 0;
			
			if(cur >= constants.minimumHighBirthAge && cur <= constants.maximumHighBirthAge) {
				humans = constants.getBirthedPeople(age.get(cur), constants.percBirthHigh);
			} else if(cur >= constants.minimumBirthAge && cur <= constants.maximumBirthAge) {
				humans = constants.getBirthedPeople(age.get(cur), constants.percBirthLow);
			}
			
			int fieldIndex = 0;
			Object[] fields = bodycount.keySet().toArray();
			int add = 5;
			
			if(humans >= 500) add = 20;
			else if(humans >= 1000) add = 40;
			else if(humans >= 5000) add = 100;
			else if(humans >= 35000) add = 150;
			else if(humans >= 150000) add = 350;
			else if(humans >= 500000) add = 500;
			
			for (int i = 0; i < humans; i+=add) {
				newHuman(world, (String) fields[fieldIndex], add, HumanProperty.generatePropertySet(this), 0);
				if(fieldIndex == fields.length-1) fieldIndex = 0;
				else fieldIndex++;
			}
		}
	}
	
}
