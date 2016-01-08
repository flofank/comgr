package ch.fhnw.comgr.robosim.tools;

import ch.fhnw.comgr.robosim.RoboSimController;
import ch.fhnw.ether.controller.event.IKeyEvent;
import ch.fhnw.ether.controller.tool.AbstractTool;

public class RobotTool extends AbstractTool {
	private RoboSimController controller;
    private float reso_axis_bottom = 4;

	public RobotTool(RoboSimController controller) {
		super(controller);
		this.controller = controller;
	}
	
	@Override
	public void keyPressed(IKeyEvent e) {
		switch (e.getKeyCode()) {
		    case IKeyEvent.VK_RIGHT:
		        controller.rotateBottom(reso_axis_bottom);
		        break;
		    case IKeyEvent.VK_LEFT:
		        controller.rotateBottom(-reso_axis_bottom);
		        break;
			default:
				super.keyPressed(e);
		}
	}
}
