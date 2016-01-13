/*
 * Copyright (c) 2013 - 203 Stefan Muller Arisona, Simon Schubiger, Samuel von Stachelski
 * Copyright (c) 2013 - 203 FHNW & ETH Zurich
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *  Neither the name of FHNW / ETH Zurich nor the names of its contributors may
 *   be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */package ch.fhnw.comgr.robosim;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle.Control;


import ch.fhnw.ether.scene.DefaultScene;
import ch.fhnw.ether.scene.IScene;
import ch.fhnw.ether.scene.camera.Camera;
import ch.fhnw.ether.scene.camera.ICamera;
import ch.fhnw.ether.scene.light.DirectionalLight;
import ch.fhnw.ether.scene.light.ILight;
import ch.fhnw.ether.scene.light.PointLight;
import ch.fhnw.ether.scene.mesh.DefaultMesh;
import ch.fhnw.ether.scene.mesh.IMesh;


import ch.fhnw.ether.scene.mesh.IMesh.Flag;
import ch.fhnw.ether.scene.mesh.geometry.DefaultGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry.Primitive;
import ch.fhnw.ether.scene.mesh.material.ColorMaterial;

import ch.fhnw.ether.view.IView;
import ch.fhnw.ether.view.IView.Config;
import ch.fhnw.ether.view.IView.ViewFlag;
import ch.fhnw.ether.view.IView.ViewType;
import ch.fhnw.ether.view.gl.DefaultView;
import ch.fhnw.util.color.RGB;
import ch.fhnw.util.color.RGBA;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec2;
import ch.fhnw.util.math.Vec3;
import ch.fhnw.util.math.geometry.GeodesicSphere;

public final class RoboSim {
	private static final RGB AMBIENT = RGB.BLACK;
	private static final RGB COLOR = RGB.WHITE;

	

	

	public static void main(String[] args) {
		new RoboSim();
	}

	public RoboSim() {
		
		
		RoboSimController controller = new RoboSimController();
		controller.run(time -> {
		
			// Create view
			IView view = new DefaultView(controller, 30, 30, 800, 800, new Config(ViewType.INTERACTIVE_VIEW, 0, ViewFlag.SMOOTH_LINES), "Robot Simulation");

			IScene scene = new DefaultScene(controller);
			controller.setScene(scene);
			
			// Create and add camera
			ICamera camera = new Camera(new Vec3(-2, 0, 3), Vec3.ZERO);
			scene.add3DObject(camera);
			controller.setCamera(view, camera);
			
			// Add first light and light geometry
			GeodesicSphere s = new GeodesicSphere(4);
			IMesh lightMesh = new DefaultMesh(new ColorMaterial(RGBA.YELLOW), DefaultGeometry.createV(Primitive.TRIANGLES, s.getTriangles()), Flag.DONT_CAST_SHADOW);
			lightMesh.setTransform(Mat4.trs(0, 0, 0, 0, 0, 0, 0.1f, 0.1f, 0.1f));
			lightMesh.setPosition(new Vec3(0, -1, 2));
			ILight light = new DirectionalLight(Vec3.Z, AMBIENT, COLOR);
//			ILight light = new PointLight(lightMesh.getPosition(), AMBIENT, COLOR, 10);
			light.setPosition(lightMesh.getPosition());
			controller.setLight(light, lightMesh);
			
//			ILight light1 = new SpotLight(new Vec3(-4.9, -4.9, 4.9), AMBIENT, COLOR, 10, new Vec3(4.9, 4.9, -4.9), 23, 0);
//			ILight light2 = new SpotLight(new Vec3(-3, 3, 3), AMBIENT, COLOR, 10, new Vec3(3, -3, -3), 13, 0);
//			ILight light3 = new SpotLight(new Vec3(4.9, -4.9, 4.9), AMBIENT, COLOR, 10, new Vec3(-4.9, 4.9, -4.9), 23, 0);
//			ILight light4 = new SpotLight(new Vec3(3, 3, 3), AMBIENT, COLOR, 10, new Vec3(-3, -3, -3), 13, 0);
//			controller.getScene().add3DObjects(light3, light1);
//			scene.add3DObject(new DirectionalLight(new Vec3(0, 0, 1), RGB.WHITE, RGB.WHITE));
//			scene.add3DObject(new DirectionalLight(new Vec3(0, 1, 0.3), RGB.WHITE, RGB.WHITE));

			// Add floor
			scene.add3DObject(RoboSimMeshUtilities.createFloor(new Vec2(-3, -3), new Vec2(3, 3), 1));
			// Add walls
			scene.add3DObject(RoboSimMeshUtilities.createWallX(new Vec3(-3, -3, 0), new Vec3(3, -3, 3), 3));
			scene.add3DObject(RoboSimMeshUtilities.createWallX(new Vec3(-3, 3, 0), new Vec3(3, 3, 3), 3));
			scene.add3DObject(RoboSimMeshUtilities.createWallY(new Vec3(-3, -3, 0), new Vec3(-3, 3, 3), 3));
			scene.add3DObject(RoboSimMeshUtilities.createWallY(new Vec3(3, -3, 0), new Vec3(3, 3, 3), 3));
			
			controller.initRobot();
			controller.initWidgets();
		});

	}

}
