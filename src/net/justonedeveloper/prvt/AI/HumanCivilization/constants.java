package net.justonedeveloper.prvt.AI.HumanCivilization;

import java.util.Random;

public class constants {
	
	public static int percBirthHigh = 40, percBirthLow = 55, percBirthRest = 0, tolerance = 5, percInherit = 70,
	minimumBirthAge = 25, maximumBirthAge = 48, minimumHighBirthAge = 30, maximumHighBirthAge = 38;
	
	static Random r = new Random();
	
	public static boolean probability(float d) {
		d *= 10;
		if(Math.round(d) < r.nextInt(1000)) return true;
		return false;
	}
	
	public static int getBirthedPeople(int BirthPercentage, int totalHumans) {
		double d = randomInt(BirthPercentage-tolerance, BirthPercentage+tolerance);
		return (int) Math.round(totalHumans * (d / 100));
	}
	
	public static int randomInt(int min, int inclusiveMax) {
		return r.nextInt(inclusiveMax - min + 1) + min;
	}

}
