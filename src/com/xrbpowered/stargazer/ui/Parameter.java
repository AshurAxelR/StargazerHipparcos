package com.xrbpowered.stargazer.ui;

import java.util.ArrayList;

import com.sun.glass.events.KeyEvent;
import com.xrbpowered.gl.client.ClientInput;
import com.xrbpowered.stargazer.StargazerHipparcos;
import com.xrbpowered.stargazer.render.UIStarsPane;

public class Parameter {

	public final String name;
	public final int hotkey;
	public final int gridx, gridy;
	public float defaultValue = 0f;

	public int gridWidth = 1;
	
	public float deltaStep = 0.1f;
	protected float value;
	
	public Parameter(String name, int hotkey, int gridx, int gridy, float defaultValue) {
		parameters.add(this);
		this.name = name;
		this.hotkey = hotkey;
		this.gridx = gridx;
		this.gridy = gridy;
		this.defaultValue = defaultValue;
		setValue(defaultValue);
	}

	public Parameter(String name, int hotkey, int gridx, int gridy) {
		this(name, hotkey, gridx, gridy, 0f);
	}

	public Parameter setDeltaStep(float deltaStep) {
		this.deltaStep = deltaStep;
		return this;
	}
	
	protected void setValue(float v) {
		value = v;
	}
	
	public void reset() {
		setValue(defaultValue);
	}
	
	public void update(float delta) {
		setValue(value + delta*deltaStep);
	}
	
	public float getValue() {
		return factor(value);
	}
	
	public String formatValue() {
		return value>=0 ? String.format("X%.2f",  value+1f) : String.format("X:%.2f",  -value+1f);
	}

	public static ArrayList<Parameter> parameters = new ArrayList<>();
	
	public static Parameter zoom = new Parameter("Zoom FOV", KeyEvent.VK_Z, 0, 0) {
		@Override
		protected void setValue(float v) {
			if(v>0.5f) v = 0.5f;
			super.setValue(v);
			if(StargazerHipparcos.stars!=null)
				StargazerHipparcos.stars.updateFov();
		}
		@Override
		public String formatValue() {
			return String.format("%.0f\u00b0", UIStarsPane.baseFov * getValue());
		}
	};
	
	public static Parameter exposure = new Parameter("Exposure", KeyEvent.VK_X, 1, 0, -0.5f).setDeltaStep(0.5f);
	
	public static Parameter contrast = new Parameter("Mag. Exponent", KeyEvent.VK_C, 2, 0, 0.3f).setDeltaStep(0.05f);
	
	public static Parameter saturation = new Parameter("Saturation", KeyEvent.VK_V, 3, 0, -0.75f) {
		@Override
		protected void setValue(float v) {
			if(v>0) v = 0;
			super.setValue(v);
		}
	}.setDeltaStep(0.05f);
	
	public static Parameter getActiveParameter(ClientInput input) {
		Parameter active = null;
		for(Parameter param : parameters) {
			if(input.isKeyDown(param.hotkey)) {
				active = param;
				break;
			}
		}
		return active;
	}
	
	public static float factor(float x) {
		return x>=0 ? (x+1f) : 1f/(-x+1f);
	}

}
