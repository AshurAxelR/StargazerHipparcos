package com.xrbpowered.stargazer.ui;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import com.xrbpowered.gl.res.asset.AssetManager;
import com.xrbpowered.gl.ui.UINode;
import com.xrbpowered.gl.ui.pane.UIPane;
import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;

public class UIHudPane extends UIPane {

	public static final Color clearColor = new Color(0, true);

	public static final Color darkColor = new Color(0x223344);
	public static final Color darkTextColor = new Color(0x778899);
	public static final Color selectedTextColor = new Color(0xff9900);
	public static final Color boldTextColor = Color.WHITE;
	
	public static Font font;
	public static Font fontSmallBold;
	public static Font fontBold;

	public static int cellWidth = 200;
	public static int cellHeight = 80;
	
	protected float fadeDuration = 2f;
	
	protected boolean fade = false;
	protected float fadeTime = 0f;
	
	public static void initFonts() {
		try {
			font = AssetManager.defaultAssets.loadFont("font/RobotoCondensed-Regular.ttf").deriveFont(16f);
			fontBold = AssetManager.defaultAssets.loadFont("font/RobotoCondensed-Bold.ttf").deriveFont(24f);
			fontSmallBold = fontBold.deriveFont(16f);
		}
		catch(IOException e) {
			e.printStackTrace();
			font = null;
			fontBold = null;
		}
	}

	private static UIHudPane hud;

	private static class UIHudOverlay extends UINode {
		public UIHudOverlay(UIContainer parent) {
			super(parent);
		}
		@Override
		public void layout() {
			hud.setLocation(getWidth()/2f-hud.getWidth()/2, getHeight()-hud.getHeight()*1.5f);
			super.layout();
		}
	}
	
	public UIHudPane(UIContainer parent) {
		super(new UIHudOverlay(parent), false);
		hud = this;
		setSize(800, 80);
		
		for(Parameter param : Parameter.parameters) {
			new UIParameterDisplay(this, param);
		}
	}

	@Override
	protected void paintSelf(GraphAssist g) {
		g.graph.setBackground(clearColor);
		g.graph.clearRect(0, 0, (int)getWidth(), (int)getHeight());
		//g.line(0, 80, 1600, 80, darkColor);
	}
	
	public void show() {
		pane.alpha = 1f;
		fade = false;
		fadeTime = 0f;
		getParent().repaint();
	}
	
	public void fadeOut() {
		fade = true;
		repaint();
	}
	
	@Override
	public void updateTime(float dt) {
		if(fade) {
			fadeTime += dt;
			if(fadeTime>fadeDuration) {
				pane.alpha = 0f;
				fade = false;
			}
			else {
				pane.alpha = (float)Math.cos(fadeTime*Math.PI/fadeDuration)*0.5f+0.5f;
			}
		}
		super.updateTime(dt);
	}
	
}
