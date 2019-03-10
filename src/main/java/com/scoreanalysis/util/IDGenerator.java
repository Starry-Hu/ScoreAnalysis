package com.scoreanalysis.util;

import java.util.UUID;


public class IDGenerator {

	public static String generator() {
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}
}
