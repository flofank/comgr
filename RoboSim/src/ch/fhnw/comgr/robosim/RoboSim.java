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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ch.fhnw.ether.controller.IController;
import ch.fhnw.ether.formats.obj.ObjReader;
import ch.fhnw.ether.scene.DefaultScene;
import ch.fhnw.ether.scene.IScene;
import ch.fhnw.ether.scene.light.DirectionalLight;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.ui.Slider;
import ch.fhnw.ether.view.IView;
import ch.fhnw.ether.view.gl.DefaultView;
import ch.fhnw.util.color.RGB;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;

public final class RoboSim {
	private static final RGB AMBIENT = RGB.BLACK;
	private static final RGB COLOR = RGB.WHITE;
	private float speed = 0;
	private float axis[] = { (float) 0.5, (float) 0.5, (float) 0.5, (float) 0.5, (float) 0.5, (float) 0.5, (float) 0.5,
			(float) 0.5 };
	private double t1[] = new double[9];
	private double t2[] = new double[9];
	static final int FPS_MIN = 0;
	static final int FPS_MAX = 30;
	static final int FPS_INIT = 15; // initial frames per second
	int sim = 0;
	List<Vec3> Bahn;

	public static void main(String[] args) {
		new RoboSim();
	}

	public RoboSim() {
		final List<IMesh> meshes = new ArrayList<>();
		double[] d = { 0, 0, 0.384, -0.384, -0.77, 0, 0, 0, 0, 0, 0 };
		double[] a = { -0.0, -0.384, 0, 0.39, 0.01, 1.23456, 0, 0, 0, 0, 0, 0, 0 };
		double[] di = new double[8];// { 0,-0.478, -0.05, 0, -0.425, 0, -0, 0 };
		double[] ai = new double[8];// { 0,-0.05, -0.425, 0, 0, 0, 0, 0 };
		double alpha[] = { 0 * (Math.PI / 180), -90 * (Math.PI / 180), 90 * (Math.PI / 180), 0 * (Math.PI / 180),
				-90 * (Math.PI / 180), 90 * (Math.PI / 180), -90 * (Math.PI / 180), 0 * (Math.PI / 180),
				0 * (Math.PI / 180) };
		double alphai[] = new double[alpha.length];
		for (int i = 0; i < alpha.length - 1; i++) {
			alphai[i] = 0 + (alpha[i]);
			ai[i] = 0 + (a[i]);
			di[i] = 0 + (d[i]);

		}
		RobotSolver R = new RobotSolver(d, a, alpha);
		IController controller = new RoboSimController();
		controller.run(time -> {
			new DefaultView(controller, 0, 10, 512, 512, IView.INTERACTIVE_VIEW, "Robot Simulation");

			IScene scene = new DefaultScene(controller);
			controller.setScene(scene);

			scene.add3DObject(new DirectionalLight(new Vec3(0, 0, 1), RGB.BLACK, RGB.RED));
			scene.add3DObject(new DirectionalLight(new Vec3(0, 1, 0.5), RGB.BLACK, RGB.BLUE));

			try {
				final URL obj = RoboSim.class.getResource("i1.obj");

				new ObjReader(obj).getMeshes().forEach(mesh -> meshes.add(mesh));
				System.out.println("number of meshes before merging: " + meshes.size());

				IMesh tmp1 = meshes.get(3);
				IMesh tmp2 = meshes.get(4);
				IMesh tmp3 = meshes.get(5);
				IMesh tmp4 = meshes.get(0);
				IMesh tmp5 = meshes.get(1);
				IMesh tmp0 = meshes.get(2);
				meshes.set(0, tmp0);
				meshes.set(1, tmp1);
				meshes.set(2, tmp2);
				meshes.set(3, tmp3);
				meshes.set(4, tmp4);
				meshes.set(5, tmp5);

				for (int i = 1; i < meshes.size(); i++) {
					// meshes.get(i).setPosition(new Vec3(1,1,1));
				}
				scene.add3DObjects(meshes);
				controller.getUI().addWidget(
						new Slider(0, 1, "Angle 1", "Axis 2", speed, (slider, view) -> axis[1] = slider.getValue()));
				controller.getUI().addWidget(
						new Slider(0, 2, "Angle 2", "Axis 4", speed, (slider, view) -> axis[3] = slider.getValue()));
				controller.getUI().addWidget(
						new Slider(0, 3, "Angle 3", "Axis 5", speed, (slider, view) -> axis[4] = slider.getValue()));
				controller.getUI().addWidget(
						new Slider(2, 1, "Angle 4", "Axis 6", speed, (slider, view) -> axis[5] = slider.getValue()));
				controller.getUI().addWidget(
						new Slider(2, 2, "Angle 5", "Axis 7", speed, (slider, view) -> axis[6] = slider.getValue()));
				controller.getUI().addWidget(new Slider(2, 3, "Simulation", "Simualtion", speed,
						(slider, view) -> speed = slider.getValue()));
				List<Vec3> f1 = R.solveBahn(new Vec3(0, 0, 1.23456), new Vec3(0.8, 0, 0.8));
				List<Vec3> f2 = R.solveBahn(new Vec3(0.8, 0, 0.8), new Vec3(-0.7, 0.8, 0.8));
				List<Vec3> f3 = R.solveBahn(new Vec3(-0.7, 0.8, 0.8), new Vec3(-0.7, -0.8, 0.8));
				List<Vec3> f4 = R.solveBahn(new Vec3(-0.7, -0.8, 0.8), new Vec3(-0.7, -0.8, 0.5));
				List<Vec3> f5 = R.solveBahn(new Vec3(-0.7, -0.8, 0.5), new Vec3(0, 0, 1.23456));
				Bahn = new ArrayList<Vec3>();
				Bahn.addAll(0, f1);
				Bahn.addAll(Bahn.size(), f2);
				Bahn.addAll(Bahn.size(), f3);
				Bahn.addAll(Bahn.size(), f4);
				Bahn.addAll(Bahn.size(), f5);

			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		controller.animate((time, interval) -> {
			if (speed > 0) {
				if (sim < Bahn.size()) {
					if (sim == 0) {
						Bahn = R.solveBahn(new Vec3(0, 0, 1.23456), new Vec3(0.5, 0.5, 0.5));
					}
					List<Mat4> T1 = R.solve(R.solveAngles(Bahn.get(sim)));
					for (int i = 0; i < meshes.size() - 1; i++) {
						// meshes.get(i).setTransform(T2.get(i-1).inverse());

						meshes.get(i).setTransform(T1.get(i));

					}
					sim++;
					meshes.get(7).setTransform(T1.get(7));
				} else {
					sim = 0;
				}

			} else {
				for (int i = 0; i < axis.length; i++) {
					t1[i] = axis[i] * 270 - 135;
					t2[i] = -(axis[i] * 270 - 135);

				}
				List<Mat4> T1 = R.solve(t1);
				for (int i = 0; i < meshes.size() - 1; i++) {

					meshes.get(i).setTransform(T1.get(i));

				}
				meshes.get(7).setTransform(T1.get(7));

			}

		});

	}

}
