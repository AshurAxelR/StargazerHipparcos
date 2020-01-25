package com.xrbpowered.stargazer.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;

import java.awt.Color;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.system.MemoryStack;

import com.xrbpowered.gl.res.buffer.RenderTarget;
import com.xrbpowered.gl.res.mesh.StaticMesh;
import com.xrbpowered.gl.res.shader.Shader;
import com.xrbpowered.gl.res.shader.VertexInfo;
import com.xrbpowered.gl.res.texture.Texture;
import com.xrbpowered.gl.scene.CameraActor;
import com.xrbpowered.gl.scene.Controller;
import com.xrbpowered.gl.scene.WalkController;
import com.xrbpowered.gl.ui.pane.UIOffscreen;
import com.xrbpowered.stargazer.StargazerHipparcos;
import com.xrbpowered.stargazer.data.HipparcosData;
import com.xrbpowered.stargazer.ui.Parameter;
import com.xrbpowered.zoomui.UIContainer;

public class UIStarsPane extends UIOffscreen {

	public static final float baseFov = 45f;
	
	private VertexInfo starInfo;
	private Shader starShader;
	
	private StaticMesh stars;
	private Texture spectrum;

	public CameraActor camera;
	public Controller controller;

	public UIStarsPane(UIContainer parent) {
		super(parent);
	}

	@Override
	public void setSize(float width, float height) {
		super.setSize(width, height);
		camera.setAspectRatio(getWidth(), getHeight());
	}
	
	@Override
	public void setupResources() {
		clearColor = new Color(0x000911);

		camera = new CameraActor.Perspective().setRange(0.1f, 20f).setAspectRatio(getWidth(), getHeight());
		
		controller = new WalkController(getClient().input).setActor(camera);
		controller.moveSpeed = 0f;
		controller.setMouseLook(true);

		updateFov();

		spectrum = new Texture(BlackBodySpectrum.generateImage(512, 1), false, true);
		
		starInfo = new VertexInfo().addAttrib("in_Position", 3).addAttrib("in_Brightness", 1).addAttrib("in_Temperature", 1);
		
		starShader = new Shader(starInfo, "points_v.glsl", "points_f.glsl") {
			private int projectionMatrixLocation;
			private int viewMatrixLocation;
			private int screenHeightLocation;
			private int exposureLocation;
			private int contrastLocation;
			private int saturationLocation;
			@Override
			protected void storeUniformLocations() {
				projectionMatrixLocation = GL20.glGetUniformLocation(pId, "projectionMatrix");
				viewMatrixLocation = GL20.glGetUniformLocation(pId, "viewMatrix");
				screenHeightLocation  = GL20.glGetUniformLocation(pId, "screenHeight");
				exposureLocation  = GL20.glGetUniformLocation(pId, "exposure");
				contrastLocation  = GL20.glGetUniformLocation(pId, "contrast");
				saturationLocation  = GL20.glGetUniformLocation(pId, "saturation");
				try(MemoryStack stack = stackPush()) {
					IntBuffer pOut = stack.mallocInt(2);
					GL11.glGetIntegerv(GL20.GL_ALIASED_POINT_SIZE_RANGE, pOut);
					GL20.glUniform1f(GL20.glGetUniformLocation(pId, "maxSize"), (float)pOut.get(1));
				}
			}
			@Override
			public void updateUniforms() {
				glDisable(GL_DEPTH_TEST);
				glEnable(GL20.GL_POINT_SPRITE);
				glEnable(GL32.GL_PROGRAM_POINT_SIZE);
				glEnable(GL11.GL_BLEND);
				glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
				uniform(projectionMatrixLocation, camera.getProjection());
				uniform(viewMatrixLocation, camera.getView());
				GL20.glUniform1f(screenHeightLocation, getHeight());
				GL20.glUniform1f(exposureLocation, Parameter.exposure.getValue() / Parameter.zoom.getValue());
				GL20.glUniform1f(contrastLocation, Parameter.contrast.getValue());
				GL20.glUniform1f(saturationLocation, Parameter.saturation.getValue());
			}
			@Override
			public void unuse() {
				glDisable(GL11.GL_BLEND);
				glEnable(GL_DEPTH_TEST);
				super.unuse();
			}
		};

		stars = HipparcosData.createPointMesh(starInfo, HipparcosData.loadData());
		
		super.setupResources();
	}
	
	public void updateFov() {
		if(camera!=null) {
			float fov = Parameter.zoom.getValue();
			((CameraActor.Perspective)camera).setFov(baseFov * fov);
			controller.mouseSensitivity = 0.002f * fov;
		}
	}
	
	@Override
	public void updateTime(float dt) {
		super.updateTime(dt);
		StargazerHipparcos.client.updateTime(dt);
		controller.update(dt);
	}
	
	@Override
	public void render(RenderTarget target) {
		super.render(target);
		starShader.use();
		spectrum.bind(0);
		stars.draw();
		starShader.unuse();
	}
}
