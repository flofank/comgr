package ch.fhnw.comgr.robosim;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ch.fhnw.ether.formats.obj.ObjReader;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;

public class Robot {
	private static final Robot INSTANCE = new Robot();
	private List<IMesh> parts;
	private List<Mat4> rotations;
	private List<Vec3> rotationAxes;
	
	private Robot() {
		try {
			final URL obj = RoboSim.class.getResource("s.obj");
			
			ObjReader or = new ObjReader(obj);
			List<IMesh> meshes = or.getMeshes();
			IMesh tmp1=meshes.get(3);
            tmp1.setName("robot1");
            IMesh tmp2=meshes.get(4);
            tmp2.setName("robot2");
            IMesh tmp3=meshes.get(5);
            tmp3.setName("robot3");
            IMesh tmp4=meshes.get(0);
            tmp4.setName("robot4");
            IMesh tmp5=meshes.get(1);
            tmp5.setName("robot5");
            IMesh tmp0=meshes.get(2);
            tmp0.setName("robot6");
            parts = new ArrayList<>();
            parts.add(tmp0);
            parts.add(tmp1);
            parts.add(tmp2);
            parts.add(tmp3);
            parts.add(tmp4);
            parts.add(tmp5);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Init rotations matrixes
		rotations = new ArrayList<>();
		rotationAxes = new ArrayList<>();
		rotations.add(Mat4.ID);
		rotationAxes.add(Vec3.Z);
		rotations.add(Mat4.ID);
		rotationAxes.add(Vec3.Z);
		rotations.add(Mat4.ID);
		rotationAxes.add(Vec3.Y);
		rotations.add(Mat4.ID);
		rotationAxes.add(Vec3.Z);
		rotations.add(Mat4.ID);
		rotationAxes.add(Vec3.Y);
		rotations.add(Mat4.ID);
		rotationAxes.add(Vec3.Z);
	}
	
	public List<IMesh> getMeshes() {
		return parts;
	}

	public static Robot getInstance() {
		return INSTANCE;
	}

	public void rotate(int part, int angle) {
		rotations.set(part, rotations.get(part).postMultiply(Mat4.rotate(angle, rotationAxes.get(part))));
		for (int p = 0; p < parts.size(); p++) {
			Mat4 rot = Mat4.ID;
			for (int r = 0; r <= p; r++) {
				rot = rot.postMultiply(rotations.get(r));
			}
			parts.get(p).setTransform(rot);
		}
	}
}