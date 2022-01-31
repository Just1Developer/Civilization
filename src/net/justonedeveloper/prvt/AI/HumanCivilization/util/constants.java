package net.justonedeveloper.prvt.AI.HumanCivilization.util;

import java.util.Random;

public class constants {
	
	public static final int percBirthHigh = 4, percBirthLow = 3, tolerance = 5, percInherit = 70,		//Actually, Birth rate ~100% / 30 years ~= 3%
	minimumBirthAge = 25, maximumBirthAge = 48, minimumHighBirthAge = 30, maximumHighBirthAge = 38,
	dieOfAgeLvl1 = 75, dieOfAgeLvl2 = 85, dieOfAgeLvl3 = 100, maxAge = 120,
	DensityToleranceMin = 10, DensityToleranceMax = 70,
	HumanSpreadPercentage = 20;
	
	/**
	 * Death Probablilties
	 * 75 - 1%		85 - 12%		95 - 32%		105 - 58%		115 - 88%
	 * 76 - 2%		86 - 14%		96 - 34%		106 - 61%		116 - 91%
	 * 77 - 3%		87 - 16%		97 - 36%		107 - 64%		117 - 94%
	 * 78 - 4%		88 - 18%		98 - 38%		108 - 67%		118 - 97%
	 * 79 - 5%		89 - 20%		99 - 40%		109 - 70%		119 - 98%
	 * 80 - 6%		90 - 22%		100 - 43%		110 - 73%		120 - 99%
	 * 81 - 7%		91 - 24%		101 - 46%		111 - 76%		121 - 100%
	 * 82 - 8%		92 - 26%		102 - 49%		112 - 79%
	 * 83 - 9%		93 - 28%		103 - 52%		113 - 82%
	 * 84 - 10%		94 - 30%		104 - 55%		114 - 85%
	 * */
	
	static final Random r = new Random();
	
	public static boolean probability(float d) {
		d *= 10;
		return Math.round(d) < r.nextInt(1000);
	}
	
	public static long getBirthedPeople(long totalHumans, int BirthPercentage) {
		double d = randomInt(BirthPercentage-tolerance, BirthPercentage+tolerance);
		return (int) Math.round(totalHumans * (d / 100));
	}
	public static long getDeadPeople(long totalHumans, int DeathPercentage) {
		return (int) Math.round(totalHumans * ((double) DeathPercentage / 100));
	}
	public static long getPeopleOf(long totalHumans, int percent) {
		return (int) Math.round(totalHumans * ((double) percent / 100));
	}
	
	public static int randomInt(int min, int inclusiveMax) {
		return r.nextInt(inclusiveMax - min + 1) + min;
	}
	
	public static int randomDensityTolerance() {
		return randomInt(DensityToleranceMin, DensityToleranceMax);
	}
	
}
