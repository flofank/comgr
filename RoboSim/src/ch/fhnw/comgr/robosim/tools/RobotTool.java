package ch.fhnw.comgr.robosim.tools;

import ch.fhnw.comgr.robosim.RoboSimController;
import ch.fhnw.comgr.robosim.Robot;
import ch.fhnw.ether.controller.event.IKeyEvent;
import ch.fhnw.ether.controller.event.IPointerEvent;
import ch.fhnw.ether.controller.tool.AbstractTool;
import ch.fhnw.ether.controller.tool.PickUtilities;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.util.math.Vec3;

public class RobotTool extends AbstractTool {
	private RoboSimController controller;
    private float reso_axis_bottom = 4;
    private Robot robot;

	public RobotTool(RoboSimController controller, Robot robot) {
		super(controller);
		this.controller = controller;
		this.robot = robot;
	}
	
	@Override
	public void keyPressed(IKeyEvent e) {
		switch (e.getKeyCode()) {
		    case IKeyEvent.VK_A:
		    	robot.rotate(6, 1);
		    	break;
		    case IKeyEvent.VK_Y:
		    case IKeyEvent.VK_Z:
		    	robot.rotate(6, -1);
		    	break;
		    case IKeyEvent.VK_S:
		    	robot.rotate(5, 1);
		    	break;
		    case IKeyEvent.VK_X:
		    	robot.rotate(5, -1);
		    	break;
		    case IKeyEvent.VK_D:
		    	robot.rotate(4, 1);
		    	break;
		    case IKeyEvent.VK_C:
		    	robot.rotate(4, -1);
		    	break;
		    case IKeyEvent.VK_F:
		    	robot.rotate(3, 1);
		    	break;
		    case IKeyEvent.VK_V:
		    	robot.rotate(3, -1);
		    	break;
		    case IKeyEvent.VK_G:
		    	robot.rotate(2, 1);
		    	break;
		    case IKeyEvent.VK_B:
		    	robot.rotate(2, -1);
		    	break;
		    case IKeyEvent.VK_H:
		    	robot.rotate(1, 1);
		    	break;
		    case IKeyEvent.VK_N:
		    	robot.rotate(1, -1);
		    	break;
		    case IKeyEvent.VK_T:
		    	tryPickUp();
		    	break;
		    case IKeyEvent.VK_R:
		    	robot.release();
		    	break;
		    	// Foot can't be moved
//		    case IKeyEvent.VK_J:
//		    	robot.rotate(0, 1);
//		    	break;
//		    case IKeyEvent.VK_M:
//		    	robot.rotate(0, -1);
//		    	break;
		    case IKeyEvent.VK_SPACE:
		    	robot.reset();
		    	break;
			default:
				super.keyPressed(e);
		}
	}

	private void tryPickUp() {
		for (IMesh cube : controller.getCubes()) {
			if (canPickUp(robot, cube)) {
				robot.pickUp(cube);
			}
		}
	}
	
	private boolean canPickUp(Robot robot, IMesh cube) {
		Vec3 rd = robot.getMagnetDir();
		// Pick from above
		if (near(rd.x, 0) && near(rd.y, 0) && near(rd.z, -1)) {
			return near(robot.getMagnetPos().z, cube.getBounds().getMaxZ());
		}
		return false;
	}
	
	private boolean near(float a, float b) {
		float m = 0.2f;
		return a - b < m && a - b > -m;
	}
	
	private boolean near(Vec3 a, Vec3 b) {
		return near(a.x, b.x) && near(a.y, b.y) && near(a.z, b.z);
	}
	
    @Override
    public void pointerPressed(IPointerEvent e) {

    }

}
