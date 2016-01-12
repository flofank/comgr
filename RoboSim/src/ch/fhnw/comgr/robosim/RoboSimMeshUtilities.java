package ch.fhnw.comgr.robosim;

import java.io.IOException;

import javax.naming.directory.InvalidAttributesException;

import ch.fhnw.ether.image.Frame;
import ch.fhnw.ether.scene.I3DObject;
import ch.fhnw.ether.scene.mesh.DefaultMesh;
import ch.fhnw.ether.scene.mesh.MeshUtilities;
import ch.fhnw.ether.scene.mesh.geometry.DefaultGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry;
import ch.fhnw.ether.scene.mesh.geometry.IGeometry.Primitive;
import ch.fhnw.ether.scene.mesh.material.ColorMapMaterial;
import ch.fhnw.ether.scene.mesh.material.IMaterial;
import ch.fhnw.ether.scene.mesh.material.Texture;
import ch.fhnw.util.color.RGBA;
import ch.fhnw.util.math.Vec2;
import ch.fhnw.util.math.Vec3;

public class RoboSimMeshUtilities {
	
	public static I3DObject createFloor(Vec2 start, Vec2 end, int detail) {
		float width = (end.x - start.x) / detail;
		float depth = (end.y - start.y) / detail;
		float[] vertices = new float[(int) ((width) * (depth) * 2 * 9)];
		float[] texCoords = new float[(int) ((width) * (depth) * 2 * 6)];
		float x = start.x;
		float y = start.y;
		int v = 0;
		int t = 0;
		while (x < end.x) {
			while (y < end.y) {
				vertices[v++] = x; vertices[v++] = y; v++;
				vertices[v++] = x + detail; vertices[v++] = y; v++;
				vertices[v++] = x + detail; vertices[v++] = y + detail; v++;
				vertices[v++] = x; vertices[v++] = y; v++;
				vertices[v++] = x; vertices[v++] = y + detail; v++;
				vertices[v++] = x + detail; vertices[v++] = y + detail; v++;
				texCoords[t++] = 0; texCoords[t++] = 0; texCoords[t++] = 0;
				texCoords[t++] = 1; texCoords[t++] = 1; texCoords[t++] = 1;
				texCoords[t++] = 0; texCoords[t++] = 0; texCoords[t++] = 0;
				texCoords[t++] = 1; texCoords[t++] = 1; texCoords[t++] = 1;
				y += detail;
			}
			y = start.y;
			x += detail;
		}
		IMaterial m = new ColorMapMaterial(RGBA.WHITE, new Texture(RoboSim.class.getResource("assets/boden2.jpg")));
		IGeometry g = DefaultGeometry.createVM(Primitive.TRIANGLES, vertices, texCoords);
		return new DefaultMesh(m, g);
	}
	
	public static I3DObject createWallX(Vec3 start, Vec3 end, int detail) {
		float width = (end.x - start.x) / detail;
		float height = (end.z - start.z) / detail;
		float[] vertices = new float[(int) ((width) * (height) * 2 * 9)];
		float[] texCoords = new float[(int) ((width) * (height) * 2 * 6)];
		float x = start.x;
		float z = start.z;
		int v = 0;
		int t = 0;
		while (x < end.x) {
			while (z < end.z) {
				vertices[v++] = x; vertices[v++] = start.y; vertices[v++] = z;
				vertices[v++] = x + detail; vertices[v++] = start.y; vertices[v++] = z;
				vertices[v++] = x + detail; vertices[v++] = start.y; vertices[v++] = z + detail;
				vertices[v++] = x; vertices[v++] = start.y; vertices[v++] = z;
				vertices[v++] = x; vertices[v++] = start.y; vertices[v++] = z + detail;
				vertices[v++] = x + detail; vertices[v++] = start.y; vertices[v++] = z + detail;
				texCoords[t++] = 0; texCoords[t++] = 0; texCoords[t++] = 0;
				texCoords[t++] = 1; texCoords[t++] = 1; texCoords[t++] = 1;
				texCoords[t++] = 0; texCoords[t++] = 0; texCoords[t++] = 0;
				texCoords[t++] = 1; texCoords[t++] = 1; texCoords[t++] = 1;
				z += detail;
			}
			z = start.z;
			x += detail;
		}
		IMaterial m = new ColorMapMaterial(RGBA.WHITE, new Texture(RoboSim.class.getResource("assets/wand2.jpg")));
		IGeometry g = DefaultGeometry.createVM(Primitive.TRIANGLES, vertices, texCoords);
		return new DefaultMesh(m, g);
	}
	
	public static I3DObject createWallY(Vec3 start, Vec3 end, int detail) {
		float width = (end.y - start.y) / detail;
		float height = (end.z - start.z) / detail;
		float[] vertices = new float[(int) ((width) * (height) * 2 * 9)];
		float[] texCoords = new float[(int) ((width) * (height) * 2 * 6)];
		float y = start.y;
		float z = start.z;
		int v = 0;
		int t = 0;
		while (y < end.y) {
			while (z < end.z) {
				vertices[v++] = start.x; vertices[v++] = y; vertices[v++] = z;
				vertices[v++] = start.x; vertices[v++] = y + detail; vertices[v++] = z;
				vertices[v++] = start.x; vertices[v++] = y + detail; vertices[v++] = z + detail;
				vertices[v++] = start.x; vertices[v++] = y; vertices[v++] = z;
				vertices[v++] = start.x; vertices[v++] = y; vertices[v++] = z + detail;
				vertices[v++] = start.x; vertices[v++] = y + detail; vertices[v++] = z + detail;
				texCoords[t++] = 0; texCoords[t++] = 0; texCoords[t++] = 0;
				texCoords[t++] = 1; texCoords[t++] = 1; texCoords[t++] = 1;
				texCoords[t++] = 0; texCoords[t++] = 0; texCoords[t++] = 0;
				texCoords[t++] = 1; texCoords[t++] = 1; texCoords[t++] = 1;
				z += detail;
			}
			z = start.z;
			y += detail;
		}
		IMaterial m = new ColorMapMaterial(RGBA.WHITE, new Texture(RoboSim.class.getResource("assets/wand2.jpg")));
		IGeometry g = DefaultGeometry.createVM(Primitive.TRIANGLES, vertices, texCoords);
		return new DefaultMesh(m, g);
	}
	
//	public static I3DObject createPlane(Vec3 start, Vec3 end, detail, IMaterial mat) {
//		float dx = end.x - start.x;
//		float dy = end.y - start.y;
//		float dz = end.z - start.z;
//		if (dx * dy * dz != 0) {
//			throw new InvalidAttributesException("Only differences in 2 coordinates allowed");
//		}
//		float ux = dx / detail;
//		float uy = dy / detail;
//		float uz = dz / detail;
//		float x = start.x;
//		float y = start.y;
//		float z = start.z;
//		while (x < end.x) {
//			while (y < end.y) {
//				while (z < end.z) {
//					vertices[v++] = x; vertices[v++] = y; v++;
//					vertices[v++] = x + detail; vertices[v++] = y; v++;
//					vertices[v++] = x + detail; vertices[v++] = y + detail; v++;
//					vertices[v++] = x; vertices[v++] = y; v++;
//					vertices[v++] = x; vertices[v++] = y + detail; v++;
//					vertices[v++] = x + detail; vertices[v++] = y + detail; v++;
//					texCoords[t++] = 0; texCoords[t++] = 0; texCoords[t++] = 0;
//					texCoords[t++] = 1; texCoords[t++] = 1; texCoords[t++] = 1;
//					texCoords[t++] = 0; texCoords[t++] = 0; texCoords[t++] = 0;
//					texCoords[t++] = 1; texCoords[t++] = 1; texCoords[t++] = 1;
//					z += detail;
//				}
//				z = start.z;
//				y += detail;
//			}
//			y = start.y;
//			x += detail;
//		}
//		
//	}
}
