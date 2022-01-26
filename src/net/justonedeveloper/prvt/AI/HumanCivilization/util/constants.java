package net.justonedeveloper.prvt.AI.HumanCivilization.util;

import java.util.Random;

public class constants {
	
	public static final int percBirthHigh = 55, percBirthLow = 40, percBirthRest = 0, tolerance = 5, percInherit = 70,
	minimumBirthAge = 25, maximumBirthAge = 48, minimumHighBirthAge = 30, maximumHighBirthAge = 38;
	
	static final Random r = new Random();
	
	public static boolean probability(float d) {
		d *= 10;
		if(Math.round(d) < r.nextInt(1000)) return true;
		return false;
	}
	
	public static long getBirthedPeople(long totalHumans, int BirthPercentage) {
		double d = randomInt(BirthPercentage-tolerance, BirthPercentage+tolerance);
		return (int) Math.round(totalHumans * (d / 100));
	}
	
	public static int randomInt(int min, int inclusiveMax) {
		return r.nextInt(inclusiveMax - min + 1) + min;
	}

}
