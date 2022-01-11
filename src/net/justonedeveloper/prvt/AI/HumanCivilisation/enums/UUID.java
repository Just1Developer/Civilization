package net.justonedeveloper.prvt.AI.HumanCivilisation.enums;

import java.util.ArrayList;
import java.util.Random;

public class UUID {

	private static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private static int intervalls = 5, intervallength = 5;
	
	private static ArrayList<String> active = new ArrayList<String>();		//Probability of one Certain UUID is 4.72 * 10^(-49) %, of a Double 2.23 * 10^(-99) %
																			//					that of 6, the Max GG will count, is 1.11 * 10^(-300) %
	public static String generate() {
		Random r = new Random();
		StringBuilder uuid;
		do {
			uuid = new StringBuilder();
			for (int i = 0; i < intervalls; i++) {
				uuid.append('-');
				for (int i2 = 0; i2 < intervallength; i2++) {
					uuid.append(chars.charAt(r.nextInt(chars.length())));
				}
			}
		} while (active.contains(uuid.toString()));
		active.add(uuid.toString());
		return uuid.toString().replaceFirst("-", "");
	}

}
