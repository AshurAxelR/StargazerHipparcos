package com.xrbpowered.stargazer.ui;

import static com.xrbpowered.stargazer.ui.UIHudPane.*;

import java.awt.event.KeyEvent;

import com.xrbpowered.stargazer.StargazerHipparcos;
import com.xrbpowered.zoomui.GraphAssist;
import com.xrbpowered.zoomui.UIContainer;
import com.xrbpowered.zoomui.UIElement;

public class UIParameterDisplay extends UIElement {

	public final Parameter parameter;
	
	public UIParameterDisplay(UIContainer parent, Parameter param) {
		super(parent);
		this.parameter = param;
		setSize(param.gridWidth*cellWidth, cellHeight);
		setLocation(param.gridx*cellWidth, param.gridy*cellHeight);
	}

	@Override
	public void paint(GraphAssist g) {
		boolean active = StargazerHipparcos.activeParameter==this.parameter;
		
		g.setColor(active ? selectedTextColor : darkTextColor);
		g.setFont(font);
		g.drawString(parameter.name, 50, 20, GraphAssist.LEFT, GraphAssist.CENTER);
		
		g.fillRect(32-10, 20-10, 20, 20, active ? selectedTextColor : darkColor);
		
		g.setFont(fontSmallBold);
		g.setColor(boldTextColor);
		g.drawString(KeyEvent.getKeyText(parameter.hotkey), 32, 20, GraphAssist.CENTER, GraphAssist.CENTER);
		
		g.setColor(boldTextColor);
		g.setFont(fontBold);
		g.drawString(parameter.formatValue(), 50, 56);
	}

}
