package com.xrbpowered.stargazer;

import static java.awt.event.KeyEvent.*;

import org.lwjgl.glfw.GLFW;

import com.xrbpowered.gl.client.UIClient;
import com.xrbpowered.gl.ui.common.UIFpsOverlay;
import com.xrbpowered.stargazer.render.UIStarsPane;
import com.xrbpowered.stargazer.ui.Parameter;
import com.xrbpowered.stargazer.ui.UIHudPane;

public class StargazerHipparcos extends UIClient {

	public static StargazerHipparcos client;
	public static GlobalSettings settings;
	
	public static UIStarsPane stars;
	public static UIHudPane hud;
	
	public static Parameter activeParameter = null;
	
	public StargazerHipparcos() {
		super("Stargazer: Hipparcos");
		client = this;
		
		fullscreen = settings.fullscreen;
		windowedWidth = settings.windowedWidth;
		windowedHeight = settings.windowedHeight;
		vsync = settings.vsync;
		noVsyncSleep = settings.noVsyncSleep;
		multisample = 0;
		getContainer().setBaseScale(settings.uiScaling);
		
		UIHudPane.initFonts();
		
		stars = new UIStarsPane(getContainer());
		hud = new UIHudPane(getContainer());
		hud.fadeOut();
		
		if(settings.showFps)
			new UIFpsOverlay(this);
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
			case VK_MINUS:
				if(activeParameter!=null) {
					activeParameter.update(-1);
					hud.repaint();
				}
				break;
			case VK_EQUALS:
				if(activeParameter!=null) {
					activeParameter.update(1);
					hud.repaint();
				}
				break;
			case VK_BACK_SPACE:
				if(activeParameter!=null) {
					activeParameter.reset();
					hud.repaint();
				}
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
		settings = GlobalSettings.load();
		new StargazerHipparcos().run();
	}


}
