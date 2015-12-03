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

import ch.fhnw.ether.controller.DefaultController;
import ch.fhnw.ether.controller.event.IKeyEvent;
import ch.fhnw.ether.scene.camera.ICamera;
import ch.fhnw.ether.scene.light.ILight;
import ch.fhnw.ether.scene.mesh.IMesh;
import ch.fhnw.util.math.Mat4;
import ch.fhnw.util.math.Vec3;

public class RoboSimController extends DefaultController {
	private static final float INC_XY = 0.25f;
	private static final float INC_Z = 0.25f;
	private IMesh cube;
	private float angle = 0;
	private ILight light;

	@Override
	public void keyPressed(IKeyEvent e) {
		switch (e.getKeyCode()) {
		case IKeyEvent.VK_RIGHT:
		case IKeyEvent.VK_LEFT:
			angle += e.getKeySym() - IKeyEvent.VK_UP;
			cube.setTransform(Mat4.rotate(angle, Vec3.Z));
			break;
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
	
	public void setCube(IMesh cube) {
		this.cube = cube;
	}

	public void setLight(ILight light) {
		this.light = light;
	}
}
