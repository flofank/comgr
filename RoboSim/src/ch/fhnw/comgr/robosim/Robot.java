package ch.fhnw.comgr.robosim;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ch.fhnw.ether.controller.IController;
import ch.fhnw.ether.formats.obj.ObjReader;
import ch.fhnw.ether.scene.mesh.DefaultMesh;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.material.ColorMapMaterial;
import ch.fhnw.ether.scene.mesh.material.IMaterial;
import ch.fhnw.ether.scene.mesh.material.ShadedMaterial;
import ch.fhnw.ether.scene.mesh.material.Texture;
import ch.fhnw.util.color.RGB;
import ch.fhnw.util.color.RGBA;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;

public class Robot {
	private static final Robot INSTANCE = new Robot();
	private IMaterial mat = new ShadedMaterial(new RGB(0.6f, 0.10f, 0), new RGB(0.5f, 0, 0), new RGB(0.1f, 0.1f, 0.1f), RGB.WHITE, 0.5f, 0.5f, 1f);
//	private IMaterial mat = new ColorMapMaterial(RGBA.WHITE, new Texture(RoboSim.class.getResource("assets/metal.jpg")));
	private List<IMesh> parts;
	private IMesh obj;
	private List<Mat4> rotations;
	private List<Vec3> rotationAxes;
	private List<Vec3> preRotationTranslations;
	
	private Robot() {
		try {
			final URL obj = RoboSim.class.getResource("s.obj");
			
			ObjReader or = new ObjReader(obj);
			List<IMesh> meshes = or.getMeshes();
			parts = new ArrayList<>();
			// Ignore 3
//			parts.add(meshes.get(2));
//			parts.add(meshes.get(4));
//			parts.add(meshes.get(5));
//			parts.add(meshes.get(0));
//			parts.add(meshes.get(1));
//			parts.add(meshes.get(6));
//			parts.add(meshes.get(7));
			parts.add(new DefaultMesh(mat, meshes.get(2).getGeometry()));
			parts.add(new DefaultMesh(mat, meshes.get(4).getGeometry()));
			parts.add(new DefaultMesh(mat, meshes.get(5).getGeometry()));
			parts.add(new DefaultMesh(mat, meshes.get(0).getGeometry()));
			parts.add(new DefaultMesh(mat, meshes.get(1).getGeometry()));
			parts.add(new DefaultMesh(mat, meshes.get(6).getGeometry()));
			parts.add(new DefaultMesh(mat, meshes.get(7).getGeometry()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Init rotations matrixes
		rotations = new ArrayList<>();
		rotationAxes = new ArrayList<>();
		preRotationTranslations = new ArrayList<>();
		// Part 0
		rotations.add(Mat4.ID);
		rotationAxes.add(Vec3.Z);
		preRotationTranslations.add(Vec3.ZERO);
		// Part 1
		rotations.add(Mat4.ID);
		rotationAxes.add(Vec3.Z);
		preRotationTranslations.add(Vec3.ZERO);
		// Part 2
		rotations.add(Mat4.ID);
		rotationAxes.add(Vec3.Y);
//		preRotationTranslations.add(Vec3.ZERO);
		preRotationTranslations.add(new Vec3(0, 0, 0.375));
		// Part 3
		rotations.add(Mat4.ID);
		rotationAxes.add(Vec3.Y);
		preRotationTranslations.add(new Vec3(0, 0, 0.775));
		// Part 4
		rotations.add(Mat4.ID);
		rotationAxes.add(Vec3.Z);
		preRotationTranslations.add(new Vec3(0, 0.02, 0));
		// Part 5
		rotations.add(Mat4.ID);
		rotationAxes.add(Vec3.Y);
		preRotationTranslations.add(new Vec3(0, 0, 1.225));
		// Part 6
		rotations.add(Mat4.ID);
		rotationAxes.add(Vec3.Z);
		preRotationTranslations.add(new Vec3(0, 0.02, 0));
	}
	
	public List<IMesh> getMeshes() {
		return parts;
	}

	public static Robot getInstance() {
		return INSTANCE;
	}

	public void rotate(int part, int angle) {
		Mat4 newRot = rotations.get(part);
		newRot = newRot.postMultiply(Mat4.translate(preRotationTranslations.get(part).scale(1)));
		newRot = newRot.postMultiply(Mat4.rotate(angle, rotationAxes.get(part)));
		newRot = newRot.postMultiply(Mat4.translate(preRotationTranslations.get(part).scale(-1)));
		rotations.set(part, newRot);
		recalcRotations();
	}
	
	private void recalcRotations() {
		for (int p = 0; p < parts.size(); p++) {
			Mat4 rot = Mat4.ID;
			for (int r = 0; r <= p; r++) {
				rot = rot.postMultiply(rotations.get(r));
			}
			parts.get(p).setTransform(rot);
		}
		if (obj != null) {
			obj.setPosition(getMagnetPos());
//			Mat4 rot = Mat4.ID;
//			rot = rot.postMultiply(Mat4.translate(new Vec3(0,0,1.392)));
//			rot = rot.postMultiply(Mat4.scale(0.2f));
//			for (int r = 0; r < parts.size(); r++) {
//				rot = rot.postMultiply(rotations.get(r));
//			}
//			obj.setTransform(rot);
		}
	}

	public void reset() {
		for (int i = 0; i < rotations.size(); i++) {
			rotations.set(i, Mat4.ID);
		}
		recalcRotations();
	}

	public void setAngle(int part, float angle) {
		Mat4 newRot = Mat4.ID;
		newRot = newRot.postMultiply(Mat4.translate(preRotationTranslations.get(part).scale(1)));
		newRot = newRot.postMultiply(Mat4.rotate(angle, rotationAxes.get(part)));
		newRot = newRot.postMultiply(Mat4.translate(preRotationTranslations.get(part).scale(-1)));
		rotations.set(part, newRot);
		recalcRotations();
	}

	public void release() {
		System.out.println("Releasing " + obj.getName());
		obj = null;
	}

	public void pickUp(IMesh object) {
		obj = object;
		obj.setPosition(getMagnetPos());
		System.out.println("Picked up " + obj.getName());
	}

	public Vec3 getMagnetPos() {
		return new Vec3(parts.get(6).getTransformedPositionData()[0], 
				parts.get(6).getTransformedPositionData()[1],
				parts.get(6).getTransformedPositionData()[2]);
	}

	public Vec3 getMagnetDir() {
		Mat4 look = Mat4.translate(Vec3.Z);
		for (int r = 0; r < parts.size(); r++) {
			look = look.postMultiply(rotations.get(r));
		}
		return new Vec3(look.m02, look.m12, look.m22);
	}
}