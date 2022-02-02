package net.justonedeveloper.prvt.AI.HumanCivilization.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Entity {
	
	private final String UUID = net.justonedeveloper.prvt.AI.HumanCivilization.util.UUID.generate();
	
	public Entity() {
		
	}
	
	public String getUUID() {
		return UUID;
	}
	
	//----------------------------------GENERAL STATIC METHODS----------------------------------
	
	private static Random r = new Random();
	
	public static int CompareArrays(Object[] Array1, Object[] Array2) {
		int match = 0, len = Array1.length;
		if(Array2.length < Array1.length) len = Array2.length;	//Take length of shorter Array
		for(int i = 0; i < len; i++) {
			if(Array1[i] != null && Array1[i].equals(Array2[i])) match++;
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
		String newS = "";
		newS.replace("?", Character.toString(s.charAt(0)));
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
	
	public static String[] ConvertToStringArray(ArrayList<String> s) {
		String[] n = new String[s.size()];
		for(int i = 0; i < s.size(); i++) {
			n[i] = s.get(i);
		}
		return n;
	}
	
}
