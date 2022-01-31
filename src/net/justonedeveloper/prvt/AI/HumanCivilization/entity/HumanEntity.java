package net.justonedeveloper.prvt.AI.HumanCivilization.entity;

import net.justonedeveloper.prvt.AI.HumanCivilization.Civilization;
import net.justonedeveloper.prvt.AI.HumanCivilization.GridMap;
import net.justonedeveloper.prvt.AI.HumanCivilization.World;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.PopulationType;
import net.justonedeveloper.prvt.AI.HumanCivilization.util.Log;
import net.justonedeveloper.prvt.AI.HumanCivilization.util.constants;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.HumanProperty;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.PopulationDensityPreference;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.PopulationPreference;
import net.justonedeveloper.prvt.AI.HumanCivilization.enums.properties.Profession;

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
	
	public static HumanEntity newHuman(World w, String Field, int amount, HumanProperty[] props, int age) {return newHuman(w, Field, amount, props, age, constants.randomDensityTolerance()); }
	public static HumanEntity newHuman(World w, String Field, int amount, HumanProperty[] props, int age, int DensityTolerance) {
		//if human exists, add to bodycount
		HumanEntity h = HumanAlreadyExists(props);
		if(h != null) {
			return h.addHuman(Field, amount, age);
		} else {
			return new HumanEntity(w, Field, amount, age).setProps(props, DensityTolerance);
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
	public HumanEntity setProps(HumanProperty[] props, int MaxDensityTolerance) {
		allProps = props;
		for(HumanProperty h : props) {
			if(h instanceof Profession) profession = (Profession) h;
			else if(h instanceof PopulationPreference) PrefPopulation = (PopulationPreference) h;
			else if(h instanceof PopulationDensityPreference) PrefDensity = (PopulationDensityPreference) h;
		}
		if(MaxDensityTolerance <= constants.DensityToleranceMax && MaxDensityTolerance >= constants.DensityToleranceMin) DensityMaxTolerance = MaxDensityTolerance;
		return this;
	}
	
	private long totalBodycount = 0;
	private final HashMap<String, Long> bodycount = new HashMap<>();
	private final HashMap<Integer, Long> age = new HashMap<>();		//<Age, Number of Population that age>
	
	//Begin Properties
	
	private HumanProperty[] allProps;
	
	private PopulationPreference PrefPopulation = HumanProperty.getRandomPopulationPreference();
	private PopulationDensityPreference PrefDensity = HumanProperty.getRandomPopulationDensityPreference();
	private int DensityMaxTolerance = constants.randomDensityTolerance();			//In percent, ranges from 10% to 50%
	private Profession profession = HumanProperty.getRandomProfession();
	
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
	public void dieOfAge(long amount, int age/*, boolean killRest If the method should kill more if */) {
		/*if(killRest) {
			long rest = amount, currentPeople = this.age.get(age);
			int offset = 0;
			while(totalBodycount > 0 && rest > 0 && (age-offset >= 0 || age+offset <= constants.maxAge)) {
				if(currentPeople > rest) {
				
				}
				offset++;
				currentPeople = this.age.get(age+offset) + this.age.get(age-offset);
			}
		} else {*/
			die(amount);
			if(this.age.get(age) <= amount) this.age.remove(age);
			else this.age.put(age, this.age.get(age) - amount);
//		}
	}
	public void die(long amount) {
		int index = 0, max = bodycount.size(); long rest = amount, amountPerRun;
		String[] fields = bodycount.keySet().toArray(new String[0]);
		
		amountPerRun = rest / max;																						//TODO removed Math.round, check if it still works
		
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
	}
	public void die(String Field, long amount) {
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
			/*
			  1. Eigenen Wert kopieren und 1 höher Einfügen (vorher fehlend)
			  2. Wert darunter nehmen und in die aktuelle Stelle einfügen (eig. unnötig, da vom letzen Schritt gedeckt.)
			  3. Am Ende die "Neugeborenen" hinzufügen
			*/

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
			Log.log("OLD AGE", dead + " People succumbed to old age at rate " + dieProb + " (Age " + a + " | Total before: " + age.get(a) + ")");
			dieOfAge(dead, a);
		}
	}
	
	public String[] getFieldCluster(String field, int tolerance) { return getFieldCluster(field, new int[] {-tolerance, tolerance}, null); }
	public String[] getFieldCluster(String field, int[] tolerance) { return getFieldCluster(field, tolerance, null); }
	public String[] getFieldCluster(String field, int tolerance, ArrayList<PopulationType> exclude) { return getFieldCluster(field, new int[] {-tolerance, tolerance}, exclude); }
	public String[] getFieldCluster(String Field, int[] tolerance, ArrayList<PopulationType> exclude) {
		ArrayList<String> fields = new ArrayList<>();
		GridMap g = World.currentWorld.getGridMap();
		
		if(Field == null) return ConvertToStringArray(fields);
		if(g == null) return ConvertToStringArray(fields);
		if(exclude == null) exclude = new ArrayList<>();
		
		//variables
		ArrayList<String> newFields = new ArrayList<>();
		newFields.add(Field);
		PopulationType popType = PopulationType.parseType(bodycount.get(Field));
		
		//Integers
		int x_offset = 1, y_offset = 0, coordX = Integer.parseInt(Field.split("x")[0]), coordY = Integer.parseInt(Field.split("x")[1]), count = 0, max = g.getSize()*g.getSize();
		int fails = 0;		//Formula for Radius: pi*r² -> 3.5*x_offset*x_offset
		
		while(count < max) {
			
			for(int iX = -1; iX < 2; iX++) {
				for(int iY = -1; iY < 2; iY++) {
					final String field = (coordX + (x_offset * iX)) + "x" + (coordY + (y_offset * iY));
					if (g.FieldExists(field)) {
						
						if(PopulationType.isInBounds(g.getFieldPopulation(field), popType, tolerance) && !newFields.contains(field) &&
								!exclude.contains(PopulationType.parseType(g.getFieldPopulation(field)))) {
							
							newFields.add(field);
							fails = 0;
							
						} else fails++;
					}
				}
			}
			if(y_offset < x_offset) y_offset++;
			else x_offset++;
			
			if(3.5*x_offset*x_offset <= fails) break;
			
			count++;
			
		}
		String[] converted = ConvertToStringArray(newFields);
		Arrays.sort(converted);					//so Clusters are always sorted the same
		return converted;
	}

	public ArrayList<String>[] spread(String field, int percent, int range /*How many fields max. it spreads*/, boolean updateAffectedFields) {
		ArrayList<String> affectedFields = new ArrayList<>(), affectedFieldsDirect = new ArrayList<>(), affectedFieldsFar = new ArrayList<>();
		String[] fields = getFieldCluster(field, 0);
		String[] adjacent = world.getGridMap().getAdjacentFields(fields, range);
		int currentFieldsSize = fields.length, adjacentSize = adjacent.length;
		
		//Equalize inside in range of City Type (Don't change city type) (Add all, )
		long totalInnerPop = 0, newPerField, rest;
		//Calculate Total Population
		for(String f : fields) {
			totalInnerPop += getPopulation(f);
		}
		//Divide up and add the rest to the OG Field (can't risk errors in Property calculation)
		newPerField = totalInnerPop / currentFieldsSize;
		while(newPerField * currentFieldsSize > totalInnerPop) { newPerField--; }
		rest = totalInnerPop - (newPerField * currentFieldsSize);
		
		for(String f : fields) {
			bodycount.put(f, newPerField);
			affectedFields.add(f);
			affectedFieldsDirect.add(f);
		}
		bodycount.put(field, newPerField + rest);
		
		//spread
		long totalMoving = constants.getPeopleOf(totalInnerPop, percent);
		
		//Copied Algorithm from above; if something doesn't work the error might be here
		//Divide up and add the rest to the OG Field (can't risk errors in Property calculation) (dont care)
		newPerField = totalMoving / adjacentSize;
		while(newPerField * adjacentSize > totalMoving) { newPerField--; }
		rest = totalMoving - (newPerField * adjacentSize);
		
		for(String f : adjacent) {
			bodycount.put(f, newPerField);
			affectedFields.add(f);
			affectedFieldsFar.add(f);
		}
		bodycount.put(field, bodycount.get(field) + rest);
		
		//update
		if(updateAffectedFields) world.getGridMap().refreshPopulation(affectedFields);
		
		//So smart returning all lists for Whatever reason
		ArrayList<String>[] returnLists = new ArrayList[3];
		returnLists[0] = affectedFields;
		returnLists[1] = affectedFieldsDirect;
		returnLists[2] = affectedFieldsFar;
		
		return returnLists;
	}
	public void spread() {
		ArrayList<String> totalFields = (ArrayList<String>) Arrays.asList(bodycount.keySet().toArray(new String[1]));
		ArrayList<String[]> clusters = new ArrayList<>();
		for(String f : totalFields) {
			String[] cluster = getFieldCluster(f, 0);
			
			//only add cluster if it isn't already in there
			boolean contains = false;
			for(String[] ar : clusters) {
				if(Entity.ArrayToString(ar).equals(Entity.ArrayToString(cluster))) contains = true;		//Clusters are always sorted the same no matter the startfield
			}
			if(contains) continue;
			clusters.add(cluster);
		}
		//Sort Clusters by size
		ArrayList<String[]> remaining = (ArrayList<String[]>) clusters.clone(), sorted = new ArrayList<>();
		
		for(int i = PopulationType.ALL.length-1; i >= 0; i--) {			//All different PopulationTypes; Decreasing
			for (String[] clust : (ArrayList<String[]>) remaining.clone()) {
				if (clust[0] == null) {
					clusters.remove(clust);
					remaining.remove(clust);
					continue;
				}
				if (PopulationType.ALL[i] == PopulationType.parseType(bodycount.get(clust[0]))) {						//TODO maybe add sorting by Size as well
					sorted.add(clust);
					remaining.remove(clust);
				}
			}
		}
		
		//spread it
		ArrayList<String> update = new ArrayList<>();
		for (String[] clust : clusters) {
			for(String ar : spread(clust[0], constants.HumanSpreadPercentage, (clust.length/10)+1, false)[0]) {
				if(update.contains(ar)) update.add(ar);
			}
		}
		world.getGridMap().refreshPopulation(update);
	}

}
