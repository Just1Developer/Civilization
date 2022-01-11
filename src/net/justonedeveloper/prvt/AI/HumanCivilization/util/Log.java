package net.justonedeveloper.prvt.AI.HumanCivilization.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
	
	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.YY | HH:mm:ss");
	
	public static void log(String s) { log("...", s); }
	public static void log(String tag, String s) {
		System.out.println("[" + dtf.format(LocalDateTime.now()) + "]   [" + tag + "] " + s);
	}

}
