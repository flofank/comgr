/*
 * Copyright (c) 2013 - 2015 Stefan Muller Arisona, Simon Schubiger, Samuel von Stachelski
 * Copyright (c) 2013 - 2015 FHNW & ETH Zurich
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
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.ui.Button;
import ch.fhnw.ether.view.IView;
import ch.fhnw.ether.view.gl.DefaultView;
import ch.fhnw.util.color.RGB;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec2;
import ch.fhnw.util.math.Vec3;

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
			IView view = new DefaultView(controller, 50, 50, 512, 512, IView.INTERACTIVE_VIEW, "Robot Simulation");
	
			IScene scene = new DefaultScene(controller);
			controller.setScene(scene);
			
			// Create and add camera
			ICamera camera = new Camera(new Vec3(-5, -5, 2), Vec3.ZERO);
			scene.add3DObject(camera);
			controller.setCamera(view, camera);
			
			scene.add3DObject(new DirectionalLight(new Vec3(0, 0, 1), RGB.BLACK, RGB.RED));
			scene.add3DObject(new DirectionalLight(new Vec3(0, 1, 0.5), RGB.BLACK, RGB.BLUE));

			// Add floor
			scene.add3DObject(RoboSimMeshUtilities.createFloor(new Vec2(-15, -15), new Vec2(15, 15), 3));
			// Add walls
			scene.add3DObject(RoboSimMeshUtilities.createWallX(new Vec3(-15, -15, 0), new Vec3(15, -15, 5), 5));
			scene.add3DObject(RoboSimMeshUtilities.createWallX(new Vec3(-15, 15, 0), new Vec3(15, 15, 5), 5));
			scene.add3DObject(RoboSimMeshUtilities.createWallY(new Vec3(-15, -15, 0), new Vec3(-15, 15, 5), 5));
			scene.add3DObject(RoboSimMeshUtilities.createWallY(new Vec3(15, -15, 0), new Vec3(15, 15, 5), 5));
			
//			try {
//				final URL obj = RoboSim.class.getResource("s.obj");
//				//final URL obj = new URL("file:///Users/radar/Desktop/aventador/aventador_red.obj");
//				//final URL obj = new URL("file:///Users/radar/Desktop/demopolis/berlin_mitte_o2_o3/o2_small.obj");
//				
//				
//				new ObjReader(obj).getMeshes().forEach(mesh -> meshes.add(mesh));
//				System.out.println("number of meshes before merging: " + meshes.size());
//				//final List<IMesh> merged = MeshUtilities.mergeMeshes(meshes);
//				//System.out.println("number of meshes after merging: " + merged.size());
//				//double[] d={270*(Math.PI/180),0,90*(Math.PI/180),270*(Math.PI/180),90*(Math.PI/180),0};
//				IMesh tmp1=meshes.get(3);
//                tmp1.setName("robot1");
//                IMesh tmp2=meshes.get(4);
//                tmp2.setName("robot2");
//                IMesh tmp3=meshes.get(5);
//                tmp3.setName("robot3");
//                IMesh tmp4=meshes.get(0);
//                tmp4.setName("robot4");
//                IMesh tmp5=meshes.get(1);
//                tmp5.setName("robot5");
//                IMesh tmp0=meshes.get(2);
//                tmp0.setName("robot6");
//                meshes.set(0,tmp0);
//				meshes.set(1,tmp1);
//				meshes.set(2,tmp2);
//				meshes.set(3,tmp3);
//				meshes.set(4,tmp4);
//				meshes.set(5,tmp5);
//				
//				
//				scene.add3DObjects(meshes);
//
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			controller.initRobot();
			controller.initButtons();
		});
//		controller.animate((time, interval) -> {
//            angle += 2*speed;
//
//            List<Mat4> T=R.solve(angle);
//            for(int i=1;i<T.size();i++){
//
//				meshes.get(i).setTransform(T.get(i));
//
//			}
//
//        });
	}
}
