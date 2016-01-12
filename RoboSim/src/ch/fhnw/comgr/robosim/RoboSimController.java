/*
 * Copyright (c) 2014, FHNW (Simon Schubiger)
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
 *  Neither the name of FHNW nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
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
 */
package ch.fhnw.comgr.robosim;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import ch.fhnw.comgr.robosim.tools.PositioningTool;
import ch.fhnw.comgr.robosim.tools.RobotTool;
import ch.fhnw.ether.controller.DefaultController;
import ch.fhnw.ether.controller.event.IEventScheduler;
import ch.fhnw.ether.controller.event.IKeyEvent;
import ch.fhnw.ether.controller.tool.ITool;
import ch.fhnw.ether.scene.I3DObject;
import ch.fhnw.ether.scene.light.DirectionalLight;
import ch.fhnw.ether.scene.light.ILight;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.MeshUtilities;
import ch.fhnw.ether.scene.mesh.material.ShadedMaterial;
import ch.fhnw.ether.ui.Button;
import ch.fhnw.ether.ui.Slider;
import ch.fhnw.util.color.RGB;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;

public class RoboSimController extends DefaultController {
	private static final float INC_XY = 0.25f;
	private static final float INC_Z = 0.25f;
	private static final RGB COLOR = RGB.WHITE;
	private static final RGB AMBIENT = RGB.BLACK;
	private List<IMesh> cubes = new ArrayList<>();
	private ILight light = new DirectionalLight(Vec3.Z, AMBIENT, COLOR);
	private IMesh lightMesh;

	// 2do: state im controller?
	private int angle_bottom = 0;
	private int angle_mid1 = 0;
	private int angle_mid2 = 0;

	private float reso_axis_mid1 = 1;
	private float reso_axis_mid2 = 1;

	private Vec3 posCube=new Vec3(-0.5,-0.5,0.4);
	private ITool positioningTool;
	private ITool robotTool;

	private Robot robot;

	//    IKEngine ik;

	public RoboSimController() {
		positioningTool = new PositioningTool(this);
	}

	@Override
	public void keyPressed(IKeyEvent e) {
		switch (e.getKeyCode()) {
		case IKeyEvent.VK_UP:
			lightMesh.setPosition(lightMesh.getPosition().add(Vec3.X.scale(INC_XY)));
			break;
		case IKeyEvent.VK_DOWN:
			lightMesh.setPosition(lightMesh.getPosition().add(Vec3.X_NEG.scale(INC_XY)));
			break;
		case IKeyEvent.VK_RIGHT:
			lightMesh.setPosition(lightMesh.getPosition().add(Vec3.Y_NEG.scale(INC_XY)));
			break;
		case IKeyEvent.VK_LEFT:
			lightMesh.setPosition(lightMesh.getPosition().add(Vec3.Y.scale(INC_XY)));
			break;
		case IKeyEvent.VK_PAGE_UP:
			lightMesh.setPosition(lightMesh.getPosition().add(Vec3.Z.scale(INC_Z)));
			break;
		case IKeyEvent.VK_PAGE_DOWN:
			lightMesh.setPosition(lightMesh.getPosition().add(Vec3.Z_NEG.scale(INC_Z)));
			break;
		default:
			super.keyPressed(e);
		}
		light.setPosition(lightMesh.getPosition());
	}

	public void initWidgets() {
		getUI().addWidget(new Button(0, 9, "Move Cube", "CubeTool", KeyEvent.VK_P, (button, v) -> moveObject(posCube)));

		getUI().addWidget(new Button(0, 2, "Positioning", "PositioningTool", KeyEvent.VK_P, (button, v) -> setCurrentTool(positioningTool)));
		getUI().addWidget(new Button(0, 1, "Robot", "RoboterControl", KeyEvent.VK_R, (button, v) -> setCurrentTool(robotTool)));
		getUI().addWidget(new Button(0, 0, "Add Cube", "Add Cube", KeyEvent.VK_R, (button, v) -> addCube()));
		getUI().addWidget(new Slider(0, 3, "Robot rotation 1", "Robot Angle 1", 0, (slider, view) -> robot.setAngle(1, slider.getValue() * 360)));
		getUI().addWidget(new Slider(0, 4, "Robot rotation 2", "Robot Angle 2", 0, (slider, view) -> robot.setAngle(2, slider.getValue() * 360)));
		getUI().addWidget(new Slider(0, 5, "Robot rotation 3", "Robot Angle 3", 0, (slider, view) -> robot.setAngle(3, slider.getValue() * 360)));
		getUI().addWidget(new Slider(0, 6, "Robot rotation 4", "Robot Angle 4", 0, (slider, view) -> robot.setAngle(4, slider.getValue() * 360)));
		getUI().addWidget(new Slider(0, 7, "Robot rotation 5", "Robot Angle 5", 0, (slider, view) -> robot.setAngle(5, slider.getValue() * 360)));
		getUI().addWidget(new Slider(0, 8, "Robot rotation 6", "Robot Angle 6", 0, (slider, view) -> robot.setAngle(6, slider.getValue() * 360)));

	}

	public void initRobot() {
		robot = Robot.getInstance(); 
		robotTool = new RobotTool(this, robot);
		getScene().add3DObjects(robot.getMeshes());
	}

	public void addCube() {
		IMesh cube = MeshUtilities.createCube(new ShadedMaterial(RGB.BLACK, RGB.BLUE, RGB.GRAY, RGB.WHITE, 10, 1, 1f));
		cube.setTransform(Mat4.scale( 0.2f));

		cube.setPosition(new Vec3(0.5,.5,0.1));
		cubes.add(cube);
		getScene().add3DObject(cube);
	}

	public void setLight(ILight light, IMesh lightMesh) {
		this.light = light;
		this.lightMesh = lightMesh;
		getScene().add3DObjects(light);
		getScene().add3DObjects(lightMesh);
	}

	public void moveObject(I3DObject obj, Vec3 pos) {
		RobotSolver solver=new RobotSolver();


		List<Vec3>	Bahn = solver.solveBahn(new Vec3(0, 0, 1.23456), new Vec3(0.5, 0.5, 0.2));
		for(int i=0;i<Bahn.size();i++){
			solver.solveAngles(Bahn.get(i));
		}

	}
	public void moveObject(Vec3 pos) {
		RobotSolver solver=new RobotSolver();
		List<double[]>angles=new ArrayList<>();
		int pointer=0;
		List<Vec3>	Bahn1 = solver.solveBahn(new Vec3(0, 0, 1.23456), new Vec3(0.5, 0.5, 0.4));
		List<Vec3>	Bahn2 = solver.solveBahn(new Vec3(0.5, 0.5, 0.4), new Vec3(0.5, -0.5, 1));
		List<Vec3>	Bahn3 = solver.solveBahn(new Vec3(0.5, -0.5, 1), pos);
		for(int i=0;i<Bahn1.size();i++){
			angles.add(pointer, solver.solveAngles(Bahn1.get(i)));
			pointer++;
		}
		for(int i=0;i<Bahn2.size();i++){
			angles.add(pointer, solver.solveAngles(Bahn2.get(i)));
			pointer++;
		}
		for(int i=0;i<Bahn3.size();i++){
			angles.add(pointer, solver.solveAngles(Bahn3.get(i)));
			pointer++;
		}
		this.animate(new IEventScheduler.IAnimationAction() {
			private int inter=0;

			@Override
			public void run(double time, double interval) {
				if(inter<angles.size()){
					for(int i=1;i<angles.get(inter).length-1;i++){
						robot.setAngle(i, (float)angles.get(inter)[i]);

					}

				}
				else{
					return;
				}
				inter++;
			}
		});

	}

	public boolean obstructed(I3DObject obj) {
		boolean obstructed = false;
		for (IMesh mesh : cubes) {
			obstructed = obstructed || (mesh.getBounds().intersects(obj.getBounds()) && mesh != obj);
		}
		return obstructed;
	}

	public void rotateBottom(float delta) {
		Mat4 transform = Mat4.rotate(angle_bottom+=delta, Vec3.Z);
		List<IMesh> meshes = getRobotMeshes();
		for (IMesh m :  meshes) {
			if (meshes.indexOf(m) != 0) {
				m.setTransform(transform);
			}
		}
	}

	public void rotateMid1(float delta) {
		//        Mat4 transform = Mat4.multiply(Mat4.translate(0, 0, 0.273f), Mat4.rotate(angle_bottom+=delta, Vec3.Y));
		Mat4 transform = Mat4.rotate(angle_bottom+=delta, Vec3.Y);
		List<IMesh> meshes = getRobotMeshes();
		//        meshes.get(3).getGeometry().getAttributes()
		for (IMesh m :  meshes) {
			if (meshes.indexOf(m) > 2) {
				m.setTransform(transform);
			}

		}
	}

	private void rotateMid2(float delta) {
		List<IMesh> meshes = getRobotMeshes();
		for (IMesh m :  meshes) {
			if (meshes.indexOf(m) > 1) {
				m.setTransform(Mat4.rotate(angle_mid2+=delta, Vec3.Z));
			}

		}
	}

	private List<IMesh> getRobotMeshes() {
		List<IMesh> robotParts = new ArrayList<>();
		for (IMesh m : getScene().getMeshes()) {
			if (m.getName().substring(0, 5).equals("robot"))  {
				robotParts.add(m);
			}
		}


		System.out.println(robotParts.size());
		return robotParts;
	}

	public void setResolutionAll(float reso) {
		//        reso_axis_bottom = reso;
		reso_axis_mid1 = reso;
		reso_axis_mid2 = reso;
	}

	public List<IMesh> getCubes() {
		return cubes;
	}

}
