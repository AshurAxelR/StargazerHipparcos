package com.xrbpowered.stargazer.render;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Random;

import com.xrbpowered.utils.MathUtils;

public class BlackBodySpectrum {

	public static int minTemp = 1000;
	public static int maxTemp = 40000; 
	public static int meanTemp = 8000;

	// http://www.tannerhelland.com/4435/convert-temperature-rgb-algorithm-code/

	public static double getRed(double t) {
		t *= 0.01;
		if(t<66.0)
			return 1.0;
		else {
			double r = 329.698727446 * Math.pow(t - 60.0, -0.1332047592);
			return MathUtils.snap(r/255.0);
		}
	}
	
	public static double getGreen(double t) {
		t *= 0.01;
		if(t<66.0) {
			double g = 99.4708025861 * Math.log(t)  - 161.1195681661;
			return MathUtils.snap(g/255.0);
		}
		else {
			double g = 288.1221695283 * Math.pow(t - 60.0, -0.0755148492);
			return MathUtils.snap(g/255.0);
		}
	}
	
	public static double getBlue(double t) {
		t *= 0.01;
		if(t>=66.0)
			return 1.0;
		else if(t<=19.0)
			return 0.0;
		else {
			double b = 138.5177312231 * Math.log(t - 10.0)  - 305.0447927307;
			return MathUtils.snap(b/255.0);
		}
	}
	
	public static Color getColor(double t) {
		return new Color((float)getRed(t), (float)getGreen(t), (float)getBlue(t)); 
	}
	
	public static String getSpectralClass(double t) {
		if(t>=60000.0) return "WR";
		else if(t>=30000.0) return "O";
		else if(t>=10000.0) return "B";
		else if(t>=7500.0) return "A";
		else if(t>=6000.0) return "F";
		else if(t>=5200.0) return "G";
		else if(t>=3700.0) return "K";
		else if(t>=2300.0) return "M";
		else if(t>=1500.0) return "C";
		else return "S";
	}
	
	public static double randomTemp(Random random, double mean) {
		double t = Math.log(1-random.nextDouble())/(-1.0/(mean-minTemp)) + minTemp;
		return t>maxTemp ? maxTemp : t;
	}

	public static double randomTemp(Random random) {
		return randomTemp(random, meanTemp);
	}

	public static int[] generate(int w, int h, boolean hasAlpha) {
		int[] rgb = new int[w*h*(hasAlpha ? 4 : 3)];
		int offs = 0;
		for(int y=0; y<h; y++) {
			for(int x=0; x<w; x++) {
				double t = MathUtils.lerp(minTemp, maxTemp, x/(double)w);
				rgb[offs++] = (int)(getRed(t)*255.0);
				rgb[offs++] = (int)(getGreen(t)*255.0);
				rgb[offs++] = (int)(getBlue(t)*255.0);
				if(hasAlpha)
					rgb[offs++] = 255;
			}
		}
		return rgb;
	}
	
	public static BufferedImage generateImage(int w, int h) {
		int[] rgb = generate(w, h, false);
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		raster.setPixels(0, 0, w, h, rgb);
		return image;
	}
	
}
