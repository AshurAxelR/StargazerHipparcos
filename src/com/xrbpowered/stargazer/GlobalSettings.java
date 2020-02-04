package com.xrbpowered.stargazer;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

import com.xrbpowered.stargazer.ui.Parameter;

public class GlobalSettings {

	public static final int minWindowWidth = 1600;
	public static final int minWindowHeight = 800;

	public static final String path = "./stargazer.cfg";
	
	public boolean fullscreen = true;
	public int windowedWidth = 1920;
	public int windowedHeight = 1080;
	public float uiScaling = 0f;
	public boolean vsync = false;
	public int noVsyncSleep = 4;
	public float mouseSensitivity = 0.002f;
	public boolean showFps = false;
	
	public static HashMap<String, String> loadValues() {
		try {
			HashMap<String, String> values = new HashMap<>();
			Scanner in = new Scanner(new File(path));
			while(in.hasNextLine()) {
				String[] s = in.nextLine().trim().split("\\s*:\\s*", 2);
				if(s.length==2)
					values.put(s[0], s[1]);
			}
			in.close();
			return values;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	private static int getInt(String value, int min, int max, int fallback) {
		if(value==null)
			return fallback;
		try {
			int n = Integer.parseInt(value);
			if(n<min || n>max)
				return fallback;
			return n;
		}
		catch(NumberFormatException e) {
			return fallback;
		}
	}
	
	private static float getFloat(String value, float min, float max, float fallback) {
		if(value==null)
			return fallback;
		try {
			float x = Float.parseFloat(value);
			if(x<min || x>max)
				return fallback;
			return x;
		}
		catch(NumberFormatException e) {
			return fallback;
		}
	}
	
	private static boolean getBoolean(String value, boolean fallback) {
		if(value==null)
			return fallback;
		if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes"))
			return true;
		else if(value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no"))
			return false;
		else
			return fallback;
	}
	
	public static GlobalSettings load() {
		HashMap<String, String> values = loadValues();
		GlobalSettings s = new GlobalSettings();
		if(values==null)
			return s;
		
		s.fullscreen = getBoolean(values.get("fullscreen"), s.fullscreen);
		s.windowedWidth = getInt(values.get("windowedWidth"), minWindowWidth, Integer.MAX_VALUE, s.windowedWidth);
		s.windowedHeight = getInt(values.get("windowedHeight"), minWindowHeight, Integer.MAX_VALUE, s.windowedHeight);
		s.uiScaling = getInt(values.get("uiScaling"), 0, 400, 0) / 100f;
		s.vsync = getBoolean(values.get("vsync"), s.vsync);
		s.noVsyncSleep = getInt(values.get("noVsyncSleep"), 0, 1000, s.noVsyncSleep);
		s.mouseSensitivity = getFloat(values.get("mouseSensitivity"), 0.0001f, 0.01f, 0.002f);
		s.showFps = getBoolean(values.get("showFps"), s.showFps);
		
		Parameter.exposure.setDefaultValue(values.get("defaultExposure"));
		Parameter.contrast.setDefaultValue(values.get("defaultContrast"));
		Parameter.saturation.setDefaultValue(values.get("defaultSaturation"));

		return s;
	}
	
}
