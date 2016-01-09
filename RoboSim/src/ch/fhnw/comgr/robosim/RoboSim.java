/*
 * Copyright (c) 2013 - 205 Stefan Muller Arisona, Simon Schubiger, Samuel von Stachelski
 * Copyright (c) 2013 - 205 FHNW & ETH Zurich
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

import ch.fhnw.ether.controller.IController;

import ch.fhnw.ether.formats.obj.ObjReader;
import ch.fhnw.ether.scene.DefaultScene;
import ch.fhnw.ether.scene.IScene;
import ch.fhnw.ether.scene.camera.Camera;
import ch.fhnw.ether.scene.camera.ICamera;
import ch.fhnw.ether.scene.light.DirectionalLight;
import ch.fhnw.ether.scene.light.ILight;
import ch.fhnw.ether.scene.light.PointLight;
import ch.fhnw.ether.scene.light.SpotLight;
import ch.fhnw.ether.scene.mesh.DefaultMesh;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.IMesh.Flag;
import ch.fhnw.ether.scene.mesh.geometry.DefaultGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry.Primitive;
import ch.fhnw.ether.scene.mesh.material.ColorMaterial;
import ch.fhnw.ether.ui.Button;
import ch.fhnw.ether.view.IView;
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
	private float angle = 0;
	private float speed = 0.3f;

	
	public static void main(String[] args) {
		new RoboSim();
	}

	public RoboSim() {
		// Create controller
		final List<IMesh> meshes = new ArrayList<>();
		double[] d={0,0,0,0,0,0,0,0};
		double[] a={0,0,0,0,0,0,0,0};
		RobotSolver R=new RobotSolver(d,a);
		RoboSimController controller = new RoboSimController();
		controller.run(time -> {
			// Create view
			IView view = new DefaultView(controller, 50, 50, 800, 800, IView.INTERACTIVE_VIEW, "Robot Simulation");
	
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
//			ILight light = new DirectionalLight(Vec3.Z, AMBIENT, COLOR);
//			ILight light = new SpotLight(lightMesh.getPosition(), AMBIENT, COLOR, 10, Vec3.Z_NEG, 15, 0);
			ILight light = new PointLight(lightMesh.getPosition(), AMBIENT, COLOR, 10);
			light.setPosition(lightMesh.getPosition());
			controller.setLight(light, lightMesh);
			
//			scene.add3DObject(new DirectionalLight(new Vec3(0, 0, 1), RGB.WHITE, RGB.WHITE));
//			scene.add3DObject(new DirectionalLight(new Vec3(0, 1, 0.5), RGB.WHITE, RGB.WHITE));

			// Add floor
			scene.add3DObject(RoboSimMeshUtilities.createFloor(new Vec2(-5, -5), new Vec2(5, 5), 5));
			// Add walls
			scene.add3DObject(RoboSimMeshUtilities.createWallX(new Vec3(-5, -5, 0), new Vec3(5, -5, 5), 5));
			scene.add3DObject(RoboSimMeshUtilities.createWallX(new Vec3(-5, 5, 0), new Vec3(5, 5, 5), 5));
			scene.add3DObject(RoboSimMeshUtilities.createWallY(new Vec3(-5, -5, 0), new Vec3(-5, 5, 5), 5));
			scene.add3DObject(RoboSimMeshUtilities.createWallY(new Vec3(5, -5, 0), new Vec3(5, 5, 5), 5));
			
			controller.initRobot();
			controller.initWidgets();
		});
	}
}
