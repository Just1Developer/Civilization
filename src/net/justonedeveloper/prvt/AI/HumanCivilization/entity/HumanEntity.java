package net.justonedeveloper.prvt.AI.HumanCivilization.entity;

import net.justonedeveloper.prvt.AI.HumanCivilization.Civilization;
import net.justonedeveloper.prvt.AI.HumanCivilization.World;
import net.justonedeveloper.prvt.AI.HumanCivilization.util.Log;
import net.justonedeveloper.prvt.AI.HumanCivilization.util.constants;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.HumanProperty;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.PopulationDensityPreference;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.PopulationPreference;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.Profession;

import java.lang.reflect.Array;
import java.util.*;

public class HumanEntity extends Entity {
	
	public static List<HumanEntity> allHumans = new ArrayList<>();
	
	public static long totalPopulation() {
		long humans = 0;
		for (HumanEntity allHuman : allHumans) {
			humans += allHuman.getPopulation();
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

		//DONE check for ConcurrentModificationException cause

		for(HumanEntity h : allHumans) {
			if(ArrayMatch(h.allProps, props)) return h;
		}
		return null;
	}
	
	public static void birthCycle() {
		System.out.println("Starting Birth Cycle");
		for(Object h : allHumans.toArray()) {
			((HumanEntity) h).birth();
		}
		System.out.println("Birth Cycle Finished.");
	}
	public static void advanceAge() {
		System.out.println("Starting Ageing Process");
		for(Object h : allHumans.toArray()) {
			((HumanEntity) h).age();
		}
		System.out.println("Ageing Process Finished.");
	}
	public static void spreadHumans() {
		System.out.println("Starting Spreading Process");
		for(Object h : allHumans.toArray()) {
			((HumanEntity) h).spread();
		}
		System.out.println("Spreading Process Finished.");
	}
	
	
	
	//----------NON-STATIC HUMANENTITY------------
	
	
	
	public World world;
	public HumanEntity setProps(HumanProperty[] props) {
		allProps = props;
		return this;
	}
	
	private long totalBodycount = 0;
	private HashMap<String, Long> bodycount = new HashMap<>();
	private HashMap<Integer, Long> age = new HashMap<>();		//<Age, Number of Population that age>
	
	//Begin Properties
	
	private HumanProperty[] allProps;
	
	private PopulationPreference PrefPopulation;
	private PopulationDensityPreference PrefDensity;
	private int DensityMaxTolerance;			//In percent, ranges from 10% to 50%
	private Profession profession;
	
	//End Properties
	
	//Start get Properties
	
	public HumanProperty[] getAllProps() {
		return allProps;
	}
	public PopulationPreference getPrefPopulation() {
		return PrefPopulation;
	}
	public PopulationDensityPreference getPrefPopulationDensity() {
		return PrefDensity;
	}
	public Profession getProfession() {
		return profession;
	}
	
	public long getPopulation() {
		return totalBodycount;
	}
	public long getPopulation(String field) {
		if(bodycount.containsKey(field)) return bodycount.get(field);
		return 0;
	}
	
	//End get Properties
	
	public HumanEntity addHuman(String Field, int amount, int age) {
		if(World.currentWorld.getGridMap().FieldExists(Field)) {
			long current = 0;
			if(bodycount.containsKey(Field)) current = bodycount.get(Field);
			
			//
			World.currentWorld.getGridMap().alterPopulation(Field, amount);
			totalBodycount += amount;
			bodycount.put(Field, current+amount);
			
			//Update Age List
			long AgePpl = 0;
			if(this.age.containsKey(age)) AgePpl = this.age.get(age);
			AgePpl += amount;
			this.age.put(age, AgePpl);
		} else System.out.println("Human Spawn Failed, Field does not exist!");
		return this;
	}
	
	//Kill People on all Fields equally
	public HumanEntity die(long amount) {
		int index = 0, max = bodycount.size(); long rest = amount, amountPerRun;
		String[] fields = bodycount.keySet().toArray(new String[bodycount.size()]);
		
		amountPerRun = Math.round(rest / max);
		
		while (rest > 0 && totalBodycount > 0) {
			if(amountPerRun > rest) amountPerRun = rest;
			
			//Kill People
			Log.log("KILL-DEBUG", "Killing " + amountPerRun + " People on Field " + fields[index]);
			die(fields[index], amountPerRun);
			rest -= amountPerRun;
			
			//Loop around
			if(index < max-1) index++;
			else index = 0;
		}
		
		return this;
	}
	public HumanEntity die(String Field, long amount) {
		if(World.currentWorld.getGridMap().FieldExists(Field)) {
			Log.log("KILL-DEBUG", "Field " + Field + " exists.");
			long current = 0;
			if(bodycount.containsKey(Field)) current = bodycount.get(Field);
			Log.log("KILL-DEBUG", "long current = " + current);
			
			Log.log("KILL-DEBUG", "Altering Population: " + World.currentWorld.getGridMap().getFieldPopulation(Field));
			World.currentWorld.getGridMap().alterPopulation(Field, -amount);
			totalBodycount -= amount;
			
			Log.log("KILL-DEBUG", "Altered Population: " + World.currentWorld.getGridMap().getFieldPopulation(Field));
			
			if(bodycount.get(Field) <= amount) {
				long rest = amount - bodycount.get(Field);
				bodycount.put(Field, 0L);
				World.currentWorld.getGridMap().alterPopulation(Field, -(amount-rest));
				while (rest > 0) {
					String near = world.getGridMap().getNearestField(Field, null, null);
					
					if(near.equals(Field)) break;
					
					if(!bodycount.containsKey(near)) continue;
					if(bodycount.get(near) >= rest) {
						World.currentWorld.getGridMap().alterPopulation(Field, -rest);
						bodycount.put(near, bodycount.get(near) - rest);
						break;
					} else {
						World.currentWorld.getGridMap().alterPopulation(Field, -(bodycount.get(near)));
						rest -= bodycount.get(near);
						bodycount.put(near, 0L);
					}
				}
				//DONE: Kill of Humans in surrounding Field
			} else {
				bodycount.put(Field, current-amount);
				World.currentWorld.getGridMap().alterPopulation(Field, -amount);
			}
			
			if(totalBodycount <= 0) {
				Civilization.deleteHumanEntity(this);
				//or: totalBodycount = 0;
			}
		}
		return this;
	}
	
	public void birth() {

		int[] ages = SortConvert(age.keySet().toArray(), false);

		for(int cur : ages) {
			long humans = 0;
			
			if(age.get(cur) == null) continue;		//DONE why can this happen
			
			if(cur >= constants.minimumHighBirthAge && cur <= constants.maximumHighBirthAge) {
				humans = constants.getBirthedPeople(age.get(cur), constants.percBirthHigh);
			} else if(cur >= constants.minimumBirthAge && cur <= constants.maximumBirthAge) {
				humans = constants.getBirthedPeople(age.get(cur), constants.percBirthLow);
			}
			//System.out.println("Generating birth for age " + cur + " (" + age.get(cur) + ") + " + age.values());		//TODO age.values() == [0] --> WHY
			
			int fieldIndex = 0;
			Object[] fields = bodycount.keySet().toArray();
			int add = randomInt(3, 8);
			
			//switch / case doesn't work with >=, just ==

			if(humans >= 500000) add = randomInt(490, 510);
			else if(humans >= 150000) add = randomInt(340, 360);
			else if(humans >= 35000) add = randomInt(140, 160);
			else if(humans >= 5000) add = randomInt(90, 110);
			else if(humans >= 1000) add = randomInt(35, 45);
			else if(humans >= 500) add = randomInt(15, 25);
			
			for (int i = 0; i < humans; i+=add) {
				newHuman(world, (String) fields[fieldIndex], add, HumanProperty.generatePropertySet(this), 0);
				if(fieldIndex == fields.length-1) fieldIndex = 0;
				else fieldIndex++;
			}
		}
	}
	
	public void age() {
		int[] ages = SortConvert(age.keySet().toArray(), true);
		
		for(int c : ages) {		//[25] | [5000]		//[25, 26] | [2500, 2500]
			/**
			 * 1. Eigenen Wert kopieren und 1 höher Einfügen (vorher fehlend)
			 * 2. Wert darunter nehmen und in die aktuelle Stelle einfügen (eig. unnötig, da vom letzen Schritt gedeckt.)
			 * 3. Am Ende die "Neugeborenen" hinzufügen
			*/
			//For Debugging: Log.log("DEBUG", "Amount Humans with Age 0: " + age.get(0));

			age.put(c+1, age.get(c));
			if((age.get(c-1) == null || age.get(c-1) == 0) && c > 0) age.remove(c);		//If the age below doesn't exist, it won't be overridden in the next run, so we do it here
			//Above: For remove unused
		}
		
		//Die of Old Age
		
		int dieProb = 0;
		for(int a = constants.dieOfAgeLvl1; a <= constants.maxAge; a++) {
			
			if(a < constants.maxAge-3) {
				if(a >= constants.dieOfAgeLvl3) dieProb += 2;
				else if(a >= constants.dieOfAgeLvl2) dieProb++;
				dieProb++;
			}
			
			if(!age.containsKey(a)) continue;
			
			long dead = constants.getDeadPeople(age.get(a), dieProb);
			Log.log("TESTING", dead + " People succumbed to old age at rate " + dieProb + " (Age " + a + " | Total before: " + age.get(a) + ")");
			die(dead);
		}
	}

	public String[] getFieldCluster(String field, int tolerance) {
		return null;
	}

	public void spread(String field, double percent, int tolerance, int range /*How many fields max. it spreads*/) {
		String[] fields = getFieldCluster(field, 0);
		//Equalize inside in range of City Type (Don't change city type) (Add all, )
		String[] adjacent = world.getGridMap().getAdjacentFields(fields, range);
		//spread
	}
	public void spread() {
	
	}

}
