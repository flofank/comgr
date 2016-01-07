package ch.fhnw.comgr.robosim;

import java.util.Map;

import ch.fhnw.ether.controller.event.IKeyEvent;
import ch.fhnw.ether.controller.event.IPointerEvent;
import ch.fhnw.ether.controller.tool.ITool;
import ch.fhnw.ether.controller.tool.PickTool;
import ch.fhnw.ether.controller.tool.PickUtilities;
import ch.fhnw.ether.controller.tool.PickUtilities.PickMode;
import ch.fhnw.ether.scene.I3DObject;
import ch.fhnw.ether.scene.camera.IViewCameraState;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.ether.scene.mesh.MeshUtilities;
import ch.fhnw.ether.scene.mesh.material.ShadedMaterial;
import ch.fhnw.ether.view.ProjectionUtilities;
import ch.fhnw.util.color.RGB;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;
import ch.fhnw.util.math.geometry.Line;
import ch.fhnw.util.math.geometry.Plane;

public class PositioningTool extends PickTool {
	private RoboSimController controller;
	private I3DObject pick;

	public PositioningTool(RoboSimController controller) {
		super(controller);
		this.controller = controller;
	}
	
	@Override
	public void keyPressed(IKeyEvent e) {
		switch (e.getKeyCode()) {
//			case IKeyEvent.VK_RIGHT:
//			case IKeyEvent.VK_LEFT:
//				if (pick instanceof IMesh) {
//					float angle = (e.getKeySym() - IKeyEvent.VK_UP) * 3;
//					((IMesh) pick).setTransform(Mat4.rotate(angle, Vec3.Z));
//					
//					((IMesh) pick).setTransform(((IMesh) pick).getTransform().(angle, Vec3.Z));
//				}
			case IKeyEvent.VK_UP:
			case IKeyEvent.VK_DOWN:
				int s = (e.getKeySym() - IKeyEvent.VK_UP - 1) * -1;
				pick.setPosition(pick.getPosition().add(new Vec3(0,0,s)));
				break;				
			default:
				super.keyPressed(e);
		}
	}
	
	@Override
	public void pointerPressed(IPointerEvent e) {
		pick = pick(e);
		System.out.println("picked " + pick);
	}
	
	@Override
	public void pointerDragged(IPointerEvent e) {
		if (!e.isModifierDown()) {
			Vec3 oldPos = pick.getPosition();
			IViewCameraState state = getController().getRenderManager().getViewCameraState(e.getView());
			Line line = ProjectionUtilities.getRay(state, e.getX(), e.getY());
			Plane plane = new Plane(new Vec3(0, 0, 1));
			Vec3 p = plane.intersection(line);
			pick.setPosition(new Vec3(p.x, p.y, oldPos.z));
			if (controller.obstructed(pick)) {
				pick.setPosition(oldPos);
			}
		} else {
			super.pointerDragged(e);
		}
	}
	
	private I3DObject pick(IPointerEvent e) {
		int x = e.getX();
		int y = e.getY();
		Map<Float, I3DObject> pickables = PickUtilities.pickFromScene(PickMode.POINT, x, y, 0, 0, e.getView());
		if (pickables.isEmpty()) {
			return null;
		} else {
			return pickables.values().iterator().next();
		}
	}

}
