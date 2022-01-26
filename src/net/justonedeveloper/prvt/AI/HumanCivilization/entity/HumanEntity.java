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
	private static Random r = new Random();
	
	public static int totalPopulation() {
		int humans = 0;
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
	
	public HumanEntity die(String Field, int amount) {
		if(World.currentWorld.getGridMap().FieldExists(Field)) {
			long current = 0;
			if(bodycount.containsKey(Field)) current = bodycount.get(Field);
			
			World.currentWorld.getGridMap().alterPopulation(Field, -amount);
			totalBodycount -= amount;
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

	//----------------------------------GENERAL STATIC METHODS----------------------------------

	public static int CompareArrays(Object[] Array1, Object[] Array2) {
		int match = 0, len = Array1.length;
		if(Array2.length < Array1.length) len = Array2.length;	//Take length of shorter Array
		for(int i = 0; i < len; i++) {
			if(Array1[i].equals(Array2[i])) match++;
		}
		return match;
	}

	public static boolean ArrayMatch(Object[] Array1, Object[] Array2) {
		return (CompareArrays(Array1, Array2) == Array1.length && Array1.length == Array2.length);	//Every item must match
	}

	public static String ArrayToString(Object[] a) {
		StringBuilder s = new StringBuilder("[");
		for(Object o : a) {
			s.append(", ").append(o);
		}
		s.append("]");
		return s.toString().replaceFirst(", ", "");
	}

	public static int[] SortConvert(Object[] array, boolean reverse) {
		int[] conv = new int[array.length], conv2 = new int[array.length];

		//System.arraycopy(array, 0, conv, 0, array.length);
		//Above doesn't work, so I'm doing it manually

		for(int i = 0; i < array.length; i++) {
			conv[i] = (Integer) array[i];
		}

		Arrays.sort(conv);

		if(!reverse) return conv;

		int l = array.length-1;

		for(int i = 0; i <= l; i++) {
			conv2[l-i] = conv[i];
		}

		return conv2;
	}

	public static int randomInt(int min, int max) {		//Both values inclusive
		return r.nextInt(max - min + 1) + min;
	}

}
