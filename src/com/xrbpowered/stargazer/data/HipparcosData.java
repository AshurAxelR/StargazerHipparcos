package com.xrbpowered.stargazer.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.xrbpowered.gl.res.mesh.StaticMesh;
import com.xrbpowered.gl.res.shader.VertexInfo;

public class HipparcosData {

	public static String csvPath = "../../Hipparcos2/hip2.dat";
	public static String dataPath = "hip2.bin";
	
	public static final double pointDist = 10f;
	public static final double baseLum = 100f;
	
	public static class StarData {
		public int id;
		public double ra, de;
		public float mag, color;
	}

	public static ArrayList<StarData> load(String path) {
		try {
			Scanner in = new Scanner(new File(path));
			System.out.println("Loading...");
			
			ArrayList<StarData> stars = new ArrayList<>();
			while(in.hasNextLine()) {
				String[] s = in.nextLine().split("\\s+", 28);
				int offs = s[0].isEmpty() ? 0 : -1;
				StarData star = new StarData();
				star.id = Integer.parseInt(s[1+offs]);
				star.ra = Double.parseDouble(s[5+offs]);
				star.de = Double.parseDouble(s[6+offs]);
				star.mag = Float.parseFloat(s[20+offs]);
				star.color = Float.parseFloat(s[24+offs]);
				stars.add(star);
			}
			
			in.close();
			System.out.println("Done");
			return stars;
		}
		catch(NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static ArrayList<StarData> load() {
		return load(csvPath);
	}

	public static float[] loadData(String path) {
		try {
			ZipInputStream zip = new ZipInputStream(new FileInputStream(path));
			zip.getNextEntry();
			DataInputStream in = new DataInputStream(zip);
			System.out.println("Loading...");
			
			int numStars = in.readInt();
			float[] data = new float [numStars*5];
			for(int i=0; i<data.length; i++) {
				data[i] = in.readFloat();
			}
			
			zip.close();
			System.out.println("Done");
			return data;
		}
		catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static float[] loadData() {
		return loadData(dataPath);
	}

	public static void saveData(float[] data, String path) {
		try {
			ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(path));
			zip.putNextEntry(new ZipEntry("stardata"));
			DataOutputStream out = new DataOutputStream(zip);
			System.out.println("Saving...");

			int numStars = data.length/5;
			out.writeInt(numStars);
			for(int i=0; i<data.length; i++) {
				out.writeFloat(data[i]);
			}
			
			zip.closeEntry();
			zip.close();
			System.out.println("Done");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void convertData() {
		float[] data = createPointData(load());
		saveData(data, dataPath);
	}
	


	public static float[] createPointData(ArrayList<StarData> stars) {
		int numStars = stars.size();
		float[] data = new float [numStars*5];
		int offs = 0;
		for(StarData star : stars) {
			data[offs++] = (float)(Math.sin(star.ra)*Math.cos(star.de)*pointDist);
			data[offs++] = (float)(Math.sin(star.de)*pointDist);
			data[offs++] = (float)(Math.cos(star.ra)*Math.cos(star.de)*pointDist);
			
			data[offs++] = star.mag;
			
			float c = star.color;
			//if(c<0f) c *= 1.615f;
			c *= 1.615f;
			data[offs++] = 4600f*(1f/(0.92f*c+1.7f)+1f/(0.92f*c+0.62f));
		}
		return data;
	}
	

	public static StaticMesh createPointMesh(VertexInfo info, float[] data) {
		return new StaticMesh(info, data, 1, data.length/5, false);
	}
	
	public static StaticMesh createPointMesh(VertexInfo info, ArrayList<StarData> stars) {
		return new StaticMesh(info, createPointData(stars), 1, stars.size(), false);
	}

}
