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
	
	public static int totalPopulation() {
		int humans = 0;
		for(Iterator<HumanEntity> all = allHumans.iterator(); all.hasNext(); ) {
			humans += all.next().getPopulation();
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
			if(h.allProps.equals(props)) return h;
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
		System.out.println("Starting Ageing");
		for(Object h : allHumans.toArray()) {
			((HumanEntity) h).age();
		}
		System.out.println("Ageing Finished.");
	}
	
	
	
	//----------NON-STATIC HUMANENTITY------------
	
	
	
	public World world;
	public HumanEntity setProps(HumanProperty[] props) {
		allProps = props;
		return this;
	}
	
	private int totalBodycount = 0;
	private HashMap<String, Integer> bodycount = new HashMap<>();
	private HashMap<Integer, Integer> age = new HashMap<>();		//<Age, Number of Population that age>
	
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
	
	public int getPopulation() {
		return totalBodycount;
	}
	public int getPopulation(String field) {
		if(bodycount.containsKey(field)) return bodycount.get(field);
		return 0;
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
			
			World.currentWorld.getGridMap().alterPopulation(Field, -amount);
			totalBodycount -= amount;
			if(bodycount.get(Field) <= amount) {
				int rest = amount - bodycount.get(Field);
				bodycount.put(Field, 0);
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
						bodycount.put(near, 0);
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
		
		//DONE make sure this accesses nothing static [ConcurrentModificationException debug]
		
		Log.log("1", "Current Age KeySet: " + age.keySet());
		Log.log("1", "Current Age ValueSet: " + age.values());
		
		for(int cur : age.keySet()) {
			int humans = 0;
			
			if(age.get(cur) == null) continue;		//DONE why can this happen
			
			if(cur >= constants.minimumHighBirthAge && cur <= constants.maximumHighBirthAge) {
				humans = constants.getBirthedPeople(age.get(cur), constants.percBirthHigh);
			} else if(cur >= constants.minimumBirthAge && cur <= constants.maximumBirthAge) {
				humans = constants.getBirthedPeople(age.get(cur), constants.percBirthLow);
			}
			Log.log("2", "Current Age KeySet: " + age.keySet());
			Log.log("2", "Current Age ValueSet: " + age.values());
			System.out.println("Generating birth for age " + cur + " (" + age.get(cur) + ") + " + age.values());		//TODO age.values() == [0] --> WHY
			
			int fieldIndex = 0;
			Object[] fields = bodycount.keySet().toArray();
			int add = 5;
			
			//switch / case doesn't work with >=, just ==
			if(humans >= 500) add = 20;
			else if(humans >= 1000) add = 40;
			else if(humans >= 5000) add = 100;
			else if(humans >= 35000) add = 150;
			else if(humans >= 150000) add = 350;
			else if(humans >= 500000) add = 500;
			Log.log("3", "Current Age KeySet: " + age.keySet());
			Log.log("3", "Current Age ValueSet: " + age.values());
			
			for (int i = 0; i < humans; i+=add) {
				newHuman(world, (String) fields[fieldIndex], add, HumanProperty.generatePropertySet(this), 0);
				if(fieldIndex == fields.length-1) fieldIndex = 0;
				else fieldIndex++;
			}
			Log.log("4", "Current Age KeySet: " + age.keySet());
			Log.log("4", "Current Age ValueSet: " + age.values());
		}
		Log.log("5", "Current Age KeySet: " + age.keySet());
		Log.log("5", "Current Age ValueSet: " + age.values());
	}
	
	public void age() {
		int[] ages = SortConvertAndReverse(age.keySet().toArray());
		Log.log("age-1", "Current Age KeySet: " + age.keySet());
		Log.log("age-1", "Current Age ValueSet: " + age.values());
		String agess = "[";
		for(int i : ages) {
			agess += ", " + i;
		}
		agess = agess.replaceFirst(", ", "") + "]";
		Log.log("age-1", "Ages: " + agess);
		int log = 0;
		
		for(int c : ages) {		//[25] | [5000]		//[25, 26] | [2500, 2500]
		
		}
		
		for(int c : ages) {		//c = current
			if(age.get(c-1) == null) age.put(c, 0);		//DONE Fixed why there can be null values
			log++;
			if(log < 2) {
				Log.log("age-1-" + c, "Current Age KeySet: " + age.keySet());
				Log.log("age-1-" + c, "Current Age ValueSet: " + age.values());
			}
			else age.put(c, age.get(c-1));
			if(log < 2) {
				Log.log("age-2-" + c, "Current Age KeySet: " + age.keySet());
				Log.log("age-2-" + c, "Current Age ValueSet: " + age.values());
			}
			if(c > 0) continue;
			if(log < 2) {
				Log.log("age-3-" + c, "Current Age KeySet: " + age.keySet());
				Log.log("age-3-" + c, "Current Age ValueSet: " + age.values());
			}
			age.put(0, 0);
			if(log < 2) {
				Log.log("age-4-" + c, "Current Age KeySet: " + age.keySet());
				Log.log("age-4-" + c, "Current Age ValueSet: " + age.values());
			}
			break;
		}
		Log.log("age-5", "Current Age KeySet: " + age.keySet());
		Log.log("age-5", "Current Age ValueSet: " + age.values());
	}
	
	private int[] SortConvertAndReverse(Object[] array) {
		int[] conv = new int[array.length], conv2 = new int[array.length];
		
		//System.arraycopy(array, 0, conv, 0, array.length);
		//Above doesn't work, so I'm doing it manually
		
		for(int i = 0; i < array.length; i++) {
			conv[i] = (Integer) array[i];
		}
		
		Arrays.sort(conv);
		
		int l = array.length-1;
		
		for(int i = 0; i <= l; i++) {
			conv2[l-i] = conv[i];
		}
		
		return conv2;
	}
	
}
