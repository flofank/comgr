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
import ch.fhnw.ether.controller.event.IKeyEvent;
import ch.fhnw.ether.controller.tool.ITool;
import ch.fhnw.ether.scene.I3DObject;
import ch.fhnw.ether.scene.light.ILight;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.MeshUtilities;
import ch.fhnw.ether.scene.mesh.material.ShadedMaterial;
import ch.fhnw.ether.ui.Button;
import ch.fhnw.util.color.RGB;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;

public class RoboSimController extends DefaultController {
	private static final float INC_XY = 0.25f;
	private static final float INC_Z = 0.25f;
	private List<IMesh> meshes = new ArrayList<>();
	private ILight light;

    // 2do: state im controller?
    private int angle_bottom = 0;
    private int angle_mid1 = 0;
    private int angle_mid2 = 0;

    private float reso_axis_mid1 = 1;
    private float reso_axis_mid2 = 1;
    
    private ITool positioningTool;
    private ITool robotTool;

//    IKEngine ik;

    public RoboSimController() {
        positioningTool = new PositioningTool(this);
        robotTool = new RobotTool(this);
    }
	
	@Override
	public void keyPressed(IKeyEvent e) {
		switch (e.getKeyCode()) {
            case IKeyEvent.VK_W:
                light.setPosition(light.getPosition().add(Vec3.Y.scale(INC_XY)));
                break;
            case IKeyEvent.VK_S:
                light.setPosition(light.getPosition().add(Vec3.Y_NEG.scale(INC_XY)));
                break;
            case IKeyEvent.VK_A:
                light.setPosition(light.getPosition().add(Vec3.X_NEG.scale(INC_XY)));
                break;
            case IKeyEvent.VK_D:
                light.setPosition(light.getPosition().add(Vec3.X.scale(INC_XY)));
                break;
            default:
                super.keyPressed(e);
            }
	}

	public void initButtons() {
		getUI().addWidget(new Button(0, 3, "Quit", "Quit", KeyEvent.VK_ESCAPE, (button, v) -> System.exit(0)));
		getUI().addWidget(new Button(0, 2, "Positioning", "PositioningTool", KeyEvent.VK_P, (button, v) -> setCurrentTool(positioningTool)));
		getUI().addWidget(new Button(0, 1, "Robot", "RoboterControl", KeyEvent.VK_R, (button, v) -> setCurrentTool(robotTool)));
		getUI().addWidget(new Button(0, 0, "Add Cube", "Add Cube", KeyEvent.VK_R, (button, v) -> addCube()));
	}
	
	public void addCube() {
		IMesh cube = MeshUtilities.createCube(new ShadedMaterial(RGB.BLACK, RGB.BLUE, RGB.GRAY, RGB.WHITE, 10, 1, 1f));
		meshes.add(cube);
		getScene().add3DObject(cube);
	}

	public void setLight(ILight light) {
		this.light = light;
	}
	
	public void moveObject(I3DObject obj, Vec3 pos) {
		
	}

	public boolean obstructed(I3DObject obj) {
		boolean obstructed = false;
		for (IMesh mesh : meshes) {
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

    private void rotateMid1(float delta) {
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



}
