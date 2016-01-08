package ch.fhnw.comgr.robosim.tools;

import ch.fhnw.comgr.robosim.RoboSimController;
import ch.fhnw.comgr.robosim.Robot;
import ch.fhnw.ether.controller.event.IKeyEvent;
import ch.fhnw.ether.controller.tool.AbstractTool;

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
		    case IKeyEvent.VK_Q:
		    	robot.rotate(5, 1);
		    	break;
		    case IKeyEvent.VK_A:
		    	robot.rotate(5, -1);
		    	break;
		    case IKeyEvent.VK_W:
		    	robot.rotate(4, 1);
		    	break;
		    case IKeyEvent.VK_S:
		    	robot.rotate(4, -1);
		    	break;
		    case IKeyEvent.VK_E:
		    	robot.rotate(3, 1);
		    	break;
		    case IKeyEvent.VK_D:
		    	robot.rotate(3, -1);
		    	break;
		    case IKeyEvent.VK_R:
		    	robot.rotate(2, 1);
		    	break;
		    case IKeyEvent.VK_F:
		    	robot.rotate(2, -1);
		    	break;
		    case IKeyEvent.VK_T:
		    	robot.rotate(1, 1);
		    	break;
		    case IKeyEvent.VK_G:
		    	robot.rotate(1, -1);
		    	break;
		    case IKeyEvent.VK_Z:
		    	robot.rotate(0, 1);
		    	break;
		    case IKeyEvent.VK_H:
		    	robot.rotate(0, -1);
		    	break;
		    case IKeyEvent.VK_SPACE:
		    	robot.reset();
			default:
				super.keyPressed(e);
		}
		}
}
