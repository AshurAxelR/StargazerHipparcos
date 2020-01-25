package com.xrbpowered.stargazer;

import static java.awt.event.KeyEvent.*;

import org.lwjgl.glfw.GLFW;

import com.xrbpowered.gl.client.UIClient;
import com.xrbpowered.stargazer.render.UIStarsPane;
import com.xrbpowered.stargazer.ui.Parameter;
import com.xrbpowered.stargazer.ui.UIHudPane;

public class StargazerHipparcos extends UIClient {

	public static StargazerHipparcos client;
	
	public static UIStarsPane stars;
	public static UIHudPane hud;
	
	public static Parameter activeParameter = null;
	
	public StargazerHipparcos() {
		super("Stargazer: Hipparcos");
		client = this;
		
		fullscreen = true;
		windowedWidth = 1920;
		windowedHeight = 1080;
		vsync = false;
		noVsyncSleep = 4;
		multisample = 0;
		
		UIHudPane.initFonts();
		
		stars = new UIStarsPane(getContainer());
		hud = new UIHudPane(getContainer());
		hud.fadeOut();
		
		//new UIFpsOverlay(this);
	}

	public void updateTime(float dt) {
		Parameter param = Parameter.getActiveParameter(input);
		if(param!=activeParameter) {
			activeParameter = param;
			if(activeParameter==null)
				hud.fadeOut();
			else
				hud.show();
		}
	}
	
	@Override
	public void keyPressed(char c, int code) {
		switch(code) {
			case VK_ESCAPE:
				requestExit();
				break;
		}
	}
	
	@Override
	public void mouseScroll(float x, float y, float delta) {
		if(activeParameter!=null) {
			activeParameter.update(delta);
			hud.repaint();
		}
	}
	
	@Override
	public void mouseDown(float x, float y, int button) {
		if((button==GLFW.GLFW_MOUSE_BUTTON_MIDDLE || button==GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
			if(activeParameter!=null) {
				activeParameter.reset();
				hud.repaint();
			}
		}
	}
	
	public static void main(String[] args) {
		//HipparcosData.convertData();
		new StargazerHipparcos().run();
	}


}
